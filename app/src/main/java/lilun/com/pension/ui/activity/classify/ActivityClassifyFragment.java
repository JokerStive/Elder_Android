package lilun.com.pension.ui.activity.classify;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import lilun.com.pension.R;
import lilun.com.pension.app.Event;
import lilun.com.pension.app.OrganizationChildrenConfig;
import lilun.com.pension.app.User;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.adapter.ActivityCategoryAdapter;
import lilun.com.pension.module.adapter.OrganizationActivityAdapter;
import lilun.com.pension.module.bean.ActivityCategory;
import lilun.com.pension.module.bean.OrganizationActivity;
import lilun.com.pension.module.callback.TitleBarClickCallBack;
import lilun.com.pension.module.utils.Preconditions;
import lilun.com.pension.module.utils.StringUtils;
import lilun.com.pension.ui.activity.activity_add.AddActivityFragment;
import lilun.com.pension.ui.activity.activity_detail.ActivityChatFragment;
import lilun.com.pension.ui.activity.activity_list.ActivityListFragment;
import lilun.com.pension.ui.announcement.AnnouncementFragment;
import lilun.com.pension.widget.ElderModuleClassifyDecoration;
import lilun.com.pension.widget.NormalItemDecoration;
import lilun.com.pension.widget.PositionTitleBar;
import lilun.com.pension.widget.filter_view.FilterLayoutView;

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

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.title_bar)
    PositionTitleBar titleBar;

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.rg_classify)
    RecyclerView mClassifyRecycler;

    @Bind(R.id.swipe_layout)
    SwipeRefreshLayout mSwipeLayout;

    //    private ArrayList<Information> announcements;
    private ArrayList<ActivityCategory> activityCategories;

    private OrganizationActivityAdapter mContentAdapter;
    private List<OrganizationActivity> organizationActivities = new ArrayList<>();
    private String parentId;


    public static ActivityClassifyFragment newInstance(String parentId) {
        ActivityClassifyFragment fragment = new ActivityClassifyFragment();
        Bundle args = new Bundle();
        args.putString("parentId", parentId);
        fragment.setArguments(args);
        return fragment;
    }
//
//    public static ActivityClassifyFragment newInstance(List<Information> announcements) {
//        ActivityClassifyFragment fragment = new ActivityClassifyFragment();
//        Bundle args = new Bundle();
//        args.putSerializable("announcements", (Serializable) announcements);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Subscribe
    public void refreshData(Event.RefreshActivityData event) {
        refreshData(false);
    }

    @Override
    protected void getTransferData(Bundle arguments) {
        parentId = arguments.getString("parentId");
        Preconditions.checkNull(parentId);
//        announcements = (ArrayList<Information>) arguments.getSerializable("announcements");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_activity_classify;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ActivityClassifyPresenter();
        mPresenter.bindView(this);
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        _mActivity.setSupportActionBar(toolbar);
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
                    start(AddActivityFragment.newInstance(activityCategories));

                }
            }
        });


        replaceLoadRootFragment(R.id.fl_announcement_container, AnnouncementFragment.newInstance(parentId), false);


        mClassifyRecycler.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mClassifyRecycler.addItemDecoration(new ElderModuleClassifyDecoration());

        //我参与的活动列表
        mRecyclerView.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new NormalItemDecoration(10));

        //刷新
        mSwipeLayout.setOnRefreshListener(() -> {
                    if (mPresenter != null) {
                        refreshData(activityCategories == null);
                    }
                }
        );


        //设置数据
        setAdapter();
    }

    private void setAdapter() {
        mContentAdapter = new OrganizationActivityAdapter(organizationActivities, R.layout.item_activity_small, FilterLayoutView.LayoutType.SMALL, false);
        mContentAdapter.setOnItemClickListener((activityItem) -> {
            start(ActivityChatFragment.newInstance(activityItem));

        });
        mContentAdapter.setEmptyView();
        mRecyclerView.setAdapter(mContentAdapter);
    }


    @Override
    protected void initEvent() {
        super.initData();
        refreshData(true);
    }


    private void refreshData(boolean needRefreshClassify) {
        mSwipeLayout.setRefreshing(true);
        if (needRefreshClassify) {
            getClassifies();
        }
        getAboutMe(0);
    }

    private void getClassifies() {
        mPresenter.getClassifies();
    }

    private void getAboutMe(int skip) {
        //TODO 获取关于我的活动 1.是活动类型  2.创建人是自己  3.参加的活动
        String filter = "{\"where\":{\"categoryId\":{\"like\":\"" + OrganizationChildrenConfig.activity() + "\"}," +
                "\"or\":[{\"masterId\":\"" + User.getUserId() + "\"}," +
                "{\"partnerList\":\"" + User.getUserId() + "\"}]}}";
        mPresenter.getAboutMe(filter, skip);
    }

    @Override
    public void showClassifies(List<ActivityCategory> activityCategories) {
        completeRefresh();
        this.activityCategories = (ArrayList<ActivityCategory>) activityCategories;
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
