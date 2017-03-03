package lilun.com.pension.ui.education.course_details;

import java.util.List;

import lilun.com.pension.base.IPresenter;
import lilun.com.pension.base.IView;
import lilun.com.pension.module.bean.EdusColleageCourse;

/**
 * Created by zp on 2017/3/2.
 */

public class CourseDetailContract {
    interface View extends IView<Presenter> {
        void showJoinCourse();

        void showQuitCourse();

        void showCourseDetail(EdusColleageCourse orders);
    }

    interface Presenter extends IPresenter<View> {



        void joinCourse(String courseId,String filter);

        void quitCourse(String courseId,String filter);

        void getCourseDetail(String courseId, String filter);
    }


}
