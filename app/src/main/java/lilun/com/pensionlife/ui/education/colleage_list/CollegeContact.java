package lilun.com.pensionlife.ui.education.colleage_list;

import java.util.List;

import lilun.com.pensionlife.base.IPresenter;
import lilun.com.pensionlife.base.IView;
import lilun.com.pensionlife.module.bean.Organization;

/**
 * 大学列表c
 */
public interface CollegeContact {
    interface View extends IView<Presenter> {
        void showCollege(List<Organization> colleges);

    }

    interface Presenter extends IPresenter<View> {
        void getCollege();
    }
}
