package lilun.com.pension.ui.announcement;

import java.util.concurrent.TimeUnit;

import lilun.com.pension.base.RxPresenter;
import lilun.com.pension.module.utils.RxUtils;
import rx.Observable;

/**
*公告栏P
*@author yk
*create at 2017/2/6 15:39
*email : yk_developer@163.com
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
                    if (view!=null){
                        view.setVpCurrentPosition();
                    }
                }));

    }


}
