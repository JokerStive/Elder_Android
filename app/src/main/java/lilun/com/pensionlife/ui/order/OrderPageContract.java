package lilun.com.pensionlife.ui.order;

import java.util.List;

import lilun.com.pensionlife.base.IPresenter;
import lilun.com.pensionlife.base.IView;
import lilun.com.pensionlife.module.bean.ProductOrder;

/**
 * 我的订单c
 *
 * @author yk
 *         create at 2017/3/3 8:58
 *         email : yk_developer@163.com
 */
public interface OrderPageContract {
    interface View extends IView<Presenter> {
        void showMyOrders(List<ProductOrder> orders,boolean isLoadMore);

        void completeRefresh();
    }

    interface Presenter extends IPresenter<View> {
        void getMyOrders(String status,int skip);
        void changeOrderStatus(String orderId, int status);
    }
}
