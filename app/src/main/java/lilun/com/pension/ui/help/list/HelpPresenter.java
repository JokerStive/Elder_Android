package lilun.com.pension.ui.help.list;

import java.util.List;

import lilun.com.pension.app.OrganizationChildrenConfig;
import lilun.com.pension.base.RxPresenter;
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
//    @Override
//    public void getClassifies() {
//        String neighbor_help = App.context.getString(R.string.neighbor_help);
//        List<ElderModule> elderModules = (List<ElderModule>) ACache.get().getAsObject("helpClassify");
//        if (elderModules != null && elderModules.size() != 0) {
//            Logger.i("help classify has cache");
//            view.showClassifies(elderModules);
//            return;
//        }
//        String filter = "{\"where\":{\"parent\":\"" + neighbor_help + "\"},\"order\":\"orderId\"}";
//        addSubscribe(NetHelper.getApi()
//                .getElderModules(filter)
//                .compose(RxUtils.handleResult())
//                .compose(RxUtils.applySchedule())
//                .subscribe(new RxSubscriber<List<ElderModule>>() {
//                    @Override
//                    public void _next(List<ElderModule> elderModules) {
//                        ACache.get().put("helpClassify", (Serializable) elderModules);
//                        view.showClassifies(elderModules);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        super.onError(e);
//                        view.completeRefresh();
//                    }
//                }));
//    }


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


}
