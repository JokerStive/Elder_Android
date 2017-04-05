package lilun.com.pension.ui.agency.list;

import java.util.ArrayList;
import java.util.List;

import lilun.com.pension.R;
import lilun.com.pension.app.App;
import lilun.com.pension.base.RxPresenter;
import lilun.com.pension.module.bean.ConditionOption;
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
    public void getOrganizationAgency(String organizationId, String filter, int skip) {
        addSubscribe(NetHelper.getApi()
                .getOrganizations(organizationId, StringUtils.addFilterWithDef(filter, skip))
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<Organization>>() {
                    @Override
                    public void _next(List<Organization> organizations) {
                        view.showOrganizations(organizations, skip != 0);
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
                .getProducts(StringUtils.addFilterWithDef(filter, skip))
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<OrganizationProduct>>() {
                    @Override
                    public void _next(List<OrganizationProduct> products) {
                        view.showProducts(products, skip != 0);
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
    public List<List<ConditionOption>> getConditionOptionsList() {
        String where_roomType = "room_type";
        String where_price = "price";
        String where_area = "area";

        String[] rooTypes = App.context.getResources().getStringArray(R.array.product_roomType_option);
        String[] prices = App.context.getResources().getStringArray(R.array.product_price_option);
        String[] areas = App.context.getResources().getStringArray(R.array.product_area_option);

        List<List<ConditionOption>> optionsList = new ArrayList<>();

        //房型可选项
        List<ConditionOption> rooTypeOptions = new ArrayList<>();
        for (String roomType : rooTypes) {
            ConditionOption conditionOption = new ConditionOption(where_roomType, roomType);
            rooTypeOptions.add(conditionOption);
        }
        optionsList.add(rooTypeOptions);

        //价格可选项
        List<ConditionOption> priceOptions = new ArrayList<>();
        for (String price : prices) {
            ConditionOption conditionOption = new ConditionOption(where_price, price);
            priceOptions.add(conditionOption);
        }
        optionsList.add(priceOptions);

        //面积可选项
        List<ConditionOption> areaOptions = new ArrayList<>();
        for (String area : areas) {
            ConditionOption conditionOption = new ConditionOption(where_area, area);
            areaOptions.add(conditionOption);
        }
        optionsList.add(areaOptions);

        return optionsList;
    }
}
