package lilun.com.pension.ui.residential.main;

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
import lilun.com.pension.module.bean.ProductOrder;
import lilun.com.pension.widget.NormalItemDecoration;
import lilun.com.pension.widget.NormalTitleBar;

/**
 * 我的订单P
 *
 * @author yk
 *         create at 2017/3/3 11:33
 *         email : yk_developer@163.com
 */
public class MyOderFragment extends BaseFragment<MyOrderContract.Presenter> implements MyOrderContract.View {


    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.swipe_layout)
    SwipeRefreshLayout mSwipeLayout;

    @Bind(R.id.titleBar)
    NormalTitleBar titleBar;


    @Override
    protected void initPresenter() {
        mPresenter = new MyOrderPresenter();
        mPresenter.bindView(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_recycler;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        titleBar.setTitle(getString(R.string.my_orders));
        titleBar.setOnBackClickListener((this::pop));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new NormalItemDecoration(27));
        //刷新
        mSwipeLayout.setOnRefreshListener(() -> {
                    if (mPresenter != null) {
                        getMyOrder(0);
                    }
                }
        );

    }


    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        getMyOrder(0);
    }


    private void getMyOrder(int skip) {
        mSwipeLayout.setRefreshing(true);
        mPresenter.getMyOrders(skip);

    }

    @Override
    public void showMyOrders(List<ProductOrder> orders) {
        if (User.isCustomer()) {

        }
    }

    @Override
    public void completeRefresh() {
        if (mSwipeLayout != null && mSwipeLayout.isRefreshing()) {
            mSwipeLayout.setRefreshing(false);
        }
    }
}
