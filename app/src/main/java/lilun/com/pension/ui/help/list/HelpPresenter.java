package lilun.com.pension.ui.help.list;

import java.util.ArrayList;
import java.util.List;

import lilun.com.pension.app.OrganizationChildrenConfig;
import lilun.com.pension.base.RxPresenter;
import lilun.com.pension.module.bean.ConditionOption;
import lilun.com.pension.module.bean.OrganizationAid;
import lilun.com.pension.module.utils.RxUtils;
import lilun.com.pension.module.utils.StringUtils;
import lilun.com.pension.net.NetHelper;
import lilun.com.pension.net.RxSubscriber;

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
        addSubscribe(NetHelper.getApi()
                .getOrganizationAids(OrganizationChildrenConfig.aid(), StringUtils.addFilterWithDef(filter, skip))
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
    public List<List<ConditionOption>> getConditionOptionsList() {
        List<List<ConditionOption>> optionsList = new ArrayList<>();

        List<ConditionOption> kindOptions = new ArrayList<>();
        ConditionOption kindOptionNull = new ConditionOption("kind", "", "不限");
        ConditionOption kindOptionAsk = new ConditionOption("kind", "0", "邻居问");
        ConditionOption kindOptionHelp = new ConditionOption("kind", "1", "帮邻居");
        kindOptions.add(kindOptionNull);
        kindOptions.add(kindOptionAsk);
        kindOptions.add(kindOptionHelp);


        List<ConditionOption> statusOptions = new ArrayList<>();
        ConditionOption statusOptionNull = new ConditionOption("status", "", "不限");
        ConditionOption statusOption0 = new ConditionOption("status", "0", "新建");
        ConditionOption statusOption1 = new ConditionOption("status", "1", "已回复");
        ConditionOption statusOption2 = new ConditionOption("status", "2", "已解决");
        ConditionOption statusOption3 = new ConditionOption("status", "3", "已取消");
        statusOptions.add(statusOptionNull);
        statusOptions.add(statusOption0);
        statusOptions.add(statusOption1);
        statusOptions.add(statusOption2);
        statusOptions.add(statusOption3);

        List<ConditionOption> priorityOptions = new ArrayList<>();
        ConditionOption priorityOptionNull = new ConditionOption("priority", "", "不限");
        ConditionOption priorityOption0 = new ConditionOption("priority", "0", "一般");
        ConditionOption priorityOption1 = new ConditionOption("priority", "1", "加急");
        ConditionOption priorityOption2 = new ConditionOption("priority", "2", "紧急");
        ConditionOption priorityOption10 = new ConditionOption("priority", "10", "危急");
        priorityOptions.add(priorityOptionNull);
        priorityOptions.add(priorityOption0);
        priorityOptions.add(priorityOption1);
        priorityOptions.add(priorityOption2);
        priorityOptions.add(priorityOption10);

        optionsList.add(kindOptions);
        optionsList.add(statusOptions);
        optionsList.add(priorityOptions);
        return optionsList;
    }


}
