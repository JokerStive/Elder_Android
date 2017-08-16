package lilun.com.pensionlife.ui.agency.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.app.OrganizationChildrenConfig;
import lilun.com.pensionlife.app.User;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.adapter.AgencyServiceAdapter;
import lilun.com.pensionlife.module.adapter.NormalFilterAdapter;
import lilun.com.pensionlife.module.bean.ConditionOption;
import lilun.com.pensionlife.module.bean.Organization;
import lilun.com.pensionlife.module.bean.OrganizationProduct;
import lilun.com.pensionlife.module.utils.Preconditions;
import lilun.com.pensionlife.ui.agency.detail.ProductDetailFragment;
import lilun.com.pensionlife.widget.DividerDecoration;
import lilun.com.pensionlife.widget.FilterPriceView;
import lilun.com.pensionlife.widget.SearchTitleBar;
import lilun.com.pensionlife.widget.filter_view.FilterView;

/**
 * 养老机构V
 *
 * @author yk
 *         create at 2017/2/7 16:04
 *         email : yk_developer@163.com
 */
public class ProductListFragment extends BaseFragment<AgencyListContract.Presenter> implements AgencyListContract.View {
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
    private AgencyServiceAdapter mAgencyServiceAdapter;
    // 0 获取服务从categoryId  1 获取服务从organizationId


    private int mType;
    private SearchTitleBar.LayoutType layoutType;
    private List<OrganizationProduct> products;
    //    private String[] filterTitle = new String[]{"区域", "价格", "星级"};
    private ProductFilter productFilter = new ProductFilter();


    /**
     * @param title
     * @param categoryId 组织id  或 类型
     * @param type       0-查询区域服务   1-查询商家所有服务
     * @return
     */
    public static ProductListFragment newInstance(String title, String categoryId, int type) {
        ProductListFragment fragment = new ProductListFragment();
        Bundle args = new Bundle();
        args.putString("categoryId", categoryId);
        args.putString("title", title);
        args.putInt("type", type);
        fragment.setArguments(args);
        return fragment;
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
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_search_filter_list;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        searchBar.isChangeLayout(User.isCustomer());
        layoutType = User.isCustomer() ? SearchTitleBar.LayoutType.SMALL : SearchTitleBar.LayoutType.SMALL;
        searchBar.setNoNullLayout();
        searchBar.setFragment(this);
        searchBar.setOnItemClickListener(new SearchTitleBar.OnItemClickListener() {
            @Override
            public void onBack() {
                pop();
            }

            @Override
            public void onSearch(String searchStr) {
                ProductFilter.WhereBean.TitleBean titleBean = productFilter.getWhere().getTitle();
                if (titleBean == null) {
                    titleBean = new ProductFilter.WhereBean.TitleBean();
                }
                titleBean.setLike(searchStr);
                productFilter.getWhere().setTitle(titleBean);
                refreshProductWithFilter();
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
        mRecyclerView.addItemDecoration(new DividerDecoration(App.context, LinearLayoutManager.VERTICAL, 1, App.context.getResources().getColor(R.color.gray)));
        //刷新
        mSwipeLayout.setOnRefreshListener(() -> {
                    if (mPresenter != null) {
                        refreshProductWithFilter();
                    }
                }
        );


        initFilterView();
        initFilter();

    }


    private void refreshProductWithFilter() {
        getProducts(0);
    }


    private void initFilterView() {
        List<View> pops = new ArrayList<>();
        List<String> filterTitles = new ArrayList<>();

        //除了区域以外的条件弹窗
        List<ConditionOption> conditionOptionsList = mPresenter.getConditionOptionsList("score");
        if (conditionOptionsList != null) {
            for (int i = 0; i < conditionOptionsList.size(); i++) {
                ConditionOption conditionOption = conditionOptionsList.get(i);
                filterTitles.add(conditionOption.getCondition());
                RecyclerView recyclerView = new RecyclerView(App.context);
                recyclerView.setLayoutManager(new LinearLayoutManager(App.context, LinearLayoutManager.VERTICAL, false));
                NormalFilterAdapter adapter = new NormalFilterAdapter(conditionOption);
                adapter.setOnItemClickListener((position, title, whereKey, whereValue) -> {
                    filterView.setTabText(position == 0 ? "智能排序" : title, position == 0);
                    productFilter.where.setScore(null);
                    if (whereKey.equals("score") && whereValue != null) {
                        productFilter.where.setScore(Integer.parseInt(whereValue));
                    }
                    refreshProductWithFilter();
                });
                recyclerView.setAdapter(adapter);
                pops.add(recyclerView);
            }
        }

        //价格筛选
        filterTitles.add("筛选");
        FilterPriceView priceRangeView = new FilterPriceView(_mActivity, "价格");
        priceRangeView.setOnConfirmListener((range, show, isDef) -> {

            productFilter.where.setPrice(null);
            //如果沒有价格区间，并且不显示，就是隐藏
            if (range == null && TextUtils.isEmpty(show)) {
                filterView.hint();
                return;
            } else if (range != null) {
                ProductFilter.WhereBean.PriceBean price = new ProductFilter.WhereBean.PriceBean();
                price.setBetween(range);
                productFilter.where.setPrice(price);
            }
            filterView.setTabText(show, isDef);
            refreshProductWithFilter();
        });
        priceRangeView.setUnit("元");
        pops.add(priceRangeView);


        //TODO 区域
//        filterTitles.add(0, "区域");
//        AreaFilter areaFilter = new AreaFilter(mContent, 3, "区域");
//        areaFilter.setOnItemClickListener((id, name, isDef) -> {
//            Logger.d("选中的区域-- " + id);
//            filterView.setTabText(name, isDef);
//            productFilter.where.setAreasList(id);
//            refreshProductWithFilter();
//        });
//        pops.add(0, areaFilter);
        filterView.setTitlesAndPops(filterTitles, pops, mSwipeLayout);
    }

    private void initFilter() {
        if (mType != 0) {
            productFilter.where.setOrganizationId(OrganizationChildrenConfig.product(mCategoryId));
            productFilter.where.setAreaIds(null);
        } else {
            productFilter.where.setCategoryId(mCategoryId);
//            productFilter.where.getAreaIds().setInq(User.levelIds(true));
//            if (mCategoryId.contains(Constants.service_residentail)) {
            //如果居家服务，就要服务区域是当前组织的层级id
//            }
//            if (!User.isCustomer()) {
//                productFilter.where.setCreatorId(User.getUserId());
//            }

        }
    }


    private void setServiceRecyclerAdapter(List<OrganizationProduct> products) {
        mAgencyServiceAdapter = getServiceAdapterFromLayoutType(products);
        if (mAgencyServiceAdapter != null) {
            mAgencyServiceAdapter.setOnItemClickListener((product) -> {
                start(ProductDetailFragment.newInstance(product.getId()), SINGLETASK);
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
            final AgencyServiceAdapter finalAdapter = adapter;
            adapter.setOnLoadMoreListener(() -> getProducts(finalAdapter.getItemCount()));
        }
        return adapter;
    }


    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        refreshProductWithFilter();
    }


    private void getProducts(int skip) {
        mSwipeLayout.setRefreshing(true);
        Gson gson = new Gson();

        String filter = gson.toJson(productFilter);
        Logger.d("product--filter = " + filter);
        mPresenter.getProductAgency(filter, skip);
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
