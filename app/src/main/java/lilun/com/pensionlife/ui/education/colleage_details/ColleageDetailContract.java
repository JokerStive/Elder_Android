package lilun.com.pensionlife.ui.education.colleage_details;

import java.util.List;

import lilun.com.pensionlife.base.IPresenter;
import lilun.com.pensionlife.base.IView;
import lilun.com.pensionlife.module.bean.Course;

/**
 * Created by zp on 2017/2/23.
 */

public interface ColleageDetailContract {

    interface View extends IView<Presenter> {
        void showColleageCouseList(List<Course> orders);
    }

    interface Presenter extends IPresenter<View> {


        void getColleageCouse(String courseId, String filter, int skip);
    }
}
