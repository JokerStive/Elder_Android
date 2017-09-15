package lilun.com.pensionlife.ui.education.course_list;

import java.util.ArrayList;
import java.util.List;

import lilun.com.pensionlife.base.RxPresenter;
import lilun.com.pensionlife.module.bean.ConditionOption;
import lilun.com.pensionlife.module.bean.Option;
import lilun.com.pensionlife.module.bean.OrganizationProduct;
import lilun.com.pensionlife.module.bean.OrganizationProductCategory;
import lilun.com.pensionlife.module.bean.Semester;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.net.NetHelper;
import lilun.com.pensionlife.net.RxSubscriber;

/**
 * 老年教育P
 *
 * @author yk
 *         create at 2017/2/13 10:55
 *         email : yk_developer@163.com
 */
public class CourseListPresenter extends RxPresenter<CourseListContract.View> implements CourseListContract.Presenter {


    @Override
    public void getCourses(String filter, int skip) {
        addSubscribe(NetHelper.getApi()
                .getProducts(StringUtils.addFilterWithDef(filter, skip))
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<OrganizationProduct>>() {
                    @Override
                    public void _next(List<OrganizationProduct> products) {
                        view.completeRefresh();
                        view.showCollageCourseList(products, skip != 0);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.completeRefresh();
                    }
                })
        );

    }

    @Override
    public void getCourseCategories(String filter, int skip) {
        addSubscribe(NetHelper.getApi()
                .getOrganizationProductCategories(StringUtils.addFilterWithDef(filter, skip))
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<OrganizationProductCategory>>() {
                    @Override
                    public void _next(List<OrganizationProductCategory> categories) {
                        view.getCategorySuccess(categories);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                })
        );
    }

    @Override
    public List<ConditionOption> getConditionOptionsList() {
        List<ConditionOption> conditionOptionList = new ArrayList<>();

        List<Option> kindOptions = new ArrayList<>();
        Option optionDef = new Option("createAt DESC", "时间排序");
        Option optionDESC = new Option("createAt DESC", "升序排序");
        Option optionHelp = new Option("createAt ", "降序排序");
        kindOptions.add(optionDef);
        kindOptions.add(optionDESC);
        kindOptions.add(optionHelp);
        ConditionOption conditionOptionOrder = new ConditionOption("order", "时间", kindOptions);

        conditionOptionList.add(conditionOptionOrder);
        return conditionOptionList;
    }

    @Override
    public void getSemesters(String filter) {
        addSubscribe(NetHelper.getApi()
                .getSemesters(StringUtils.addFilterWithDef(filter, 0))
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<Semester>>() {
                    @Override
                    public void _next(List<Semester> semesters) {
                        view.getSemesterSuccess(semesters);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                })
        );
    }

}
