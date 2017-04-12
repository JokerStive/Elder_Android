package lilun.com.pension.ui.change_organization;

import java.util.List;

import lilun.com.pension.base.IPresenter;
import lilun.com.pension.base.IView;
import lilun.com.pension.module.bean.Organization;

/**
 * 养老机构分类C
 *
 * @author yk
 *         create at 2017/2/13 10:43
 *         email : yk_developer@163.com
 */
public interface ChangeOrganizationContract {
    interface View extends IView<Presenter> {
        void showOrganizations(List<Organization> organizations,boolean isLoadMore);

        void changedRoot();

        void changedBelong();

        void completeRefresh();
    }

    interface Presenter extends IPresenter<View> {
        void getOrganizations(String organizationId,String filter,int skip);

        void changeDefBelongOrganization(String organizationId);
    }

}
