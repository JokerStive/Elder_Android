package lilun.com.pensionlife.ui.order.detail;

import lilun.com.pensionlife.base.IPresenter;
import lilun.com.pensionlife.base.IView;

/**
 * 我的订单c
 *
 * @author yk
 *         create at 2017/3/3 8:58
 *         email : yk_developer@163.com
 */
public interface MerchantOrderDetailContract {
    interface View extends IView<Presenter> {
    }

    interface Presenter extends IPresenter<View> {

    }
}
