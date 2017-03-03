package lilun.com.pension.ui.education.classify;

import java.util.ArrayList;
import java.util.List;

import lilun.com.pension.R;
import lilun.com.pension.app.App;
import lilun.com.pension.base.RxPresenter;
import lilun.com.pension.module.bean.ActivityCategory;
import lilun.com.pension.module.bean.ElderEdus;
import lilun.com.pension.module.bean.ElderModule;
import lilun.com.pension.module.bean.ElderEdusColleage;
import lilun.com.pension.module.bean.LocationBean;
import lilun.com.pension.module.bean.OrganizationAccount;
import lilun.com.pension.module.bean.OrganizationActivity;
import lilun.com.pension.module.utils.RxUtils;
import lilun.com.pension.net.NetHelper;
import lilun.com.pension.net.RxSubscriber;

/**
 * 老年教育P
 *
 * @author yk
 *         create at 2017/2/13 10:55
 *         email : yk_developer@163.com
 */
public class EducationClassifyPresenter extends RxPresenter<EducationClassifyContract.View>
        implements EducationClassifyContract.Presenter {


    @Override
    public void getClassifies() {
        String education = App.context.getString(R.string.pension_education);
        String filter = "{\"where\":{\"parent\":\"" + education + "\"},\"order\":\"orderId\"}";
        addSubscribe(NetHelper.getApi()
                .getElderModules(filter)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<ElderModule>>() {
                    @Override
                    public void _next(List<ElderModule> elderModules) {
                        view.showClassifies(elderModules);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.completeRefresh();
                    }
                }));
    }

    @Override
    public void getOrgActivityCategory(String filter) {
        addSubscribe(NetHelper.getApi()
                .getActivityCategories(filter)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<ActivityCategory>>() {
                    @Override
                    public void _next(List<ActivityCategory> activityCategories) {
                        view.showOrgActivityCategory(activityCategories);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.completeRefresh();
                    }
                }));
    }


    @Override
    public void getAboutMe(String filter, int skip) {
//        addSubscribe(NetHelper.getApi()
//                .getOrganizationsEdus(filter)
//                .compose(RxUtils.handleResult())
//                .compose(RxUtils.applySchedule())
//                .subscribe(new RxSubscriber<List<ElderEdusColleage>>() {
//                    @Override
//                    public void _next(List<ElderEdusColleage> elderModules) {
////                        if (skip == 0)
////                            view.showColleage(elderModules, false);
////                        else
////                            view.showColleage(elderModules, true);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        super.onError(e);
//                        view.completeRefresh();
//                    }
//                }));

        view.completeRefresh();
        view.showAboutMe(getData(), false);
    }

    public List<ElderEdus> getData() {
        List<ElderEdus> data = new ArrayList<>();
        ElderEdus edus1 = new ElderEdus();
        edus1.setSource(OrganizationActivity.TYPE);
        edus1.setTitle("十八届六中全会精神学习");
        edus1.setCreatedAt("2017-02-23T11:17:00.749Z");
        edus1.setAddress("南岸石门社区25号");
        edus1.setDescription("全会听取和讨论了习近平受中央政治局委托作的工作报告，审议通过了《关于新形势下党内政治生活的若干准则》和《中国共产党党内监督条例》，审议通过了《关于召开党的第十九次全国代表大会的决议》。");
        data.add(edus1);
        ElderEdus edus2 = new ElderEdus();
        edus2.setSource(ElderEdusColleage.TYPE);
        edus2.setTitle("四川大学");
        LocationBean loc = new LocationBean();
        loc.setLat(23.1132f);
        loc.setLng(11.2344f);
        edus2.setLocation(loc);
        edus2.setCreatedAt("2017-02-23T11:17:00.749Z");
        edus2.setAddress("四川省成都市武侯区一环路南一段24号");
        edus2.setDescription("四川大学由原四川大学、原成都科学技术大学、原华西医科大学三所全国重点大学经过两次合并而成。原四川大学起始于1896年四川总督鹿传霖奉光绪特旨创办的四川中西学堂，是西南地区最早的近代高等学校；原成都科学技术大学是新中国院系调整时组建的第一批多科型工科院校；原华西医科大学源于1910年由西方基督教会组织在成都创办的华西协合大学，是西南地区最早的西式大学和中国最早培养研究生的大学之一。1994年，原四川大学和原成都科技大学合并为四川联合大学，1998年更名为四川大学。2000年，四川大学与原华西医科大学合并，组建了新的四川大学。");
        data.add(edus2);

        ElderEdus edus3 = new ElderEdus();
        edus3.setSource(ElderEdusColleage.TYPE);
        edus3.setTitle("华东网上大学");
        edus3.setCreatedAt("2017-02-23T11:17:00.749Z");
        edus3.setAddress("重庆市雀跃区一环路南一段24号");
        edus3.setDescription("网络大学通常实行弹性学制，允许学生自由选择学习期限，例如人大网校即规定高中起点专科、专科起点本科，学制三年，学习期限2－5年，80学分；高中起点本科学制五年，学习期限4－7年，160学分。网络大学需要学生有很强的自制力和自主性，因为和传统教学方式不同，远程教育主要是学生通过点击网上课件（或是光盘课件）来完成课程的学习，通过电子邮件或贴贴子的方式向教师提交作业或即时交流，并可根据学生的具体情况安排集中面授。");
        data.add(edus3);
        return data;
    }
}
