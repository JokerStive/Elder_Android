package lilun.com.pensionlife.ui.education.course_details;

import android.widget.Toast;

import java.util.List;

import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.base.RxPresenter;
import lilun.com.pensionlife.module.bean.CourseSchedule;
import lilun.com.pensionlife.module.bean.OrderLimit;
import lilun.com.pensionlife.module.bean.OrganizationProduct;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.net.NetHelper;
import lilun.com.pensionlife.net.RxSubscriber;

/**
 * Created by zp on 2017/3/2.
 */

public class CourseDetailPresenter extends RxPresenter<CourseDetailContract.View>
        implements CourseDetailContract.Presenter {


    @Override
    public void joinCourse(String courseId, String filter) {
        addSubscribe(NetHelper.getApi()
                .joinCourse(courseId, "")
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<Object>(((BaseFragment) view).getActivity()) {
                    @Override
                    public void _next(Object o) {
//                        view.showJoinCourse();

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        Toast.makeText(App.context, "提交失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    @Override
    public void quitCourse(String courseId, String filter) {
        addSubscribe(NetHelper.getApi()
                .quitCourse(courseId, "")
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<Object>(((BaseFragment) view).getActivity()) {
                    @Override
                    public void _next(Object o) {

//                        view.showQuitCourse();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        Toast.makeText(App.context, "提交失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    @Override
    public void getCourseDetail(String courseId, String filter) {
        addSubscribe(NetHelper.getApi()
                .getProduct(courseId, filter)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<OrganizationProduct>() {
                    @Override
                    public void _next(OrganizationProduct course) {
                        view.showCourseDetail(course);
                    }
                }));
    }

    @Override
    public void getCourseSchedules(String productId) {
        String filter = "{\"include\":\"teachers\",\"where\":{\"classId\":\"" + productId + "\"}}";
        addSubscribe(NetHelper.getApi()
                .getCourseSchedules(filter)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<CourseSchedule>>() {
                    @Override
                    public void _next(List<CourseSchedule> schedules) {
                        view.showSchedules(schedules);
                    }
                }));
    }

    @Override
    public void getProtocol(String productId) {

    }

    @Override
    public void getSemester(String eduSemesterId) {

    }

    @Override
    public void getIsOrder(String productId) {
        addSubscribe(NetHelper.getApi()
                .getOrderLimit(productId)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<OrderLimit>() {

                    @Override
                    public void _next(OrderLimit orderLimit) {
                        view.showIsOrdered(orderLimit);
                    }
                }));

    }
}
