package lilun.com.pension.ui.activity.list;

import java.util.List;

import lilun.com.pension.base.IPresenter;
import lilun.com.pension.base.IView;
import lilun.com.pension.module.bean.ActivityCategory;
import lilun.com.pension.module.bean.OrganizationActivity;

/**
*邻居互助契约类
*@author yk
*create at 2017/2/13 10:43
*email : yk_developer@163.com
*/
public interface ActivityClassifyContract {
    interface View extends IView<Presenter> {
        void showClassifies(List<ActivityCategory> activityCategories);
        void showAboutMe(List<OrganizationActivity> activities, boolean isLoadMore);
        void completeRefresh();
    }

    interface Presenter extends IPresenter<View> {
        void getClassifies();
        void getAboutMe(String filter, int skip);
    }

}
