package lilun.com.pension.ui.education.colleage_details;

import java.util.List;

import lilun.com.pension.base.IPresenter;
import lilun.com.pension.base.IView;
import lilun.com.pension.module.bean.EdusColleageCourse;

/**
 * Created by zp on 2017/2/23.
 */

public interface ColleageDetailContract {

    interface View extends IView<Presenter> {
        void showColleageCouseList(List<EdusColleageCourse> orders);
    }

    interface Presenter extends IPresenter<View> {


        void getColleageCouse(String courseId, String filter, int skip);
    }
}
