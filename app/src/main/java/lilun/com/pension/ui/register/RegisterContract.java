package lilun.com.pension.ui.register;

import lilun.com.pension.base.IPresenter;
import lilun.com.pension.base.IView;

/**
 * Created by zp on 2017/4/13.
 */

public interface RegisterContract {

    interface View extends IView<Presenter> {
        void successOfIDCode(String code);
    }

    interface Presenter extends IPresenter<View> {
        void getIDCode(String phone);
    }
}
