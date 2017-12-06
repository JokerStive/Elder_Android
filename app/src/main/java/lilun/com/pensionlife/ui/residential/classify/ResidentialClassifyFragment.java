package lilun.com.pensionlife.ui.residential.classify;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import java.util.List;

import butterknife.Bind;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.adapter.ProductCategoryAdapter;
import lilun.com.pensionlife.module.bean.OrganizationProductCategory;
import lilun.com.pensionlife.module.callback.TitleBarClickCallBack;
import lilun.com.pensionlife.ui.agency.list.ProductListFragment;
import lilun.com.pensionlife.ui.announcement.AnnouncementFragment;
import lilun.com.pensionlife.widget.ElderModuleClassifyDecoration;
import lilun.com.pensionlife.widget.PositionTitleBar;
import lilun.com.pensionlife.widget.recycler_view.AutoExtendSpanSizeLookup;

/**
 * 居家服务分类V
 *
 * @author yk
 *         create at 2017/2/7 16:04
 *         email : yk_developer@163.com
 */
public class ResidentialClassifyFragment extends BaseFragment<ResidentialClassifyContract.Presenter> implements ResidentialClassifyContract.View {


    @Bind(R.id.title_bar)
    PositionTitleBar titleBar;

    @Bind(R.id.recycler_view)
    RecyclerView mClassifyRecycler;

    @Bind(R.id.swipe_layout)
    SwipeRefreshLayout mSwipeLayout;


    //    private ArrayList<Information> announcements;
    private ProductCategoryAdapter mClassifyAdapter;
    private String parentId;
    //
//    public static ResidentialClassifyFragment newInstance(List<Information> announcements) {
//        ResidentialClassifyFragment fragment = new ResidentialClassifyFragment();
//        Bundle args = new Bundle();
//        args.putSerializable("announcements", (Serializable) announcements);
//        fragment.setArguments(args);
//        return fragment;
//    }


    public static ResidentialClassifyFragment newInstance(String parentId) {
        ResidentialClassifyFragment fragment = new ResidentialClassifyFragment();
        Bundle args = new Bundle();
        args.putString("parentId", parentId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void getTransferData(Bundle arguments) {
        parentId = arguments.getString("parentId");
//        announcements = (ArrayList<Information>) arguments.getSerializable("announcements");
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ResidentialClassifyPresenter();
        mPresenter.bindView(this);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_second_module;
    }


    @Override
    protected void initView(LayoutInflater inflater) {
        titleBar.setTitle(getString(R.string.residential_service));
        titleBar.setTitleBarClickListener(new TitleBarClickCallBack() {
            @Override
            public void onBackClick() {
                pop();
            }

            @Override
            public void onPositionChanged() {

            }

            @Override
            public void onRightClick() {
            }
        });


        replaceLoadRootFragment(R.id.fl_announcement_container, AnnouncementFragment.newInstance(parentId), false);


        //刷新
        mSwipeLayout.setOnRefreshListener(() -> {
                    if (mPresenter != null) {
                        refreshData();
                    }
                }
        );


    }


    @Override
    protected void initEvent() {
        refreshData();
    }

    private void refreshData() {
        mSwipeLayout.setRefreshing(true);
        mPresenter.getClassifies();
    }


    @Override
    public void showClassifies(List<OrganizationProductCategory> productCategories) {
        completeRefresh();
        if (mClassifyAdapter == null) {

            GridLayoutManager manager = new GridLayoutManager(_mActivity, 6);
            manager.setSpanSizeLookup(new AutoExtendSpanSizeLookup(productCategories.size(), 6));

            mClassifyRecycler.addItemDecoration(new ElderModuleClassifyDecoration());

            mClassifyAdapter = new ProductCategoryAdapter(this, productCategories, getResources().getColor(R.color.residential));
            mClassifyAdapter.setOnItemClickListener((baseQuickAdapter,view, i) -> {
                OrganizationProductCategory category = mClassifyAdapter.getData().get(i);
                start(ProductListFragment.newInstance(category.getName(), category.getId(), 0));
            });
            mClassifyRecycler.setAdapter(mClassifyAdapter);

            mClassifyRecycler.setLayoutManager(manager);
        } else {
            mClassifyAdapter.replaceAll(productCategories);
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
