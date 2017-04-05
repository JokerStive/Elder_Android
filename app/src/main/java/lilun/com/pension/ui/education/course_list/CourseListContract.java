package lilun.com.pension.ui.education.course_list;

import java.util.List;

import lilun.com.pension.base.IPresenter;
import lilun.com.pension.base.IView;
import lilun.com.pension.module.bean.ConditionOption;
import lilun.com.pension.module.bean.EdusColleageCourse;
import lilun.com.pension.module.bean.ElderEdus;

/**
 * 老年教育契约类
 *
 * @author yk
 *         create at 2017/2/13 10:43
 *         email : yk_developer@163.com
 */
public interface CourseListContract {
    interface View extends IView<Presenter> {
        void showCollgCourseList(List<EdusColleageCourse> orders, boolean isLoadMore);

        void completeRefresh();


    }

    interface Presenter extends IPresenter<View> {

        void getCollgCourseList(String courseId,String filter, int skip);
        List<List<ConditionOption>> getConditionOptionsList();
    }
}
