package lilun.com.pension.ui.residential.classify;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import lilun.com.pension.R;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.adapter.ProductCategoryAdapter;
import lilun.com.pension.module.bean.Information;
import lilun.com.pension.module.bean.ProductCategory;
import lilun.com.pension.module.callback.TitleBarClickCallBack;
import lilun.com.pension.ui.announcement.AnnouncementFragment;
import lilun.com.pension.ui.order.OrderListFragment;
import lilun.com.pension.widget.ElderModuleClassifyDecoration;
import lilun.com.pension.widget.PositionTitleBar;
import lilun.com.pension.widget.recycler_view.AutoExtentionSpanSizeLookup;

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


    private ArrayList<Information> announcements;
    private ProductCategoryAdapter mClassifyAdapter;

    public static ResidentialClassifyFragment newInstance(List<Information> announcements) {
        ResidentialClassifyFragment fragment = new ResidentialClassifyFragment();
        Bundle args = new Bundle();
        args.putSerializable("announcements", (Serializable) announcements);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void getTransferData(Bundle arguments) {
        announcements = (ArrayList<Information>) arguments.getSerializable("announcements");
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
        titleBar.setTvRightText(getString(R.string.all_orders));
        titleBar.setTitleBarClickListener(new TitleBarClickCallBack() {
            @Override
            public void onBackClick() {
                pop();
            }

            @Override
            public void onPositionClick() {

            }

            @Override
            public void onRightClick() {
                //TODO 查看所有订单
                start(new OrderListFragment());
            }
        });


        replaceLoadRootFragment(R.id.fl_announcement_container, AnnouncementFragment.newInstance(announcements), false);

        mClassifyRecycler.addItemDecoration(new ElderModuleClassifyDecoration());
        //刷新
        mSwipeLayout.setOnRefreshListener(() -> {
                    if (mPresenter != null) {
                        refreshData();
                    }
                }
        );


        //设置数据
//        setAdapter();

    }


//    private void setAdapter() {
//        mAdapter = new ProductorAdapter(this, products);
//        mAdapter.addHeaderView(mClassifyRecycler);
//        mClassifyRecycler.setAdapter(mAdapter);
//    }


    @Override
    protected void initEvent() {
        refreshData();
    }

    private void refreshData() {
        mSwipeLayout.setRefreshing(true);
        mPresenter.getClassifies();
    }


    @Override
    public void showClassifies(List<ProductCategory> productCategories) {
        completeRefresh();
        if (mClassifyAdapter == null) {
            int spanCount = spanCountByData(productCategories);
            GridLayoutManager manager = new GridLayoutManager(_mActivity, spanCount);
            manager.setSpanSizeLookup(new AutoExtentionSpanSizeLookup(productCategories.size(), spanCount));
            mClassifyRecycler.setLayoutManager(manager);
            mClassifyAdapter = new ProductCategoryAdapter(this, productCategories, getResources().getColor(R.color.residential));
            mClassifyAdapter.setOnRecyclerViewItemChildClickListener(new BaseQuickAdapter.OnRecyclerViewItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

                }
            });
            mClassifyRecycler.setAdapter(mClassifyAdapter);
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
