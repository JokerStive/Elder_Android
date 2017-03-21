package lilun.com.pension.ui.activity.activity_detail;

import lilun.com.pension.base.IPresenter;
import lilun.com.pension.base.IView;
import lilun.com.pension.module.bean.ActivityDetail;

/**
 * Created by zp on 2017/3/6.
 */

public interface ActivityDetailContact {
    interface View extends IView<Presenter>{
        void showActivityDetail(ActivityDetail activityDetail);
        void completeRefresh();
        void refreshActivityData();

    }
    interface Presenter extends IPresenter<View> {
        void getActivityDetail(String id);
        void joinActivity(String activityId);
        void quitActivity(String activityId);
        void cancelActivity(String activityId);
    }
}
