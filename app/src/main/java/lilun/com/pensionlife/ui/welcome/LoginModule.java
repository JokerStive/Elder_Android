package lilun.com.pensionlife.ui.welcome;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.List;

import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.app.Constants;
import lilun.com.pensionlife.app.User;
import lilun.com.pensionlife.module.bean.Account;
import lilun.com.pensionlife.module.bean.OrganizationAccount;
import lilun.com.pensionlife.module.bean.TokenInfo;
import lilun.com.pensionlife.module.utils.ACache;
import lilun.com.pensionlife.module.utils.DeviceUtils;
import lilun.com.pensionlife.module.utils.PreUtils;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.net.NetHelper;
import rx.Observable;

/**
 * 登录M
 *
 * @author yk
 *         create at 2017/1/23 13:57
 *         email : yk_developer@163.com
 */
public class LoginModule implements LoginContract.Module {

    public static String noOrganizationAccountIdMappingLocation = "0";
    public static String locationEqualsDefaultOrganizationId = "1";
    public static String locationIsEmpty = "2";


    @Override

    public Observable<TokenInfo> login(String username, String password) {
        return NetHelper.getApi()

                .login(getAccount(username, password))
                .compose(RxUtils.handleResult());
    }

    @Override
    public Observable<Account> getAccountInfo(TokenInfo tokenInfo, String username, String password) {
        putToken(tokenInfo.getId());
        String created = tokenInfo.getCreated();
        String loginTime = StringUtils.IOS2ToUTC(created, 2);
        User.putLoginTime(loginTime);
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

        //第一求助人
        if (account.getProfile() != null) {
            User.putHelpPhone(account.getProfile().getFirstHelperPhone());
            User.putAddress(account.getProfile().getAddress());
        }

        //居住地方
        User.putLocation(account.getLocation());
        //用户名
        User.putUserId(account.getId());

        //默认所属组织账号id,这个必须存在，否则脏数据
        User.putBelongOrganizationAccountId(account.getDefaultOrganizationId());

        //是否是个人用户
        User.putIsCustomer(true);

        //姓名
        User.putName(account.getName());

        //用户名
        User.putUserName(account.getUsername());

        //
        if (null != account.getImage())
            User.puttUserAvatar(account.getImage());


        //手机号
        User.putMobile(account.getMobile());


        //个人资料
        User.putContactId(account.getDefaultContactId() == null ? "" : account.getDefaultContactId());


        //商家跑单人员
        User.putBusinessId(getBusinessId(account.getRoles()));

        //认证协议
        User.putCertificateLicense(account.getCertificateLicense());

    }

    private String getBusinessId(List<String> roles) {
        String result = "";
        if (roles != null) {
            for (String roleId : roles) {
                if (roleId.endsWith("/#role/跑单")) {
                    result = roleId.substring(0, roleId.lastIndexOf("/#role"));
                }
            }
        }
        return result;
    }


    @Override
    public void putBelongOrganizations(List<OrganizationAccount> organizations) {
        ACache.get().put(User.belongOrganizations, (Serializable) organizations);
    }


    @Override
    public String isNeedChangeDefaultOrganizationId() {
        String result = locationIsEmpty;

        //居住地
        String location = User.getLocation();
        String belongOrganizationAccountId = User.getBelongOrganizationAccountId();


        //居住地不存在，则是在商家端注册的，需要切换
        if (!TextUtils.isEmpty(location)) {
            //居住地存在
            String organizationIdMappingOrganizationAccountId = getOrganizationIdMappingOrganizationAccountId(location);
            if (TextUtils.isEmpty(organizationIdMappingOrganizationAccountId)) {
                return noOrganizationAccountIdMappingLocation;
            }
            if (TextUtils.equals(belongOrganizationAccountId, organizationIdMappingOrganizationAccountId)) {
                // 和defaultOrganizationId对应，则不需要切换组织
                result = locationEqualsDefaultOrganizationId;
            } else {
                // 和defaultOrganizationId不对应，则需要切换到居住村对应的组织
                result = organizationIdMappingOrganizationAccountId;
            }
        } else {
            result = locationIsEmpty;
        }

        return result;
    }

    @Override
    public String getLongestOrganizationAccountId() {
        List<OrganizationAccount> list = (List<OrganizationAccount>) ACache.get().getAsObject(User.belongOrganizations);
        if (list == null || list.size() == 0) {
            return "";
        }
        for (int i = 0; i < list.size(); i++) {
            String organizationId = list.get(i).getOrganizationId();
            boolean condition = organizationId.contains("#department") || organizationId.contains("社会组织") || organizationId.endsWith("/#staff");
            if (condition) {
                list.remove(i);
                i--;
            }
        }

        if (list.size() <= 0) {
            return null;
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
        return list.get(0).getId();
    }

    @Override
    public String getOrganizationIdMappingOrganizationAccountId(String targetOrganizationId) {
        List<OrganizationAccount> organizationAccounts = (List<OrganizationAccount>) ACache.get().getAsObject(User.belongOrganizations);
        if (!TextUtils.isEmpty(targetOrganizationId) && organizationAccounts != null && organizationAccounts.size() != 0) {
            for (OrganizationAccount organizationAccount : organizationAccounts) {
                String organizationId = StringUtils.removeSpecialSuffix(organizationAccount.getOrganizationId());
                String id = organizationAccount.getId();
                if (TextUtils.equals(targetOrganizationId, organizationId)) {
                    return id;
                }
            }
        }
        return null;
    }


    @Override
    public boolean saveUserAboutOrganization(String belongOrganizationAccountId) {
        boolean result = false;
        User.putCurrentOrganizationHasChanged(false);
        ACache.get().remove("chooseIds");
        List<OrganizationAccount> organizationAccounts = (List<OrganizationAccount>) ACache.get().getAsObject(User.belongOrganizations);
        if (!TextUtils.isEmpty(belongOrganizationAccountId) && organizationAccounts != null) {
            for (OrganizationAccount organizationAccount : organizationAccounts) {

                String organizationAccountId = organizationAccount.getId();
                String organizationId = StringUtils.removeSpecialSuffix(organizationAccount.getOrganizationId());


                if (organizationId.equals(Constants.organization_root)) {
                    User.putRootOrganizationAccountId(organizationAccount.getId());
                }

                //判断是否是商家，sb方法
//                if (organizationId.contains("社会组织")) {
//                    String[] split = organizationId.split("/");
//                    if (split.length > 2) {
//                        User.putisMerchant(true);
//                    }
//                }

                //如果其中一个organizationAccountId和account的defacltOrganizationId相同，则这个organizationAccount就是默认的
//                organizationAccountId.equals(belongOrganizationAccountId) && !organizationId.equals(Constants.organization_root)
                if (organizationAccountId.equals(belongOrganizationAccountId) && !organizationId.equals(Constants.organization_root)) {

                    //默认组织id
                    User.putBelongsOrganizationId(organizationId);

                    //默认组织账号id
                    User.putBelongOrganizationAccountId(organizationAccountId);


//                    //当前组织id
//                    if (!User.currentOrganizationHasChanged()){
                    User.putCurrentOrganizationId(organizationId);
//                    }


                    //当前组织账号id
                    User.putCurrentOrganizationAccountId(organizationAccountId);

                    result = true;
                }

            }

        }

        return result;
    }


    private Account getAccount(String username, String password) {
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        String clientId = DeviceUtils.getUniqueIdForThisApp(App.context) + "@" + username;
        account.setClientId(clientId);
        return account;
    }


}
