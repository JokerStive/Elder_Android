package lilun.com.pensionlife.ui.education.course_details;

import lilun.com.pensionlife.base.IPresenter;
import lilun.com.pensionlife.base.IView;
import lilun.com.pensionlife.module.bean.Course;

/**
 * Created by zp on 2017/3/2.
 */

public class CourseDetailContract {
    interface View extends IView<Presenter> {
        void showJoinCourse();

        void showQuitCourse();

        void showCourseDetail(Course orders);
    }

    interface Presenter extends IPresenter<View> {



        void joinCourse(String courseId,String filter);

        void quitCourse(String courseId,String filter);

        void getCourseDetail(String courseId, String filter);
    }


}
