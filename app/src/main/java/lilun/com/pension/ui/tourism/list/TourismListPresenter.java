package lilun.com.pension.ui.tourism.list;

import java.util.List;

import lilun.com.pension.base.RxPresenter;
import lilun.com.pension.module.bean.Tourism;
import lilun.com.pension.module.utils.RxUtils;
import lilun.com.pension.module.utils.StringUtils;
import lilun.com.pension.net.NetHelper;
import lilun.com.pension.net.RxSubscriber;

/**
 * 旅游首页
 *
 * @author yk
 *         create at 2017/4/13 9:47
 *         email : yk_developer@163.com
 */
public class TourismListPresenter extends RxPresenter<TourismListContract.View> implements TourismListContract.Presenter {
    @Override
    public void getJourneys(String filter, int skip) {
        addSubscribe(NetHelper.getApi()
                .getTourisms(StringUtils.addFilterWithDef(filter, skip))
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<Tourism>>() {
                    @Override
                    public void _next(List<Tourism> tourism) {
                        view.showJourney(tourism, skip != 0);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.completeRefresh();
                    }
                })
        );

    }

    @Override
    public void getJourneyTags() {
    }

}
