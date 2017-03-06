package lilun.com.pension.ui.education.list;

import java.util.ArrayList;
import java.util.List;

import lilun.com.pension.app.OrganizationChildrenConfig;
import lilun.com.pension.base.RxPresenter;
import lilun.com.pension.module.bean.ElderEdus;
import lilun.com.pension.module.bean.ElderEdusColleage;
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
public class EducationListPresenter extends RxPresenter<EducationListContract.View> implements EducationListContract.Presenter {

    @Override
    public void getColleage(String filter, int skip) {
        addSubscribe(NetHelper.getApi()
                .getOrganizationsEdus(filter)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<ElderEdusColleage>>() {
                    @Override
                    public void _next(List<ElderEdusColleage> colleages) {
                        view.completeRefresh();
                        if (skip == 0)
                            view.showEdusList(colleages, false);
                        else
                            view.showEdusList(colleages, true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.completeRefresh();
                    }
                }));
    }


}
