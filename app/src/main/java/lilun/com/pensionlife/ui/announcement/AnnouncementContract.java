package lilun.com.pensionlife.ui.announcement;

import java.util.List;

import lilun.com.pensionlife.base.IPresenter;
import lilun.com.pensionlife.base.IView;
import lilun.com.pensionlife.module.bean.Information;

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
