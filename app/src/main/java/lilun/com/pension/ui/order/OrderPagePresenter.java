package lilun.com.pension.ui.order;

import java.util.List;

import lilun.com.pension.app.User;
import lilun.com.pension.base.RxPresenter;
import lilun.com.pension.module.bean.ProductOrder;
import lilun.com.pension.module.utils.RxUtils;
import lilun.com.pension.module.utils.StringUtils;
import lilun.com.pension.net.NetHelper;
import lilun.com.pension.net.RxSubscriber;

/**
 * 我的订单P
 *
 * @author yk
 *         create at 2017/3/3 11:32
 *         email : yk_developer@163.com
 */
public class OrderPagePresenter extends RxPresenter<OrderPageContract.View> implements OrderPageContract.Presenter {
    @Override
    public void getMyOrders(String status, int skip) {
        String filter;
        if (User.isCustomer()) {
            filter = "{\"include\":[\"product\",\"assignee\"],\"where\":{\"creatorId\":\"" + User.getUserId() + "\",\"status\":\"" + status + "\"}}";
        } else {
            filter = "{\"include\":[\"product\",\"assignee\"],\"where\":{\"assigneeId\":\"" + User.getUserId() + "\",\"status\":\"" + status + "\"}}";
        }
        addSubscribe(NetHelper.getApi()
                .getOrders(StringUtils.addFilterWithDef(filter, skip))
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<ProductOrder>>() {
                    @Override
                    public void _next(List<ProductOrder> orders) {
                        view.showMyOrders(orders, skip != 0);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.completeRefresh();
                    }
                }));
    }
}

