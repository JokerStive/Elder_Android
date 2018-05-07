package lilun.com.pensionlife.ui.order.personal_detail;

import lilun.com.pensionlife.base.IPresenter;
import lilun.com.pensionlife.base.IView;
import lilun.com.pensionlife.module.bean.ProductOrder;

/**
 * 订单详情C
 *
 * @author yk
 *         create at 2017/3/6 9:42
 *         email : yk_developer@163.com
 */
public interface OrderDetailContract {
    interface View extends IView<Presenter> {
        void showOrder(ProductOrder order);

        void changeOrderStatusSuccess(int status);

        void reFundSuccess();
    }

    interface Presenter extends IPresenter<View> {
        void getOrder(String orderId);

        void changeOrderStatus(String orderId, int status);

        void reFund(String orderId, String desc);
    }
}
