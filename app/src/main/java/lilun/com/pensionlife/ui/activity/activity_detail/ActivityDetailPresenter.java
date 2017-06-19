package lilun.com.pensionlife.ui.activity.activity_detail;

import java.util.List;

import lilun.com.pensionlife.base.RxPresenter;
import lilun.com.pensionlife.module.bean.ActivityDetail;
import lilun.com.pensionlife.module.bean.ActivityEvaluate;
import lilun.com.pensionlife.module.bean.NestedReply;
import lilun.com.pensionlife.module.bean.OrganizationReply;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.net.NetHelper;
import lilun.com.pensionlife.net.RxSubscriber;

/**
 * Created by zp on 2017/3/6.
 */

public class ActivityDetailPresenter extends RxPresenter<ActivityDetailContact.View> implements ActivityDetailContact.Presenter {
    @Override
    public void getActivityDetail(String id) {
        addSubscribe(NetHelper.getApi()
                .getOrganizationActivitiesDetail(id, "{}")
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<ActivityDetail>() {
                    @Override
                    public void _next(ActivityDetail activityDetail) {
                        view.showActivityDetail(activityDetail);
                    }
                }));
    }

    @Override
    public void joinActivity(String activityId) {
        addSubscribe(NetHelper.getApi()
                .joinActivity(activityId)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<Object>() {
                    @Override
                    public void _next(Object object) {
                        view.sucJoinActivity();
                    }
                }));
    }

    @Override
    public void quitActivity(String activityId) {
        addSubscribe(NetHelper.getApi()
                .quitActivity(activityId)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<Object>() {
                    @Override
                    public void _next(Object object) {
                        view.sucQuitActivity();
                    }
                }));
    }

    @Override
    public void cancelActivity(String activityId) {
        addSubscribe(NetHelper.getApi()
                .cancelActivity(activityId)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<Object>() {
                    @Override
                    public void _next(Object o) {
                        view.successCancelActivity();
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

    @Override
    public void addAnswer(String activityId, String quesetionId, String answer, int index) {
        addSubscribe(NetHelper.getApi()
                .addAnswer(activityId, quesetionId, new OrganizationReply(answer))
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<OrganizationReply>() {
                    @Override
                    public void _next(OrganizationReply organizationReply) {
                        view.showAnswer(organizationReply, index);
                    }
                }));
    }

    @Override
    public void getActivityRank(String activityId) {
        addSubscribe(NetHelper.getApi()
                .getActivityRank(activityId)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<ActivityEvaluate>() {
                    @Override
                    public void _next(ActivityEvaluate evaluate) {
                        view.showActivityRank(evaluate);
                    }
                }));
    }

    @Override
    public void postActivityRank(String activityId, int rating) {
        addSubscribe(NetHelper.getApi()
                .postActivityRank(activityId, rating + "")
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<ActivityEvaluate>() {
                    @Override
                    public void _next(ActivityEvaluate evaluate) {
                        view.showActivityRank(evaluate);
                    }
                }));
    }

    @Override
    public void getAvgRank(String activityId) {

    }
}
