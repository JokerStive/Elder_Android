package lilun.com.pension.ui.welcome;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import java.io.Serializable;
import java.util.List;

import lilun.com.pension.app.App;
import lilun.com.pension.app.Constants;
import lilun.com.pension.app.User;
import lilun.com.pension.module.bean.Account;
import lilun.com.pension.module.bean.OrganizationAccount;
import lilun.com.pension.module.bean.TokenInfo;
import lilun.com.pension.module.utils.ACache;
import lilun.com.pension.module.utils.PreUtils;
import lilun.com.pension.module.utils.RxUtils;
import lilun.com.pension.module.utils.StringUtils;
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
                .compose(RxUtils.handleResult());
    }

    @Override
    public Observable<Account> getAccountInfo(TokenInfo tokenInfo, String username, String password) {
        putToken(tokenInfo.getId());
        User.putUserName(username);
        User.putPassword(password);
        App.mqttConnectAndSub();
        return NetHelper.getApi()
                .getAccountInfo(tokenInfo.getUserId())
                .compose(RxUtils.handleResult());
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
        OrganizationAccount organizationAccount = account.getOa();
        if (organizationAccount != null) {
            String organizationId = organizationAccount.getOrganizationId();
            if (!TextUtils.isEmpty(organizationId)) {
                String defOrganizationId = StringUtils.removeSpecialSuffix(organizationId);
                Logger.d("账户默认所属组织 = " + defOrganizationId);

                //默认组织id
                User.putBelongsOrganizationId(defOrganizationId);
                Logger.d("默认组织id" + organizationAccount.getId());
                //当前组织id
                User.putCurrentOrganizationId(defOrganizationId);

                //创建者是否是自己
                User.putIsCustomer(account.isCustomer());

                //用户名
                User.putName(account.getName());
                User.putUserName(account.getUsername());

                //个人资料
                User.putContactId(account.getDefaultContactId());

                //默认组织账号id
                User.putBelongOrganizationAccountId(organizationAccount.getId());
                Logger.d("默认组织账号id" + organizationAccount.getId());

                //当前组织账号id
                User.putCurrentOrganizationAccountId(organizationAccount.getId());

                //手机号
                User.putMobile(account.getMobile());
            }
        } else {
//            ToastHelper.get().showWareShort("脏数据,账号没有所属组织");
        }

    }


    @Override
    public void putBelongOrganizations(List<OrganizationAccount> organizations) {
        ACache.get().put(User.belongOrganizations, (Serializable) organizations);
        for (OrganizationAccount oa : organizations) {
            String organizationId = StringUtils.removeSpecialSuffix(oa.getOrganizationId());
            if (organizationId.equals(Constants.organization_root)) {
                User.putRootOrganizationAccountId(oa.getId());
            }

            if (organizationId.contains(Constants.special_organization_root)) {
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
