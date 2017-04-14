package lilun.com.pension.ui.residential.detail;

import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.base.RxPresenter;
import lilun.com.pension.module.bean.ProductOrder;
import lilun.com.pension.module.utils.RxUtils;
import lilun.com.pension.net.NetHelper;
import lilun.com.pension.net.RxSubscriber;

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
        String filter = "{\"include\":[\"product\",\"assignee\"]}";
        addSubscribe(NetHelper.getApi()
                .getOrder(orderId, filter)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<ProductOrder>(((BaseFragment)view).getActivity()) {
                    @Override
                    public void _next(ProductOrder order) {
                        view.showOrder(order);
                    }

                }));
    }

    @Override
    public void changeOrderStatus(String orderId,String status) {
        addSubscribe(NetHelper.getApi()
                .putOrderStatus(orderId, status)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<Object>(((BaseFragment)view).getActivity()) {
                    @Override
                    public void _next(Object o) {
                        view.changeOrderStatusSuccess(status);
                    }

                }));
    }


}
