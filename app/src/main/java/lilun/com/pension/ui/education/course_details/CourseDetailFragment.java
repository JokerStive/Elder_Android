package lilun.com.pension.ui.education.course_details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import lilun.com.pension.R;
import lilun.com.pension.app.IconUrl;
import lilun.com.pension.app.User;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.bean.EdusColleageCourse;
import lilun.com.pension.module.bean.IconModule;
import lilun.com.pension.module.utils.BitmapUtils;
import lilun.com.pension.module.utils.Preconditions;
import lilun.com.pension.module.utils.StartOtherUtils;
import lilun.com.pension.module.utils.StringUtils;
import lilun.com.pension.ui.education.classify.EducationClassifyFragment;
import lilun.com.pension.ui.education.colleage_details.ColleageDetailFragment;
import lilun.com.pension.widget.CircleImageView;
import lilun.com.pension.widget.image_loader.ImageLoaderUtil;
import lilun.com.pension.widget.slider.BannerPager;

/**
 * 大学课程详情
 * Created by zp on 2017/2/23.
 */

public class CourseDetailFragment extends BaseFragment<CourseDetailContract.Presenter>
        implements CourseDetailContract.View {

    EdusColleageCourse mCourse;
    private boolean isJoin;
    private boolean retNeedRef = false;


    @Bind(R.id.bp_course_icon)
    BannerPager bgCourseIcon;
    @Bind(R.id.tv_course_name)
    TextView tvCourseName;
    @Bind(R.id.tv_connect_phone)
    TextView tvConnectPhone;
    @Bind(R.id.tv_connect_person)
    TextView tvConnectPerson;
    @Bind(R.id.cig_connect_icon)
    CircleImageView tvConnectIcon;
    @Bind(R.id.tv_course_sign_date)
    TextView tvSignDate;
    @Bind(R.id.tv_course_during_date)
    TextView tvDuringDate;
    @Bind(R.id.tv_course_start_end_time)
    TextView tvStartEndTime;
    @Bind(R.id.tv_course_plan)
    TextView tvCoursePlan;
    @Bind(R.id.tv_course_desp)
    TextView tvCourseDesp;
    @Bind(R.id.tv_colleage_name)
    TextView tvCoulleageName;
    @Bind(R.id.tv_service_provider)
    TextView tvServiceProvider;


//    @Bind(R.id.join_in)
//    Button btJoinIn;
//    @Bind(R.id.cancle)
//    Button btCancel;
//    @Bind(R.id.other_status)
//    Button btOtherStatus;


    @OnClick({R.id.tv_connect_phone, R.id.iv_back, R.id.tv_service_provider})
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.join_in:
//                mPresenter.joinCourse(mCourse.getId(), "");
//                break;
//            case R.id.cancle:
//                mPresenter.quitCourse(mCourse.getId(), "");
//                break;
            case R.id.tv_connect_phone:
                if (mCourse != null && mCourse.getContact() != null)
                    StartOtherUtils.cellPhone(_mActivity, mCourse.getContact().getMobile());
                break;
            case R.id.iv_back:
                pop();
                break;
            case R.id.tv_service_provider:
                ColleageDetailFragment fragment = findFragment(ColleageDetailFragment.class);
                if (fragment != null) {
                    popTo(ColleageDetailFragment.class, false);
                } else {
                    start(ColleageDetailFragment.newInstance(mCourse.getSchool()));
                }
                break;
        }
    }


    public static CourseDetailFragment newInstance(EdusColleageCourse course) {
        CourseDetailFragment fragment = new CourseDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("course", course);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void getTransferData(Bundle arguments) {
        mCourse = (EdusColleageCourse) arguments.getSerializable("course");
        Preconditions.checkNull(mCourse);

    }

    @Override
    protected void initPresenter() {
        mPresenter = new CourseDetailPresenter();
        mPresenter.bindView(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_course_detail;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        //显示图片
        List<String> urls = new ArrayList<>();
        if (mCourse.getPicture() != null) {
            for (IconModule iconModule : mCourse.getPicture()) {
                String url = IconUrl.eduCourses(mCourse.getId(), iconModule.getFileName());
                urls.add(url);
            }
        } else {
            String url = IconUrl.organizationEdus(mCourse.getId(), null);
            urls.add(url);
        }
        bgCourseIcon.setData(urls);
        tvCourseName.setText(mCourse.getName());

        tvServiceProvider.setVisibility(View.GONE);
        isJoin = false;


        String filter = "{\"include\":[\"contact\",\"school\"]}";
        mPresenter.getCourseDetail(mCourse.getId(), filter);

    }


    @Override
    public void showJoinCourse() {
//        isJoin = true;
//        btJoinIn.setVisibility(View.GONE);
//        btCancel.setVisibility(View.VISIBLE);
//
//        String uri = IconUrl.account(User.getUserId(), BitmapUtils.picName(mCourse.getPicture()));
//        InforPopupWindow.newInstance(_mActivity, uri, "恭喜你报名成功！").showAtLocation(btJoinIn, Gravity.CENTER, 0, 0);
//        retNeedRef = !retNeedRef;
    }

    @Override
    public void showQuitCourse() {
//        isJoin = false;
//        btJoinIn.setVisibility(View.VISIBLE);
//        btCancel.setVisibility(View.GONE);
//        String uri = IconUrl.account(User.getUserId(), BitmapUtils.picName(mCourse.getPicture()));
//        InforPopupWindow.newInstance(_mActivity, uri, "取消报名成功！").showAtLocation(btJoinIn, Gravity.CENTER, 0, 0);
//        retNeedRef = !retNeedRef;
    }

    @Override
    public void showCourseDetail(EdusColleageCourse orders) {
        mCourse = orders;
        if (orders.getJoinerList() != null && orders.getJoinerList().size() > 0) {
            for (int i = 0; i < orders.getJoinerList().size(); i++) {
                if (User.getUserId().equals(orders.getJoinerList().get(i))) {
                    isJoin = true;
                    break;
                }
            }
        }
        tvCoursePlan.setText(orders.getPlan());
        tvCourseDesp.setText(orders.getContent());
        if (orders.getContact() != null) {
            tvConnectPhone.setText(getString(R.string.connect_phone_, orders.getContact().getMobile()));
            tvConnectPerson.setText(getString(R.string.connect_person_, orders.getContact().getUsername()));
            // if (!TextUtils.isEmpty(BitmapUtils.picName((ArrayList<IconModule>) orders.getContact().getPicture())))
            ImageLoaderUtil.instance().loadImage(
                    IconUrl.account(orders.getContact().getId(), BitmapUtils.picName((ArrayList<IconModule>) orders.getContact().getPicture())),
                    R.drawable.icon_def,tvConnectIcon);
//                Glide.with(this)
//                        .load(IconUrl.account(orders.getContact().getId(), BitmapUtils.picName((ArrayList<IconModule>) orders.getContact().getPicture())))
//                        .into(tvConnectIcon);
        }

        String signDateStr = StringUtils.IOS2ToUTC(orders.getStartSingnDate(), 0) + " ~ " + StringUtils.IOS2ToUTC(orders.getEndSingnDate(), 0);
        String duringDateStr = StringUtils.IOS2ToUTC(orders.getStartDate(), 0) + " ~ " + StringUtils.IOS2ToUTC(orders.getEndDate(), 0);
        String startEndTimeStr = StringUtils.IOS2ToUTC(orders.getStartCourseTime(), 1) + " ~ " + StringUtils.IOS2ToUTC(orders.getEndCourseTime(), 1);

        tvSignDate.setText(getString(R.string.course_sign_date_, signDateStr));
        tvDuringDate.setText(getString(R.string.course_during_date_, duringDateStr));
        tvStartEndTime.setText(getString(R.string.course_start_end_time_, startEndTimeStr));
        tvServiceProvider.setVisibility(View.VISIBLE);
        if (orders.getSchool() != null) {
            tvCoulleageName.setText(orders.getSchool().getName());
        }
//        if (StringUtils.IOS2DateTime(mCourse.getStartSingnDate()).isAfterNow()) {
//            btOtherStatus.setVisibility(View.VISIBLE);
//            btJoinIn.setVisibility(View.GONE);
//            btCancel.setVisibility(View.GONE);
//            btOtherStatus.setText("报名未开始");
//        } else if (StringUtils.IOS2DateTime(mCourse.getEndSingnDate()).isBeforeNow()) {
//            btOtherStatus.setVisibility(View.VISIBLE);
//            btJoinIn.setVisibility(View.GONE);
//            btCancel.setVisibility(View.GONE);
//            btOtherStatus.setText("报名已结束");
//        } else {
//            btOtherStatus.setVisibility(View.GONE);
//            if (isJoin) {
//                btJoinIn.setVisibility(View.GONE);
//                btCancel.setVisibility(View.VISIBLE);
//            } else {
//                btJoinIn.setVisibility(View.VISIBLE);
//                btCancel.setVisibility(View.GONE);
//            }
//        }
    }

    @Override
    public void onDestroy() {
        if (retNeedRef) {
            EducationClassifyFragment fragment = findFragment(EducationClassifyFragment.class);
            if (fragment != null) {
                fragment.refreshData();
            }
        }
        super.onDestroy();
    }
}

