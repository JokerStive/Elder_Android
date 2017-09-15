package lilun.com.pensionlife.ui.agency.classify;

import com.orhanobut.logger.Logger;

import java.io.Serializable;
import java.util.List;

import lilun.com.pensionlife.base.RxPresenter;
import lilun.com.pensionlife.module.bean.Organization;
import lilun.com.pensionlife.module.bean.OrganizationProductCategory;
import lilun.com.pensionlife.module.utils.ACache;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.net.NetHelper;
import lilun.com.pensionlife.net.RxSubscriber;

/**
 * 养老机构P
 *
 * @author yk
 *         create at 2017/2/15 19:12
 *         email : yk_developer@163.com
 */
public class AgencyClassifyPresenter extends RxPresenter<AgencyClassifyContract.View> implements AgencyClassifyContract.Presenter {

    @Override
    public void getClassifiesByAgency() {
        List<Organization> organizations = (List<Organization>) ACache.get().getAsObject("agencyOrganizationClassify");
        if (organizations != null && organizations.size() != 0) {
            Logger.i("agency organization classify has cache");
            view.showClassifiesByAgency(organizations);
            return;
        }
        String id = "/社会组织/营利/养老";
//        String filter = "{\"where\":{\"parent\":\"" + pension_agency + "\"},\"order\":\"orderId\"}";

        addSubscribe(NetHelper.getApi()
                .getOrganizations(id, StringUtils.addFilterWithDef("", 0))
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<Organization>>() {
                    @Override
                    public void _next(List<Organization> Organizations) {
                        ACache.get().put("agencyOrganizationClassify", (Serializable) Organizations);
                        view.showClassifiesByAgency(Organizations);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.completeRefresh();
                    }
                }));
    }

    @Override
    public void getClassifiesByService(String categoryId) {
        List<OrganizationProductCategory> productCategories = (List<OrganizationProductCategory>) ACache.get().getAsObject(categoryId+"classify");
        if (productCategories != null && productCategories.size() != 0) {
            Logger.i("agency product classify has cache");
            view.showClassifiesByService(productCategories,categoryId);
            return;
        }

        String filter = "{\"where\":{\"parentId\":\"" + categoryId+ "\"},\"order\":\"orderId\"}";
        addSubscribe(NetHelper.getApi()
                .getOrgProductCategories(filter)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<OrganizationProductCategory>>() {
                    @Override
                    public void _next(List<OrganizationProductCategory> productCategories) {
                        ACache.get().put(categoryId+"classify", (Serializable) productCategories);
                        view.showClassifiesByService(productCategories,categoryId);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.completeRefresh();
                    }
                }));

    }


}
