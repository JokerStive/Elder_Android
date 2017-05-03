package lilun.com.pension.ui.agency.classify;

import com.orhanobut.logger.Logger;

import java.io.Serializable;
import java.util.List;

import lilun.com.pension.app.Config;
import lilun.com.pension.base.RxPresenter;
import lilun.com.pension.module.bean.Organization;
import lilun.com.pension.module.bean.ProductCategory;
import lilun.com.pension.module.utils.ACache;
import lilun.com.pension.module.utils.RxUtils;
import lilun.com.pension.module.utils.StringUtils;
import lilun.com.pension.net.NetHelper;
import lilun.com.pension.net.RxSubscriber;

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
                .getOrganizations(id, StringUtils.addFilterWithDef("",0))
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
    public void getClassifiesByService() {
        List<ProductCategory> productCategories = (List<ProductCategory>) ACache.get().getAsObject("agencyProductClassify");
        if (productCategories != null && productCategories.size() != 0) {
            Logger.i("agency product classify has cache");
            view.showClassifiesByService(productCategories);
            return;
        }

        String filter = "{\"where\":{\"parentId\":\"" + Config.agency_product_categoryId + "\"},\"order\":\"orderId\"}";
        addSubscribe(NetHelper.getApi()
                .getProductCategories(filter)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<ProductCategory>>() {
                    @Override
                    public void _next(List<ProductCategory> productCategories) {
                        ACache.get().put("agencyProductClassify", (Serializable) productCategories);
                        view.showClassifiesByService(productCategories);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.completeRefresh();
                    }
                }));

    }


}
