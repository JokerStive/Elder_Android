package lilun.com.pension.ui.home;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import java.util.List;

import lilun.com.pension.app.OrganizationChildrenConfig;
import lilun.com.pension.app.User;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.base.RxPresenter;
import lilun.com.pension.module.bean.Account;
import lilun.com.pension.module.bean.Information;
import lilun.com.pension.module.bean.OrganizationAccount;
import lilun.com.pension.module.utils.RxUtils;
import lilun.com.pension.module.utils.StringUtils;
import lilun.com.pension.net.NetHelper;
import lilun.com.pension.net.RxSubscriber;

/**
 * 首页P
 *
 * @author yk
 *         create at 2017/2/6 16:23
 *         email : yk_developer@163.com
 */
public class HomePresenter extends RxPresenter<HomeContract.View> implements HomeContract.Presenter {


    @Override
    public void getInformation() {
        String filter = "{\"where\":{\"organizationId\":\"" + OrganizationChildrenConfig.information() + "\",\"isCat\":\"false\",\"parentId\":{\"like\":\"/#information/公告\"}}}";
        addSubscribe(NetHelper.getApi()
                .getInformations(StringUtils.addFilterWithDef(filter, 0))
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<Information>>() {
                    @Override
                    public void _next(List<Information> information) {
                        view.showInformation(information);
                    }
                }));
    }

    @Override
    public boolean needChangeDefOrganization(String changeOrganizationId) {
        List<OrganizationAccount> belongOrganization = User.getBelongOrganization();
        if (belongOrganization != null) {
            for (OrganizationAccount oa : belongOrganization) {
                String organizationId = oa.getOrganizationId();
                if (TextUtils.equals(organizationId, changeOrganizationId)) {
                    Logger.d("需要切换默认所属组织" + organizationId);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void changeDefBelongOrganization(String organizationId, int clickId) {
        Account account = new Account();
        account.setDefaultOrganizationId(organizationId);
        addSubscribe(NetHelper.getApi()
                .putAccount(User.getUserId(), account)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<Object>(((BaseFragment) view).getActivity()) {
                    @Override
                    public void _next(Object o) {
                        User.puttCurrentOrganizationId(organizationId);
                        view.changeOrganizationSuccess(clickId);
                    }
                }));
    }
}
