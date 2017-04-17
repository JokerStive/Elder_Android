package lilun.com.pension.ui.register;

import java.util.List;

import lilun.com.pension.base.IPresenter;
import lilun.com.pension.base.IView;
import lilun.com.pension.module.bean.Account;
import lilun.com.pension.module.bean.Area;

/**
 * Created by zp on 2017/4/13.
 */

public interface RegisterContract {

    /**
     * 第2步获取验证码的 VP
     */
    interface ViewStep2 extends IView<PresenterStep2> {
        void successOfIDCode();
    }

    interface PresenterStep2 extends IPresenter<ViewStep2> {
        void getIDCode(String phone);
    }
  /**
     * 第3步  检测  验证码的 VP
     */
    interface ViewStep3 extends IView<PresenterStep3> {
        void successOfCheckIDCode();
    }

    interface PresenterStep3 extends IPresenter<ViewStep3> {
        void checkIDCode(String phone, String aIDCode);
    }


    /**
     * 第5步获取 地区的 VP
     */
    interface ViewStep5 extends IView<PresenterStep5> {
        void successOfCommitRegister(Account backAccount);

        void successOfChildLocation(List<Area> areas);
    }

    interface PresenterStep5 extends IPresenter<ViewStep5> {
        void getChildLocation(String locationName);

        void commitRegister(String organizationId, String IDCode, String address, Account account);
    }

    /**
     * 第6步获取注册的 VP
     */
    interface ViewStep6 extends IView<PresenterStep6> {
        void successOfUpdateImage();

    }

    interface PresenterStep6 extends IPresenter<ViewStep6> {
        void updateImage(String id, String imageName, String path);
    }
}
