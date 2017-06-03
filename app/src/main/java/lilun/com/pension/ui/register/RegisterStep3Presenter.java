package lilun.com.pension.ui.register;

import lilun.com.pension.app.App;
import lilun.com.pension.base.RxPresenter;
import lilun.com.pension.module.utils.RxUtils;
import lilun.com.pension.module.utils.ToastHelper;
import lilun.com.pension.net.ApiException;
import lilun.com.pension.net.NetHelper;
import lilun.com.pension.net.RxSubscriber;
import me.yokeyword.fragmentation.SupportActivity;

/**
 * Created by zp on 2017/4/14.
 */

public class RegisterStep3Presenter extends RxPresenter<RegisterContract.ViewStep3>
        implements RegisterContract.PresenterStep3 {


    @Override
    public void checkIDCode(SupportActivity _mActivity, String phone, String aIDCode) {
        addSubscribe(NetHelper.getApi()
                .checkIDCode(phone, aIDCode)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<Boolean>(_mActivity) {
                    @Override
                    public void _next(Boolean bool) {
                        view.successOfCheckIDCode(bool);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);

                    }
                }));
    }

    @Override
    public void getIDCode(SupportActivity _mActivity, String phone, String type) {
        addSubscribe(NetHelper.getApi()
                .getIDCode(phone, type)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<Object>(_mActivity) {
                    @Override
                    public void _next(Object s) {

                        view.successOfIDCode();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.hideDialog();
                        if (e == null) {
                            ToastHelper.get(App.context).showWareShort("网络连接失败");
                            return;
                        }
                        if (e instanceof ApiException) {
                            if (((ApiException) e).getErrorMessage().contains("Remote service error, code 15;")) {
                                ToastHelper.get(App.context).showWareShort("短信发送太多，请1小时后尝试");
                                return;
                            }
                        }
                        int[] errorCode = {600, 601, 603};
                        String[] errorMessage = {
                                "1分钟只能发送一条短信",
                                "短信发送太多，请1小时后尝试",
                                "该手机号码被注册，请更换手机号码"};
                        super.onError(e, errorCode, errorMessage);
                    }
                }));
    }
}
