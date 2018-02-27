package lilun.com.pensionlife.ui.welcome;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.List;

import lilun.com.pensionlife.app.User;
import lilun.com.pensionlife.base.RxPresenter;
import lilun.com.pensionlife.module.bean.Account;
import lilun.com.pensionlife.module.bean.AppVersion;
import lilun.com.pensionlife.module.bean.OrganizationAccount;
import lilun.com.pensionlife.module.utils.Preconditions;
import lilun.com.pensionlife.module.utils.RegexUtils;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.module.utils.ToastHelper;
import lilun.com.pensionlife.net.ApiException;
import lilun.com.pensionlife.net.NetHelper;
import lilun.com.pensionlife.net.RxSubscriber;

/**
 * 登录P
 *
 * @author yk
 *         create at 2017/1/23 13:57
 *         email : yk_developer@163.com
 */
public class LoginPresenter extends RxPresenter<LoginContract.View> implements LoginContract.Presenter {

    private LoginContract.Module mModule;
    private LoginContract.View mView;

    public LoginPresenter(@NonNull LoginContract.View view) {
        this.mView = Preconditions.checkNotNull(view);
        this.mModule = new LoginModule();
    }

    @Override
    public void login(String username, String password) {
        if (!checkAccountData(username, password)) {
            return;
        }
//        if (!RegexUtils.checkMobile(username)){
//
//        }

//        username = username.replace(" ", "");
//        password = password.replace(" ", "");
        final String finalUsername = username;
        final String finalPassword = password;
        addSubscribe(mModule.login(username, password)
                .flatMap(tokenInfo -> mModule.getAccountInfo(tokenInfo, finalUsername, finalPassword))
                .flatMap(account -> mModule.getBelongOrganizations(account))
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<OrganizationAccount>>(getActivity()) {
                    @Override
                    public void _next(List<OrganizationAccount> organizationAccounts) {
                        mModule.putBelongOrganizations(organizationAccounts);

                        String needChangeDefaultOrganizationId = mModule.isNeedChangeDefaultOrganizationId();
                        if (TextUtils.equals(needChangeDefaultOrganizationId, LoginModule.locationIsEmpty)) {
                            ToastHelper.get().showLong("location为空，检查数据");
                        } else if (TextUtils.equals(needChangeDefaultOrganizationId, LoginModule.locationEqualsDefaultOrganizationId)) {
                            loginSuccess(User.getBelongOrganizationAccountId());
                        } else if (TextUtils.equals(needChangeDefaultOrganizationId, LoginModule.noOrganizationAccountIdMappingLocation)) {
                            ToastHelper.get().showLong("没有找到location对应的organizationAccount,检查数据");
                        } else {
                            changeDefOrganizationAccountId(needChangeDefaultOrganizationId);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.hideDialog();
                        if (e instanceof ApiException){
                            int errorCode = ((ApiException) e).getErrorCode();
                            if (errorCode==613){
                                mView.activateAccount(username);
                                return;
                            }
                        }
                        int[] errorCode = {401};
                        String[] errorMessage = {"账号或密码错误，请重新输入"};
                        super.onError(e, errorCode, errorMessage);
                    }
                }));
    }

    /**
     * 登陆成功贮备
     */
    private void loginSuccess(String targetOrganizationAccountId) {
        if (mModule.saveUserAboutOrganization(targetOrganizationAccountId)) {
            view.loginSuccess();
        } else {
            ToastHelper.get().showShort("没有找到和居住地对应的数据，或者居住地是地球村,检查账户");
        }
    }

    @Override
    public boolean checkAccountData(String username, String password) {
        if (TextUtils.isEmpty(username)) {
            ToastHelper.get().showWareShort("用户名不能为空");
            return false;
        }

        if (!RegexUtils.checkMobile(username)) {
            ToastHelper.get().showWareShort("手机号格式错误");
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            ToastHelper.get().showWareShort("密码不能为空");
            return false;
        }

        return true;
    }


    private void changeDefOrganizationAccountId(String targetId) {
        Account account = new Account();
        account.setDefaultOrganizationId(targetId);
        NetHelper.getApi().putAccount(User.getUserId(), account)
                .compose(RxUtils.applySchedule())
                .compose(RxUtils.handleResult())
                .subscribe(new RxSubscriber<Account>(getActivity()) {
                    @Override
                    public void _next(Account account) {
                        loginSuccess(targetId);
                    }
                });
    }

    @Override
    public void getVersionInfo(String appName, String versionName) {
        addSubscribe(NetHelper.getApi()
                .getVersionInfo(appName, versionName)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<AppVersion>() {
                    @Override
                    public void _next(AppVersion version) {
                        view.showVersionInfo(version);
                    }
                }));
    }


}
