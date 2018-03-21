package lilun.com.pensionlife.ui.education.course_details;

import java.util.List;

import lilun.com.pensionlife.base.IPresenter;
import lilun.com.pensionlife.base.IView;
import lilun.com.pensionlife.module.bean.CourseSchedule;
import lilun.com.pensionlife.module.bean.Information;
import lilun.com.pensionlife.module.bean.OrderLimit;
import lilun.com.pensionlife.module.bean.OrganizationProduct;

/**
 * Created by zp on 2017/3/2.
 */

public class CourseDetailContract {
    interface View extends IView<Presenter> {

        void showCourseDetail(OrganizationProduct orders);

        void showProtocol(Information protocol);

        void showSchedules(List<CourseSchedule> schedules);

        void showIsOrdered(OrderLimit orderLimit);

    }

    interface Presenter extends IPresenter<View> {

        void joinCourse(String courseId, String filter);

        void quitCourse(String courseId, String filter);

        void getCourseDetail(String courseId, String filter);

        void getCourseSchedules(String productId);

        void getProtocol(String productId);

        void getSemester(String eduSemesterId);


        void getIsOrder(String productId);


    }


}
