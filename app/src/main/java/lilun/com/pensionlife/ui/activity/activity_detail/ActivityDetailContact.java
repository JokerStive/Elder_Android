package lilun.com.pensionlife.ui.activity.activity_detail;

import java.util.List;

import lilun.com.pensionlife.base.IPresenter;
import lilun.com.pensionlife.base.IView;
import lilun.com.pensionlife.module.bean.Account;
import lilun.com.pensionlife.module.bean.ActivityDetail;
import lilun.com.pensionlife.module.bean.ActivityEvaluate;
import lilun.com.pensionlife.module.bean.NestedReply;
import lilun.com.pensionlife.module.bean.OrganizationReply;

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

        void showActivityRank(ActivityEvaluate evaluate);

        void successCancelActivity();

    }

    interface Presenter extends IPresenter<View> {
        void getActivityDetail(String id);

        void joinActivity(String activityId);

        void quitActivity(String activityId);


        void replyList(String activityId, String filter, int skip);

        void addAnswer(String activityId, String quesetionId, String answer, int index);

        void getActivityRank(String activityId);

        void postActivityRank(String activityId, int rating);

        void getAvgRank(String activityId);

        void cancelActivity(String activityId);
    }

    /**
     * 参与者列表接口
     */
    interface VPartner extends IView<PPartner> {

        void completeRefresh();

        void showPartners(List<Account> accounts);

        void successDeletePartners(String userId, String userName);


    }

    interface PPartner extends IPresenter<VPartner> {
        void queryPartners(String activityId, String filter, int skip);

        void deletePartners(String activityId, String userId, String userName);


    }
}
