package lilun.com.pension.ui.welcome;

import lilun.com.pension.base.IPresenter;
import lilun.com.pension.base.IView;
import lilun.com.pension.module.bean.Account;
import lilun.com.pension.module.bean.TokenInfo;
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
    }

    interface Presenter extends IPresenter<View> {

        void login(String username, String password);

        boolean checkAccountData(String username, String password);

    }

    interface Module {
        Observable<TokenInfo> login(String username, String password);

        Observable<Account> getAccountInfo(TokenInfo accountId);

        void putToken(String token);

        void putAccountInfo(Account account);

    }

}
