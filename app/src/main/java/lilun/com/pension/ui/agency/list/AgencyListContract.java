package lilun.com.pension.ui.agency.list;

import java.util.List;

import lilun.com.pension.base.IPresenter;
import lilun.com.pension.base.IView;
import lilun.com.pension.module.bean.Organization;
import lilun.com.pension.module.bean.OrganizationProduct;

/**
*养老机构分类C
*@author yk
*create at 2017/2/13 10:43
*email : yk_developer@163.com
*/
public interface AgencyListContract {
    interface View extends IView<Presenter> {
        void showProducts(List<OrganizationProduct> products, boolean isLoadMore);
        void showOrganizations(List<Organization> organizations, boolean isLoadMore);
        void completeRefresh();
    }

    interface Presenter extends IPresenter<View> {
        void getOrganizationAgency(String organizationId,String filter,int skip);
        void getProductAgency(String filter,int skip);
    }

}
