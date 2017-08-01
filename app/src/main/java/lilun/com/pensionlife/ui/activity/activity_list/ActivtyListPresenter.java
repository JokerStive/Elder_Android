package lilun.com.pensionlife.ui.activity.activity_list;

import java.util.List;

import lilun.com.pensionlife.base.RxPresenter;
import lilun.com.pensionlife.module.bean.OrganizationActivity;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.net.NetHelper;
import lilun.com.pensionlife.net.RxSubscriber;

/**
 * Created by zp on 2017/3/6.
 */

public class ActivtyListPresenter extends RxPresenter<ActivityListContract.View>
        implements ActivityListContract.Presenter {

    @Override
    public void getOrganizationActivities(String filter, int skip) {
        addSubscribe(NetHelper.getApi()
                .getOrganizationActivities(StringUtils.addFilterWithDef(filter, skip))
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<OrganizationActivity>>() {
                    @Override
                    public void _next(List<OrganizationActivity> activityCategories) {
                        view.completeRefresh();
                        view.showActivityList(activityCategories, skip == 0);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.completeRefresh();
                    }
                }));
    }
}
