package lilun.com.pensionlife.ui.activity.activity_question;

import java.util.List;

import lilun.com.pensionlife.base.RxPresenter;
import lilun.com.pensionlife.module.bean.NestedReply;
import lilun.com.pensionlife.module.bean.OrganizationReply;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.net.NetHelper;
import lilun.com.pensionlife.net.RxSubscriber;

/**
 * Created by zp on 2017/4/19.
 */

public class ActivityQuestionListPresenter extends RxPresenter<ActivityQuestionListContract.View>
        implements ActivityQuestionListContract.Presenter {
    @Override
    public void addAnswer(String activityId, String quesetionId, String answer, int index) {
        addSubscribe(NetHelper.getApi()
                .addAnswer(activityId, quesetionId, new OrganizationReply(answer))
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<OrganizationReply>() {
                    @Override
                    public void _next(OrganizationReply organizationReply) {
                        view.showAnswer(organizationReply,index);
                    }
                }));
    }

    @Override
    public void addQuestion(String activityId, String quesetion) {
        addSubscribe(NetHelper.getApi()
                .addQuestion(activityId, new OrganizationReply(quesetion))
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<OrganizationReply>() {
                    @Override
                    public void _next(OrganizationReply organizationReply) {
                        view.showQuestion(organizationReply);
                    }
                }));

    }

    @Override
    public void replyList(String activityId, String filter, int skip) {
        addSubscribe(NetHelper.getApi()
                .replyList(activityId, StringUtils.addFilterWithDef(filter, skip))
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<NestedReply>>() {
                    @Override
                    public void _next(List<NestedReply> nestedReplies) {
                        view.completeRefresh();
                        view.showReplyList(nestedReplies);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.completeRefresh();
                    }
                }));
    }

}
