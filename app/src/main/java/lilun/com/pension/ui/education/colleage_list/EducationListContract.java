package lilun.com.pension.ui.education.colleage_list;

import java.util.List;

import lilun.com.pension.base.IPresenter;
import lilun.com.pension.base.IView;
import lilun.com.pension.module.bean.ElderEdusColleage;

/**
 * 老年教育契约类
 *
 * @author yk
 *         create at 2017/2/13 10:43
 *         email : yk_developer@163.com
 */
public interface EducationListContract {
    interface View extends IView<Presenter> {
        void showEdusList(List<ElderEdusColleage> orders, boolean isLoadMore);

        void completeRefresh();
    }

    interface Presenter extends IPresenter<View> {
        void getColleage(String filter, int skip);
    }
}
