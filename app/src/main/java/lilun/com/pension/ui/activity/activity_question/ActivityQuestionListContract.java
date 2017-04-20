package lilun.com.pension.ui.activity.activity_question;

import java.util.List;

import lilun.com.pension.base.IPresenter;
import lilun.com.pension.base.IView;
import lilun.com.pension.module.bean.NestedReply;
import lilun.com.pension.module.bean.OrganizationReply;

/**
 * Created by zp on 2017/4/19.
 */

public class ActivityQuestionListContract {
    interface View extends IView<Presenter> {
        void completeRefresh();

        void showAnswer(OrganizationReply organizationReply, int position);

        void showQuestion(OrganizationReply organizationReply);

        void showReplyList(List<NestedReply> nestedReplies);
    }

    interface Presenter extends IPresenter<View> {
        void addAnswer(String activityId, String quesetionId, String answer, int index);

        void addQuestion(String activityId, String quesetion);

        void replyList(String activityId, String filter, int skip);
    }
}
