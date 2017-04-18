package lilun.com.pension.ui.activity.activity_list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import lilun.com.pension.R;
import lilun.com.pension.app.App;
import lilun.com.pension.app.Event;
import lilun.com.pension.app.User;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.adapter.OrganizationActivityAdapter;
import lilun.com.pension.module.bean.ActivityCategory;
import lilun.com.pension.module.bean.ConditionOption;
import lilun.com.pension.module.bean.Option;
import lilun.com.pension.module.bean.OrganizationActivity;
import lilun.com.pension.module.utils.Preconditions;
import lilun.com.pension.module.utils.StringUtils;
import lilun.com.pension.ui.activity.activity_detail.ActivityDetailFragment;
import lilun.com.pension.widget.NormalItemDecoration;
import lilun.com.pension.widget.SearchTitleBar;
import lilun.com.pension.widget.filter_view.FilterLayoutView;

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
    FilterLayoutView filterView;

    private OrganizationActivityAdapter mActivityAdapter;
    private ActivityCategory mCategory;

    private FilterLayoutView.LayoutType layoutType = FilterLayoutView.LayoutType.BIG;
    private List<OrganizationActivity> activities;
    private String searchStr = "";
    private String join_status = "";
    private String activity_status = "";

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
        return R.layout.layout_activity_filter_list;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        searchBar.setNoNullLayout();
        searchBar.setFragment(this);
        searchBar.isChangeLayout(false);
        searchBar.setOnItemClickListener(new SearchTitleBar.OnItemClickListener() {
            @Override
            public void onBack() {
                pop();
            }

            @Override
            public void onSearch(String str) {
                searchStr = str;
                getActivityList(0);
            }

            @Override
            public void onChangeLayout(SearchTitleBar.LayoutType layoutType) {

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
        if (layoutType == FilterLayoutView.LayoutType.BIG) {
            layoutId = R.layout.item_activity_big;
        } else if (layoutType == FilterLayoutView.LayoutType.SMALL) {
            layoutId = R.layout.item_activity_small;
        }

        adapter = new OrganizationActivityAdapter(activities, layoutId, layoutType);
        return adapter;
    }

    private void initConditionModules() {
        List<View> pops = new ArrayList<>();
        List<String> filterTitles = new ArrayList<>();
        filterTitles.addAll(Arrays.asList(App.context.getResources().getStringArray(R.array.activity_filter_status)));
        //除了区域以外的条件弹窗
        List<ConditionOption> conditionOptionsList = new ArrayList<>();
        List<Option> actJoinOptions = new ArrayList<>();
        actJoinOptions.add(new Option("", App.context.getResources().getStringArray(R.array.activity_join_status)[0]));
        actJoinOptions.add(new Option("0", App.context.getResources().getStringArray(R.array.activity_join_status)[1]));
        actJoinOptions.add(new Option("1", App.context.getResources().getStringArray(R.array.activity_join_status)[2]));
        conditionOptionsList.add(new ConditionOption(
                App.context.getResources().getStringArray(R.array.activity_filter_status)[0], "0", actJoinOptions));

        List<Option> actStatusOptions = new ArrayList<>();
        actStatusOptions.add(new Option("", App.context.getResources().getStringArray(R.array.activity_status)[0]));
        actStatusOptions.add(new Option("0", App.context.getResources().getStringArray(R.array.activity_status)[1]));
        actStatusOptions.add(new Option("1", App.context.getResources().getStringArray(R.array.activity_status)[2]));
        actStatusOptions.add(new Option("2", App.context.getResources().getStringArray(R.array.activity_status)[3]));
        conditionOptionsList.add(new ConditionOption(
                App.context.getResources().getStringArray(R.array.activity_filter_status)[1], "1", actStatusOptions));
        if (conditionOptionsList != null) {

            filterView.setTitlesAndDatas(filterTitles, conditionOptionsList, mSwipeLayout);
            filterView.setOnOptionClickListener((whereKey, whereValue) -> {
                Log.d("zp", whereKey + "  " + whereValue);

                //我的状态
                if (whereKey.equals(App.context.getResources().getStringArray(R.array.activity_filter_status)[0])) {

                    if ("0".equals(whereValue)) {
                        //已报名的
                        join_status = ",\"partnerList\":\"" + User.getUserId() + "\"";
                    } else if ("1".equals(whereValue)) {
                        //未报名的
                        join_status = ",\"partnerList\":{\"neq\":\"" + User.getUserId() + "\"}";
                    } else {
                        //全部
                        join_status = "";
                    }
                } else if (whereKey.equals(App.context.getResources().getStringArray(R.array.activity_filter_status)[1])) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String localtime = format.format(new Date());
                    String gtmDate = StringUtils.localToGTM(localtime);
                    if (whereValue != null) {
                        switch (whereValue) {
                            case "0":
                                //未开始
                                activity_status = ",\"startTime\":{\"gt\":\"" + gtmDate + "\"}";
                                break;
                            case "1":
                                activity_status = ",\"startTime\":{\"gte\":\"" + gtmDate + "\"},\"endTime\":{\"lte\":\"" + gtmDate + "\"}";
                                break;

                            case "2":
                                //已结束   现在时间>结束时间
                                activity_status = ",\"endTime\":{\"lt\":\"" + gtmDate + "\"}";
                                break;
                            default:
                                activity_status = "";
                                break;
                        }
                    }
                }
                getActivityList(0);


            });
            filterView.setOnLayoutlistener(new FilterLayoutView.OnLayoutClickListener() {
                @Override
                public void onChangeLayout(FilterLayoutView.LayoutType type) {
                    layoutType = type;
                    if (activities != null && activities.size() != 0) {
                        setRecyclerAdapter(activities);
                    }
                }
            });
        }
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
        String filter = "{\"where\":{\"categoryId\":\"" + mCategory.getId() + "\"" + join_status + activity_status + ",\"title\":{\"like\":\"" + searchStr + "\"}}}";
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
