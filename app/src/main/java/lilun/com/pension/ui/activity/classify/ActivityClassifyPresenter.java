package lilun.com.pension.ui.activity.classify;

import com.orhanobut.logger.Logger;

import java.io.Serializable;
import java.util.List;

import lilun.com.pension.app.OrganizationChildrenConfig;
import lilun.com.pension.base.RxPresenter;
import lilun.com.pension.module.bean.ActivityCategory;
import lilun.com.pension.module.bean.OrganizationActivity;
import lilun.com.pension.module.utils.ACache;
import lilun.com.pension.module.utils.RxUtils;
import lilun.com.pension.module.utils.StringUtils;
import lilun.com.pension.net.NetHelper;
import lilun.com.pension.net.RxSubscriber;

/**
 * 社区活动P
 *
 * @author yk
 *         create at 2017/2/13 10:55
 *         email : yk_developer@163.com
 */
public class ActivityClassifyPresenter extends RxPresenter<ActivityClassifyContract.View> implements ActivityClassifyContract.Presenter {

    @Override
    public void getClassifies() {
        List<ActivityCategory> activityCategories = (List<ActivityCategory>) ACache.get().getAsObject("activityClassify");
        if (activityCategories != null && activityCategories.size() != 0) {
            Logger.i("activity classify has cache");
            view.showClassifies(activityCategories);
            return;
        }
        String filter = "{\"where\":{\"setting\":{\"exists\": false}},\"order\":\"orderId\"}";
        addSubscribe(NetHelper.getApi()
                .getActivityCategories(filter)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<ActivityCategory>>() {
                    @Override
                    public void _next(List<ActivityCategory> activityCategories) {
                        ACache.get().put("activityClassify", (Serializable) activityCategories);
                        view.showClassifies(activityCategories);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.completeRefresh();
                    }
                }));
    }

    @Override
    public void getAboutMe(String filter, int skip) {
        addSubscribe(NetHelper.getApi()
                .getOrganizationActivities(StringUtils.addFilterWithDef(filter, skip))
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<OrganizationActivity>>() {
                    @Override
                    public void _next(List<OrganizationActivity> activities) {
                        view.showAboutMe(activities, skip != 0);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.completeRefresh();
                    }
                }));
    }


}
