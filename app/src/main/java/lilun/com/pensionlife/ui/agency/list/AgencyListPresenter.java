package lilun.com.pensionlife.ui.agency.list;

import java.util.ArrayList;
import java.util.List;

import lilun.com.pensionlife.base.RxPresenter;
import lilun.com.pensionlife.module.bean.ConditionOption;
import lilun.com.pensionlife.module.bean.Option;
import lilun.com.pensionlife.module.bean.OrganizationProduct;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.net.NetHelper;
import lilun.com.pensionlife.net.RxSubscriber;

/**
 * 养老机构列表P
 *
 * @author yk
 *         create at 2017/2/21 11:46
 *         email : yk_developer@163.com
 */
public class AgencyListPresenter extends RxPresenter<AgencyListContract.View> implements AgencyListContract.Presenter {

    @Override
    public void getProducts(String filter, int skip) {
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
    public List<ConditionOption> getConditionOptionsList(String whereKey) {
        List<ConditionOption> conditionOptionList = new ArrayList<>();

        List<Option> kindOptions = new ArrayList<>();
        Option option0 = new Option(null, "智能排序");
        Option option1 = new Option("1", "一星");
        Option option2 = new Option("2", "二星");
        Option option3 = new Option("3", "三星");
        Option option4 = new Option("4", "四星");
        Option option5 = new Option("5", "五星");
        kindOptions.add(option0);
        kindOptions.add(option1);
        kindOptions.add(option2);
        kindOptions.add(option3);
        kindOptions.add(option4);
        kindOptions.add(option5);
        ConditionOption conditionOptionKind = new ConditionOption(whereKey, "智能排序", kindOptions);

        conditionOptionList.add(conditionOptionKind);
        return conditionOptionList;
    }
}
