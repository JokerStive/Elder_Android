package lilun.com.pension.ui.residential.main;

import java.util.List;

import lilun.com.pension.base.IPresenter;
import lilun.com.pension.base.IView;
import lilun.com.pension.module.bean.ProductOrder;

/**
 * 我的订单c
 *
 * @author yk
 *         create at 2017/3/3 8:58
 *         email : yk_developer@163.com
 */
public interface MyOrderContract {
    interface View extends IView<Presenter> {
        void showMyOrders(List<ProductOrder> orders);

        void completeRefresh();
    }

    interface Presenter extends IPresenter<View> {
        void getMyOrders(int skip);
    }
}
