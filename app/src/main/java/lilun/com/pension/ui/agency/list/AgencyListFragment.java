package lilun.com.pension.ui.agency.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import java.util.List;

import butterknife.Bind;
import lilun.com.pension.R;
import lilun.com.pension.app.OrganizationChildrenConfig;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.adapter.AgencyAdapter;
import lilun.com.pension.module.adapter.AgencyServiceAdapter;
import lilun.com.pension.module.bean.Organization;
import lilun.com.pension.module.bean.OrganizationProduct;
import lilun.com.pension.module.utils.Preconditions;
import lilun.com.pension.ui.agency.detail.AgencyDetailFragment;
import lilun.com.pension.ui.agency.detail.ServiceDetailFragment;
import lilun.com.pension.widget.NormalItemDecoration;
import lilun.com.pension.widget.NormalTitleBar;

/**
 * 养老机构V
 *
 * @author yk
 *         create at 2017/2/7 16:04
 *         email : yk_developer@163.com
 */
public class AgencyListFragment extends BaseFragment<AgencyListContract.Presenter> implements AgencyListContract.View {


    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.swipe_layout)
    SwipeRefreshLayout mSwipeLayout;

    @Bind(R.id.titleBar)
    NormalTitleBar titleBar;
    private String mCategoryId;
    private String mTitle;
    private AgencyAdapter mAgencyAdapter;
    private AgencyServiceAdapter mAgencyServiceAdapter;
    private int mType;

    public static AgencyListFragment newInstance(String title, String categoryId, int type) {
        AgencyListFragment fragment = new AgencyListFragment();
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
        return R.layout.layout_recycler;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        titleBar.setTitle(mTitle);
        titleBar.setOnBackClickListener(this::pop);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false));
        if (mType==0){
            mRecyclerView.addItemDecoration(new NormalItemDecoration(27));
        }else{
            mRecyclerView.addItemDecoration(new NormalItemDecoration(17));
        }
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
        if (savedInstanceState == null) {
            mSwipeLayout.setRefreshing(true);
            getData(0);
        }
    }

    private void getData(int skip) {
        if (mType == 0) {
            getOrganizations(skip);
        } else if (mType==1){
            getProductsByCategoryId(skip);
        }else if (mType==2){
            getProductsByOrganizationId(skip);
        }
    }


    private void getProductsByCategoryId(int skip) {
        String filter = "{\"where\":{\"categoryId\":\"" + mCategoryId + "\"}}";
        mPresenter.getProductAgency(filter, skip);
    }

    private void getProductsByOrganizationId(int skip) {
        String filter = "{\"where\":{\"organizationId\":\"" + OrganizationChildrenConfig.product(mCategoryId) + "\"}}";
        mPresenter.getProductAgency(filter, skip);
    }


    private void getOrganizations(int skip) {
        mPresenter.getOrganizationAgency(mCategoryId, "",skip);
    }


    @Override
    public void showProducts(List<OrganizationProduct> products, boolean isLoadMore) {
        completeRefresh();
        if (products != null) {
            if (mAgencyServiceAdapter == null) {
                mAgencyServiceAdapter = new AgencyServiceAdapter(this, products);
                mAgencyServiceAdapter.setOnItemClickListener(product -> {
                    start(ServiceDetailFragment.newInstance(product),SINGLETASK);
                });
                mRecyclerView.setAdapter(mAgencyServiceAdapter);
            } else if (isLoadMore) {
                mAgencyServiceAdapter.addAll(products);
            } else {
                mAgencyServiceAdapter.replaceAll(products);
            }
        }
    }

    @Override
    public void showOrganizations(List<Organization> organizations, boolean isLoadMore) {
        completeRefresh();
        if (organizations != null) {
            if (mAgencyAdapter == null) {
                mAgencyAdapter = new AgencyAdapter(this, organizations);
                mAgencyAdapter.setOnItemClickListener(agency -> {
                    start(AgencyDetailFragment.newInstance("",agency),SINGLETASK);
                });
                mRecyclerView.setAdapter(mAgencyAdapter);
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
