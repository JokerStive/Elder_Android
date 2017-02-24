package lilun.com.pension.ui.health.classify;

import java.util.ArrayList;
import java.util.List;

import lilun.com.pension.R;
import lilun.com.pension.app.App;
import lilun.com.pension.base.RxPresenter;
import lilun.com.pension.module.bean.ElderModule;
import lilun.com.pension.module.bean.HealtheaProduct;
import lilun.com.pension.module.utils.RxUtils;
import lilun.com.pension.net.NetHelper;
import lilun.com.pension.net.RxSubscriber;

/**
 * 健康服务P
 *
 * @author yk
 *         create at 2017/2/13 10:55
 *         email : yk_developer@163.com
 */
public class HealthClassifyPresenter extends RxPresenter<HealthClassifyContract.View> implements HealthClassifyContract.Presenter {


    @Override
    public void getClassifies() {
        String health_service = App.context.getString(R.string.health_service);
        String filter = "{\"where\":{\"parent\":\"" + health_service + "\"},\"order\":\"orderId\"}";
        addSubscribe(NetHelper.getApi()
                .getElderModules(filter)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<ElderModule>>() {
                    @Override
                    public void _next(List<ElderModule> elderModules) {
                        view.showClassifies(elderModules);
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
        view.showAboutMe(getData(), false);
    }


    public List<HealtheaProduct> getData() {
        List<HealtheaProduct> data = new ArrayList<>();
        HealtheaProduct product1 = new HealtheaProduct();
        product1.setName("有高血压怎么办");
        product1.setAddress(" 患了高血压病,一要在全球高血压联盟推荐的6类降压药中选择,且主张联合应用；二要尽可能选用长效降压药,一日1次用药便可维持血压24小时稳定,减少波动；三要与生活方式改善及饮食调理紧密结合,特别注意低盐,低脂,控制体重和体育锻炼. 全球高血压联盟推荐的6类降压药是利尿剂,血管紧张素转化酶抑制剂（ACEI）,血管紧张素Ⅱ受体拮抗剂,－受体阻滞剂,钙拮抗剂和抗胆碱药.缬沙坦是不错的,可以考虑.");
        data.add(product1);
        HealtheaProduct product2 = new HealtheaProduct();
        product2.setName("佳之佳药房");
        product2.setAddress("重庆市南岸区长生桥镇长生路62号");
        data.add(product2);
        HealtheaProduct product3 = new HealtheaProduct();
        product3.setName("昌野药房");
        product3.setAddress("重庆市南岸区通江大道98号");
        data.add(product3);

        return data;
    }
}
