package lilun.com.pensionlife.ui.education.classify;

import java.util.List;

import lilun.com.pensionlife.base.IPresenter;
import lilun.com.pensionlife.base.IView;
import lilun.com.pensionlife.module.bean.ActivityCategory;
import lilun.com.pensionlife.module.bean.ElderEdus;
import lilun.com.pensionlife.module.bean.ElderModule;

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
