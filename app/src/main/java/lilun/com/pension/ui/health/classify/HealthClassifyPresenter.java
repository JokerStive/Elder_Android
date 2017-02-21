package lilun.com.pension.ui.health.classify;

import java.util.List;

import lilun.com.pension.R;
import lilun.com.pension.app.App;
import lilun.com.pension.base.RxPresenter;
import lilun.com.pension.module.bean.ElderModule;
import lilun.com.pension.module.utils.RxUtils;
import lilun.com.pension.net.NetHelper;
import lilun.com.pension.net.RxSubscriber;

/**
 * 健康服务P
 *
 * @author yk
 *         create at 2017/2/13 10:55
 *         email : yk_developer@163.com
 */
public class HealthClassifyPresenter extends RxPresenter<HealthClassifyContract.View> implements HealthClassifyContract.Presenter {
    @Override
    public void getClassifies() {
        String health_service = App.context.getString(R.string.health_service);
        String filter = "{\"where\":{\"parent\":\"" + health_service + "\"},\"order\":\"orderId\"}";
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

    }


}
