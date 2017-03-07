package lilun.com.pension.ui.health.list;

import java.util.List;

import lilun.com.pension.base.IPresenter;
import lilun.com.pension.base.IView;
import lilun.com.pension.module.bean.Information;

/**
 * Created by zp on 2017/3/7.
 */

public interface HealthListContact {
    interface View extends IView<Presenter>{
        void showDataList(List<Information> list, boolean isLoadMore);
        void completeRefresh();
    };
    interface Presenter extends IPresenter<View>{
        void getDataList(String filter, int skip);
    };
}
