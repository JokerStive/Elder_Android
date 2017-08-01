package lilun.com.pensionlife.ui.activity.classify;

import java.util.List;

import lilun.com.pensionlife.base.IPresenter;
import lilun.com.pensionlife.base.IView;
import lilun.com.pensionlife.module.bean.ActivityCategory;
import lilun.com.pensionlife.module.bean.OrganizationActivity;

/**
*邻居互助契约类
*@author yk
*create at 2017/2/13 10:43
*email : yk_developer@163.com
*/
public interface ActivityClassifyContract {
    interface View extends IView<Presenter> {
        void showClassifies(List<ActivityCategory> activityCategories);
        void showAboutMe(List<OrganizationActivity> activities, boolean isFirstLoad);
        void completeRefresh();
    }

    interface Presenter extends IPresenter<View> {
        void getClassifies(String organizationId);
        void getAboutMe(String filter, int skip);
    }

}
