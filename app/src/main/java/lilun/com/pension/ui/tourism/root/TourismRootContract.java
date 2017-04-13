package lilun.com.pension.ui.tourism.root;

import java.util.List;

import lilun.com.pension.base.IPresenter;
import lilun.com.pension.base.IView;
import lilun.com.pension.module.bean.Tourism;

/**
*旅游首页C
*@author yk
*create at  9:41
*email : yk_developer@163.com
*/
public interface TourismRootContract {
    interface View extends IView<Presenter> {
       void  showPopularDestination(List<String> destinations);

        void showPopularJourney(List<Tourism>  journeys, boolean isLoadMore);
        void completeRefresh();
    }

    interface Presenter extends IPresenter<View> {
        void getPopularJourneys(String filter, int skip);
        void getPopularDestinations();
    }
}
