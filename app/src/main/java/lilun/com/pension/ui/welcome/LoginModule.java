package lilun.com.pension.ui.welcome;

import lilun.com.pension.app.User;
import lilun.com.pension.module.bean.Account;
import lilun.com.pension.module.bean.TokenInfo;
import lilun.com.pension.module.utils.PreUtils;
import lilun.com.pension.module.utils.RxUtils;
import lilun.com.pension.net.NetHelper;
import rx.Observable;

/**
*登录M
*@author yk
*create at 2017/1/23 13:57
*email : yk_developer@163.com
*/
public class LoginModule implements LoginContract.Module {


    @Override
    public Observable<TokenInfo> login(String username, String password) {
        return NetHelper.getApi()
                .login(getAccount(username, password))
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule());
    }

    @Override
    public Observable<Account> getAccountInfo(TokenInfo tokenInfo) {
        putToken(tokenInfo.getId());
        return NetHelper.getApi()
                .getAccountInfo(tokenInfo.getUserId())
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule());
    }

    @Override
    public void putToken(String token) {
        PreUtils.putString(User.token,token);
    }

    @Override
    public void putAccountInfo(Account account) {
        User.putUserId(account.getId());
        User.putBelongsOrganizationId(account.getDefaultOrganizationId());
        User.puttCurrentOrganizationId(account.getDefaultOrganizationId());
    }

    private Account getAccount(String username, String password) {
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        return account;
    }
}
