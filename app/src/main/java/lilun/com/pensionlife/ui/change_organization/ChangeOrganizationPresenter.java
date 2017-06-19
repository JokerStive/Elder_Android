package lilun.com.pensionlife.ui.change_organization;

import com.orhanobut.logger.Logger;

import java.util.List;

import lilun.com.pensionlife.app.Constants;
import lilun.com.pensionlife.app.User;
import lilun.com.pensionlife.base.BaseActivity;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.base.RxPresenter;
import lilun.com.pensionlife.module.bean.Account;
import lilun.com.pensionlife.module.bean.Organization;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.net.NetHelper;
import lilun.com.pensionlife.net.RxSubscriber;

/**
 * 切换组织P
 *
 * @author yk
 *         create at 2017/4/11 10:34
 *         email : yk_developer@163.com
 */
public class ChangeOrganizationPresenter extends RxPresenter<ChangeOrganizationContract.View> implements ChangeOrganizationContract.Presenter {
    @Override
    public void getOrganizations(String id, String filter, int skip,boolean isAddCrumb) {
        addSubscribe(NetHelper.getApi()
                .getOrganizations(id, StringUtils.addFilterWithDef(filter, skip))
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<Organization>>() {
                    @Override
                    public void _next(List<Organization> organizations) {
                        view.showOrganizations(organizations, skip != 0,isAddCrumb);
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
        BaseActivity activity = null;
        if (view instanceof BaseActivity) {
            activity = (BaseActivity) view;
        } else if (view instanceof BaseFragment) {
            activity = (BaseActivity) ((BaseFragment) view).getActivity();
        }
        Account account = new Account();
        account.setDefaultOrganizationId(organizationId);
        addSubscribe(NetHelper.getApi()
                .putAccount(User.getUserId(), account)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<Object>(activity) {
                    @Override
                    public void _next(Object o) {
                        if (organizationId.equals(User.getRootOrganizationAccountId())) {
                            view.changedRoot();
                            User.putCurrentOrganizationId(Constants.organization_root);
                            Logger.d("切换到地球村成功" + User.getCurrentOrganizationId());
                        } else{
                            User.putCurrentOrganizationId(User.getBelongsOrganizationId());
                            Logger.d("切换回默认所属组织成功" + User.getCurrentOrganizationId());
                            view.changedBelong();
                        }
                    }
                }));
    }
}
