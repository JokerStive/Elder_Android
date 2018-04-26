package lilun.com.pensionlife.ui.order;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import lilun.com.pensionlife.app.Event;
import lilun.com.pensionlife.base.RxPresenter;
import lilun.com.pensionlife.module.bean.ProductOrder;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.net.NetHelper;
import lilun.com.pensionlife.net.RxSubscriber;

/**
 * 我的订单P
 *
 * @author yk
 *         create at 2017/3/3 11:32
 *         email : yk_developer@163.com
 */
public class OrderPagePresenter extends RxPresenter<OrderPageContract.View> implements OrderPageContract.Presenter {
    @Override
    public void getMyOrders(String filter, int skip) {
        addSubscribe(NetHelper.getApi()
                .getOrders(StringUtils.addFilterWithDef(filter, skip))
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<ProductOrder>>() {
                    @Override
                    public void _next(List<ProductOrder> orders) {
                        view.showMyOrders(orders, skip != 0);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.completeRefresh();
                    }
                }));
    }


    @Override
    public void changeOrderStatus(String orderId, int status) {
        addSubscribe(NetHelper.getApi()
                .changeOrderStatus(orderId, status)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<Object>(getActivity()) {
                    @Override
                    public void _next(Object o) {
                        EventBus.getDefault().post(new Event.RefreshMyOrderData());
                    }

                    @Override
                    public void onError(Throwable e, int[] errorCode, String[] errorMessage) {
                        super.onError(e, errorCode, errorMessage);
                    }
                }));
    }
}

