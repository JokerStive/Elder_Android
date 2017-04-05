package lilun.com.pension.ui.home;

import java.util.List;

import lilun.com.pension.base.IPresenter;
import lilun.com.pension.base.IView;
import lilun.com.pension.module.bean.Information;

/**
 * 首页契约类
 *
 * @author yk
 *         create at 2017/2/6 16:19
 *         email : yk_developer@163.com
 */
public interface HomeContract {
    interface View extends IView<Presenter> {
        void showInformation(List<Information> informations);
    }

    interface Presenter extends IPresenter<View> {

        void getInformation();

        void needChangeDefOrganization(String accountId,String changeOrganizationId);

        void changeDefBelongOrganization(String accountId, String organizationId);
    }
}
