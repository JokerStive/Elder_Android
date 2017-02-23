package lilun.com.pension.ui.help.help_detail;

import android.app.Activity;

import lilun.com.pension.base.RxPresenter;
import lilun.com.pension.module.bean.OrganizationAid;
import lilun.com.pension.module.utils.RxUtils;
import lilun.com.pension.net.NetHelper;
import lilun.com.pension.net.RxSubscriber;

/**
*互助详情P
*@author yk
*create at 2017/2/20 9:35
*email : yk_developer@163.com
*/
public class HelpDetailPresenter extends RxPresenter<HelpDetailContract.View> implements HelpDetailContract.Presenter{
    @Override
    public void getHelpDetail(Activity activity, String aidId) {
        addSubscribe(NetHelper.getApi()
                .getAidDetail(aidId)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<OrganizationAid>(activity) {
                    @Override
                    public void _next(OrganizationAid aid) {
                        view.showHelpDetail(aid);
                    }

                }));
    }

    @Override
    public void createHelpReply(String replyContent) {

    }

    @Override
    public void changeHelpStatus(String helpStatus) {

    }
}
