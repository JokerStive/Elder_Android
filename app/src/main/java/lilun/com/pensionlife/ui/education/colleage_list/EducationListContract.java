package lilun.com.pensionlife.ui.education.colleage_list;

import java.util.List;

import lilun.com.pensionlife.base.IPresenter;
import lilun.com.pensionlife.base.IView;
import lilun.com.pensionlife.module.bean.ConditionOption;
import lilun.com.pensionlife.module.bean.ElderEdusColleage;

/**
 * 老年教育契约类
 *
 * @author yk
 *         create at 2017/2/13 10:43
 *         email : yk_developer@163.com
 */
public interface EducationListContract {
    interface View extends IView<Presenter> {
        void showOrganizationEdu(List<ElderEdusColleage> orders, boolean isLoadMore);

        void completeRefresh();
    }

    interface Presenter extends IPresenter<View> {
        void getOrganizationEdu(String filter, int skip);
        List<List<ConditionOption>> getConditionOptionsList();
    }
}
