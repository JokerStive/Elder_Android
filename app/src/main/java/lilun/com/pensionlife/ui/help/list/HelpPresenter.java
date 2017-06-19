package lilun.com.pensionlife.ui.help.list;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import lilun.com.pensionlife.base.RxPresenter;
import lilun.com.pensionlife.module.bean.ConditionOption;
import lilun.com.pensionlife.module.bean.Option;
import lilun.com.pensionlife.module.bean.OrganizationAid;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.net.NetHelper;
import lilun.com.pensionlife.net.RxSubscriber;

/**
 * 邻居互助P
 *
 * @author yk
 *         create at 2017/2/13 10:55
 *         email : yk_developer@163.com
 */
public class HelpPresenter extends RxPresenter<HelpContract.View> implements HelpContract.Presenter {

    @Override
    public void getAboutMe(String filter, int skip) {
        Logger.d("求助filter = "+filter);
        addSubscribe(NetHelper.getApi()
                .getMyAids(StringUtils.addFilterWithDef(filter, skip))
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<OrganizationAid>>() {
                    @Override
                    public void _next(List<OrganizationAid> organizationAids) {
                        view.showAboutMe(organizationAids, skip != 0);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.completeRefresh();
                    }
                }));
    }


    @Override
    public void getHelps(String filter, int skip) {
        Logger.d("求助filter = "+filter);
        addSubscribe(NetHelper.getApi()
                .getOrganizationAids(StringUtils.addFilterWithDef(filter, skip))
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<OrganizationAid>>() {
                    @Override
                    public void _next(List<OrganizationAid> organizationAids) {
                        view.showAboutMe(organizationAids, skip != 0);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.completeRefresh();
                    }
                }));
    }

    @Override
    public List<ConditionOption> getConditionOptionsList(String kind,String status,String priority) {
        List<ConditionOption> conditionOptionList = new ArrayList<>();

        List<Option> kindOptions = new ArrayList<>();
        Option optionNull = new Option("", "不限");
        Option optionAsk = new Option("0", "邻居问");
        Option optionHelp = new Option("1", "帮邻居");
        kindOptions.add(optionNull);
        kindOptions.add(optionAsk);
        kindOptions.add(optionHelp);
        ConditionOption conditionOptionKind = new ConditionOption(kind, "类型", kindOptions);


        List<Option> statusOptions = new ArrayList<>();
        Option statusOptionNull = new Option("", "不限");
        Option statusOption0 = new Option("0", "新建");
        Option statusOption1 = new Option("1", "已回复");
        Option statusOption2 = new Option("2", "已解决");
        statusOptions.add(statusOptionNull);
        statusOptions.add(statusOption0);
        statusOptions.add(statusOption1);
        statusOptions.add(statusOption2);
        ConditionOption conditionOptionStatus = new ConditionOption(status, "状态", statusOptions);


        List<Option> priorityOptions = new ArrayList<>();
        Option priorityOptionNull = new Option("", "不限");
        Option priorityOption0 = new Option("0", "一般");
        Option priorityOption1 = new Option("1", "加急");
        Option priorityOption2 = new Option( "2", "紧急");
        Option priorityOption10 = new Option( "10", "危急");
        priorityOptions.add(priorityOptionNull);
        priorityOptions.add(priorityOption0);
        priorityOptions.add(priorityOption1);
        priorityOptions.add(priorityOption2);
        priorityOptions.add(priorityOption10);
        ConditionOption conditionOptionPriority= new ConditionOption(priority, "优先级", priorityOptions);

        conditionOptionList.add(conditionOptionKind);
        conditionOptionList.add(conditionOptionStatus);
        conditionOptionList.add(conditionOptionPriority);
        return conditionOptionList;
    }


}
