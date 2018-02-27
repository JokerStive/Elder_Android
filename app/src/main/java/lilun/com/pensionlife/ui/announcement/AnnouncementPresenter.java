package lilun.com.pensionlife.ui.announcement;

import android.text.TextUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import lilun.com.pensionlife.app.User;
import lilun.com.pensionlife.base.RxPresenter;
import lilun.com.pensionlife.module.bean.Information;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.net.NetHelper;
import lilun.com.pensionlife.net.RxSubscriber;
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
        addSubscribe(Observable.interval(0, 3000, TimeUnit.MILLISECONDS)
                .compose(RxUtils.applySchedule())
                .subscribe(aLong -> {
//                    Logger.d(getClass().getFrom() + "定时器");
                    if (view != null) {
                        view.setVpCurrentPosition();
                    }
                }));

    }


    @Override
    public void getAnnounce(String parentId) {
        String parentIdFilter;
        if (TextUtils.isEmpty(parentId)) {
            parentIdFilter =User.spliceId("/#information/公告");
        } else {
            parentIdFilter =User.spliceId("/#information/公告/"+parentId);
        }

        String filter = "{\"where\":{\"visible\":0,\"isCat\":false,\"parentId\":{\"inq\":" + parentIdFilter + "}}}";
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

//    private String spliceParentId(String name) {
//        String result = "[";
//        String currentOrganizationId = User.getCurrentOrganizationId();
//        String[] split = currentOrganizationId.split("/");
//        if (split.length < 4) return "";
//        for (int i = 4; i < split.length; i++) {
//            String parentId = "";
//            for (int j = 1; j <= i; j++) {
//                parentId += "/" + split[j];
//            }
//            if (i == 4) {
//                result += "\"" + parentId + "/#information/" + name + "\"";
//            } else {
//                result += "," + "\"" + parentId + "/#information/" + name + "\"";
//            }
//        }
//        result += "]";
//        return result;
//    }


    @Override
    public void unBindView() {
        unSubscribe();
    }
}
