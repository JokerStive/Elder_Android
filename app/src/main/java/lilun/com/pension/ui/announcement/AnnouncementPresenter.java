package lilun.com.pension.ui.announcement;

import java.util.List;
import java.util.concurrent.TimeUnit;

import lilun.com.pension.app.OrganizationChildrenConfig;
import lilun.com.pension.base.RxPresenter;
import lilun.com.pension.module.bean.Information;
import lilun.com.pension.module.utils.RxUtils;
import lilun.com.pension.module.utils.StringUtils;
import lilun.com.pension.net.NetHelper;
import lilun.com.pension.net.RxSubscriber;
import rx.Observable;

/**
 * 公告栏P
 *
 * @author yk
 *         create at 2017/2/6 15:39
 *         email : yk_developer@163.com
 */
public class AnnouncementPresenter extends RxPresenter<AnnouncementContract.View> implements AnnouncementContract.Presenter {


//    private AnnouncementContract.ViewStep2 mView;

//    public AnnouncementPresenter(AnnouncementContract.ViewStep2 view) {
//        this.mView = Preconditions.checkNotNull(view);
//        mView.bindPresenter(this);
//    }

    @Override
    public void initTimer() {
        addSubscribe(Observable.interval(0, 2000, TimeUnit.MILLISECONDS)
                .compose(RxUtils.applySchedule())
                .subscribe(aLong -> {
//                    Logger.d(getClass().getName() + "定时器");
                    if (view != null) {
                        view.setVpCurrentPosition();
                    }
                }));

    }


    @Override
    public void getAnnounce(String parentId) {
//        /#information/公告;
        String filter = "{\"where\":{\"organizationId\":\"" + OrganizationChildrenConfig.information() + "\",\"isCat\":\"false\",\"parentId\":{\"like\":\"" + parentId + "\"}}}";
        addSubscribe(NetHelper.getApi()
                .getInformations(StringUtils.addFilterWithDef(filter, 0))
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<Information>>() {
                    @Override
                    public void _next(List<Information> information) {
                        view.showAnnounce(information);
//                        view.showInformation(information);
                    }
                }));
    }


    @Override
    public void unBindView() {
        unSubscribe();
    }
}
