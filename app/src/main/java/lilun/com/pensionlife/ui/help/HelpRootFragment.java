package lilun.com.pensionlife.ui.help;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.Bind;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.app.Config;
import lilun.com.pensionlife.app.Event;
import lilun.com.pensionlife.app.User;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.adapter.CycleAidAdapter;
import lilun.com.pensionlife.module.bean.OrganizationAid;
import lilun.com.pensionlife.module.callback.TitleBarClickCallBack;
import lilun.com.pensionlife.module.utils.Preconditions;
import lilun.com.pensionlife.module.utils.ToastHelper;
import lilun.com.pensionlife.ui.announcement.AnnouncementFragment;
import lilun.com.pensionlife.ui.help.help_detail.AskDetailFragment;
import lilun.com.pensionlife.ui.help.help_detail.HelpDetailFragment;
import lilun.com.pensionlife.ui.help.list.HelpContract;
import lilun.com.pensionlife.ui.help.list.HelpFragment;
import lilun.com.pensionlife.ui.help.list.HelpPresenter;
import lilun.com.pensionlife.widget.DividerDecoration;
import lilun.com.pensionlife.widget.PositionTitleBar;

/**
 * 邻里互助宿主activity
 *
 * @author yk
 *         create at 2017/2/13 9:34
 *         email : yk_developer@163.com
 *         二级页面由宿主activity+fragment构成
 *         1、activity控制标题栏和公告栏（titleBar+AnnouncementFragment+fragmentContainer）
 *         2、初始化显示 ： 邻居互助分类+我的求助列表（recyclerView+headView），MyHelpFragment替换fragmentContainer
 *         3、点击分类  ： 修改activity标题栏、XX分类的fragment替换fragmentContainer、传递的参数有互助分类名称（name）、icon地址
 *         4、回退 ：层级为宿主activity时宿主activity出栈，否则fragment出栈
 */
public class HelpRootFragment extends BaseFragment<HelpContract.Presenter> implements HelpContract.View, View.OnClickListener {

    @Bind(R.id.title_bar)
    PositionTitleBar titleBar;

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.swipe_layout)
    SwipeRefreshLayout mSwipeLayout;
    @Bind(R.id.tv_need_help)
    TextView tvNeedHelp;
    @Bind(R.id.tv_find_help)
    TextView tvFindHelp;
    private CycleAidAdapter adapter;
    private String parentId;

    public static HelpRootFragment newInstance(String parentId) {
        HelpRootFragment fragment = new HelpRootFragment();
        Bundle args = new Bundle();
        args.putString("parentId", parentId);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void getTransferData(Bundle arguments) {
        parentId = arguments.getString("parentId");
        Preconditions.checkNull(parentId);
    }

    @Subscribe
    public void refreshData(Event.RefreshHelpData event) {
        Logger.d("需要刷新help root 页面的湖数据");
        refreshData();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_aid_classify;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new HelpPresenter();
        mPresenter.bindView(this);
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        titleBar.setTitle(getString(R.string.neighbor_help));
        titleBar.setTvRightText("关于我的");
        //  titleBar.setFragment(this);
        titleBar.setTitleBarClickListener(new TitleBarClickCallBack() {
            @Override
            public void onBackClick() {
                pop();
            }

            @Override
            public void onRightClick() {
                start(HelpFragment.newInstance(true));
            }

            @Override
            public void onPositionChanged() {
                refreshData();
            }
        });

        tvFindHelp.setOnClickListener(this);
        tvNeedHelp.setOnClickListener(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerDecoration(App.context, LinearLayoutManager.VERTICAL, (int) App.context.getResources().getDimension(R.dimen.dp_1), Color.parseColor("#f5f5f9")));
        mSwipeLayout.setOnRefreshListener(this::refreshData);

    }


    @Override
    protected void initEvent() {
        super.initData();
        refreshData();
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);

        replaceLoadRootFragment(R.id.fl_announcement_container, AnnouncementFragment.newInstance(parentId), false);
    }

    private void refreshData() {
        mSwipeLayout.setRefreshing(true);
        getHelps(0);
    }


    private void getHelps(int skip) {
        String filter = "{\"order\":\"createdAt DESC\",\"where\":{\"organizationId\":\"" + User.getCurrentOrganizationId() + "/#aid" + "\"}}";
        mPresenter.getHelps(filter, skip);
    }


    @Override
    public void showAboutMe(List<OrganizationAid> helps, boolean isLoadMore) {
        completeRefresh();
        if (adapter == null) {
            adapter = new CycleAidAdapter(helps);
            adapter.setOnItemClickListener((baseQuickAdapter, view, i) -> {
                OrganizationAid aid = helps.get(i);
                start(aid.getKind() == 0 ? AskDetailFragment.newInstance(aid.getId(), User.creatorIsOwn(aid.getCreatorId())) : HelpDetailFragment.newInstance(aid.getId()));
            });
            adapter.setOnLoadMoreListener(() -> getHelps(adapter.getItemCount()), mRecyclerView);
            mRecyclerView.setAdapter(adapter);
        } else if (isLoadMore) {
            adapter.addAll(helps, Config.defLoadDatCount);
        } else {
            adapter.replaceAll(helps);
        }
    }

    @Override
    public void completeRefresh() {
        if (mSwipeLayout != null && mSwipeLayout.isRefreshing()) {
            mSwipeLayout.setRefreshing(false);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_need_help:
                if (!User.canOperate(User.getCurrentOrganizationId())) {
                    ToastHelper.get().showShort("当前组织下，不能这样做");
                    return;
                } else {
                    start(AddHelpFragment.newInstance());
                }
                break;
            case R.id.tv_find_help:
                start(HelpFragment.newInstance(false));
                break;
        }
    }
}
