package lilun.com.pension.ui.education.classify;

import java.util.ArrayList;
import java.util.List;

import lilun.com.pension.R;
import lilun.com.pension.app.App;
import lilun.com.pension.base.RxPresenter;
import lilun.com.pension.module.bean.ActivityCategory;
import lilun.com.pension.module.bean.ElderEdus;
import lilun.com.pension.module.bean.ElderModule;
import lilun.com.pension.module.bean.ElderEdusColleage;
import lilun.com.pension.module.bean.LocationBean;
import lilun.com.pension.module.bean.OrganizationActivity;
import lilun.com.pension.module.utils.RxUtils;
import lilun.com.pension.module.utils.StringUtils;
import lilun.com.pension.net.NetHelper;
import lilun.com.pension.net.RxSubscriber;

/**
 * 老年教育P
 *
 * @author yk
 *         create at 2017/2/13 10:55
 *         email : yk_developer@163.com
 */
public class EducationClassifyPresenter extends RxPresenter<EducationClassifyContract.View>
        implements EducationClassifyContract.Presenter {


    @Override
    public void getClassifies() {
        String education = App.context.getString(R.string.pension_education);
        String filter = "{\"where\":{\"parent\":\"" + education + "\"},\"order\":\"orderId\"}";
        addSubscribe(NetHelper.getApi()
                .getElderModules(filter)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<ElderModule>>() {
                    @Override
                    public void _next(List<ElderModule> elderModules) {
                        view.showClassifies(elderModules);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.completeRefresh();
                    }
                }));
    }

    @Override
    public void getOrgActivityCategory(String filter) {
        addSubscribe(NetHelper.getApi()
                .getActivityCategories(filter)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<ActivityCategory>>() {
                    @Override
                    public void _next(List<ActivityCategory> activityCategories) {
                        view.showOrgActivityCategory(activityCategories);
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
        .getMyCourse(StringUtils.addFilterWithDef(filter, skip))
        .compose(RxUtils.handleResult())
        .compose(RxUtils.applySchedule())
        .subscribe(new RxSubscriber<List<ElderEdus>>() {
            @Override
            public void _next(List<ElderEdus> eduses) {
                view.completeRefresh();
                view.showAboutMe(eduses, false);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.completeRefresh();
            }
        }));
    }
}
