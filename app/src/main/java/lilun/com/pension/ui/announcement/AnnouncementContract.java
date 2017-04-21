package lilun.com.pension.ui.announcement;

import java.util.List;

import lilun.com.pension.base.IPresenter;
import lilun.com.pension.base.IView;
import lilun.com.pension.module.bean.Information;

/**
 * Created by yk on 2017/1/6.
 * 广告栏
 */
public interface AnnouncementContract {
    interface View extends IView<Presenter>{
        void setVpCurrentPosition();
        void showAnnounce(List<Information> announces);
    }

    interface Presenter extends IPresenter<View> {
        void initTimer();
        void getAnnounce(String parentId);
    }
}
