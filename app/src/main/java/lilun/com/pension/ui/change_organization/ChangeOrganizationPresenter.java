package lilun.com.pension.ui.change_organization;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import java.util.List;

import lilun.com.pension.app.Constants;
import lilun.com.pension.app.User;
import lilun.com.pension.base.BaseActivity;
import lilun.com.pension.base.RxPresenter;
import lilun.com.pension.module.bean.Account;
import lilun.com.pension.module.bean.Organization;
import lilun.com.pension.module.utils.RxUtils;
import lilun.com.pension.module.utils.StringUtils;
import lilun.com.pension.net.NetHelper;
import lilun.com.pension.net.RxSubscriber;

/**
 * 切换组织P
 *
 * @author yk
 *         create at 2017/4/11 10:34
 *         email : yk_developer@163.com
 */
public class ChangeOrganizationPresenter extends RxPresenter<ChangeOrganizationContract.View> implements ChangeOrganizationContract.Presenter {
    @Override
    public void getOrganizations(String id, String filter, int skip) {
        addSubscribe(NetHelper.getApi()
                .getOrganizations(id, StringUtils.addFilterWithDef(filter, skip))
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<Organization>>() {
                    @Override
                    public void _next(List<Organization> organizations) {
                        view.showOrganizations(organizations, skip != 0);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.completeRefresh();
                    }
                })
        );
    }

    @Override
    public void changeDefBelongOrganization(String organizationId) {
        Account account = new Account();
        account.setDefaultOrganizationId(organizationId);
        addSubscribe(NetHelper.getApi()
                .putAccount(User.getUserId(), account)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<Object>((BaseActivity)view) {
                    @Override
                    public void _next(Object o) {
//                        User.puttCurrentOrganizationId(organizationId);
                        if (organizationId.equals(User.getRootOrganizationAccountId())) {
                            Logger.d("切换到地球村成功" + organizationId);
                            view.changedRoot();
                            User.putBelongsOrganizationId(Constants.organization_root);
                        } else if (TextUtils.equals(organizationId, User.getBelongOrganizationAccountId())) {
                            Logger.d("切换回默认所属组织成功" + organizationId);
                            User.putBelongsOrganizationId(User.getCurrentOrganizationId());

                            view.changedBelong();
                        }
                    }
                }));
    }
}