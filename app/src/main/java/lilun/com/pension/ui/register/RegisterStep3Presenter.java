package lilun.com.pension.ui.register;

import lilun.com.pension.base.RxPresenter;
import lilun.com.pension.module.utils.RxUtils;
import lilun.com.pension.net.NetHelper;
import lilun.com.pension.net.RxSubscriber;
import me.yokeyword.fragmentation.SupportActivity;

/**
 * Created by zp on 2017/4/14.
 */

public class RegisterStep3Presenter extends RxPresenter<RegisterContract.ViewStep3>
implements  RegisterContract.PresenterStep3{


    @Override
    public void checkIDCode(SupportActivity _mActivity, String phone, String aIDCode) {
        addSubscribe(NetHelper.getApi()
        .checkIDCode(phone,aIDCode)
        .compose(RxUtils.handleResult())
        .compose(RxUtils.applySchedule())
        .subscribe(new RxSubscriber<Boolean>(_mActivity) {
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
