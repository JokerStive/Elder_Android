package lilun.com.pension.ui.help.list;

import java.util.List;

import lilun.com.pension.base.IPresenter;
import lilun.com.pension.base.IView;
import lilun.com.pension.module.bean.ConditionOption;
import lilun.com.pension.module.bean.OrganizationAid;

/**
*邻居互助契约类
*@author yk
*create at 2017/2/13 10:43
*email : yk_developer@163.com
*/
public interface HelpContract {
    interface View extends IView<Presenter> {
        void showAboutMe(List<OrganizationAid>  helps, boolean isLoadMore);
        void completeRefresh();
    }

    interface Presenter extends IPresenter<View> {
        void getAboutMe(String filter, int skip);
        void getHelps(String filter, int skip);
        List<List<ConditionOption>> getConditionOptionsList();
    }

}
