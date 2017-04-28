package lilun.com.pension.ui.help;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.Bind;
import lilun.com.pension.R;
import lilun.com.pension.app.Event;
import lilun.com.pension.app.User;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.adapter.CycleAidAdapter;
import lilun.com.pension.module.bean.OrganizationAid;
import lilun.com.pension.module.bean.PushMessage;
import lilun.com.pension.module.callback.TitleBarClickCallBack;
import lilun.com.pension.module.utils.Preconditions;
import lilun.com.pension.module.utils.ToastHelper;
import lilun.com.pension.ui.announcement.AnnouncementFragment;
import lilun.com.pension.ui.help.list.HelpContract;
import lilun.com.pension.ui.help.list.HelpFragment;
import lilun.com.pension.ui.help.list.HelpPresenter;
import lilun.com.pension.widget.NormalItemDecoration;
import lilun.com.pension.widget.PositionTitleBar;

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

    //    private ArrayList<Information> informationList;
    //    private List<ElderModule> elderModules;
//    private RecyclerView mClassifyRecycler;
    private CycleAidAdapter adapter;
    private int currentPosition = 0;
    private String parentId;
//    private List<OrganizationAid> organizationAids = new ArrayList<>();


//    public static HelpRootFragment newInstance(List<Information> announcements) {
//        HelpRootFragment fragment = new HelpRootFragment();
//        Bundle args = new Bundle();
//        args.putSerializable("informationList", (Serializable) announcements);
//        fragment.setArguments(args);
//        return fragment;
//    }

    public static HelpRootFragment newInstance(String parentId) {
        HelpRootFragment fragment = new HelpRootFragment();
        Bundle args = new Bundle();
        args.putString("parentId", parentId);
        fragment.setArguments(args);
        return fragment;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshPushMessage(PushMessage pushMessage) {
        Gson gson = new Gson();
        OrganizationAid aid = gson.fromJson(pushMessage.getData(), OrganizationAid.class);
        if (aid.getKind() != 2 && adapter != null) {
            adapter.add(aid);
            mRecyclerView.smoothScrollToPosition(adapter.getItemCount());
        }
    }

    @Override
    protected void getTransferData(Bundle arguments) {
        parentId = arguments.getString("parentId");
        Preconditions.checkNull(parentId);
    }

    @Subscribe
    public void refreshData(Event.RefreshHelpData event) {
        getHelps(0);
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
        titleBar.setFragment(this);
        titleBar.setTitleBarClickListener(new TitleBarClickCallBack() {
            @Override
            public void onBackClick() {
                pop();
            }

            @Override
            public void onRightClick() {
                start(HelpFragment.newInstance(true));
            }

        });

        tvFindHelp.setOnClickListener(this);
        tvNeedHelp.setOnClickListener(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new NormalItemDecoration(10));


        //刷新
//        mRecyclerView.setScrollX();
        mSwipeLayout.setEnabled(false);
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
        String filter = "{\"where\":{\"visible\":\"0\",\"kind\":{\"neq\":\"2\"}}}";
        mPresenter.getHelps(filter, skip);
    }


    @Override
    public void showAboutMe(List<OrganizationAid> helps, boolean isLoadMore) {
        completeRefresh();
        if (adapter == null) {
            adapter = new CycleAidAdapter(helps);
            mRecyclerView.setAdapter(adapter);
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
                if (User.currentOrganizationHasChanged()) {
                    ToastHelper.get().showShort("当前区域不是自己默认的");
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
