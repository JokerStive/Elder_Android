package lilun.com.pension.ui.education.classify;

import java.util.List;

import lilun.com.pension.base.IPresenter;
import lilun.com.pension.base.IView;
import lilun.com.pension.module.bean.ActivityCategory;
import lilun.com.pension.module.bean.ElderEdus;
import lilun.com.pension.module.bean.ElderModule;
import lilun.com.pension.module.bean.ElderEdusColleage;
import lilun.com.pension.module.bean.OrganizationProduct;

/**
*老年教育契约类
*@author yk
*create at 2017/2/13 10:43
*email : yk_developer@163.com
*/
public interface EducationClassifyContract {
    interface View extends IView<Presenter> {
        void showClassifies(List<ElderModule> elderModules);
        void showAboutMe(List<ElderEdus> orders, boolean isLoadMore);
        void showOrgActivityCategory(List<ActivityCategory> activityCategories);
        void completeRefresh();
    }

    interface Presenter extends IPresenter<View> {
        void getClassifies();
        void getOrgActivityCategory(String filter);
        void getAboutMe(String filter, int skip);
    }
}
