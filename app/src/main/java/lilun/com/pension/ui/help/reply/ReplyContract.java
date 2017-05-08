package lilun.com.pension.ui.help.reply;

import java.util.List;

import lilun.com.pension.base.IPresenter;
import lilun.com.pension.base.IView;
import lilun.com.pension.module.bean.OrganizationReply;

/**
*互助回答C
*@author yk
*create at 2017/2/28 9:33
*email : yk_developer@163.com
*/
public class ReplyContract {
    interface View extends IView<Presenter> {
        void showReplies(List<OrganizationReply> replies,boolean isLoadMore);
        void newReplySuccess(OrganizationReply reply);
        void completeRefresh();
    }

    interface Presenter extends IPresenter<View> {
        void getReplies(String whatModule,String aidId, int skip);
        void newReply(OrganizationReply reply);
        void newAidReply(OrganizationReply reply);
    }
}
