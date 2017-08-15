package lilun.com.pensionlife.ui.order.personal_detail;

import lilun.com.pensionlife.base.RxPresenter;
import lilun.com.pensionlife.module.bean.ProductOrder;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.net.NetHelper;
import lilun.com.pensionlife.net.RxSubscriber;

/**
 * 订单详情P
 *
 * @author yk
 *         create at 2017/3/6 9:44
 *         email : yk_developer@163.com
 */
public class OrderDetailPresenter extends RxPresenter<OrderDetailContract.View> implements OrderDetailContract.Presenter {
    @Override
    public void getOrder(String orderId) {
//        String filter = "{\"include\":[\"product\",\"assignee\"]}";
        addSubscribe(NetHelper.getApi()
                .getOrder(orderId, null)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<ProductOrder>(getActivity()) {
                    @Override
                    public void _next(ProductOrder order) {
                        view.showOrder(order);
                    }

                }));
    }

    @Override
    public void changeOrderStatus(String orderId,String status) {
        addSubscribe(NetHelper.getApi()
                .changeOrderStatus(orderId, status)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<Object>(getActivity()) {
                    @Override
                    public void _next(Object o) {
                        view.changeOrderStatusSuccess(status);
                    }

                    @Override
                    public void onError(Throwable e, int[] errorCode, String[] errorMessage) {
                        super.onError(e, errorCode, errorMessage);
                    }
                }));
    }


}
