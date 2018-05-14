package lilun.com.pensionlife.ui.order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;

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
import lilun.com.pensionlife.module.utils.ToastHelper;
import lilun.com.pensionlife.module.utils.UIUtils;
import lilun.com.pensionlife.pay.Order;
import lilun.com.pensionlife.ui.agency.detail.ProviderDetailFragment;
import lilun.com.pensionlife.ui.order.personal_detail.OrderChatFragment;
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
    private int mStatus;


    public static OrderPageFragment newInstance(int status) {
        OrderPageFragment fragment = new OrderPageFragment();
        Bundle args = new Bundle();
        args.putInt("status", status);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void getTransferData(Bundle arguments) {
        super.getTransferData(arguments);
        mStatus = arguments.getInt("status");
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
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (personalOrderAdapter != null) {
                    if (AbsListView.OnScrollListener.SCROLL_STATE_IDLE == newState) {
                        personalOrderAdapter.setmScrollIdle(true);
                        personalOrderAdapter.notifyDataChanged();
                    } else personalOrderAdapter.setmScrollIdle(false);
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
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

    //收到到一个MQTT新消息
    @Subscribe
    public void newChatMsg(Event.NewChatMsg chatMsg) {
        if (personalOrderAdapter != null && chatMsg.getPushMessage().getUnRead()) {
            for (int i = 0; i < personalOrderAdapter.getData().size(); i++) {
                if (personalOrderAdapter.getData().get(i).getId().equals(chatMsg.getPushMessage().getActivityId())) {
                    personalOrderAdapter.notityUnRead(i);
                }
            }
        }
    }

    /**
     * 获取我需要显示的数据回
     * { "fields": ["id", "name", "status", "registerDate", "assigneeId", "orgCategoryId", "createdAt", "productBackupId"],"include":{"relation": "productBackup","scope": {"fields": ["id","name","title","price","unit", "organizationId","image","orgCategory","extend","areas","startTime", "endTime"]}} }     *
     * "where":{"productBackupId":{"$exists":true}}   //以前的老数据不要，获取有订单备份的数据
     *
     * @param skip
     */
    private void getMyOrder(int skip) {
        mSwipeLayout.setRefreshing(true);
        String needFields = "{ \"fields\": [\"paid\",\"id\", \"name\", \"status\", \"registerDate\", \"assigneeId\", \"orgCategoryId\", \"createdAt\", \"productBackupId\"],\"include\":{\"relation\": \"productBackup\",\"scope\": {\"fields\": [\"id\",\"name\",\"title\",\"price\",\"unit\", \"organizationId\",\"image\" ,\"orgCategory\",\"extend\",\"areas\",\"serviceOrganization\",\"startTime\", \"endTime\"]}}";
        String filter = needFields + ",\"order\": \"createdAt DESC\",\"where\":{\"productBackupId\":{\"$exists\":true},\"and\":[{\"creatorId\":\"" + User.getUserId() + "\"},{\"status\":\"" + mStatus + "\"}]}}";
        switch (mStatus) {
            case Order.Status.reserved:
                filter = needFields + ",\"order\": \"createdAt DESC\",\"where\":{\"productBackupId\":{\"$exists\":true},\"and\":[{\"creatorId\":\"" + User.getUserId() + "\"},{\"status\":\"" + mStatus + "\"},{\"paid\":\"" + mStatus + "\"}]}}";
                break;

            case Order.Status.accepted:
                filter = needFields + ",\"order\": \"createdAt DESC\",\"where\":{\"productBackupId\":{\"$exists\":true},\"and\":[{\"creatorId\":\"" + User.getUserId() + "\"},{\"status\":{\"inq\":[\""+Order.Status.accepted+"\",\""+Order.Status.delayed+"\",\""+Order.Status.refused+"\"]}}]}}";
                break;

            case Order.Status.completed:
                filter = needFields + ",\"order\": \"createdAt DESC\",\"where\":{\"productBackupId\":{\"$exists\":true},\"and\":[{\"creatorId\":\"" + User.getUserId() + "\"},{\"status\":{\"inq\":[\"" + Order.Status.completed + "\",\"" + Order.Status.assessed + "\",\"" + Order.Status.refunded + "\"]}}]}}";
                break;
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
                    //进入订单详情界面
                    if (order.getProductBackup() == null) {
                        ToastHelper.get().showWareShort("未查询到商家信息");
                        return;
                    }
                    ((BaseFragment) getParentFragment()).start(OrderChatFragment.newInstance(order));
                }

                @Override
                public void nextOperate(ProductOrder order) {
                    if (order.getProductBackup() == null) {
                        ToastHelper.get().showWareShort("未查询到商家信息");
                        return;
                    }
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
