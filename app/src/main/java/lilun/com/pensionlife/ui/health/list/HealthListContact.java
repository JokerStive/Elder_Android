package lilun.com.pensionlife.ui.health.list;

import java.util.List;

import lilun.com.pensionlife.base.IPresenter;
import lilun.com.pensionlife.base.IView;
import lilun.com.pensionlife.module.bean.Information;

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
