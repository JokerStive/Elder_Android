package lilun.com.pension.ui.residential.classify;

import java.util.List;

import lilun.com.pension.app.Config;
import lilun.com.pension.app.OrganizationChildrenConfig;
import lilun.com.pension.base.RxPresenter;
import lilun.com.pension.module.bean.OrganizationProduct;
import lilun.com.pension.module.bean.ProductCategory;
import lilun.com.pension.module.utils.RxUtils;
import lilun.com.pension.module.utils.StringUtils;
import lilun.com.pension.net.NetHelper;
import lilun.com.pension.net.RxSubscriber;

/**
 * 居家服务P
 *
 * @author yk
 *         create at 2017/2/15 19:12
 *         email : yk_developer@163.com
 */
public class ResidentialClassifyPresenter extends RxPresenter<ResidentialClassifyContract.View> implements ResidentialClassifyContract.Presenter {



    @Override
    public void getClassifies() {
        String filter = "{\"where\":{\"parentId\":\"" + Config.residential_service_classify_parentId + "\"},\"order\":\"orderId\"}";
        addSubscribe(NetHelper.getApi()
                .getProductCategories(filter)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<ProductCategory>>() {
                    @Override
                    public void _next(List<ProductCategory> productCategories) {
                        view.showClassifies(productCategories);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.completeRefresh();
                    }
                }));

    }

    @Override
    public void getAboutMe(String filter, int skip) {
        addSubscribe(NetHelper.getApi()
                .getProducts(OrganizationChildrenConfig.product(), StringUtils.addFilterWithDef(filter,skip))
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<OrganizationProduct>>() {
                    @Override
                    public void _next(List<OrganizationProduct> products) {
                        view.showAboutMe(products, skip!=0);
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
