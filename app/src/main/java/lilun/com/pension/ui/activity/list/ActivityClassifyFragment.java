package lilun.com.pension.ui.activity.list;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import lilun.com.pension.R;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.adapter.ActivityCategoryAdapter;
import lilun.com.pension.module.adapter.OrganizationActivityAdapter;
import lilun.com.pension.module.bean.ActivityCategory;
import lilun.com.pension.module.bean.Announcement;
import lilun.com.pension.module.bean.OrganizationActivity;
import lilun.com.pension.module.callback.TitleBarClickCallBack;
import lilun.com.pension.module.utils.StringUtils;
import lilun.com.pension.ui.activity.classify.ActivityListFragment;
import lilun.com.pension.ui.announcement.AnnouncementFragment;
import lilun.com.pension.widget.ElderModuleClassifyDecoration;
import lilun.com.pension.widget.ElderModuleItemDecoration;
import lilun.com.pension.widget.PositionTitleBar;

/**
 * 社区活动宿主activity
 *
 * @author yk
 *         create at 2017/2/13 9:34
 *         email : yk_developer@163.com
 *         二级页面由宿主activity+fragment构成
 *         1、activity控制标题栏和公告栏（titleBar+AnnouncementFragment+fragmentContainer）
 *         2、初始化显示 ： 活动分类+我参加的活动（recyclerView+headView），MyActivityFragment替换fragmentContainer
 *         3、点击分类  ： 修改activity标题栏、XX分类的fragment替换fragmentContainer、传递的参数有活动分类名称（name）、icon地址
 *         4、回退 ：层级为宿主activity时宿主activity出栈，否则fragment出栈
 */
public class ActivityClassifyFragment extends BaseFragment<ActivityClassifyContract.Presenter> implements ActivityClassifyContract.View {

    @Bind(R.id.title_bar)
    PositionTitleBar titleBar;

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.swipe_layout)
    SwipeRefreshLayout mSwipeLayout;

    private ArrayList<Announcement> announcements;
    private List<ActivityCategory> activityCategories;
    private RecyclerView mClassifyRecycler;
    private OrganizationActivityAdapter mContentAdapter;
    private List<OrganizationActivity> organizationActivities = new ArrayList<>();


    public static ActivityClassifyFragment newInstance() {
        return new ActivityClassifyFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_second_module;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ActivityClassifyPresenter();
        mPresenter.bindView(this);
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        titleBar.setTitle(getString(R.string.community_activity));
        titleBar.setTvRightText(getString(R.string.add_activity));
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
                if (activityCategories != null) {
//                    start(AddHelpFragment.newInstance(activityCategories));
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
        mClassifyRecycler.addItemDecoration(new ElderModuleClassifyDecoration());

        //我参与的活动列表
        mRecyclerView.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new ElderModuleItemDecoration());


        //刷新
        mSwipeLayout.setOnRefreshListener(() -> {
                    if (mPresenter != null) {
                        getAboutMe(0);
                    }
                }
        );


        //设置数据
        setAdapter();
    }

    private void setAdapter() {
        mContentAdapter = new OrganizationActivityAdapter(this, organizationActivities);
        mContentAdapter.addHeaderView(mClassifyRecycler);
        mRecyclerView.setAdapter(mContentAdapter);
    }


    @Override
    protected void initEvent() {
        super.initData();
        refreshData();
    }


    private void refreshData() {
        mSwipeLayout.setRefreshing(true);
        getClassifies();
        getAboutMe(0);
    }

    private void getClassifies() {
        mPresenter.getClassifies();
    }

    private void getAboutMe(int skip) {
//        String filter = "{\"where\":{\"creatorId\":\"" + User.getUserId() + "\"}}";
        String filter = "";
        mPresenter.getAboutMe(filter, skip);
    }

    @Override
    public void showClassifies(List<ActivityCategory> activityCategories) {
        completeRefresh();
        this.activityCategories = activityCategories;
        mClassifyRecycler.setLayoutManager(new GridLayoutManager(_mActivity, StringUtils.spanCountByData(activityCategories)));
        ActivityCategoryAdapter adapter = new ActivityCategoryAdapter(this, activityCategories);
        adapter.setOnItemClickListener((activityCategory -> {
            start(ActivityListFragment.newInstance(activityCategory));
        }));

        mClassifyRecycler.setAdapter(adapter);
    }

    @Override
    public void showAboutMe(List<OrganizationActivity> activities, boolean isLoadMore) {
        completeRefresh();
        if (activities != null) {
            for (OrganizationActivity aid : activities) {
//                aid.setItemType(aid.getKind());
            }
            if (isLoadMore) {
                mContentAdapter.addAll(activities);
            } else {
                mContentAdapter.replaceAll(activities);
            }
        }
    }

    @Override
    public void completeRefresh() {
        if (mSwipeLayout != null && mSwipeLayout.isRefreshing()) {
            mSwipeLayout.setRefreshing(false);
        }
    }


}
