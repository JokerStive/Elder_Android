package lilun.com.pension.ui.activity.activity_detail;

import java.util.List;

import lilun.com.pension.base.IPresenter;
import lilun.com.pension.base.IView;
import lilun.com.pension.module.bean.Account;
import lilun.com.pension.module.bean.ActivityDetail;
import lilun.com.pension.module.bean.ActivityEvaluate;
import lilun.com.pension.module.bean.NestedReply;
import lilun.com.pension.module.bean.OrganizationReply;

/**
 * Created by zp on 2017/3/6.
 */

public interface ActivityDetailContact {
    interface View extends IView<Presenter> {
        void showActivityDetail(ActivityDetail activityDetail);

        void completeRefresh();

        void refreshActivityData();

        void sucJoinActivity();

        void sucQuitActivity();

        void showReplyList(List<NestedReply> nestedReplies);

        void showAnswer(OrganizationReply organizationReply, int position);

        void showAvgRank();

        void showActivityRank(ActivityEvaluate evaluate);

        void successActivityRank();

    }

    interface Presenter extends IPresenter<View> {
        void getActivityDetail(String id);

        void joinActivity(String activityId);

        void quitActivity(String activityId);

        void cancelActivity(String activityId);

        void replyList(String activityId, String filter, int skip);

        void addAnswer(String activityId, String quesetionId, String answer, int index);

        void getActivityRank(String activityId);

        void postActivityRank(String activityId, int rating);

        void getAvgRank(String activityId);
    }

    /**
     * 参与者列表接口
     */
    interface VPartner extends IView<PPartner> {

        void completeRefresh();

        void showPartners(List<Account> accounts);

        void successDeletePartners();

        void successCancelActivity();
    }

    interface PPartner extends IPresenter<VPartner> {
        void queryPartners(String activityId, String filter, int skip);

        void deletePartners(String activityId, String userId);

        void cancelActivity(String activityId);
    }
}
