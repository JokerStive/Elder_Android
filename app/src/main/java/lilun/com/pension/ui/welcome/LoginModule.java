package lilun.com.pension.ui.welcome;

import com.orhanobut.logger.Logger;

import lilun.com.pension.app.User;
import lilun.com.pension.module.bean.Account;
import lilun.com.pension.module.bean.OrganizationAccount;
import lilun.com.pension.module.bean.TokenInfo;
import lilun.com.pension.module.utils.PreUtils;
import lilun.com.pension.module.utils.RxUtils;
import lilun.com.pension.module.utils.StringUtils;
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
        OrganizationAccount oa = account.getOa();
        String organizationId = StringUtils.removeSpecialSuffix(oa.getOrganizationId());
        Logger.d("账户默认所属组织 = "+organizationId);
        User.putBelongsOrganizationId(organizationId);
        User.puttCurrentOrganizationId(organizationId);
    }

    private Account getAccount(String username, String password) {
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        return account;
    }
}
