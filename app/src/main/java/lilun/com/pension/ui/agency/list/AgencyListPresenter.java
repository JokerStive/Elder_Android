package lilun.com.pension.ui.agency.list;

import java.util.List;

import lilun.com.pension.app.OrganizationChildrenConfig;
import lilun.com.pension.base.RxPresenter;
import lilun.com.pension.module.bean.Organization;
import lilun.com.pension.module.bean.OrganizationProduct;
import lilun.com.pension.module.utils.RxUtils;
import lilun.com.pension.module.utils.StringUtils;
import lilun.com.pension.net.NetHelper;
import lilun.com.pension.net.RxSubscriber;

/**
 * 养老机构列表P
 *
 * @author yk
 *         create at 2017/2/21 11:46
 *         email : yk_developer@163.com
 */
public class AgencyListPresenter extends RxPresenter<AgencyListContract.View> implements AgencyListContract.Presenter {
    @Override
    public void getOrganizationAgency(String organizationId,String filter, int skip) {
        addSubscribe(NetHelper.getApi()
                .getOrganizations(organizationId, StringUtils.addFilterWithDef(filter,skip))
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<Organization>>() {
                    @Override
                    public void _next(List<Organization> organizations) {
                        view.showOrganizations(organizations, skip!=0);
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
    public void getProductAgency(String filter, int skip) {
        addSubscribe(NetHelper.getApi()
                .getProducts(OrganizationChildrenConfig.product(), StringUtils.addFilterWithDef(filter,skip))
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<OrganizationProduct>>() {
                    @Override
                    public void _next(List<OrganizationProduct> products) {
                        view.showProducts(products, skip!=0);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.completeRefresh();
                    }
                })
        );

    }
}
