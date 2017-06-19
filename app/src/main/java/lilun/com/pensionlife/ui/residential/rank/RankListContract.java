package lilun.com.pensionlife.ui.residential.rank;

import java.util.List;

import lilun.com.pensionlife.base.IPresenter;
import lilun.com.pensionlife.base.IView;
import lilun.com.pensionlife.module.bean.Rank;

/**
*评价C
*@author yk
*create at 2017/2/28 9:33
*email : yk_developer@163.com
*/
public class RankListContract {
    interface View extends IView<Presenter> {
        void showRanks(List<Rank> replies, boolean isLoadMore);
        void completeRefresh();
    }

    interface Presenter extends IPresenter<View> {
        void getRanks(String whatModule, String aidId, int skip);
    }
}
