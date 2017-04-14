package lilun.com.pension.ui.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.Bind;
import lilun.com.pension.R;
import lilun.com.pension.app.Constants;
import lilun.com.pension.app.Event;
import lilun.com.pension.app.User;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.adapter.PersonalOrderAdapter;
import lilun.com.pension.module.bean.ProductOrder;
import lilun.com.pension.module.utils.Preconditions;
import lilun.com.pension.ui.help.RankFragment;
import lilun.com.pension.ui.residential.detail.OrderDetailActivity;
import lilun.com.pension.widget.NormalTitleBar;

/**
 * 我的订单P
 *
 * @author yk
 *         create at 2017/3/3 11:33
 *         email : yk_developer@163.com
 */
public class OrderPageFragment extends BaseFragment<OrderPageContract.Presenter> implements OrderPageContract.View {


    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.swipe_layout)
    SwipeRefreshLayout mSwipeLayout;
    @Bind(R.id.titleBar)
    NormalTitleBar titleBar;


    private PersonalOrderAdapter personalOrderAdapter;
    private String mStatus;


    public static OrderPageFragment newInstance(String status) {
        OrderPageFragment fragment = new OrderPageFragment();
        Bundle args = new Bundle();
        args.putString("status", status);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void getTransferData(Bundle arguments) {
        super.getTransferData(arguments);
        mStatus = arguments.getString("status");
        Preconditions.checkNull(mStatus);
    }

    @Subscribe
    public void refreshData(Event.RefreshMyOrderData event) {
        getMyOrder(0);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new OrderPagePresenter();
        mPresenter.bindView(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_recycler;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        titleBar.setVisibility(View.GONE);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false));
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
        String filter = "{\"include\":[\"product\",\"assignee\"],\"where\":{\"creatorId\":\"" + User.getUserId() + "\",\"status\":\"" + mStatus + "\"}}";
        mPresenter.getMyOrders(filter, skip);

    }

    @Override
    public void showMyOrders(List<ProductOrder> orders, boolean isLoadMore) {
        completeRefresh();
        if (User.isCustomer()) {
            showPersonalOrder(orders, isLoadMore);
        } else {
            showPersonalOrder(orders, isLoadMore);
        }
    }


    /**
     * 个人订单展示
     */
    private void showPersonalOrder(List<ProductOrder> orders, boolean isLoadMore) {
        if (personalOrderAdapter == null) {
            personalOrderAdapter = new PersonalOrderAdapter(orders);
            personalOrderAdapter.setEmptyView();
            personalOrderAdapter.setOnItemClickListener(new PersonalOrderAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(ProductOrder order) {
                    Intent intent = new Intent(_mActivity, OrderDetailActivity.class);
                    intent.putExtra("orderId", order.getId());
                    startActivity(intent);
                }

                @Override
                public void onRank(String productId) {
                    start(RankFragment.newInstance(Constants.organizationProduct, productId));
                }
            });
            personalOrderAdapter.setOnLoadMoreListener(() -> {
                getMyOrder(personalOrderAdapter.getItemCount());
            });
            mRecyclerView.setAdapter(personalOrderAdapter);
        } else if (isLoadMore) {
            personalOrderAdapter.addAll(orders);
        } else {
            personalOrderAdapter.replaceAll(orders);
        }
    }


    /**
     * 商家订单展示
     */
    private void showMerchantOrder(List<ProductOrder> orders, boolean isLoadMore) {

    }

    @Override
    public void completeRefresh() {
        if (mSwipeLayout != null && mSwipeLayout.isRefreshing()) {
            mSwipeLayout.setRefreshing(false);
        }
    }

}
