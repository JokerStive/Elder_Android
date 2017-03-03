package lilun.com.pension.ui.education.course_details;

import android.widget.Toast;

import lilun.com.pension.app.App;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.base.RxPresenter;
import lilun.com.pension.module.bean.EdusColleageCourse;
import lilun.com.pension.module.utils.RxUtils;
import lilun.com.pension.net.NetHelper;
import lilun.com.pension.net.RxSubscriber;

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
                        view.showJoinCourse();

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

                        view.showQuitCourse();
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
                .getEduCourses(courseId,filter)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<EdusColleageCourse>() {
                    @Override
                    public void _next(EdusColleageCourse edusColleageCourse) {
                        view.showCourseDetail(edusColleageCourse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        Toast.makeText(App.context, "获取课程失败", Toast.LENGTH_SHORT).show();
                    }
                }));
    }
}
