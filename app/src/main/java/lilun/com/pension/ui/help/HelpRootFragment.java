package lilun.com.pension.ui.help;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import lilun.com.pension.R;
import lilun.com.pension.app.Event;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.adapter.ElderModuleAdapter;
import lilun.com.pension.module.adapter.OrganizationAidAdapter;
import lilun.com.pension.module.bean.Announcement;
import lilun.com.pension.module.bean.ElderModule;
import lilun.com.pension.module.bean.OrganizationAid;
import lilun.com.pension.module.callback.TitleBarClickCallBack;
import lilun.com.pension.ui.announcement.AnnouncementFragment;
import lilun.com.pension.ui.help.help_detail.AskDetailFragment;
import lilun.com.pension.ui.help.help_detail.HelpDetailFragment;
import lilun.com.pension.widget.ElderModuleClassifyDecoration;
import lilun.com.pension.widget.ElderModuleItemDecoration;
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
public class HelpRootFragment extends BaseFragment<HelpContract.Presenter> implements HelpContract.View {

    @Bind(R.id.title_bar)
    PositionTitleBar titleBar;

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.swipe_layout)
    SwipeRefreshLayout mSwipeLayout;

    private ArrayList<Announcement> announcements;
    private List<ElderModule> elderModules;
    private RecyclerView mClassifyRecycler;
    private OrganizationAidAdapter mAidAdapter;
    private List<OrganizationAid> organizationAids = new ArrayList<>();


    public static HelpRootFragment newInstance() {
        return new HelpRootFragment();
    }

    @Subscribe
    public void refreshData(Event.RefreshHelpData event){
        refreshData();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_second_module;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new HelpPresenter();
        mPresenter.bindView(this);
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        titleBar.setTitle(getString(R.string.neighbor_help));
        titleBar.setTvRightText(getString(R.string.need_help));
        titleBar.setTitleBarClickListener(new TitleBarClickCallBack() {
            @Override
            public void onBackClick() {
                pop();
            }

            @Override
            public void onPositionClick() {

            }

            @Override
            public void onRightClick() {
                if (elderModules != null) {
                    start(AddHelpFragment.newInstance(elderModules));
                }
            }
        });

        //初始化公告栏
        if (announcements == null || announcements.size() == 0) {
            Logger.d("公告数据为空");
        } else {
            replaceLoadRootFragment(R.id.fl_announcement_container, AnnouncementFragment.newInstance(announcements), false);
        }


        //类别
        mClassifyRecycler = new RecyclerView(_mActivity);
        mClassifyRecycler.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false));

        //求助列表
        mClassifyRecycler.addItemDecoration(new ElderModuleClassifyDecoration());
        mRecyclerView.addItemDecoration(new ElderModuleItemDecoration());


        //刷新
        mSwipeLayout.setOnRefreshListener(() -> {
                    if (mPresenter != null) {
                        getHelps(0);
                    }
                }
        );


        //设置数据
        setAdapter();
    }

    private void setAdapter() {
        mAidAdapter = new OrganizationAidAdapter(this, organizationAids);
        mAidAdapter.addHeaderView(mClassifyRecycler);
        mAidAdapter.setOnItemClickListener(aid -> {
            start(aid.getKind() == 0 ? AskDetailFragment.newInstance(aid) : HelpDetailFragment.newInstance(aid));
        });
        mRecyclerView.setAdapter(mAidAdapter);
    }


    @Override
    protected void initEvent() {
        super.initData();
        refreshData();
    }


    private void refreshData() {
        mSwipeLayout.setRefreshing(true);
        getHelpClassifies();
        getHelps(0);
    }

    private void getHelpClassifies() {
        mPresenter.getClassifies();
    }

    private void getHelps(int skip) {
//        String filter = "{\"where\":{\"creatorId\":\"" + User.getUserId() + "\"}}";
        mPresenter.getAboutMe("", skip);
    }

    @Override
    public void showClassifies(List<ElderModule> elderModules) {
        completeRefresh();
        this.elderModules = elderModules;
        mClassifyRecycler.setLayoutManager(new GridLayoutManager(_mActivity, spanCountByData(elderModules)));
        ElderModuleAdapter adapter = new ElderModuleAdapter(this, elderModules);
        adapter.setOnItemClickListener((elderModule -> {
            start(HelpFragment.newInstance(elderModule));
        }));

        mClassifyRecycler.setAdapter(adapter);
    }

    @Override
    public void showAboutMe(List<OrganizationAid> helps, boolean isLoadMore) {
        completeRefresh();
        if (helps != null) {
            for (OrganizationAid aid : helps) {
                aid.setItemType(aid.getKind());
            }
            if (isLoadMore) {
                mAidAdapter.addAll(helps);
            } else {
                mAidAdapter.replaceAll(helps);
            }
        }
    }

    @Override
    public void completeRefresh() {
        if (mSwipeLayout != null && mSwipeLayout.isRefreshing()) {
            mSwipeLayout.setRefreshing(false);
        }
    }

    private int spanCountByData(List data) {
        int count;
        if (data.size() == 0) {
            count = 1;
        } else {
            count = data.size() >= 3 ? 3 : data.size();
        }
        return count;
    }





}
