package lilun.com.pensionlife.ui.activity.classify;

import com.orhanobut.logger.Logger;

import java.io.Serializable;
import java.util.List;

import lilun.com.pensionlife.base.RxPresenter;
import lilun.com.pensionlife.module.bean.ActivityCategory;
import lilun.com.pensionlife.module.bean.OrganizationActivity;
import lilun.com.pensionlife.module.utils.ACache;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.net.NetHelper;
import lilun.com.pensionlife.net.RxSubscriber;

/**
 * 社区活动P
 *
 * @author yk
 *         create at 2017/2/13 10:55
 *         email : yk_developer@163.com
 */
public class ActivityClassifyPresenter extends RxPresenter<ActivityClassifyContract.View> implements ActivityClassifyContract.Presenter {

    @Override
    public void getClassifies(String organizationId) {
        List<ActivityCategory> activityCategories = (List<ActivityCategory>) ACache.get().getAsObject("activityClassify_" + StringUtils.encodeURL(organizationId));
        if (activityCategories != null && activityCategories.size() != 0) {
            Logger.i("activity classify has cache");
            view.showClassifies(activityCategories);
            return;
        }
        String filter = "{\"where\":{\"organizationId\":\"" + organizationId + "/#activity-category\",\"setting\":{\"$nin\": \"isSpecial\"}},\"order\":\"orderId\"}";
//        String filter = "{\"where\":{\"organizationId\":\""+organizationId+"/#activity-category\"},\"order\":\"orderId\"}";
        addSubscribe(NetHelper.getApi()
                .getActivityCategories(filter)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<ActivityCategory>>() {
                    @Override
                    public void _next(List<ActivityCategory> activityCategories) {
                        ACache.get().put("activityClassify_" + StringUtils.encodeURL(organizationId), (Serializable) activityCategories);
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
                .getMyActivities(StringUtils.addFilterWithDef(filter, skip))
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
