package lilun.com.pensionlife.ui.agency.list;

import java.util.List;

import lilun.com.pensionlife.base.IPresenter;
import lilun.com.pensionlife.base.IView;
import lilun.com.pensionlife.module.bean.ConditionOption;
import lilun.com.pensionlife.module.bean.Organization;
import lilun.com.pensionlife.module.bean.OrganizationProduct;

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
        List<ConditionOption> getConditionOptionsList(String whereKey);
    }

}
