package lilun.com.pension.ui.agency.classify;

import java.util.List;

import lilun.com.pension.R;
import lilun.com.pension.app.App;
import lilun.com.pension.app.Config;
import lilun.com.pension.base.RxPresenter;
import lilun.com.pension.module.bean.ElderModule;
import lilun.com.pension.module.bean.ProductCategory;
import lilun.com.pension.module.utils.RxUtils;
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
        String pension_agency = App.context.getString(R.string.pension_agency);
        String filter = "{\"where\":{\"parent\":\"" + pension_agency + "\"},\"order\":\"orderId\"}";
        addSubscribe(NetHelper.getApi()
                .getElderModules(filter)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<ElderModule>>() {
                    @Override
                    public void _next(List<ElderModule> elderModules) {
                        view.showClassifiesByAgency(elderModules);
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
        String filter = "{\"where\":{\"parentId\":\"" + Config.agency_classify_service_parentId + "\"},\"order\":\"orderId\"}";
        addSubscribe(NetHelper.getApi()
                .getProductCategories(filter)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<ProductCategory>>() {
                    @Override
                    public void _next(List<ProductCategory> productCategories) {
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
