package lilun.com.pensionlife.ui.activity.activity_detail;

import java.util.List;

import lilun.com.pensionlife.base.RxPresenter;
import lilun.com.pensionlife.module.bean.Account;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.net.NetHelper;
import lilun.com.pensionlife.net.RxSubscriber;

/**
 * Created by zp on 2017/4/20.
 */

public class ActivityPartnersListPresenter extends RxPresenter<ActivityDetailContact.VPartner>
        implements ActivityDetailContact.PPartner {
    @Override
    public void queryPartners(String activityId, String filter, int skip) {
        addSubscribe(NetHelper.getApi()
                .queryPartners(activityId, StringUtils.addFilterWithDef(filter, skip))
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<Account>>() {
                    @Override
                    public void _next(List<Account> accounts) {
                        view.completeRefresh();
                        view.showPartners(accounts);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.completeRefresh();
                    }
                }));
    }

    @Override
    public void deletePartners(String activityId, String userId, String userName) {
        addSubscribe(NetHelper.getApi()
                .deleteOfPartners(activityId, userId)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<Account>() {
                    @Override
                    public void _next(Account account) {
                        view.successDeletePartners(userId,userName);
                    }
                }));
    }

    @Override
    public void addBlockUser(String activityId, String userId, String userName) {
        addSubscribe(NetHelper.getApi()
                .addBlockUser(activityId, userId)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<Object>() {
                    @Override
                    public void _next(Object o) {
                        view.successDeletePartners(userId,userName);
                    }
                }));
    }


}
