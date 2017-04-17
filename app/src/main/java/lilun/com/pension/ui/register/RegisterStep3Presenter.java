package lilun.com.pension.ui.register;

import lilun.com.pension.base.RxPresenter;
import lilun.com.pension.module.utils.RxUtils;
import lilun.com.pension.net.NetHelper;
import lilun.com.pension.net.RxSubscriber;

/**
 * Created by zp on 2017/4/14.
 */

public class RegisterStep3Presenter extends RxPresenter<RegisterContract.ViewStep3>
implements  RegisterContract.PresenterStep3{


    @Override
    public void checkIDCode(String phone, String aIDCode) {
        addSubscribe(NetHelper.getApi()
        .checkIDCode(phone,aIDCode)
        .compose(RxUtils.handleResult())
        .compose(RxUtils.applySchedule())
        .subscribe(new RxSubscriber<Boolean>() {
            @Override
            public void _next(Boolean bool) {
                view.successOfCheckIDCode();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);

            }
        }));
    }
}
