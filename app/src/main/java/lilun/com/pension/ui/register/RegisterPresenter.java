package lilun.com.pension.ui.register;

import android.util.Log;

import lilun.com.pension.base.RxPresenter;
import lilun.com.pension.module.bean.Phone;
import lilun.com.pension.module.utils.RxUtils;
import lilun.com.pension.net.NetHelper;
import lilun.com.pension.net.RxSubscriber;

/**
 * Created by zp on 2017/4/13.
 */

public class RegisterPresenter extends RxPresenter<RegisterContract.View> implements RegisterContract.Presenter {


    @Override
    public void getIDCode(String phone) {
        addSubscribe(NetHelper.getApi()
                .getIDCode(phone)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<Object>() {
                    @Override
                    public void _next(Object s) {

                        view.successOfIDCode("s");
                    }
                }));
    }
}
