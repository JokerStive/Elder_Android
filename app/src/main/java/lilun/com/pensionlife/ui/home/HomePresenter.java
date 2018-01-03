package lilun.com.pensionlife.ui.home;

import android.text.TextUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import lilun.com.pensionlife.app.User;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.base.RxPresenter;
import lilun.com.pensionlife.module.bean.Account;
import lilun.com.pensionlife.module.bean.Information;
import lilun.com.pensionlife.module.bean.QuestionNaire;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.net.NetHelper;
import lilun.com.pensionlife.net.RxSubscriber;
import rx.Observable;
import rx.Subscription;

/**
 * 首页P
 *
 * @author yk
 *         create at 2017/2/6 16:23
 *         email : yk_developer@163.com
 */
public class HomePresenter extends RxPresenter<HomeContract.View> implements HomeContract.Presenter {

    private Subscription timerSubscribe;

    @Override
    public void getInformation() {
        String parentIdFilter;
        parentIdFilter = User.spliceId("/#information/公告");
        String filter;
        if (!TextUtils.isEmpty(parentIdFilter)) {
            filter = "{\"where\":{\"visible\":0,\"isCat\":false,\"parentId\":{\"inq\":" + parentIdFilter + "}}}";
        } else {
            filter = "{\"where\":{\"visible\":0,\"isCat\":false,\"parentId\":\"/地区村//#information/公告\"}}";
        }

        addSubscribe(NetHelper.getApi()
                .getInformations(StringUtils.addFilterWithDef(filter, 0))
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<Information>>() {
                    @Override
                    public void _next(List<Information> information) {
                        view.showInformation(information);
                    }
                }));
    }

    @Override
    public void needChangeToDefOrganization() {
        boolean currentStatusCorrect = User.isCurrentStatusCorrect();
        if (!currentStatusCorrect) {
            changeBelongOrganization(User.getBelongOrganizationAccountId(), -1);
        }
    }

    @Override
    public void changeBelongOrganization(String organizationId, int clickId) {
        Account account = new Account();
        account.setDefaultOrganizationId(organizationId);
        addSubscribe(NetHelper.getApi()
                .putAccount(User.getUserId(), account)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<Object>(((BaseFragment) view).getActivity()) {
                    @Override
                    public void _next(Object o) {
                        view.changeOrganizationSuccess(clickId);
                    }
                }));
    }

//    @Override
//    public void getVersionInfo(String appName, String versionName) {
//        addSubscribe(NetHelper.getApi()
//                .getVersionInfo(appName, versionName)
//                .compose(RxUtils.handleResult())
//                .compose(RxUtils.applySchedule())
//                .subscribe(new RxSubscriber<AppVersion>() {
//                    @Override
//                    public void _next(AppVersion version) {
//                        view.showVersionInfo(version);
//                    }
//                }));
//    }

    @Override
    public void getQuestionNaire() {
        NetHelper.getQuestionnaireApi()
                .getQuestionNaire()
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<QuestionNaire>() {
                    @Override
                    public void _next(QuestionNaire questionNaire) {
                        view.saveQuestionNaire(questionNaire);
                    }
                });
    }


    @Override
    public void startTimer() {
        timerSubscribe = Observable.interval(0, 3000, TimeUnit.MILLISECONDS)
                .compose(RxUtils.applySchedule())
                .subscribe(aLong -> {
                    if (view != null) {
                        view.setVpCurrentPosition();
                    }
                });

    }

    @Override
    public void stopTimer() {
        if (timerSubscribe != null) {
            timerSubscribe.unsubscribe();
        }
    }
}


