package lilun.com.pensionlife.ui.register;

import com.vanzh.library.BaseBean;
import com.vanzh.library.DataInterface;

import java.util.List;

import lilun.com.pensionlife.base.IPresenter;
import lilun.com.pensionlife.base.IView;
import lilun.com.pensionlife.module.bean.Account;
import lilun.com.pensionlife.module.bean.Area;
import lilun.com.pensionlife.module.bean.QINiuToken;
import lilun.com.pensionlife.module.bean.Register;
import me.yokeyword.fragmentation.SupportActivity;

/**
 * Created by zp on 2017/4/13.
 */

public interface RegisterContract {

    /**
     * 注册接口
     */
    interface ViewAccount extends IView<PresenterAccount> {
        void successOfIDCode();

        void successOfRegisterAccount(Register register);
    }

    interface PresenterAccount extends IPresenter<ViewAccount> {
        void getIDCode(String phone, String type);

        void postRegisterAccount(String IdCode, Account account);
    }

    /**
     * 注册完善信息
     */
    interface ViewInfo extends IView<PresenterInfo> {
        void successOfAccountLocation(Account account);

        void successOfChildLocation(List<Area> areas, DataInterface.Response<BaseBean> response, int level, int recyclerIndex);
    }

    interface PresenterInfo extends IPresenter<ViewInfo> {
        void getChildLocation(String locationName, DataInterface.Response<BaseBean> response, int level, int recyclerIndex, int skip,int limitSkip);

        void putAccountLocation(String organizationId, String address);
    }

    /**
     * 第6步获取注册的 VP
     */
    interface ViewAvator extends IView<PresenterAvator> {
        void uploadImages(QINiuToken qiNiuToken);

    }

    interface PresenterAvator extends IPresenter<ViewAvator> {
        void getUploadToken(String modelName, String modelId, String tag);
    }
}
