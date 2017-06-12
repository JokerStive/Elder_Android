package lilun.com.pension.ui.welcome;

import android.text.TextUtils;

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

        User.putPassword(password);
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
        //居住地信息
        if (account.getProfile() != null) {
            User.putBelongToDistrict(account.getProfile().getBelongToDistrict());
        }
        //用户名
        User.putUserId(account.getId());

        //默认所属组织账号id,这个必须存在，否则脏数据
        User.putBelongOrganizationAccountId(account.getDefaultOrganizationId());

        //创建者是否是自己
        User.putIsCustomer(account.isCustomer());

        //姓名
        User.putName(account.getName());

        //用户名
        User.putUserName(account.getUsername());

        //
        if (null != account.getImage() && account.getImage().size() > 0)
            User.puttUserAvatar(account.getImage().get(0).getFileName());


        //手机号
        User.putMobile(account.getMobile());


        //个人资料
        User.putContactId(account.getDefaultContactId() == null ? "" : account.getDefaultContactId());

    }


    @Override
    public void putBelongOrganizations(List<OrganizationAccount> organizations) {
        ACache.get().put(User.belongOrganizations, (Serializable) organizations);
    }


    @Override
    public boolean saveUserAboutOrganization(String belongOrganizationAccountId) {
        boolean result=false;
        List<OrganizationAccount> organizationAccounts = (List<OrganizationAccount>) ACache.get().getAsObject(User.belongOrganizations);
        if (!TextUtils.isEmpty(belongOrganizationAccountId) && organizationAccounts != null) {
            for (OrganizationAccount organizationAccount : organizationAccounts) {

                String organizationAccountId = organizationAccount.getId();
                String organizationId = StringUtils.removeSpecialSuffix(organizationAccount.getOrganizationId());


                if (organizationId.equals(Constants.organization_root)) {
                    User.putRootOrganizationAccountId(organizationAccount.getId());
                }

                if (organizationId.contains(Constants.special_organization_root)) {
                    User.putIsCustomer(false);

                }

                //如果其中一个organizationAccountId和account的defacltOrganizationId相同，则这个organizationAccount就是默认的
                if (organizationAccountId.equals(belongOrganizationAccountId)) {

                    //默认组织id
                    User.putBelongsOrganizationId(organizationId);

                    //当前组织id
                    User.putCurrentOrganizationId(organizationId);

                    //默认组织账号id
                    User.putBelongOrganizationAccountId(organizationAccountId);

                    //当前组织账号id
                    User.putCurrentOrganizationAccountId(organizationAccountId);

                    result =  true;
                }

            }

        }

        return result;
    }


    private Account getAccount(String username, String password) {
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        return account;
    }


}
