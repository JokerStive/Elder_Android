package lilun.com.pension.ui.register;

import lilun.com.pension.base.RxPresenter;
import lilun.com.pension.module.utils.RxUtils;
import lilun.com.pension.net.NetHelper;
import lilun.com.pension.net.RxSubscriber;

/**
 * Created by zp on 2017/4/13.
 */

public class RegisterStep2Presenter extends RxPresenter<RegisterContract.ViewStep2> implements RegisterContract.PresenterStep2 {


    @Override
    public void getIDCode(String phone) {
        addSubscribe(NetHelper.getApi()
                .getIDCode(phone)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<Object>() {
                    @Override
                    public void _next(Object s) {

                        view.successOfIDCode();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                     //   e.getMessage()
                    }
                }));
    }
}
