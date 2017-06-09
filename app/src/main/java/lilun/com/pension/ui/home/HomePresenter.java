package lilun.com.pension.ui.home;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import java.util.List;

import lilun.com.pension.app.Constants;
import lilun.com.pension.app.User;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.base.RxPresenter;
import lilun.com.pension.module.bean.Account;
import lilun.com.pension.module.bean.Information;
import lilun.com.pension.module.bean.OrganizationAccount;
import lilun.com.pension.module.utils.ACache;
import lilun.com.pension.module.utils.PreUtils;
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
        String parentIdFilter;
        parentIdFilter = User.spliceId("/#information/公告");
        String filter = "{\"where\":{\"visible\":0,\"isCat\":false,\"parentId\":{\"inq\":" + parentIdFilter + "}}}";
        addSubscribe(NetHelper.getApi()
                .getInformations(StringUtils.addFilterWithDef(filter, 0))
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<Information>>() {
                    @Override
                    public void _next(List<Information> information) {
                        view.showInformation(information);
//                        view.showInformation(information);
                    }
                }));
    }

    @Override
    public void needChangeToDefOrganization() {
        boolean currentOrganizationHadChanged = PreUtils.getBoolean("currentOrganizationHadChanged", false);
        if (!currentOrganizationHadChanged) {
            if (User.getBelongsOrganizationId().equals(Constants.organization_root)) {
                String belongToDistrict = User.getBelongToDistrict();
                List<OrganizationAccount> belongOrganizationAccount = (List<OrganizationAccount>) ACache.get().getAsObject(User.belongOrganizations);
                if (!TextUtils.isEmpty(belongToDistrict) && belongOrganizationAccount != null && belongOrganizationAccount.size() > 0) {
                    for (OrganizationAccount organizationAccount : belongOrganizationAccount) {
                        String organizationID = StringUtils.removeSpecialSuffix(organizationAccount.getOrganizationId());
                        if (TextUtils.equals(organizationID, belongToDistrict)) {
                            String needChangeOrganizationAccountId = organizationAccount.getId();
                            Logger.d("异常，需要切换成自己本来的所属组织---" + belongToDistrict);
//                            Logger.d("当前正确的组织账号iD = " + User.getCurrentOrganizationAccountId());
                            changeBelongOrganization(needChangeOrganizationAccountId, -1);
                        }
                    }
                }
            }

//            if (!TextUtils.isEmpty(belongOrganizationAccountId)  && && !belongOrganizationAccountId.equals(belongToDistrict)){
//                Logger.d("异常，需要切换成自己本来的所属组织");
//                Logger.d("当前正确的组织账号iD = " + User.getCurrentOrganizationAccountId());
//                changeBelongOrganization(belongToDistrict, -1);
//            }

        } else {

        }
    }

    @Override
    public void changeBelongOrganization(String organizationId, int clickId) {
        Account account = new Account();
        account.setDefaultOrganizationId(organizationId);
        addSubscribe(NetHelper.getApi()
                .putAccount(User.getUserId(), account)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<Object>(((BaseFragment) view).getActivity()) {
                    @Override
                    public void _next(Object o) {
//                        User.putBelongsOrganizationId(User.getCurrentOrganizationId());
                        view.changeOrganizationSuccess(clickId);
                    }
                }));
    }
}
