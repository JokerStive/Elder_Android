package lilun.com.pension.ui.tourism.root;

import java.util.Arrays;
import java.util.List;

import lilun.com.pension.R;
import lilun.com.pension.app.App;
import lilun.com.pension.base.BaseFragment;
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
public class TourismRootPresenter extends RxPresenter<TourismRootContract.View> implements TourismRootContract.Presenter {
    @Override
    public void getPopularJourneys(String filter, int skip) {
        addSubscribe(NetHelper.getApi()
                .getTourism(StringUtils.addFilterWithDef(filter, skip))
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<Tourism>>(((BaseFragment) view).getActivity()) {
                    @Override
                    public void _next(List<Tourism> tourism) {
                        view.showPopularJourney(tourism, skip != 0);
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
    public void getPopularDestinations() {
        String[] popularDestination = App.context.getResources().getStringArray(R.array.popular_journey_destination);
        view.showPopularDestination(Arrays.asList(popularDestination));
    }
}
