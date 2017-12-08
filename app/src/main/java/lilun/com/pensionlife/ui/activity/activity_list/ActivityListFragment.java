package lilun.com.pensionlife.ui.activity.activity_list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.vanzh.library.utils.Lists;

import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.app.Config;
import lilun.com.pensionlife.app.Event;
import lilun.com.pensionlife.app.User;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.adapter.OrganizationActivityAdapter;
import lilun.com.pensionlife.module.bean.ActivityCategory;
import lilun.com.pensionlife.module.bean.ConditionOption;
import lilun.com.pensionlife.module.bean.Option;
import lilun.com.pensionlife.module.bean.OrganizationActivity;
import lilun.com.pensionlife.module.utils.Preconditions;
import lilun.com.pensionlife.ui.activity.activity_detail.ActivityDetailFragment;
import lilun.com.pensionlife.widget.NormalItemDecoration;
import lilun.com.pensionlife.widget.SearchTitleBar;
import lilun.com.pensionlife.widget.filter_view.FilterLayoutView;

/**
 * 分类活动V
 * 获取的数据为：
 * 2017-12-08 10:05:42 更新 https://oa.liluntech.com/issues/9195
 *  1\社区活动发布对于已经开始活动应该能够在分类中显示 ,目前在分类中看不到.
 2\社区活动自己发布的活动应该能够在分类中显示 ,目前在分类中看不到.
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
        searchBar.setLayoutTypeIcon(SearchTitleBar.LayoutType.BIG);
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
                    mActivityAdapter.removeAllFooterView();
                }
                getActivityList(skip);
            }

            @Override
            public void onChangeLayout(SearchTitleBar.LayoutType type) {
                //小图切换成大图,重设Adapter
                layoutType = type;
                if (mActivityAdapter != null && Lists.isEmpty(mActivityAdapter.getData())) return;
                setRecyclerAdapter(mActivityAdapter.getData());

            }
        });


        mRecyclerView.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new NormalItemDecoration(10));
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (mActivityAdapter != null) {
                    if (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                        mActivityAdapter.setmScrollIdle(true);
                        mActivityAdapter.notifyDataChanged();
                    } else
                        mActivityAdapter.setmScrollIdle(false);
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        //刷新
        mSwipeLayout.setOnRefreshListener(() -> {
                    if (mPresenter != null) {
                        skip = 0;
                        if (mActivityAdapter != null) {
                            mActivityAdapter.removeAllFooterView();
                        }
                        getActivityList(skip);
                    }
                }
        );
        //设置筛选条件
        initConditionModules();
        //设置 Adapter
        setRecyclerAdapter(new ArrayList<>());
    }


    private void setRecyclerAdapter(List<OrganizationActivity> activities) {
        mActivityAdapter = getAdapterFromLayoutType(activities);
        mActivityAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
        mActivityAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getActivityList(skip);
            }
        }, mRecyclerView);
        if (mActivityAdapter != null) {

            mActivityAdapter.setOnItemClickListener((baseQuickAdapter, view, i) -> {
                start(ActivityDetailFragment.newInstance(mActivityAdapter.getItem(i)));
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
                        partner_number = partnerNumber[0];
                    } else {  //升序
                        partner_number = partnerNumber[1];
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
                mActivityAdapter.removeAllFooterView();
            }
            getActivityList(skip);
        }
    }

    private void getActivityList(int skip) {
        // TODO 关联organizationId
        //{"where":{"categoryId":"/地球村/中国/重庆/重庆市/南岸区/铜元局街道/A小区/#topic_activity-category.旅游","status":"checking","and":[{"masterId":{"neq":"2c690650-3483-11e7-90b6-8f0c1da0aab2"}},{"partnerList":{"neq":"2c690650-3483-11e7-90b6-8f0c1da0aab2"}}],"or":[{"repeatedDesc":{"like": ""}},{"startTime":{"gt":"2017-05-09 15:54:22"}}],"title":{"like":""}},"order":"createdAt DESC","limit":"20","skip":"0"}
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String localtime = format.format(new Date());
        //未开始    现在时间<开始时间
        activity_status = "\"startTime\":{\"gt\":\"" + localtime + "\"}";
        //join_status = ",\"and\":[{\"masterId\":{\"neq\":\"" + User.getUserId() + "\"}},{\"partnerList\":{\"neq\":\"" + User.getUserId() + "\"}}]";
        join_status = ",\"and\":[{\"partnerList\":{\"neq\":\"" + User.getUserId() + "\"}}]";

//        String filter = "{\"where\":{\"visible\":0,\"categoryId\":{\"inq\":" + getCategoryIdJson(mCategory.getId()) + "}" + join_status +
//                ",\"or\":[{\"startTime\":{\"$exists\":false}},{" + activity_status + "}]" + ",\"title\":{\"like\":\"" + searchStr + "\"}}" + timing_status + partner_number + "}";
        String filter = "{\"where\":{\"visible\":0,\"categoryId\":{\"inq\":" + getCategoryIdJson(mCategory.getId()) + "}" + join_status +
                ",\"or\":[{\"startTime\":{\"$exists\":false}}]" + ",\"title\":{\"like\":\"" + searchStr + "\"}}" + timing_status + partner_number + "}";
        mPresenter.getOrganizationActivities(filter, skip);
    }

    /**
     * 获取当前路径及其上次路径的类型，并转为json格式
     *
     * @param curId
     * @return
     */
    public String getCategoryIdJson(String curId) {
        String organization_root_zg = "/地球村/中国";
        String ret = "[";
        if (curId == null) return "[]";
        String[] split = curId.replace(organization_root_zg + "/", "").split("/");
        if (split.length < 3) return "[]";
        String[] categorys = new String[split.length - 2];
        for (int i = 0; i < categorys.length; i++) {
            categorys[i] = organization_root_zg;
            for (int j = 0; j < split.length - i; j++) {
                if (j == split.length - i - 1)
                    categorys[i] += "/" + split[split.length - 1];
                else
                    categorys[i] += "/" + split[j];
            }
            ret += ",\"" + categorys[i] + "\"";
        }
        ret = ret.replaceFirst(",", "") + "]";

        return ret;
    }

    /**
     * @param activities
     * @param isFirstLoad 该数据是否为加载更多状态
     */
    @Override
    public void showActivityList(List<OrganizationActivity> activities, boolean isFirstLoad) {
        completeRefresh();
        skip += activities.size();

        if (isFirstLoad) {
            mActivityAdapter.replaceAll(activities);
        } else {
            mActivityAdapter.addAll(activities, Config.defLoadDatCount);
        }
//        mActivityAdapter.notifyDataChangedAfterLoadMore(true);//取消正在加载并设置加载更多

        //获取的数据比请求数据少，说明没有更多数据
//        if (activities.size() < Config.defLoadDatCount) {
//            TextView nodata = new TextView(getContext());
//            nodata.setText("-没有更多数据-");
//            nodata.setGravity(Gravity.CENTER);
//            if (App.widthDP > 820)
//                nodata.setTextSize(getResources().getDimension(R.dimen.sp_14));
//            mActivityAdapter.addFooterView(nodata);
//        }

    }

    @Override
    public void completeRefresh() {
        if (mSwipeLayout != null && mSwipeLayout.isRefreshing()) {
            mSwipeLayout.setRefreshing(false);
        }
    }


}
