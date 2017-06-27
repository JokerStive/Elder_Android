package lilun.com.pensionlife.ui.welcome;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.List;

import lilun.com.pensionlife.app.User;
import lilun.com.pensionlife.base.BaseActivity;
import lilun.com.pensionlife.base.RxPresenter;
import lilun.com.pensionlife.module.bean.Account;
import lilun.com.pensionlife.module.bean.OrganizationAccount;
import lilun.com.pensionlife.module.utils.Preconditions;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.module.utils.ToastHelper;
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
        username = username.replace(" ", "");
        password = password.replace(" ", "");
        final String finalUsername = username;
        final String finalPassword = password;
        addSubscribe(mModule.login(username, password)
                .flatMap(tokenInfo -> mModule.getAccountInfo(tokenInfo, finalUsername, finalPassword))
                .flatMap(account -> mModule.getBelongOrganizations(account))
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<OrganizationAccount>>((BaseActivity) mView) {
                    @Override
                    public void _next(List<OrganizationAccount> organizationAccounts) {
                        mModule.putBelongOrganizations(organizationAccounts);
                        String belongOrganizationAccountId = User.getBelongOrganizationAccountId();
                        if (TextUtils.isEmpty(belongOrganizationAccountId)){
                            ToastHelper.get().showShort("defaultOrganizationId不存在,请检查账户");
                        }else {
                            String needChangeDefaultOrganizationId = mModule.isNeedChangeDefaultOrganizationId();
                            if (TextUtils.isEmpty(needChangeDefaultOrganizationId)){
                                //切换到最长的
                                String longestOrganizationAccountId = mModule.getLongestOrganizationAccountId();
                                if (TextUtils.isEmpty(longestOrganizationAccountId)){
                                    ToastHelper.get().showLong("最长的oa为空");
                                }else {
                                    changeDefOrganizationAccountId(longestOrganizationAccountId);
                                }
                            }else if (TextUtils.equals("success",needChangeDefaultOrganizationId)){
                                //登陆成功
                              loginSuccess();
                            }else {
                                //切换到居住地
                                changeDefOrganizationAccountId(needChangeDefaultOrganizationId);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        int[] errorCode = {401};
                        String[] errorMessage = {"账号或密码错误，请重新输入"};
                        super.onError(e, errorCode, errorMessage);
                    }
                }));
    }

    /**
    *登陆成功贮备
    */
    private void loginSuccess() {
//        User。
    }

    @Override
    public boolean checkAccountData(String username, String password) {
        if (TextUtils.isEmpty(username)) {
            ToastHelper.get().showWareShort("用户名不能为空");
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
                .subscribe(new RxSubscriber<Account>((BaseActivity) mView) {
                    @Override
                    public void _next(Account account) {
                        if (mModule.saveUserAboutOrganization(targetId)) {
                            mView.loginSuccess();
                        }else {
                            ToastHelper.get().showShort("没有找到和居住地对应的数据，或者居住地是地球村,检查账户");
                        }
                    }
                });
    }


}
