package lilun.com.pensionlife.ui.education.colleage_list;

import java.util.List;

import lilun.com.pensionlife.base.RxPresenter;
import lilun.com.pensionlife.module.bean.ConditionOption;
import lilun.com.pensionlife.module.bean.OrganizationEdu;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.net.NetHelper;
import lilun.com.pensionlife.net.RxSubscriber;

/**
 * 老年教育P
 *
 * @author yk
 *         create at 2017/2/13 10:55
 *         email : yk_developer@163.com
 */
public class EducationListPresenter extends RxPresenter<EducationListContract.View> implements EducationListContract.Presenter {

    @Override
    public void getOrganizationEdu(String filter, int skip) {
        addSubscribe(NetHelper.getApi()
                .getOrganizationsEdus(StringUtils.addFilterWithDef(filter, skip))
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<OrganizationEdu>>() {
                    @Override
                    public void _next(List<OrganizationEdu> educations) {
                        view.completeRefresh();
                        if (skip == 0)
                            view.showOrganizationEdu(educations, false);
                        else
                            view.showOrganizationEdu(educations, true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.completeRefresh();
                    }
                }));
    }

    @Override
    public List<List<ConditionOption>> getConditionOptionsList() {

//        String where_price = "price";
//        String where_area = "level";
//
//
//        String[] prices = App.context.getResources().getStringArray(R.array.product_price_option);
//        String[] level = App.context.getResources().getStringArray(R.array.education_level);
//
//        List<List<ConditionOption>> optionsList = new ArrayList<>();
//
//
//        //价格可选项
//        List<ConditionOption> priceOptions = new ArrayList<>();
//        for (String price : prices) {
//            ConditionOption conditionOption = new ConditionOption(where_price, price);
//            priceOptions.add(conditionOption);
//        }
//        optionsList.add(priceOptions);
//
//
//        List<ConditionOption> areaOptions = new ArrayList<>();
//        for (String area : level) {
//            ConditionOption conditionOption = new ConditionOption(where_area, area);
//            areaOptions.add(conditionOption);
//        }
//        optionsList.add(areaOptions);

        return null;
    }

}
