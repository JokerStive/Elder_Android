package lilun.com.pension.ui.welcome;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import java.io.Serializable;
import java.util.List;

import lilun.com.pension.app.Constants;
import lilun.com.pension.app.User;
import lilun.com.pension.module.bean.Account;
import lilun.com.pension.module.bean.OrganizationAccount;
import lilun.com.pension.module.bean.TokenInfo;
import lilun.com.pension.module.utils.ACache;
import lilun.com.pension.module.utils.PreUtils;
import lilun.com.pension.module.utils.RxUtils;
import lilun.com.pension.module.utils.StringUtils;
import lilun.com.pension.module.utils.ToastHelper;
import lilun.com.pension.net.NetHelper;
import rx.Observable;

/**
 * 登录M
 *
 * @author yk
 *         create at 2017/1/23 13:57
 *         email : yk_developer@163.com
 */
public class LoginModule implements LoginContract.Module {


    @Override
    public Observable<TokenInfo> login(String username, String password) {
        return NetHelper.getApi()
                .login(getAccount(username, password))
                .compose(RxUtils.handleResult())
//                .compose(RxUtils.applySchedule())

                ;
    }

    @Override
    public Observable<Account> getAccountInfo(TokenInfo tokenInfo) {
        putToken(tokenInfo.getId());
        return NetHelper.getApi()
                .getAccountInfo(tokenInfo.getUserId())
                .compose(RxUtils.handleResult())
//                .compose(RxUtils.applySchedule())

                ;
    }

    @Override
    public Observable<List<OrganizationAccount>> getBelongOrganizations(Account account) {
        putAccountInfo(account);
        return NetHelper.getApi()
                .getOrganizationAccounts(account.getId())
                .compose(RxUtils.handleResult());


    }

    @Override
    public void putToken(String token) {
        PreUtils.putString(User.token, token);
    }

    @Override
    public void putAccountInfo(Account account) {
        User.putUserId(account.getId());
        OrganizationAccount oa = account.getOa();
        String organizationId1 = oa.getOrganizationId();
        if (TextUtils.isEmpty(organizationId1)) {
            ToastHelper.get().showWareShort("脏数据,账号没有所属组织");
            return;
        }
        String organizationId = StringUtils.removeSpecialSuffix(oa.getOrganizationId());
        Logger.d("账户默认所属组织 = " + organizationId);
        User.putBelongsOrganizationId(organizationId);
        User.puttCurrentOrganizationId(organizationId);
        User.putIsCustomer(account.isCustomer());
        User.putName(account.getUsername());
        User.putContactId(account.getDefaultContactId());
    }


    @Override
    public void putBelongOrganizations(List<OrganizationAccount> organizations) {
        ACache.get().put(User.belongOrganizations, (Serializable) organizations);
        for (OrganizationAccount oa : organizations) {
            if (oa.getOrganizationId().contains(Constants.special_organization_root)) {
                User.putIsCustomer(false);
                return;
            }
        }
        User.putIsCustomer(true);
    }


    private Account getAccount(String username, String password) {
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        return account;
    }
}
