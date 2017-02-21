package lilun.com.pension.ui.announcement;

import lilun.com.pension.base.IPresenter;
import lilun.com.pension.base.IView;

/**
 * Created by yk on 2017/1/6.
 * 广告栏
 */
public interface AnnouncementContract {
    interface View extends IView<Presenter>{
        void setVpCurrentPosition();
    }

    interface Presenter extends IPresenter<View> {
        void initTimer();
    }
}
