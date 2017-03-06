package lilun.com.pension.ui.residential.detail;

import lilun.com.pension.base.IPresenter;
import lilun.com.pension.base.IView;
import lilun.com.pension.module.bean.ProductOrder;

/**
*订单详情C
*@author yk
*create at 2017/3/6 9:42
*email : yk_developer@163.com
*/
public interface OrderDetailContract {
    interface View extends IView<Presenter> {
        void showOrder(ProductOrder order);
        void changeOrderStatusSuccess(String status);
    }

    interface Presenter extends IPresenter<View> {
        void getOrder(String orderId);
        void changeOrderStatus(String orderId,String status);
    }
}
