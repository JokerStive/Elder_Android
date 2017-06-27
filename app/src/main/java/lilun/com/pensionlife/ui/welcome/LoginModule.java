package lilun.com.pensionlife.ui.welcome;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.List;

import lilun.com.pensionlife.app.Constants;
import lilun.com.pensionlife.app.User;
import lilun.com.pensionlife.module.bean.Account;
import lilun.com.pensionlife.module.bean.OrganizationAccount;
import lilun.com.pensionlife.module.bean.TokenInfo;
import lilun.com.pensionlife.module.utils.ACache;
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


        //商家跑单人员
        User.putBusinessId(getBusinessId(account.getRoles()));

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
        String result = "";

        //居住地
        String belongToDistrict = User.getBelongToDistrict();
        String belongOrganizationAccountId = User.getBelongOrganizationAccountId();


        //居住地不存在，则是在商家端注册的，需要切换
        if (!TextUtils.isEmpty(belongToDistrict)) {
            //居住地存在
            String organizationIdMappingOrganizationAccountId = getOrganizationIdMappingOrganizationAccountId(belongToDistrict);
            if (TextUtils.equals(belongOrganizationAccountId, organizationIdMappingOrganizationAccountId)) {
                // 和defaultOrganizationId对应，则不需要切换组织
                result = "success";
            } else {
                // 和defaultOrganizationId不对应，则需要切换到居住村对应的组织
                result = organizationIdMappingOrganizationAccountId;
            }
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
                String organizationId = organizationAccount.getOrganizationId();
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
        List<OrganizationAccount> organizationAccounts = (List<OrganizationAccount>) ACache.get().getAsObject(User.belongOrganizations);
        if (!TextUtils.isEmpty(belongOrganizationAccountId) && organizationAccounts != null) {
            for (OrganizationAccount organizationAccount : organizationAccounts) {

                String organizationAccountId = organizationAccount.getId();
                String organizationId = StringUtils.removeSpecialSuffix(organizationAccount.getOrganizationId());


                if (organizationId.equals(Constants.organization_root)) {
                    User.putRootOrganizationAccountId(organizationAccount.getId());
                }

                if (organizationId.contains("社会组织") &&  organizationId.contains("商家")) {
                    User.putIsCustomer(false);

                }

                //如果其中一个organizationAccountId和account的defacltOrganizationId相同，则这个organizationAccount就是默认的
                if (organizationAccountId.equals(belongOrganizationAccountId) && !organizationId.equals(Constants.organization_root)) {

                    //默认组织id
                    User.putBelongsOrganizationId(organizationId);

                    //当前组织id
                    User.putCurrentOrganizationId(organizationId);

                    //默认组织账号id
                    User.putBelongOrganizationAccountId(organizationAccountId);

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
        return account;
    }


}
