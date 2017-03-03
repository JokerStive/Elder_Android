package lilun.com.pension.ui.education.course_list;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import lilun.com.pension.app.App;
import lilun.com.pension.base.RxPresenter;
import lilun.com.pension.module.bean.EdusColleageCourse;
import lilun.com.pension.module.bean.ElderEdus;
import lilun.com.pension.module.bean.ElderEdusColleage;
import lilun.com.pension.module.bean.OrganizationActivity;
import lilun.com.pension.module.utils.RxUtils;
import lilun.com.pension.module.utils.StringUtils;
import lilun.com.pension.net.NetHelper;
import lilun.com.pension.net.RxSubscriber;
import lilun.com.pension.ui.education.list.EducationListContract;

/**
 * 老年教育P
 *
 * @author yk
 *         create at 2017/2/13 10:55
 *         email : yk_developer@163.com
 */
public class CourseListPresenter extends RxPresenter<CourseListContract.View> implements CourseListContract.Presenter {





    @Override
    public void getCollgCourseList(String courseId, String filter, int skip) {
        addSubscribe(NetHelper.getApi()
                .getOrganizationsEdusCourse(courseId,StringUtils.addFilterWithDef(filter, skip))
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<EdusColleageCourse>>() {
                    @Override
                    public void _next(List<EdusColleageCourse> edusColleageCourses) {
                        view.completeRefresh();
                        view.showCollgCourseList(edusColleageCourses,false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.completeRefresh();
                        Toast.makeText(App.context, "获取课程失败", Toast.LENGTH_SHORT).show();
                    }
                }));
    }
}
