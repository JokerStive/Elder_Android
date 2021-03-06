package lilun.com.pensionlife.ui.education.course_details;

import java.util.List;

import lilun.com.pensionlife.app.User;
import lilun.com.pensionlife.base.RxPresenter;
import lilun.com.pensionlife.module.bean.Contact;
import lilun.com.pensionlife.module.bean.CourseSchedule;
import lilun.com.pensionlife.module.bean.Information;
import lilun.com.pensionlife.module.bean.OrderLimit;
import lilun.com.pensionlife.module.bean.OrganizationProduct;
import lilun.com.pensionlife.module.bean.ServiceTerm;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.net.NetHelper;
import lilun.com.pensionlife.net.RxSubscriber;

/**
 * Created by zp on 2017/3/2.
 */

public class CourseDetailPresenter extends RxPresenter<CourseDetailContract.View>
        implements CourseDetailContract.Presenter {


    @Override
    public void getProductDetail(String courseId, String filter) {
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
        String filter = "{\"include\":\"teachers\",\"where\":{\"visible\":0,\"classId\":\"" + productId + "\"}}";
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
        String filter = "{\"limit\":1,\"where\":{\"visible\":0,\"moduleName\":\"OrganizationProduct\",\"moduleId\":\"" + productId + "\"}}";
        addSubscribe(NetHelper.getApi()
                .getServiceTerms(filter)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<ServiceTerm>>() {

                    @Override
                    public void _next(List<ServiceTerm> serviceTerms) {
                        if (serviceTerms.size() > 0) {
                            String fkId = serviceTerms.get(0).getFkId();
                            getInfo(fkId);
                        }
                    }
                }));
    }



    private void getInfo(String infoId){
        addSubscribe(NetHelper.getApi()
                .getInformation(infoId)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<Information>() {

                    @Override
                    public void _next(Information information) {
                        view.showProtocol(information);
                    }
                }));
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

    @Override
    public void getContacts() {
        String filter = "{\"where\":{\"accountId\":\"" + User.getUserId() + "\"}}";
        NetHelper.getApi().getContacts(filter)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<Contact>>(getActivity()) {
                    @Override
                    public void _next(List<Contact> contacts) {
                        view.checkContact(contacts);
                    }
                });
    }
}
