package lilun.com.pension.ui.agency.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import lilun.com.pension.R;
import lilun.com.pension.app.App;
import lilun.com.pension.app.OrganizationChildrenConfig;
import lilun.com.pension.app.User;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.adapter.AgencyServiceAdapter;
import lilun.com.pension.module.adapter.NormalFilterAdapter;
import lilun.com.pension.module.bean.ConditionOption;
import lilun.com.pension.module.bean.Organization;
import lilun.com.pension.module.bean.OrganizationProduct;
import lilun.com.pension.module.utils.Preconditions;
import lilun.com.pension.ui.agency.detail.ServiceDetailFragment;
import lilun.com.pension.widget.NormalItemDecoration;
import lilun.com.pension.widget.SearchTitleBar;
import lilun.com.pension.widget.filter_view.AreaFilter;
import lilun.com.pension.widget.filter_view.FilterView;

/**
 * 养老机构V
 *
 * @author yk
 *         create at 2017/2/7 16:04
 *         email : yk_developer@163.com
 */
public class AgencyServiceListFragment extends BaseFragment<AgencyListContract.Presenter> implements AgencyListContract.View {
    @Bind(R.id.searchBar)
    SearchTitleBar searchBar;

    @Bind(R.id.filter_view)
    FilterView filterView;

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.swipe_layout)
    SwipeRefreshLayout mSwipeLayout;

    @Bind(R.id.fab_add_service)
    FloatingActionButton fabAddService;

    private String mCategoryId;
    private String mTitle;
    private AgencyServiceAdapter mAgencyServiceAdapter;
    // 0 获取服务从categoryId  1 获取服务从organizationId
    private int mType;
    private SearchTitleBar.LayoutType layoutType;
    private List<OrganizationProduct> products;
    private Map<String, Object> conditionMap;
    private Map<String, String> whereConditionMap;


    public static AgencyServiceListFragment newInstance(String title, String categoryId, int type) {
        AgencyServiceListFragment fragment = new AgencyServiceListFragment();
        Bundle args = new Bundle();
        args.putString("categoryId", categoryId);
        args.putString("title", title);
        args.putInt("type", type);
        fragment.setArguments(args);
        return fragment;
    }


    @OnClick({R.id.fab_add_service})
    public void click(View view) {
        //TODO 新增一个服务
    }

    @Override
    protected void getTransferData(Bundle arguments) {
        mCategoryId = arguments.getString("categoryId");
        mTitle = arguments.getString("title");
        mType = arguments.getInt("type");
        Preconditions.checkNull(mTitle);
        Preconditions.checkNull(mCategoryId);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new AgencyListPresenter();
        mPresenter.bindView(this);
        conditionMap = new HashMap<>();
        whereConditionMap = new HashMap<>();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_search_filter_list;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        searchBar.isChangeLayout(User.isCustomer());
        layoutType = User.isCustomer() ? SearchTitleBar.LayoutType.BIG : SearchTitleBar.LayoutType.SMALL;
        searchBar.setNoNullLayout();
        searchBar.setFragment(this);
        searchBar.setOnItemClickListener(new SearchTitleBar.OnItemClickListener() {
            @Override
            public void onBack() {
                pop();
            }

            @Override
            public void onSearch(String searchStr) {
                whereConditionMap.put("name", "{\"like\":\"" + searchStr + "\"}");
                getData(0);
            }

            @Override
            public void onChangeLayout(SearchTitleBar.LayoutType type) {
                layoutType = type;
                if (products != null && products.size() != 0) {
                    setServiceRecyclerAdapter(products);
                }
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new NormalItemDecoration(10));
        //刷新
        mSwipeLayout.setOnRefreshListener(() -> {
                    if (mPresenter != null) {
                        getData(0);
                    }
                }
        );


        initFilter();

    }

    private void initFilter() {
        List<View> pops = new ArrayList<>();
        List<String> filterTitles = new ArrayList<>();
        filterTitles.add("区域");
        //除了区域以外的条件弹窗
        List<ConditionOption> conditionOptionsList = mPresenter.getConditionOptionsList();
        if (conditionOptionsList != null) {
            for (int i = 0; i < conditionOptionsList.size(); i++) {
                ConditionOption conditionOption = conditionOptionsList.get(i);
                filterTitles.add(conditionOption.getCondition());
                RecyclerView recyclerView = new RecyclerView(App.context);
                recyclerView.setLayoutManager(new LinearLayoutManager(App.context, LinearLayoutManager.VERTICAL, false));
                NormalFilterAdapter adapter = new NormalFilterAdapter(conditionOption);
                final int finalI = i + 1;
                adapter.setOnItemClickListener((position, title, whereKey, whereValue) -> {
                    filterView.setTabText(position == 0 ? filterTitles.get(finalI) : title, position == 0);
                });
                recyclerView.setAdapter(adapter);
                pops.add(recyclerView);
            }
        }


        //TODO 区域
        AreaFilter areaFilter = new AreaFilter(mContent);
        pops.add(0, areaFilter);
        filterView.setTitlesAndPops(filterTitles, pops, mSwipeLayout);
    }


    private void setServiceRecyclerAdapter(List<OrganizationProduct> products) {
        mAgencyServiceAdapter = getServiceAdapterFromLayoutType(products);
        if (mAgencyServiceAdapter != null) {
            mAgencyServiceAdapter.setOnItemClickListener((product) -> {
                start(ServiceDetailFragment.newInstance(product), SINGLETASK);
            });
            mAgencyServiceAdapter.setEmptyView();
        }
        mRecyclerView.setAdapter(mAgencyServiceAdapter);
    }

    private AgencyServiceAdapter getServiceAdapterFromLayoutType(List<OrganizationProduct> products) {
        AgencyServiceAdapter adapter = null;
        int layoutId = 0;
        if (layoutType == SearchTitleBar.LayoutType.BIG) {
            layoutId = R.layout.item_agency_service_big;
        } else if (layoutType == SearchTitleBar.LayoutType.SMALL) {
            layoutId = R.layout.item_agency_service_small;
        }
        if (layoutId != 0) {
            adapter = new AgencyServiceAdapter(products, layoutId);
        }
        return adapter;
    }


    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            mSwipeLayout.setRefreshing(true);
            getData(0);
        }
    }

    private void getData(int skip) {
        if (mType == 0) {
            getProductsByCategoryId(skip);
        } else {
            getProductsByOrganizationId(skip);
        }
    }


    private void getProductsByCategoryId(int skip) {
        // TODO 有商家判断
        if (User.isCustomer()) {
            whereConditionMap.put("categoryId", mCategoryId);
//            filter = "{\"where\":{\"categoryId\":\"" + mCategoryId + "\"}}";
        } else {
            whereConditionMap.put("categoryId", mCategoryId);
            whereConditionMap.put("creatorId", User.getUserId());
//            filter = "{\"where\":{\"categoryId\":\"" + mCategoryId + "\",\"creatorId\":\"" + User.getUserId() + "\"}}";
        }
        String filter = getFilterWithCondition();
        mPresenter.getProductAgency(filter, skip);
    }

    private void getProductsByOrganizationId(int skip) {
        String filter = "{\"where\":{\"organizationId\":\"" + OrganizationChildrenConfig.product(mCategoryId) + "\"}}";
        mPresenter.getProductAgency(filter, skip);
    }

    private String getFilterWithCondition() {
        conditionMap.put("where", whereConditionMap);
        String filter;
        Gson gson = new Gson();
        filter = gson.toJson(conditionMap);
        Logger.d("agency service filter = " + filter);
        return filter;
    }

    @Override
    public void showProducts(List<OrganizationProduct> products, boolean isLoadMore) {
        this.products = products;
        completeRefresh();
        if (products != null) {
            if (mAgencyServiceAdapter == null) {
                setServiceRecyclerAdapter(products);
            } else if (isLoadMore) {
                mAgencyServiceAdapter.addAll(products);
            } else {
                mAgencyServiceAdapter.replaceAll(products);
            }
        }
    }

    @Override
    public void showOrganizations(List<Organization> organizations, boolean isLoadMore) {
    }

    @Override
    public void completeRefresh() {
        if (mSwipeLayout != null && mSwipeLayout.isRefreshing()) {
            mSwipeLayout.setRefreshing(false);
        }
    }


}
