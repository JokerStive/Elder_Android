package lilun.com.pensionlife.ui.help.help_detail;

import android.app.Activity;

import lilun.com.pensionlife.base.IPresenter;
import lilun.com.pensionlife.base.IView;
import lilun.com.pensionlife.module.bean.AidDetail;

/**
*互助详情契约类
*@author yk
*create at 2017/2/20 9:26
*email : yk_developer@163.com
*/
public interface HelpDetailContract {
    interface View extends IView<Presenter> {
        void showHelpDetail(AidDetail detail);
        void acceptSuccess(String replyId);
        void refreshData(int operate);
    }

    interface Presenter extends IPresenter<View> {
        void getHelpDetail(Activity activity,String aidId);
        void createHelpReply(String aidId,String replyContent);
        void acceptOneReply(String aidId,String replyId);
        void deleteAid(String aidId);
        void deleteAidAnswer(String aidId,String replyId);
    }
}
