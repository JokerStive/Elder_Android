package lilun.com.pension.ui.welcome;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.List;

import lilun.com.pension.base.BaseActivity;
import lilun.com.pension.base.RxPresenter;
import lilun.com.pension.module.bean.OrganizationAccount;
import lilun.com.pension.module.utils.Preconditions;
import lilun.com.pension.module.utils.RxUtils;
import lilun.com.pension.module.utils.ToastHelper;
import lilun.com.pension.net.RxSubscriber;

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
                .subscribe(new RxSubscriber<List<OrganizationAccount>>((BaseActivity)mView) {
                    @Override
                    public void _next(List<OrganizationAccount> organizationAccounts) {
                        mModule.putBelongOrganizations(organizationAccounts);
                        mView.loginSuccess();
                    }
                }));
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


}
