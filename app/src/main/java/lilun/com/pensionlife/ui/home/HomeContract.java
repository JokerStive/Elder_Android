package lilun.com.pensionlife.ui.home;

import java.util.List;

import lilun.com.pensionlife.base.IPresenter;
import lilun.com.pensionlife.base.IView;
import lilun.com.pensionlife.module.bean.Information;
import lilun.com.pensionlife.module.bean.QuestionNaire;

/**
 * 首页契约类
 *
 * @author yk
 *         create at 2017/2/6 16:19
 *         email : yk_developer@163.com
 */
public interface HomeContract {
    interface View extends IView<Presenter> {
        void showInformation(List<Information> informations);
        void changeOrganizationSuccess(int clickId);
//        void showVersionInfo(AppVersion version);
        void saveQuestionNaire(QuestionNaire questionNaire);
        void setVpCurrentPosition();
    }

    interface Presenter extends IPresenter<View> {

        void startTimer();

        void stopTimer();

        void getInformation();

        void needChangeToDefOrganization();

        void changeBelongOrganization(String organizationId, int clickId);

//        void getVersionInfo(String appName, String versionName);

        void getQuestionNaire();
    }
}
