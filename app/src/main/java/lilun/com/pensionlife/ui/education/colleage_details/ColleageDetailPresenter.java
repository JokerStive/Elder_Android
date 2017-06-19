package lilun.com.pensionlife.ui.education.colleage_details;

import android.widget.Toast;

import java.util.List;

import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.base.RxPresenter;
import lilun.com.pensionlife.module.bean.EdusColleageCourse;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.net.NetHelper;
import lilun.com.pensionlife.net.RxSubscriber;

/**
 * Created by zp on 2017/2/23.
 */

public class ColleageDetailPresenter extends RxPresenter<ColleageDetailContract.View>
        implements ColleageDetailContract.Presenter {
    @Override
    public void getColleageCouse(String courseId, String filter, int skip) {

        addSubscribe(NetHelper.getApi()
                .getOrganizationsEdusCourse(courseId,StringUtils.addFilterWithDef(filter, skip))
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<EdusColleageCourse>>() {
                    @Override
                    public void _next(List<EdusColleageCourse> edusColleageCourses) {
                        view.showColleageCouseList(edusColleageCourses);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        Toast.makeText(App.context, "获取课程失败", Toast.LENGTH_SHORT).show();
                    }
                }));
    }
}
