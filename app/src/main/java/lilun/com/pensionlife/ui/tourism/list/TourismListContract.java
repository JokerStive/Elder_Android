package lilun.com.pensionlife.ui.tourism.list;

import java.util.List;

import lilun.com.pensionlife.base.IPresenter;
import lilun.com.pensionlife.base.IView;
import lilun.com.pensionlife.module.bean.Tourism;

/**
*旅游首页C
*@author yk
*create at  9:41
*email : yk_developer@163.com
*/
public interface TourismListContract {
    interface View extends IView<Presenter> {
       void  showJourneyTags(List<String> tags);
        void showJourney(List<Tourism> journeys, boolean isLoadMore);
        void completeRefresh();
    }

    interface Presenter extends IPresenter<View> {
        void getJourneys(String filter, int skip);
        void getJourneyTags();
    }
}
