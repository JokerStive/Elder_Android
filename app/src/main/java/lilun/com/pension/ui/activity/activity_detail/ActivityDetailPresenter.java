package lilun.com.pension.ui.activity.activity_detail;

import lilun.com.pension.base.RxPresenter;
import lilun.com.pension.module.bean.ActivityDetail;
import lilun.com.pension.module.utils.RxUtils;
import lilun.com.pension.net.NetHelper;
import lilun.com.pension.net.RxSubscriber;

/**
 * Created by zp on 2017/3/6.
 */

public class ActivityDetailPresenter extends RxPresenter<ActivityDetailContact.View> implements ActivityDetailContact.Presenter {
    @Override
    public void getActivityDetail(String id) {
        addSubscribe(NetHelper.getApi()
                .getOrganizationActivitiesDetail(id, "{}")
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<ActivityDetail>() {
                    @Override
                    public void _next(ActivityDetail activityDetail) {
                        view.showActivityDetail(activityDetail);
                    }
                }));
    }

    @Override
    public void joinActivity(String activityId) {
        addSubscribe(NetHelper.getApi()
                .joinActivity(activityId)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<Object>() {
                    @Override
                    public void _next(Object object) {
                        view.refreshActivityData();
                    }
                }));
    }

    @Override
    public void quitActivity(String activityId) {
        addSubscribe(NetHelper.getApi()
                .quitActivity(activityId)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<Object>() {
                    @Override
                    public void _next(Object object) {
                        view.refreshActivityData();
                    }
                }));
    }

    @Override
    public void cancelActivity(String activityId) {
        addSubscribe(NetHelper.getApi()
                .cancelActivity(activityId)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<Object>() {
                    @Override
                    public void _next(Object object) {
                        view.refreshActivityData();
                    }
                }));
    }
}
