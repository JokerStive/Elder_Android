package lilun.com.pension.ui.activity.activity_list;

import java.util.List;

import lilun.com.pension.base.IPresenter;
import lilun.com.pension.base.IView;
import lilun.com.pension.module.bean.OrganizationActivity;

/**
 * Created by zp on 2017/3/6.
 */

public interface ActivityListContract {
    interface View extends IView<Presenter> {
        void showActivityList(List<OrganizationActivity> activities, boolean islOadMore);
        void completeRefresh();
    }
    interface Presenter extends IPresenter<View>{
        void getOrganizationActivities(String filter, int skip);
    }
}