package lilun.com.pension.ui.residential.classify;

import java.io.Serializable;
import java.util.List;

import lilun.com.pension.app.Config;
import lilun.com.pension.base.RxPresenter;
import lilun.com.pension.module.bean.ProductCategory;
import lilun.com.pension.module.utils.ACache;
import lilun.com.pension.module.utils.RxUtils;
import lilun.com.pension.net.NetHelper;
import lilun.com.pension.net.RxSubscriber;

/**
 * 居家服务分类P
 *
 * @author yk
 *         create at 2017/2/15 19:12
 *         email : yk_developer@163.com
 */
public class ResidentialClassifyPresenter extends RxPresenter<ResidentialClassifyContract.View> implements ResidentialClassifyContract.Presenter {


    @Override
    public void getClassifies() {
        List<ProductCategory> productCategories = (List<ProductCategory>) ACache.get().getAsObject("productClassify");
        if (productCategories != null && productCategories.size() != 0) {
            view.showClassifies(productCategories);
            return;
        }
        String filter = "{\"where\":{\"parentId\":\"" + Config.residential_service_classify_parentId + "\"},\"order\":\"orderId\"}";
        addSubscribe(NetHelper.getApi()
                .getProductCategories(filter)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<ProductCategory>>() {
                    @Override
                    public void _next(List<ProductCategory> productCategories) {
                        ACache.get().put("productClassify", (Serializable) productCategories);
                        view.showClassifies(productCategories);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.completeRefresh();
                    }
                }));

    }

//    @Override
//    public void getAboutMe(int skip) {
//        String filter = "{\"where\":{\"creatorId\":\"" + User.getUserId() + "\"},\"include\":\"product\"}";
//        addSubscribe(NetHelper.getApi()
//                .getProductOrders(StringUtils.addFilterWithDef(filter, skip))
//                .compose(RxUtils.handleResult())
//                .compose(RxUtils.applySchedule())
//                .subscribe(new RxSubscriber<List<ProductOrder>>() {
//                    @Override
//                    public void _next(List<ProductOrder> orders) {
//                        List<OrganizationProduct> products = new ArrayList<>();
//                        for(ProductOrder order:orders){
//                            products.add(order.getProduct());
//                        }
//                        view.showAboutMe(products, skip != 0);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        super.onError(e);
//                        view.completeRefresh();
//                    }
//                })
//        );
//    }
}
