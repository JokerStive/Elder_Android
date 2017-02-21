package lilun.com.pension.ui.home;

import java.util.ArrayList;
import java.util.List;

import lilun.com.pension.base.RxPresenter;
import lilun.com.pension.module.bean.Announcement;

/**
 * 首页P
 *
 * @author yk
 *         create at 2017/2/6 16:23
 *         email : yk_developer@163.com
 */
public class HomePresenter extends RxPresenter<HomeContract.View> implements HomeContract.Presenter {

    private String[] adUrls =
            {"http://ojcnzm1cb.bkt.clouddn.com/announcement_def.png",
                    "http://ojcnzm1cb.bkt.clouddn.com/announcement_def.png",
                    "http://ojcnzm1cb.bkt.clouddn.com/announcement_def.png",
                    "http://ojcnzm1cb.bkt.clouddn.com/announcement_def.png",
                    "http://ojcnzm1cb.bkt.clouddn.com/announcement_def.png",
                    "http://ojcnzm1cb.bkt.clouddn.com/announcement_def.png",
                    "http://ojcnzm1cb.bkt.clouddn.com/announcement_def.png"
            };


    @Override
    public void getAnnouncements() {
        List<Announcement> announcements = new ArrayList<>();
        for (int i = 0; i < adUrls.length; i++) {
            Announcement announcement = new Announcement();
            announcement.setImageUrl(adUrls[i]);
            announcements.add(announcement);
        }
        view.showAnnouncementFragment(announcements);
    }
}
