package lilun.com.pensionlife.ui.welcome;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import java.util.List;

import lilun.com.pensionlife.app.User;
import lilun.com.pensionlife.base.BaseActivity;
import lilun.com.pensionlife.base.RxPresenter;
import lilun.com.pensionlife.module.bean.Account;
import lilun.com.pensionlife.module.bean.OrganizationAccount;
import lilun.com.pensionlife.module.utils.ACache;
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
                        if (mModule.saveUserAboutOrganization(User.getBelongOrganizationAccountId())) {
                            changeSpecialOrganization();
                        } else {
                            ToastHelper.get().showShort("脏数据,请检查账户");
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


    /**
     * 当前是处在特殊组织时，需要切换回自己的小区
     */
    private void changeSpecialOrganization() {
        String currentOrganizationId = User.getCurrentOrganizationId();
        if (currentOrganizationId.contains("#department")) {
            Logger.d("在特殊组织下需要切换");
            List<OrganizationAccount> list = (List<OrganizationAccount>) ACache.get().getAsObject(User.belongOrganizations);
            for (int i = 0; i < list.size(); i++) {
                String organizationId = list.get(i).getOrganizationId();
                if (organizationId.contains("#department") || organizationId.contains("社会组织")) {
                    list.remove(i);
                    i--;
                }
            }
            for (int i = 0; i < list.size() - 1; i++) {
                for (int j = 1; j < list.size() - i; j++) {
                    if ((list.get(j - 1)).getOrganizationId().length() < (list.get(j)).getOrganizationId().length()) {   //比较两个整数的大小
                        OrganizationAccount temp = list.get(j - 1);
                        list.set((j - 1), list.get(j));
                        list.set(j, temp);
                    }
                }
            }

            String targetId = list.get(0).getId();
            changeDefOrganizationAccountId(targetId);
        } else {
            mView.loginSuccess();
        }
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
                        }
                    }
                });
    }


}
