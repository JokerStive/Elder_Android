package lilun.com.pensionlife.ui.help.help_detail;

import android.app.Activity;

import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.base.RxPresenter;
import lilun.com.pensionlife.module.bean.AidDetail;
import lilun.com.pensionlife.module.bean.OrganizationAid;
import lilun.com.pensionlife.module.bean.OrganizationReply;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.net.NetHelper;
import lilun.com.pensionlife.net.RxSubscriber;

/**
 * 互助详情P
 *
 * @author yk
 *         create at 2017/2/20 9:35
 *         email : yk_developer@163.com
 */
public class HelpDetailPresenter extends RxPresenter<HelpDetailContract.View> implements HelpDetailContract.Presenter {
    @Override
    public void getHelpDetail(Activity activity, String aidId) {
        addSubscribe(NetHelper.getApi()
                .getAidDetail(aidId, "{\"order\":\"createdAt DESC\"}")
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<AidDetail>(activity) {
                    @Override
                    public void _next(AidDetail detail) {
                        view.showHelpDetail(detail);
                    }


                }));
    }

    @Override
    public void createHelpReply(String aidId, String replyContent) {
        OrganizationReply reply = new OrganizationReply();
        reply.setWhatModel("OrganizationAid");
        reply.setWhatId(aidId);
        reply.setContent(replyContent);
        addSubscribe(NetHelper.getApi()
                .newAidReply(reply.getWhatId(), reply)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<Object>(((BaseFragment) view).getActivity()) {
                    @Override
                    public void _next(Object object) {
                        view.refreshData(0);

                    }

                }));
    }

    @Override
    public void acceptOneReply(String aidId, String replyId) {
        OrganizationAid aid = new OrganizationAid();
        aid.setAnswerId(replyId);
//        aid.setKind(kind);
        addSubscribe(NetHelper.getApi()
                .putAid(aidId, aid)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<Object>(((BaseFragment) view).getActivity()) {
                    @Override
                    public void _next(Object o) {
                        view.acceptSuccess(replyId);
                    }

                }));

    }

    @Override
    public void deleteAid(String aidId) {
        addSubscribe(NetHelper.getApi()
                .deleteAid(aidId)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<Object>(((BaseFragment) view).getActivity()) {
                    @Override
                    public void _next(Object o) {
                        view.refreshData(2);
                    }

                }));
    }

    @Override
    public void deleteAidAnswer(String aidId, String replyId) {
        addSubscribe(NetHelper.getApi()
                .deleteAidAnswer(aidId, replyId)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<Object>(((BaseFragment) view).getActivity()) {
                    @Override
                    public void _next(Object o) {
                        view.refreshData(1);
                    }

                }));
    }
}
