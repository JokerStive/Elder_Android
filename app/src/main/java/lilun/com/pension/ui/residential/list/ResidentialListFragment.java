package lilun.com.pension.ui.residential.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import java.util.List;

import butterknife.Bind;
import lilun.com.pension.R;
import lilun.com.pension.app.User;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.adapter.AgencyServiceAdapter;
import lilun.com.pension.module.bean.OrganizationProduct;
import lilun.com.pension.module.bean.ProductCategory;
import lilun.com.pension.module.utils.Preconditions;
import lilun.com.pension.ui.agency.detail.ServiceDetailFragment;
import lilun.com.pension.widget.NormalItemDecoration;
import lilun.com.pension.widget.NormalTitleBar;

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

    @Bind(R.id.titleBar)
    NormalTitleBar titleBar;
    private ProductCategory productCategory;
    private AgencyServiceAdapter mAdapter;
    private boolean mIsMerchant;


    public static ResidentialListFragment newInstance(ProductCategory productCategory) {
        ResidentialListFragment fragment = new ResidentialListFragment();
        Bundle args = new Bundle();
        args.putSerializable("productCategory", productCategory);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void getTransferData(Bundle arguments) {
        mIsMerchant = !User.isCustomer();
        productCategory = (ProductCategory) arguments.getSerializable("productCategory");
        Preconditions.checkNull(productCategory);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ResidentialListPresenter();
        mPresenter.bindView(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_recycler;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        titleBar.setTitle(productCategory.getName());
        titleBar.setRightText(getString(R.string.new_service));
        titleBar.setOnBackClickListener(this::pop);
        titleBar.setOnRightClickListener(() -> {
            //TODO 发布一个服务
        });


        mRecyclerView.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new NormalItemDecoration(27));
        //刷新
        mSwipeLayout.setOnRefreshListener(() -> {
                    if (mPresenter != null) {
                        getData(0);
                    }
                }
        );
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
            filter = "{\"where\":{\"categoryId\":\"" + productCategory.getId() + "\",\"creatorId\":\"" + User.getUserId() + "\"}}";
        } else {
            filter = "{\"where\":{\"categoryId\":\"" + productCategory.getId() + "\"}}";
        }
        mPresenter.getResidentialServices(filter, skip);
    }


    @Override
    public void showResidentialServices(List<OrganizationProduct> products, boolean isLoadMore) {
        completeRefresh();
        if (products != null) {
            if (mAdapter == null) {
                mAdapter = new AgencyServiceAdapter(products,R.layout.item_agency_service);
                mAdapter.setOnItemClickListener(product -> {
                    start(ServiceDetailFragment.newInstance(product));
                });
                mAdapter.setEmptyView();
                mRecyclerView.setAdapter(mAdapter);
            } else if (isLoadMore) {
                mAdapter.addAll(products);
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
