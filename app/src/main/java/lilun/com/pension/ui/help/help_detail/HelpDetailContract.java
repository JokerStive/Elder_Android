package lilun.com.pension.ui.help.help_detail;

import android.app.Activity;

import lilun.com.pension.base.IPresenter;
import lilun.com.pension.base.IView;
import lilun.com.pension.module.bean.OrganizationAid;

/**
*互助详情契约类
*@author yk
*create at 2017/2/20 9:26
*email : yk_developer@163.com
*/
public interface HelpDetailContract {
    interface View extends IView<Presenter> {
        void showHelpDetail(OrganizationAid aid);
        void replySuccess();
    }

    interface Presenter extends IPresenter<View> {
        void getHelpDetail(Activity activity,String aidId);
        void createHelpReply(String aidId,String replyContent);
        void changeHelpStatus(String helpStatus);
        void deleteAid(String aidId);
    }
}
