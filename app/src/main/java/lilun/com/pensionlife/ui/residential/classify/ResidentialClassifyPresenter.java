package lilun.com.pensionlife.ui.residential.classify;

import java.io.Serializable;
import java.util.List;

import lilun.com.pensionlife.app.Config;
import lilun.com.pensionlife.base.RxPresenter;
import lilun.com.pensionlife.module.bean.OrganizationProductCategory;
import lilun.com.pensionlife.module.utils.ACache;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.net.NetHelper;
import lilun.com.pensionlife.net.RxSubscriber;

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
        List<OrganizationProductCategory> productCategories = (List<OrganizationProductCategory>) ACache.get().getAsObject("familyProductClassify");
        if (productCategories != null && productCategories.size() != 0) {
            view.showClassifies(productCategories);
            return;
        }
        String filter = "{\"where\":{\"parentId\":\"" + Config.residential_product_categoryId + "\"},\"order\":\"orderId\"}";
        addSubscribe(NetHelper.getApi()
                .getOrgProductCategories(filter)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<OrganizationProductCategory>>() {
                    @Override
                    public void _next(List<OrganizationProductCategory> productCategories) {
                        ACache.get().put("familyProductClassify", (Serializable) productCategories);
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
