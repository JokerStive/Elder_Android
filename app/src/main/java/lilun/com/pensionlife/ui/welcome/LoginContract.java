package lilun.com.pensionlife.ui.welcome;

import java.util.List;

import lilun.com.pensionlife.base.IPresenter;
import lilun.com.pensionlife.base.IView;
import lilun.com.pensionlife.module.bean.Account;
import lilun.com.pensionlife.module.bean.AppVersion;
import lilun.com.pensionlife.module.bean.OrganizationAccount;
import lilun.com.pensionlife.module.bean.TokenInfo;
import rx.Observable;

/**
 * 登录契约接口
 *
 * @author yk
 *         create at 2017/1/23 13:57
 *         email : yk_developer@163.com
 */
public interface LoginContract {
    interface View extends IView<Presenter> {

        void loginSuccess();

        void showVersionInfo(AppVersion version);

        void activateAccount(String mobile);
    }

    interface Presenter extends IPresenter<View> {

        void login(String username, String password);

        boolean checkAccountData(String username, String password);

        void getVersionInfo(String appName, String versionName);

    }

    interface Module {
        Observable<TokenInfo> login(String username, String password);

        Observable<Account> getAccountInfo(TokenInfo accountId, String username, String password);

        Observable<List<OrganizationAccount>> getBelongOrganizations(Account account);

        void putToken(String token);

        void putAccountInfo(Account account);

        void putBelongOrganizations(List<OrganizationAccount> organizations);

        boolean saveUserAboutOrganization(String belongOrganizationAccountId);


        String isNeedChangeDefaultOrganizationId();

        String getLongestOrganizationAccountId();

        String getOrganizationIdMappingOrganizationAccountId(String organizationId);

    }

}
