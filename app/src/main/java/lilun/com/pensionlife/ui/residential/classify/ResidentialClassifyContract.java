package lilun.com.pensionlife.ui.residential.classify;

import java.util.List;

import lilun.com.pensionlife.base.IPresenter;
import lilun.com.pensionlife.base.IView;
import lilun.com.pensionlife.module.bean.ProductCategory;

/**
*居家服务分类契约类
*@author yk
*create at 2017/2/13 10:43
*email : yk_developer@163.com
*/
public interface ResidentialClassifyContract {
    interface View extends IView<Presenter> {
        void showClassifies(List<ProductCategory> productCategories);
//        void showAboutMe(List<OrganizationProduct> products, boolean isLoadMore);
        void completeRefresh();
    }

    interface Presenter extends IPresenter<View> {
        void getClassifies();
//        void getAboutMe(int skip);
    }

}
