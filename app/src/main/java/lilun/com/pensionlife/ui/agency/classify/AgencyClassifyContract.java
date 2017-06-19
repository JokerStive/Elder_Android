package lilun.com.pensionlife.ui.agency.classify;

import java.util.List;

import lilun.com.pensionlife.base.IPresenter;
import lilun.com.pensionlife.base.IView;
import lilun.com.pensionlife.module.bean.Organization;
import lilun.com.pensionlife.module.bean.ProductCategory;

/**
*养老机构分类C
*@author yk
*create at 2017/2/13 10:43
*email : yk_developer@163.com
*/
public interface AgencyClassifyContract {
    interface View extends IView<Presenter> {
        void showClassifiesByAgency(List<Organization> organizations);
        void showClassifiesByService(List<ProductCategory> productCategories);
//        void showAgencies(List<OrganizationProduct> products,boolean isLoadMore);
        void completeRefresh();
    }

    interface Presenter extends IPresenter<View> {
        void getClassifiesByAgency();
        void getClassifiesByService();
//        void getAgencies(String filter,int skip);
    }

}
