package lilun.com.pension.ui.education.classify;

import java.util.List;

import lilun.com.pension.R;
import lilun.com.pension.app.App;
import lilun.com.pension.base.RxPresenter;
import lilun.com.pension.module.bean.ActivityCategory;
import lilun.com.pension.module.bean.ElderModule;
import lilun.com.pension.module.bean.ElderEdusColleage;
import lilun.com.pension.module.utils.RxUtils;
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
    public void getAboutMe(String filter, int skip) {
        addSubscribe(NetHelper.getApi()
                .getOrganizationsEdus(filter)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<ElderEdusColleage>>() {
                    @Override
                    public void _next(List<ElderEdusColleage> elderModules) {
//                        if (skip == 0)
//                            view.showColleage(elderModules, false);
//                        else
//                            view.showColleage(elderModules, true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.completeRefresh();
                    }
                }));
    }
}
