package lilun.com.pension.ui.help.reply;

import java.util.List;

import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.base.RxPresenter;
import lilun.com.pension.module.bean.OrganizationReply;
import lilun.com.pension.module.utils.RxUtils;
import lilun.com.pension.module.utils.StringUtils;
import lilun.com.pension.net.NetHelper;
import lilun.com.pension.net.RxSubscriber;

/**
*互助回复P
*@author yk
*create at 2017/2/28 9:35
*email : yk_developer@163.com
*/
public class ReplyPresenter extends RxPresenter<ReplyContract.View> implements ReplyContract.Presenter{
    @Override
    public void getReplies(String whatModule,String aidId, int skip) {
        String filter = "{\"where\":{\"whatModel\":\""+whatModule+"\",\"whatId\":\"" + aidId + "\"}}";
        addSubscribe(NetHelper.getApi()
                .getOrganizationReplies(StringUtils.addFilterWithDef(filter,skip))
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<OrganizationReply>>(((BaseFragment)view).getActivity()) {
                    @Override
                    public void _next(List<OrganizationReply> replies) {
                        view.showReplies(replies,skip!=0);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.completeRefresh();
                    }
                }));
    }

    @Override
    public void newReply(OrganizationReply reply) {
        addSubscribe(NetHelper.getApi()
                .newOrganizationReply(reply)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<OrganizationReply>(((BaseFragment)view).getActivity()) {
                    @Override
                    public void _next(OrganizationReply reply) {
                        view.newReplySuccess(reply);
                    }

                }));
    }



    @Override
    public void newAidReply(OrganizationReply reply) {
        addSubscribe(NetHelper.getApi()
                .newAidReply(reply.getWhatId(),reply)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<Object>(((BaseFragment)view).getActivity()) {
                    @Override
                    public void _next(Object O) {
                        view.newReplySuccess(reply);
                    }

                }));
    }
}
