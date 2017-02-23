package lilun.com.pension.ui.education.list;

import java.util.List;

import lilun.com.pension.base.IPresenter;
import lilun.com.pension.base.IView;
import lilun.com.pension.module.bean.ElderEdus;
import lilun.com.pension.module.bean.OrganizationProduct;

/**
 * 老年教育契约类
 *
 * @author yk
 *         create at 2017/2/13 10:43
 *         email : yk_developer@163.com
 */
public interface EducationListContract {
    interface View extends IView<Presenter> {
        void showEdusList(List<ElderEdus> orders, boolean isLoadMore);

        void completeRefresh();
    }

    interface Presenter extends IPresenter<View> {

        void getCommunityCouse(String filter, int skip);
        void getColleage(String filter, int skip);
    }
}
