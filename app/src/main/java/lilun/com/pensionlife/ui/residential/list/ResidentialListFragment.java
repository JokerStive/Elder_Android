package lilun.com.pensionlife.ui.residential.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import butterknife.Bind;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.Config;
import lilun.com.pensionlife.app.User;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.adapter.AgencyServiceAdapter;
import lilun.com.pensionlife.module.bean.OrganizationProduct;
import lilun.com.pensionlife.module.bean.OrganizationProductCategory;
import lilun.com.pensionlife.module.utils.Preconditions;
import lilun.com.pensionlife.ui.agency.detail.ProductDetailFragment;
import lilun.com.pensionlife.widget.NormalItemDecoration;
import lilun.com.pensionlife.widget.SearchTitleBar;
import lilun.com.pensionlife.widget.filter_view.FilterView;

/**
 * 居家服务列表V
 *
 * @author yk
 *         create at 2017/2/7 16:04
 *         email : yk_developer@163.com
 */
public class ResidentialListFragment extends BaseFragment<ResidentialListContract.Presenter> implements ResidentialListContract.View {


    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.swipe_layout)
    SwipeRefreshLayout mSwipeLayout;

    @Bind(R.id.searchBar)
    SearchTitleBar searchBar;

    @Bind(R.id.filter_view)
    FilterView filterView;

    private OrganizationProductCategory organizationProductCategory;
    private AgencyServiceAdapter mAdapter;
    private boolean mIsMerchant;
    private SearchTitleBar.LayoutType layoutType = SearchTitleBar.LayoutType.BIG;
    private List<OrganizationProduct> products;


    public static ResidentialListFragment newInstance(OrganizationProductCategory organizationProductCategory) {
        ResidentialListFragment fragment = new ResidentialListFragment();
        Bundle args = new Bundle();
        args.putSerializable("OrganizationProductCategory", organizationProductCategory);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void getTransferData(Bundle arguments) {
        mIsMerchant = !User.isCustomer();
        organizationProductCategory = (OrganizationProductCategory) arguments.getSerializable("OrganizationProductCategory");
        Preconditions.checkNull(organizationProductCategory);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ResidentialListPresenter();
        mPresenter.bindView(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_search_filter_list;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        searchBar.setFragment(this);
        searchBar.setOnItemClickListener(new SearchTitleBar.OnItemClickListener() {
            @Override
            public void onBack() {
                pop();
            }

            @Override
            public void onChangeLayout(SearchTitleBar.LayoutType type) {
                layoutType = type;
                if (products != null && products.size() != 0) {
                    setRecyclerAdapter(products);
                }
            }

            @Override
            public void onSearch(String searchStr) {
//                conditionMap.put(condition_title,searchStr);
//                getHelps(0);
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new NormalItemDecoration(Config.list_decoration));
        //刷新
        mSwipeLayout.setOnRefreshListener(() -> {
                    if (mPresenter != null) {
                        getData(0);
                    }
                }
        );
    }


    private void setRecyclerAdapter(List<OrganizationProduct> products) {
        mAdapter = getAdapterFromLayoutType(products);
        if (mAdapter != null) {
            mAdapter.setOnItemClickListener((product) -> {
                start(ProductDetailFragment.newInstance(product.getId()), SINGLETASK);
            });
            mAdapter.setEmptyView();
            mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                @Override
                public void onLoadMoreRequested() {
                    getData(mAdapter.getItemCount());
                }
            }, mRecyclerView);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    private AgencyServiceAdapter getAdapterFromLayoutType(List<OrganizationProduct> products) {
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
        super.onLazyInitView(savedInstanceState);
        mSwipeLayout.setRefreshing(true);
        getData(0);
    }

    private void getData(int skip) {
        String filter;
        if (mIsMerchant) {
            filter = "{\"where\":{\"categoryId\":\"" + organizationProductCategory.getId() + "\",\"creatorId\":\"" + User.getUserId() + "\"}}";
        } else {
            filter = "{\"where\":{\"categoryId\":\"" + organizationProductCategory.getId() + "\",\"areaIds\":[" + User.getCurrentOrganizationId() + "]}}";
        }
        mPresenter.getResidentialServices(filter, skip);
    }


    @Override
    public void showResidentialServices(List<OrganizationProduct> products, boolean isLoadMore) {
        this.products = products;
        completeRefresh();
        if (products != null) {
            if (mAdapter == null) {
                setRecyclerAdapter(products);
            } else if (isLoadMore) {
                mAdapter.addAll(products, Config.defLoadDatCount);
            } else {
                mAdapter.replaceAll(products);
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
