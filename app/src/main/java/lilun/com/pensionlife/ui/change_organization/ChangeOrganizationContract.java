package lilun.com.pensionlife.ui.change_organization;

import java.util.List;

import lilun.com.pensionlife.base.IPresenter;
import lilun.com.pensionlife.base.IView;
import lilun.com.pensionlife.module.bean.Organization;

/**
 * 养老机构分类C
 *
 * @author yk
 *         create at 2017/2/13 10:43
 *         email : yk_developer@163.com
 */
public interface ChangeOrganizationContract {
    interface View extends IView<Presenter> {
        void showOrganizations(List<Organization> organizations,boolean isLoadMore,boolean isAddCrumb);

        void changedRoot();

        void changedBelong();

        void completeRefresh();
    }

    interface Presenter extends IPresenter<View> {
        void getOrganizations(String organizationId,String filter,int skip,boolean isAddCrumb);

        void changeDefBelongOrganization(String organizationId);
    }




}
