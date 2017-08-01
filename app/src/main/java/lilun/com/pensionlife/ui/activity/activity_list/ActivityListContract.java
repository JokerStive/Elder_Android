package lilun.com.pensionlife.ui.activity.activity_list;

import java.util.List;

import lilun.com.pensionlife.base.IPresenter;
import lilun.com.pensionlife.base.IView;
import lilun.com.pensionlife.module.bean.OrganizationActivity;

/**
 * Created by zp on 2017/3/6.
 */

public interface ActivityListContract {
    interface View extends IView<Presenter> {
        void showActivityList(List<OrganizationActivity> activities, boolean isFirstLoad);
        void completeRefresh();
    }
    interface Presenter extends IPresenter<View>{
        void getOrganizationActivities(String filter, int skip);
    }
}
