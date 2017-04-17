package lilun.com.pension.ui.activity.activity_list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.Bind;
import lilun.com.pension.R;
import lilun.com.pension.app.Event;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.adapter.OrganizationActivityAdapter;
import lilun.com.pension.module.bean.ActivityCategory;
import lilun.com.pension.module.bean.OrganizationActivity;
import lilun.com.pension.module.utils.Preconditions;
import lilun.com.pension.ui.activity.activity_detail.ActivityDetailFragment;
import lilun.com.pension.widget.NormalItemDecoration;
import lilun.com.pension.widget.SearchTitleBar;
import lilun.com.pension.widget.filter_view.FilterView;

/**
 * 分类活动V
 *
 * @author yk
 *         create at 2017/2/7 16:04
 *         email : yk_developer@163.com
 */
public class ActivityListFragment extends BaseFragment<ActivityListContract.Presenter>
        implements ActivityListContract.View {


    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.swipe_layout)
    SwipeRefreshLayout mSwipeLayout;

    @Bind(R.id.searchBar)
    SearchTitleBar searchBar;

    @Bind(R.id.filter_view)
    FilterView filterView;

    private OrganizationActivityAdapter mActivityAdapter;
    private ActivityCategory mCategory;
    private String[] hedaers = new String[]{"周期类型", "区域", "时间"};
    private SearchTitleBar.LayoutType layoutType = SearchTitleBar.LayoutType.BIG;
    private List<OrganizationActivity> activities;

    public static ActivityListFragment newInstance(ActivityCategory category) {
        ActivityListFragment fragment = new ActivityListFragment();
        Bundle args = new Bundle();
        args.putSerializable("OrganizationActivityCategory", category);
        fragment.setArguments(args);
        return fragment;
    }

    @Subscribe
    public void refreshData(Event.RefreshActivityData event) {
        getActivityList(0);
    }


    @Override
    protected void getTransferData(Bundle arguments) {
        mCategory = (ActivityCategory) arguments.getSerializable("OrganizationActivityCategory");
        Preconditions.checkNull(mCategory);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ActivtyListPresenter();
        mPresenter.bindView(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_search_filter_list;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        searchBar.setNoNullLayout();
        searchBar.setFragment(this);
        searchBar.setOnItemClickListener(new SearchTitleBar.OnItemClickListener() {
            @Override
            public void onBack() {
                pop();
            }

            @Override
            public void onChangeLayout(SearchTitleBar.LayoutType type) {
                layoutType = type;
                if (activities != null && activities.size() != 0) {
                    setRecyclerAdapter(activities);
                }
            }

            @Override
            public void onSearch(String searchStr) {
//                conditionMap.put(condition_title,searchStr);
//                getHelps(0);
            }
        });


        mRecyclerView.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new NormalItemDecoration(10));
        //刷新
        mSwipeLayout.setOnRefreshListener(() -> {
                    if (mPresenter != null) {
                        getActivityList(0);
                    }
                }
        );

        initConditionModules();
    }


    private void setRecyclerAdapter(List<OrganizationActivity> activities) {
        mActivityAdapter = getAdapterFromLayoutType(activities);
        if (mActivityAdapter != null) {
            mActivityAdapter.setOnItemClickListener((activityItem) -> {
                start(ActivityDetailFragment.newInstance(activityItem.getId()));
            });
            mActivityAdapter.setEmptyView();
        }
        mRecyclerView.setAdapter(mActivityAdapter);
    }


    private OrganizationActivityAdapter getAdapterFromLayoutType(List<OrganizationActivity> activities) {
        OrganizationActivityAdapter adapter = null;
        int layoutId = 0;
        if (layoutType == SearchTitleBar.LayoutType.BIG) {
            layoutId = R.layout.item_activity_big;
        } else if (layoutType == SearchTitleBar.LayoutType.SMALL) {
            layoutId = R.layout.item_activity_small;
        }

//        else {
//            layoutId = R.layout.item_aid_null;
//        }
        adapter = new OrganizationActivityAdapter(activities, layoutId, layoutType);
        return adapter;
    }

    private void initConditionModules() {
//        List<ConditionOption> kindOptions = new ArrayList<>();
//        ConditionOption kindOptionNull = new ConditionOption("kind", "", "不限");
//        ConditionOption kindOptionAsk = new ConditionOption("kind", "0", "周期活动");
//        ConditionOption kindOptionHelp = new ConditionOption("kind", "1", "单次活动");
//        kindOptions.add(kindOptionNull);
//        kindOptions.add(kindOptionAsk);
//        kindOptions.add(kindOptionHelp);
//
//        List<ConditionOption> priorityOptions = new ArrayList<>();
//        ConditionOption priorityOptionNull = new ConditionOption("priority", "", "不限");
//        ConditionOption priorityOption0 = new ConditionOption("priority", "0", "同景国际");
//        ConditionOption priorityOption1 = new ConditionOption("priority", "1", "时代都会");
//        ConditionOption priorityOption2 = new ConditionOption("priority", "2", "江南小区");
//        priorityOptions.add(priorityOptionNull);
//        priorityOptions.add(priorityOption0);
//        priorityOptions.add(priorityOption1);
//        priorityOptions.add(priorityOption2);
//
//        List<ConditionOption> timeOptions = new ArrayList<>();
//        ConditionOption timeOptionNull = new ConditionOption("priority", "", "不限");
//        ConditionOption timeOption0 = new ConditionOption("priority", "0", "最近");
//        timeOptions.add(timeOptionNull);
//        timeOptions.add(timeOption0);
//
//
//        List<ViewStep2> popViews = new ArrayList<>();
//        RecyclerView kindOptionView = new RecyclerView(App.context);
//        kindOptionView.setLayoutManager(new LinearLayoutManager(App.context, LinearLayoutManager.VERTICAL, false));
//        NormalFilterAdapter kindOptionAdapter = new NormalFilterAdapter(kindOptions);
//        kindOptionAdapter.setOnItemClickListener((position, option) -> {
//            filterView.setTabText(position == 0 ? hedaers[0] : option.getVal(), position == 0);
//        });
//        kindOptionView.setAdapter(kindOptionAdapter);
//
//        RecyclerView priorityOptionView = new RecyclerView(App.context);
//        priorityOptionView.setLayoutManager(new LinearLayoutManager(App.context, LinearLayoutManager.VERTICAL, false));
//        NormalFilterAdapter priorityOptionAdapter = new NormalFilterAdapter(priorityOptions);
//        priorityOptionAdapter.setOnItemClickListener((position, option) -> {
//            filterView.setTabText(position == 0 ? hedaers[1] : option.getVal(), position == 0);
//        });
//        priorityOptionView.setAdapter(priorityOptionAdapter);
//
//
//        RecyclerView timeOptionView = new RecyclerView(App.context);
//        timeOptionView.setLayoutManager(new LinearLayoutManager(App.context, LinearLayoutManager.VERTICAL, false));
//        NormalFilterAdapter timeOptionAdapter = new NormalFilterAdapter(timeOptions);
//        timeOptionAdapter.setOnItemClickListener((position, option) -> {
//            filterView.setTabText(position == 0 ? hedaers[0] : option.getVal(), position == 0);
//        });
//        timeOptionView.setAdapter(timeOptionAdapter);
//
//        popViews.add(kindOptionView);
//        popViews.add(priorityOptionView);
//        popViews.add(timeOptionView);
//
//        filterView.setTitlesAndPops(Arrays.asList(hedaers), popViews, mSwipeLayout);
    }


    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            mSwipeLayout.setRefreshing(true);
            getActivityList(0);
        }
    }

    private void getActivityList(int skip) {
        // TODO 关联organizationId
        String filter = "{\"where\":{\"categoryId\":\"" + mCategory.getId() + "\"}}";
        mPresenter.getOrganizationActivities(filter, skip);
    }


    @Override
    public void showActivityList(List<OrganizationActivity> activities, boolean islOadMore) {
        completeRefresh();
        this.activities = activities;
        if (mActivityAdapter == null) {
            setRecyclerAdapter(activities);
        } else if (islOadMore) {
            mActivityAdapter.addAll(activities);
        } else {
            mActivityAdapter.replaceAll(activities);
        }
    }

    @Override
    public void completeRefresh() {
        if (mSwipeLayout != null && mSwipeLayout.isRefreshing()) {
            mSwipeLayout.setRefreshing(false);
        }
    }


}
