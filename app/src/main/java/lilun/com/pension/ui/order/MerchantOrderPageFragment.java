package lilun.com.pension.ui.order;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.Bind;
import lilun.com.pension.R;
import lilun.com.pension.app.Event;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.adapter.MerchantOrderAdapter;
import lilun.com.pension.module.bean.ProductOrder;
import lilun.com.pension.module.utils.Preconditions;
import lilun.com.pension.ui.agency.merchant.MemoActivity;
import lilun.com.pension.ui.order.detail.MerchantOrderDetailActivity;
import lilun.com.pension.widget.NormalTitleBar;

/**
 * 我的订单P
 *
 * @author yk
 *         create at 2017/3/3 11:33
 *         email : yk_developer@163.com
 */
public class MerchantOrderPageFragment extends BaseFragment<OrderPageContract.Presenter> implements OrderPageContract.View {


    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.swipe_layout)
    SwipeRefreshLayout mSwipeLayout;
    @Bind(R.id.titleBar)
    NormalTitleBar titleBar;


    private MerchantOrderAdapter adapter;
    private String mStatus;


    public static MerchantOrderPageFragment newInstance(String status) {
        MerchantOrderPageFragment fragment = new MerchantOrderPageFragment();
        Bundle args = new Bundle();
        args.putString("status", status);
        fragment.setArguments(args);
        return fragment;
    }

    @Subscribe
    public void refresh(Event.RefreshMerchantOrder event) {
        getMyOrder(0);
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
        mPresenter.getMyOrders(mStatus, skip);

    }

    @Override
    public void showMyOrders(List<ProductOrder> orders, boolean isLoadMore) {
        completeRefresh();
        showMerchantOrder(orders, isLoadMore);
    }


    /**
     * 商家订单展示
     */
    private void showMerchantOrder(List<ProductOrder> orders, boolean isLoadMore) {
        if (adapter == null) {
            adapter = new MerchantOrderAdapter(orders);
            adapter.setEmptyView();
            adapter.setOnRecyclerViewItemClickListener((view, i) -> {
                Intent intent = new Intent(_mActivity, MerchantOrderDetailActivity.class);
                intent.putExtra("orderId", orders.get(i).getId());
                startActivity(intent);
            });
            adapter.setOnLoadMoreListener(() -> {
                getMyOrder(adapter.getItemCount());
            });

            adapter.setOnItemClickListener(new MerchantOrderAdapter.OnItemClickListener() {
                @Override
                public void onCall(String phone) {
                    connectCustom(phone);
                }

                @Override
                public void onMemo() {
                    Intent intent = new Intent(_mActivity, MemoActivity.class);
                    startActivity(intent);
                }
            });
            mRecyclerView.setAdapter(adapter);
        } else if (isLoadMore) {
            adapter.addAll(orders);
        } else {
            adapter.replaceAll(orders);
        }
    }

    private void connectCustom(String phone) {
        String url = "tel:" + phone;
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(url));
        startActivity(intent);

        EndCallListener callListener = new EndCallListener();
        TelephonyManager mTM = (TelephonyManager) _mActivity.getSystemService(Context.TELEPHONY_SERVICE);
        mTM.listen(callListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    @Override
    public void completeRefresh() {
        if (mSwipeLayout != null && mSwipeLayout.isRefreshing()) {
            mSwipeLayout.setRefreshing(false);
        }
    }

    //通话结束，进入备注页面
    private class EndCallListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
//            if(TelephonyManager.CALL_STATE_RINGING == state) {
//                Log.i(LOG_TAG, "RINGING, number: " + incomingNumber);
//            }
//            if(TelephonyManager.CALL_STATE_OFFHOOK == state) {
//                //wait for phone to go offhook (probably set a boolean flag) so you know your app initiated the call.
//                Log.i(LOG_TAG, "OFFHOOK");
//            }
            if (TelephonyManager.CALL_STATE_IDLE == state) {
                Intent intent = new Intent(_mActivity, MemoActivity.class);
                startActivity(intent);
            }
        }
    }
}
