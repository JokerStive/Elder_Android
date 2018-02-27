package lilun.com.pensionlife.ui.register;

import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.base.RxPresenter;
import lilun.com.pensionlife.module.bean.Account;
import lilun.com.pensionlife.module.bean.Register;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.module.utils.ToastHelper;
import lilun.com.pensionlife.net.ApiException;
import lilun.com.pensionlife.net.NetHelper;
import lilun.com.pensionlife.net.RxSubscriber;

/**
 * Created by zp on 2017/7/17.
 */

public class RegisterAccountPresenter extends RxPresenter<RegisterContract.ViewAccount>
        implements RegisterContract.PresenterAccount {

    /**
     * 获取验证码
     *
     * @param phone
     * @param type
     */
    @Override
    public void getIDCode(String phone, String type) {
        addSubscribe(NetHelper.getApi()
                .getIDCode(phone, type)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<Object>(((BaseFragment) view).getActivity()) {
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
                                ToastHelper.get().showWareShort("短信发送太多，请1小时后尝试");
                                return;
                            }

                            if (((ApiException) e).getErrorCode() == 613) {
                                view.activateAccount(phone);
                                return;
                            }
                        }
                        int[] errorCode = {600, 601, 603, 604};
                        String[] errorMessage = {
                                "1分钟只能发送一条短信",
                                "短信发送太多，请1小时后尝试",
                                "该手机号码已注册",
                                "该手机号码还未注册，请确认输入正确"};
                        super.onError(e, errorCode, errorMessage);
                    }
                }));
    }

    /**
     * post注册用户信息
     *
     * @param IdCode
     * @param account
     */
    @Override
    public void postRegisterAccount(String IdCode, Account account) {
        addSubscribe(NetHelper.getApi()
                .commitRegister(IdCode, account)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<Register>(((BaseFragment) view).getActivity()) {
                    @Override
                    public void _next(Register register) {
                        view.successOfRegisterAccount(register);
                    }
                }));
    }

    @Override
    public void activateAccount(String mobile, String code, Account account) {
        addSubscribe(NetHelper.getApi()
                .activateAccount(mobile,code,"user", account)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<Register>(((BaseFragment) view).getActivity()) {
                    @Override
                    public void _next(Register register) {
                        view.successOfRegisterAccount(register);
                    }
                }));
    }
}
