package lilun.com.pensionlife.ui.health.classify;

import java.util.List;

import lilun.com.pensionlife.base.IPresenter;
import lilun.com.pensionlife.base.IView;
import lilun.com.pensionlife.module.bean.ElderModule;
import lilun.com.pensionlife.module.bean.Information;

/**
*健康服务契约类
*@author yk
*create at 2017/2/13 10:43
*email : yk_developer@163.com
*/
public interface HealthClassifyContract {
    interface View extends IView<Presenter> {
        void showClassifies(List<ElderModule> elderModules);
        void showAboutMe(List<Information> orders, boolean isLoadMore);
        void completeRefresh();
    }

    interface Presenter extends IPresenter<View> {
        void getClassifies(String parentId);
        void getAboutMe(String filter, int skip);
    }

}
