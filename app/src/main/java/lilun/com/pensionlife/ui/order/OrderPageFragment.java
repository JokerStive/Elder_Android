package lilun.com.pensionlife.ui.order;

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
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.app.Config;
import lilun.com.pensionlife.app.Event;
import lilun.com.pensionlife.app.User;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.adapter.PersonalOrderAdapter;
import lilun.com.pensionlife.module.bean.ProductOrder;
import lilun.com.pensionlife.module.utils.Preconditions;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.module.utils.UIUtils;
import lilun.com.pensionlife.ui.agency.detail.ProviderDetailFragment;
import lilun.com.pensionlife.ui.order.personal_detail.OrderDetailActivity;
import lilun.com.pensionlife.widget.DividerDecoration;
import lilun.com.pensionlife.widget.NormalTitleBar;

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
        mRecyclerView.addItemDecoration(new DividerDecoration(App.context, LinearLayoutManager.VERTICAL, UIUtils.dp2px(App.context, App.context.getResources().getDimension(R.dimen.dp_10)), App.context.getResources().getColor(R.color.gray)));
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
        String filter;
        if (mStatus.equals("done")) {
//            filter =  "{\"where\":{\"and\":[{\"creatorId\":\"9183a520-8322-11e7-8053-8988faaa07d2\"},{\"status\":{\"inq\":[\"done\",\"assigned\"]}}]},\"limit\":\"20\",\"skip\":\"0\",\"limit\":\"10\",\"skip\":\"0\"}";
            filter = "{\"include\":\"productBackup\",\"order\": \"createdAt DESC\",\"where\":{\"and\":[{\"creatorId\":\"" + User.getUserId() + "\"},{\"status\":{\"inq\":[\"done\",\"assessed\"]}}]}}";
        } else if (mStatus.equals("assigned")) {
            filter = "{\"include\":\"productBackup\",\"order\": \"createdAt DESC\",\"where\":{\"and\":[{\"creatorId\":\"" + User.getUserId() + "\"},{\"status\":{\"inq\":[\"delay\",\"assigned\"]}}]}}";
        } else {
            filter = "{\"include\":\"productBackup\",\"order\": \"createdAt DESC\",\"where\":{\"and\":[{\"creatorId\":\"" + User.getUserId() + "\"},{\"status\":\"" + mStatus + "\"}]}}";
        }
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
                public void nextOperate(ProductOrder order) {
                    if (order.getProductBackup() == null) return;
                    ((BaseFragment) getParentFragment()).start(ProviderDetailFragment.newInstance(StringUtils.removeSpecialSuffix(order.getProductBackup().getOrganizationId())), SINGLETASK);

                }
            });
            personalOrderAdapter.setOnLoadMoreListener(() -> {
                getMyOrder(personalOrderAdapter.getItemCount());
            }, mRecyclerView);
            mRecyclerView.setAdapter(personalOrderAdapter);
        } else if (isLoadMore) {
            personalOrderAdapter.addAll(orders, Config.defLoadDatCount);
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

    @Subscribe
    public void afterRank(Event.AfterRank afterRank) {
        getMyOrder(0);
    }


}
