package lilun.com.pension.ui.agency.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import lilun.com.pension.R;
import lilun.com.pension.app.App;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.adapter.AgencyAdapter;
import lilun.com.pension.module.adapter.NormalFilterAdapter;
import lilun.com.pension.module.bean.ConditionOption;
import lilun.com.pension.module.bean.Organization;
import lilun.com.pension.module.bean.OrganizationProduct;
import lilun.com.pension.module.utils.Preconditions;
import lilun.com.pension.ui.agency.detail.AgencyDetailFragment;
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
public class AgencyOrganizationListFragment extends BaseFragment<AgencyListContract.Presenter> implements AgencyListContract.View {


    @Bind(R.id.searchBar)
    SearchTitleBar searchBar;
    @Bind(R.id.filter_view)
    FilterView filterView;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.swipe_layout)
    SwipeRefreshLayout mSwipeLayout;
    private String mCategoryId;
    private String mTitle;
    private AgencyAdapter mAgencyAdapter;
    private SearchTitleBar.LayoutType layoutType = SearchTitleBar.LayoutType.BIG;
    private List<Organization> organizations;
    private Map<String, Object> conditionMap;
    private Map<String, String> whereConditionMap;


    public static AgencyOrganizationListFragment newInstance(String title, String categoryId) {
        AgencyOrganizationListFragment fragment = new AgencyOrganizationListFragment();
        Bundle args = new Bundle();
        args.putString("categoryId", categoryId);
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void getTransferData(Bundle arguments) {
        mCategoryId = arguments.getString("categoryId");
        mTitle = arguments.getString("title");
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
        searchBar.setNoNullLayout();
        searchBar.setOnItemClickListener(new SearchTitleBar.OnItemClickListener() {
            @Override
            public void onBack() {
                pop();
            }

            @Override
            public void onSearch(String searchStr) {
                whereConditionMap.put("name", "{like," + searchStr + "}");
                getData(0);
            }

            @Override
            public void onChangeLayout(SearchTitleBar.LayoutType type) {
                layoutType = type;
                if (organizations != null && organizations.size() != 0) {
                    setOrganizationRecyclerAdapter(organizations);
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


    private void setOrganizationRecyclerAdapter(List<Organization> organizations) {
        mAgencyAdapter = getAgencyAdapterFromLayoutType(organizations);
        if (mAgencyAdapter != null) {
            mAgencyAdapter.setOnItemClickListener((agency) -> {
                start(AgencyDetailFragment.newInstance("", agency), SINGLETASK);
            });
            mAgencyAdapter.setEmptyView();
        }
        mRecyclerView.setAdapter(mAgencyAdapter);
    }

    private AgencyAdapter getAgencyAdapterFromLayoutType(List<Organization> organizations) {
        AgencyAdapter adapter = null;
        int layoutId = 0;
        if (layoutType == SearchTitleBar.LayoutType.BIG) {
            layoutId = R.layout.item_agency_big;
        } else if (layoutType == SearchTitleBar.LayoutType.SMALL) {
            layoutId = R.layout.item_agency_small;
        }
        if (layoutId != 0) {
            adapter = new AgencyAdapter(organizations, layoutId, layoutType);
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
        String filter = getFilterWithCondition();
        mPresenter.getOrganizationAgency(mCategoryId, filter, skip);
    }


    private String getFilterWithCondition() {
        conditionMap.put("where", whereConditionMap);
        Gson gson = new Gson();
        String filter = gson.toJson(conditionMap);
        Logger.d("agency organization filter = " + filter);
        return filter;
    }


    @Override
    public void showProducts(List<OrganizationProduct> products, boolean isLoadMore) {
    }

    @Override
    public void showOrganizations(List<Organization> organizations, boolean isLoadMore) {
        this.organizations = organizations;
        completeRefresh();
        if (organizations != null) {
            if (mAgencyAdapter == null) {
                setOrganizationRecyclerAdapter(organizations);
            } else if (isLoadMore) {
                mAgencyAdapter.addAll(organizations);
            } else {
                mAgencyAdapter.replaceAll(organizations);
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
