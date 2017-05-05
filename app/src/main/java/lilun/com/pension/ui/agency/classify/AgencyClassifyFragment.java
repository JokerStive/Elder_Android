package lilun.com.pension.ui.agency.classify;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import java.io.Serializable;
import java.util.List;

import butterknife.Bind;
import lilun.com.pension.R;
import lilun.com.pension.app.Config;
import lilun.com.pension.app.User;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.adapter.AgencyClassifyAdapter;
import lilun.com.pension.module.adapter.ProductCategoryAdapter;
import lilun.com.pension.module.bean.Organization;
import lilun.com.pension.module.bean.ProductCategory;
import lilun.com.pension.module.bean.Setting;
import lilun.com.pension.module.callback.TitleBarClickCallBack;
import lilun.com.pension.module.utils.ACache;
import lilun.com.pension.ui.agency.list.AgencyOrganizationListFragment;
import lilun.com.pension.ui.agency.list.AgencyServiceListFragment;
import lilun.com.pension.ui.announcement.AnnouncementFragment;
import lilun.com.pension.ui.order.MerchantOrderListFragment;
import lilun.com.pension.ui.order.OrderListFragment;
import lilun.com.pension.ui.tourism.root.TourismRootFragment;
import lilun.com.pension.widget.ElderModuleClassifyDecoration;
import lilun.com.pension.widget.PositionTitleBar;
import lilun.com.pension.widget.recycler_view.AutoExtendSpanSizeLookup;

/**
 * 养老机构V
 *
 * @author yk
 *         create at 2017/2/7 16:04
 *         email : yk_developer@163.com
 */
public class AgencyClassifyFragment extends BaseFragment<AgencyClassifyContract.Presenter> implements AgencyClassifyContract.View {


    @Bind(R.id.rv_agency)
    RecyclerView rvAgency;

    @Bind(R.id.rv_server)
    RecyclerView rvServer;


    @Bind(R.id.swipe_layout)
    SwipeRefreshLayout mSwipeLayout;


    @Bind(R.id.title_bar)
    PositionTitleBar titleBar;
    @Bind(R.id.tv_agency_title)
    TextView tvAgencyTitle;
    private String parentId;

//    private ArrayList<Information> announcements;
//
//    public static AgencyClassifyFragment newInstance(List<Information> announcements) {
//        AgencyClassifyFragment fragment = new AgencyClassifyFragment();
//        Bundle args = new Bundle();
//        args.putSerializable("announcements", (Serializable) announcements);
//        fragment.setArguments(args);
//        return fragment;
//    }


    public static AgencyClassifyFragment newInstance(String parentId) {
        AgencyClassifyFragment fragment = new AgencyClassifyFragment();
        Bundle args = new Bundle();
        args.putString("parentId", parentId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void getTransferData(Bundle arguments) {
        parentId = arguments.getString("parentId");
    }

    @Override
    protected void initPresenter() {
        mPresenter = new AgencyClassifyPresenter();
        mPresenter.bindView(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_pension_agency_root;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        titleBar.setTvRightText(getString(R.string.all_orders));
        titleBar.setFragment(this);
        titleBar.setTitleBarClickListener(new TitleBarClickCallBack() {
            @Override
            public void onBackClick() {
                pop();
            }

            @Override
            public void onRightClick() {
                //TODO 商家和个人查看所有订单
                if (User.isCustomer()) {
                    start(OrderListFragment.newInstance(Config.agency_product_categoryId));
                } else {
                    Logger.d("商家模式进入");
                    start(new MerchantOrderListFragment());
                }
            }
        });

        rvAgency.addItemDecoration(new ElderModuleClassifyDecoration());
        rvServer.addItemDecoration(new ElderModuleClassifyDecoration());


    }


    @Override
    protected void initEvent() {
        replaceLoadRootFragment(R.id.fl_announcement_container, AnnouncementFragment.newInstance(parentId), false);
        refreshData();
    }

    private void refreshData() {
        mSwipeLayout.setRefreshing(true);
        if (User.isCustomer()) {
            mPresenter.getClassifiesByAgency();
        } else {
            tvAgencyTitle.setVisibility(View.GONE);
            rvAgency.setVisibility(View.GONE);
        }
        mPresenter.getClassifiesByService();
        mSwipeLayout.setEnabled(false);
    }

    @Override
    public void showClassifiesByAgency(List<Organization> organizations) {
        completeRefresh();
        rvAgency.setLayoutManager(new GridLayoutManager(_mActivity, spanCountByData(organizations)));
        AgencyClassifyAdapter adapter = new AgencyClassifyAdapter(this, organizations);
        adapter.setOnItemClickListener((organization -> {
            start(AgencyOrganizationListFragment.newInstance(organization.getName(), organization.getId()));

        }));
        rvAgency.setAdapter(adapter);

    }


    @Override
    public void showClassifiesByService(List<ProductCategory> productCategories) {
        completeRefresh();
        cacheExpendKeys(productCategories);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(_mActivity, spanCountByData(productCategories), LinearLayoutManager.VERTICAL, false);

        rvServer.setLayoutManager(gridLayoutManager);
        ProductCategoryAdapter adapter = new ProductCategoryAdapter(this, productCategories, getResources().getColor(R.color.agency));
        adapter.setOnRecyclerViewItemClickListener((view, i) -> {

            ProductCategory productCategory = adapter.getData().get(i);
            if (productCategory.getId().equals(Config.tourism_product_categoryId)) {
                start(TourismRootFragment.newInstance(productCategory.getId()));
            } else {
                start(AgencyServiceListFragment.newInstance(productCategory.getName(), productCategory.getId(), 0));
            }
        });
        rvServer.setAdapter(adapter);
        gridLayoutManager.setSpanSizeLookup(new AutoExtendSpanSizeLookup(productCategories.size(), spanCountByData(productCategories)));
    }

    private void cacheExpendKeys(List<ProductCategory> productCategories) {
        for (ProductCategory productCategory : productCategories) {
            String categoryId = productCategory.getId();
            if (!ACache.get().isExit(categoryId)) {
                List<Setting> settings = productCategory.getSetting();
                ACache.get().put(categoryId, (Serializable) settings);
            }
        }
    }


    @Override
    public void completeRefresh() {
        if (mSwipeLayout != null && mSwipeLayout.isRefreshing()) {
            mSwipeLayout.setRefreshing(false);
        }
    }

    private int spanCountByData(List data) {
        int count;
        if (data.size() == 0) {
            count = 1;
        } else {
            count = data.size() >= 3 ? 3 : data.size();
        }
        return count;
    }


}
