package lilun.com.pension.ui.education.course_list;

import android.widget.Toast;

import java.util.List;

import lilun.com.pension.app.App;
import lilun.com.pension.base.RxPresenter;
import lilun.com.pension.module.bean.ConditionOption;
import lilun.com.pension.module.bean.EdusColleageCourse;
import lilun.com.pension.module.utils.RxUtils;
import lilun.com.pension.module.utils.StringUtils;
import lilun.com.pension.net.NetHelper;
import lilun.com.pension.net.RxSubscriber;

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

    @Override
    public List<List<ConditionOption>> getConditionOptionsList() {

//        String where_price = "price";
//        String where_area = "level";
//
//
//        String[] prices = App.context.getResources().getStringArray(R.array.product_price_option);
//        String[] level = App.context.getResources().getStringArray(R.array.education_level);
//
//        List<List<ConditionOption>> optionsList = new ArrayList<>();
//
//
//        //价格可选项
//        List<ConditionOption> priceOptions = new ArrayList<>();
//        for (String price : prices) {
//            ConditionOption conditionOption = new ConditionOption(where_price, price);
//            priceOptions.add(conditionOption);
//        }
//        optionsList.add(priceOptions);
//
//
//        List<ConditionOption> areaOptions = new ArrayList<>();
//        for (String area : level) {
//            ConditionOption conditionOption = new ConditionOption(where_area, area);
//            areaOptions.add(conditionOption);
//        }
//        optionsList.add(areaOptions);

        return null;
    }

}
