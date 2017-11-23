package lilun.com.pensionlife.ui.agency.classify;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.Config;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.adapter.AgencyClassifyAdapter;
import lilun.com.pensionlife.module.adapter.ProductCategoryAdapter;
import lilun.com.pensionlife.module.bean.Organization;
import lilun.com.pensionlife.module.bean.OrganizationProductCategory;
import lilun.com.pensionlife.module.callback.TitleBarClickCallBack;
import lilun.com.pensionlife.ui.agency.list.ProductListFragment;
import lilun.com.pensionlife.ui.announcement.AnnouncementFragment;
import lilun.com.pensionlife.ui.tourism.root.TourismRootFragment;
import lilun.com.pensionlife.widget.ElderModuleClassifyDecoration;
import lilun.com.pensionlife.widget.PositionTitleBar;
import lilun.com.pensionlife.widget.recycler_view.AutoExtendSpanSizeLookup;

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
        titleBar.setTitleBarClickListener(new TitleBarClickCallBack() {
            @Override
            public void onBackClick() {
                pop();
            }

            @Override
            public void onRightClick() {
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
//        mPresenter.getClassifiesByAgency();
//        if (User.isCustomer()) {
//            mPresenter.getClassifiesByAgency();
//        }
//
//        else {
//            tvAgencyTitle.setVisibility(View.GONE);
//            rvAgency.setVisibility(View.GONE);
//        }
        mPresenter.getClassifiesByService(Config.agency_product_categoryId);
        mPresenter.getClassifiesByService(Config.tourism_product_categoryId);
        mSwipeLayout.setEnabled(false);
    }

    @Override
    public void showClassifiesByAgency(List<Organization> organizations) {
        completeRefresh();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(_mActivity, spanCountByData(organizations));
        rvAgency.setLayoutManager(gridLayoutManager);
        AgencyClassifyAdapter adapter = new AgencyClassifyAdapter(this, organizations);
        adapter.setOnItemClickListener((organization -> {
//            start(AgencyOrganizationListFragment.newInstance(organization.getName(), organization.getId()));

        }));
        rvAgency.setAdapter(adapter);
//        gridLayoutManager.setSpanSizeLookup(new AutoExtendSpanSizeLookup(productCategories.size(), spanCountByData(productCategories)));

    }


    @Override
    public void showClassifiesByService(List<OrganizationProductCategory> productCategories, String categoryId) {
        completeRefresh();
//        cacheExpendKeys(productCategories);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(_mActivity, spanCountByData(productCategories), LinearLayoutManager.VERTICAL, false);
        gridLayoutManager.setSpanSizeLookup(new AutoExtendSpanSizeLookup(productCategories.size(), spanCountByData(productCategories)));

        ProductCategoryAdapter adapter = new ProductCategoryAdapter(this, productCategories, getResources().getColor(R.color.agency));
        adapter.setOnItemClickListener((baseQuickAdapter,view, i) -> {
            OrganizationProductCategory OrganizationProductCategory = adapter.getData().get(i);
            if (OrganizationProductCategory.getParentId().equals(Config.tourism_product_categoryId)) {
                start(TourismRootFragment.newInstance(OrganizationProductCategory.getId()));
            } else {
                start(ProductListFragment.newInstance(OrganizationProductCategory.getName(), OrganizationProductCategory.getId(), 0));
            }
        });
        show(gridLayoutManager, adapter, categoryId);
    }

    private void show(GridLayoutManager gridLayoutManager, ProductCategoryAdapter adapter, String categoryId) {
        if (categoryId.equals(Config.agency_product_categoryId)) {
            rvAgency.setLayoutManager(gridLayoutManager);
            rvAgency.setAdapter(adapter);
        } else if (categoryId.equals(Config.tourism_product_categoryId)) {
            rvServer.setLayoutManager(gridLayoutManager);
            rvServer.setAdapter(adapter);
        }
    }

//    private void cacheExpendKeys(List<OrganizationProductCategory> productCategories) {
//        for (OrganizationProductCategory OrganizationProductCategory : productCategories) {
//            String categoryId = OrganizationProductCategory.getId();
//            if (!ACache.get().isExit(categoryId)) {
//                List<Setting> settings = OrganizationProductCategory.getSetting();
//                ACache.get().put(categoryId, (Serializable) settings);
//            }
//        }
//    }


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
