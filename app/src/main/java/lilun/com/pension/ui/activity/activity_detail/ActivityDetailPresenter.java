package lilun.com.pension.ui.activity.activity_detail;

import java.util.List;

import lilun.com.pension.base.RxPresenter;
import lilun.com.pension.module.bean.OrganizationActivity;
import lilun.com.pension.module.utils.RxUtils;
import lilun.com.pension.net.NetHelper;
import lilun.com.pension.net.RxSubscriber;

/**
 * Created by zp on 2017/3/6.
 */

public class ActivityDetailPresenter extends RxPresenter<ActivityDetailContact.View>
        implements ActivityDetailContact.Presenter {
    @Override
    public void getActivityDetail(String id) {
        addSubscribe(NetHelper.getApi()
                .getOrganizationActivitiesDetail(id, "{}")
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<OrganizationActivity>() {
                    @Override
                    public void _next(OrganizationActivity organizationActivities) {
                        view.completeRefresh();
                       // if (organizationActivities != null && organizationActivities.size() > 0)
                            view.showActivityDetail(organizationActivities);
                    }
                }));
    }
}
