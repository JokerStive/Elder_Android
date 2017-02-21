package lilun.com.pension.ui.home;

import java.util.List;

import lilun.com.pension.base.IPresenter;
import lilun.com.pension.base.IView;
import lilun.com.pension.module.bean.Announcement;

/**
*首页契约类
*@author yk
*create at 2017/2/6 16:19
*email : yk_developer@163.com
*/
public interface HomeContract {
    interface View extends IView<Presenter> {
        void showAnnouncementFragment(List<Announcement> announcements);
    }

    interface Presenter extends IPresenter<View> {
        void getAnnouncements();

    }
}
