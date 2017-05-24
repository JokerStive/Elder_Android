package lilun.com.pension.ui.activity.activity_list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;

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
import lilun.com.pension.ui.activity.activity_detail.ActivityDetailFragment;
import lilun.com.pension.widget.NormalItemDecoration;
import lilun.com.pension.widget.SearchTitleBar;
import lilun.com.pension.widget.filter_view.FilterLayoutView;

/**
 * 分类活动V
 * 获取的数据为：未参加的且不是我创建的且不是已完结的
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

    private SearchTitleBar.LayoutType layoutType = SearchTitleBar.LayoutType.BIG;
    private List<OrganizationActivity> activities;
    private String searchStr = "";
    private String join_status = "";
    private String partnerNumber[] = {",\"order\":\"partnerCount DESC\"", ",\"order\":\"partnerCount ASC\""};
    private String partner_number = "";
    private String activity_status = "";
    private String timeOrder[] = {",\"order\":\"createdAt DESC\"", ",\"order\":\"createdAt ASC\""};
    private String timing_status = timeOrder[0];
    private int skip = 0;

    public static ActivityListFragment newInstance(ActivityCategory category) {
        ActivityListFragment fragment = new ActivityListFragment();
        Bundle args = new Bundle();
        args.putSerializable("OrganizationActivityCategory", category);
        fragment.setArguments(args);
        return fragment;
    }

    @Subscribe
    public void refreshData(Event.RefreshActivityData event) {
        skip = 0;
        if (mActivityAdapter != null) {
            mActivityAdapter.openLoadMore(20, true);
            mActivityAdapter.removeAllFooterView();
        }
        getActivityList(skip);
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
        searchBar.setOnItemClickListener(new SearchTitleBar.OnItemClickListener() {
            @Override
            public void onBack() {
                pop();
            }

            @Override
            public void onSearch(String str) {
                searchStr = str;
                skip = 0;
                if (mActivityAdapter != null) {
                    mActivityAdapter.openLoadMore(20, true);
                    mActivityAdapter.removeAllFooterView();
                }
                getActivityList(skip);
            }

            @Override
            public void onChangeLayout(SearchTitleBar.LayoutType type) {
                layoutType = type;
                if (activities != null && activities.size() != 0) {
                    setRecyclerAdapter(activities);
                }
            }
        });


        mRecyclerView.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new NormalItemDecoration(10));
        //刷新
        mSwipeLayout.setOnRefreshListener(() -> {
                    if (mPresenter != null) {
                        skip = 0;
                        if (mActivityAdapter != null) {
                            mActivityAdapter.openLoadMore(20, true);
                            mActivityAdapter.removeAllFooterView();
                        }
                        getActivityList(skip);
                    }
                }
        );

        initConditionModules();
    }


    private void setRecyclerAdapter(List<OrganizationActivity> activities) {
        mActivityAdapter = getAdapterFromLayoutType(activities);
        mActivityAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
        mActivityAdapter.openLoadMore(20, true);
        mActivityAdapter.setOnLoadMoreListener(20, new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {

                getActivityList(skip);
            }
        });
        if (mActivityAdapter != null) {
            mActivityAdapter.setOnItemClickListener((activityItem) -> {
                start(ActivityDetailFragment.newInstance(activityItem));
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
        actJoinOptions.add(new Option("0", App.context.getResources().getStringArray(R.array.partners_number)[0]));
        actJoinOptions.add(new Option("1", App.context.getResources().getStringArray(R.array.partners_number)[1]));
        conditionOptionsList.add(new ConditionOption(
                App.context.getResources().getStringArray(R.array.activity_filter_status)[0], "0", actJoinOptions));


        List<Option> acttimingStatusOptions = new ArrayList<>();
        acttimingStatusOptions.add(new Option("", App.context.getResources().getStringArray(R.array.timing_status)[0]));
        acttimingStatusOptions.add(new Option("0", App.context.getResources().getStringArray(R.array.timing_status)[1]));
        conditionOptionsList.add(new ConditionOption(
                App.context.getResources().getStringArray(R.array.activity_filter_status)[1], "1", acttimingStatusOptions));
        if (conditionOptionsList != null) {

            filterView.setTitlesAndDatas(filterTitles, conditionOptionsList, mSwipeLayout);
            filterView.setOnOptionClickListener((whereKey, whereValue) -> {
                Log.d("zp", whereKey + "  " + whereValue);
                //我的状态
                if (whereKey.equals(App.context.getResources().getStringArray(R.array.activity_filter_status)[0])) {
                    if ("0".equals(whereValue)) {  //降序
                        partner_number = partnerNumber[1];
                    } else {  //升序
                        partner_number = partnerNumber[0];
                    }
                    timing_status = "";
                }
                if (whereKey.equals(App.context.getResources().getStringArray(R.array.activity_filter_status)[1])) {
                    if ("0".equals(whereValue)) {  //降序
                        timing_status = timeOrder[1];
                    } else {  //升序
                        timing_status = timeOrder[0];
                    }
                    partner_number = "";
                }
                skip = 0;
                if (mActivityAdapter != null) {
                    mActivityAdapter.openLoadMore(20, true);
                    mActivityAdapter.removeAllFooterView();
                }
                getActivityList(skip);
            });
        }

    }


    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            mSwipeLayout.setRefreshing(true);
            skip = 0;
            if (mActivityAdapter != null) {
                mActivityAdapter.openLoadMore(20, true);
                mActivityAdapter.removeAllFooterView();
            }
            getActivityList(skip);
        }
    }

    private void getActivityList(int skip) {
        // TODO 关联organizationId
        //{"where":{"categoryId":"/地球村/中国/重庆/重庆市/南岸区/铜元局街道/A小区/#activity-category.旅游","status":"checking","and":[{"masterId":{"neq":"2c690650-3483-11e7-90b6-8f0c1da0aab2"}},{"partnerList":{"neq":"2c690650-3483-11e7-90b6-8f0c1da0aab2"}}],"or":[{"repeatedDesc":{"like": ""}},{"startTime":{"gt":"2017-05-09 15:54:22"}}],"title":{"like":""}},"order":"createdAt DESC","limit":"20","skip":"0"}
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String localtime = format.format(new Date());
        //未开始    现在时间<开始时间
        activity_status = "\"startTime\":{\"gt\":\"" + localtime + "\"}";
        join_status = ",\"and\":[{\"masterId\":{\"neq\":\"" + User.getUserId() + "\"}},{\"partnerList\":{\"neq\":\"" + User.getUserId() + "\"}}]";

        String filter = "{\"where\":{\"categoryId\":\"" + mCategory.getId() + "\"" + join_status +
                ",\"or\":[{\"startTime\":{\"$exists\":false}},{" + activity_status + "}]" + ",\"title\":{\"like\":\"" + searchStr + "\"}}" + timing_status + partner_number + "}";
        mPresenter.getOrganizationActivities(filter, skip);
    }


    @Override
    public void showActivityList(List<OrganizationActivity> activities, boolean islOadMore) {
        completeRefresh();
        skip += activities.size();
        if (mActivityAdapter != null && activities.size() < mActivityAdapter.getPageSize()) {
            mActivityAdapter.notifyDataChangedAfterLoadMore(false);
            TextView nodata = new TextView(getContext());
            nodata.setText("-没有更多数据-");
            nodata.setGravity(Gravity.CENTER);
            nodata.setTextSize(getResources().getDimension(R.dimen.sp_14));
            mActivityAdapter.addFooterView(nodata);
        }
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
