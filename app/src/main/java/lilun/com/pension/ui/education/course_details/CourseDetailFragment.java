package lilun.com.pension.ui.education.course_details;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;

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
import lilun.com.pension.module.utils.StringUtils;
import lilun.com.pension.ui.education.InforPopupWindow;
import lilun.com.pension.ui.education.classify.EducationClassifyFragment;
import lilun.com.pension.ui.education.colleage_details.ColleageDetailFragment;
import lilun.com.pension.widget.CircleImageView;
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
    @Bind(R.id.tv_start_date)
    TextView tvStartDate;
    @Bind(R.id.tv_end_date)
    TextView tvEndDate;
    @Bind(R.id.tv_course_plan)
    TextView tvCoursePlan;
    @Bind(R.id.tv_course_desp)
    TextView tvCourseDesp;
    @Bind(R.id.tv_colleage_name)
    TextView tvCoulleageName;
    @Bind(R.id.tv_service_provider)
    TextView tvServiceProvider;


    @Bind(R.id.join_in)
    Button btJoinIn;
    @Bind(R.id.cancel)
    Button btCancel;


    @OnClick({R.id.join_in, R.id.cancel, R.id.iv_back, R.id.tv_service_provider})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.join_in:
                mPresenter.joinCourse(mCourse.getId(), "");
                break;
            case R.id.cancel:
                mPresenter.quitCourse(mCourse.getId(), "");
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
        btJoinIn.setVisibility(View.GONE);
        btCancel.setVisibility(View.GONE);

        String filter = "{\"include\":[\"contact\",\"school\"]}";
        mPresenter.getCourseDetail(mCourse.getId(), filter);

    }


    @Override
    public void showJoinCourse() {
        isJoin = true;
        btJoinIn.setVisibility(View.GONE);
        btCancel.setVisibility(View.VISIBLE);

        String uri = IconUrl.account(User.getUserId(), BitmapUtils.picName(mCourse.getPicture()));
        InforPopupWindow.newInstance(_mActivity, uri, "恭喜你报名成功！").showAtLocation(btJoinIn, Gravity.CENTER,0,0);
        retNeedRef = !retNeedRef;
    }

    @Override
    public void showQuitCourse() {
        isJoin = false;
        btJoinIn.setVisibility(View.VISIBLE);
        btCancel.setVisibility(View.GONE);
        String uri = IconUrl.account(User.getUserId(), BitmapUtils.picName(mCourse.getPicture()));
        InforPopupWindow.newInstance(_mActivity, uri, "取消报名成功！").showAtLocation(btJoinIn, Gravity.CENTER,0,0);
        retNeedRef = !retNeedRef;
    }

    @Override
    public void showCourseDetail(EdusColleageCourse orders) {
        mCourse = orders;
        if (orders.getAccountIds() != null && orders.getAccountIds().size() > 0) {
            for (int i = 0; i < orders.getAccountIds().size(); i++) {
                if (User.getUserId().equals(orders.getAccountIds().get(i))) {
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
            Glide.with(_mActivity)
                    .load(IconUrl.account(User.getUserId(), null))
                    .placeholder(R.drawable.icon_def)
                    .error(R.drawable.icon_def)
                    .into(tvConnectIcon);
        }

        tvStartDate.setText(getString(R.string.course_start_date_, StringUtils.IOS2ToUTC(orders.getStartDate())));
        tvEndDate.setText(getString(R.string.course_end_date_, StringUtils.IOS2ToUTC(orders.getEndDate())));
        tvServiceProvider.setVisibility(View.VISIBLE);
        if (orders.getSchool() != null) {
            tvCoulleageName.setText(orders.getSchool().getName());
        }


        if (isJoin) {
            btJoinIn.setVisibility(View.GONE);
            btCancel.setVisibility(View.VISIBLE);
        } else {
            btJoinIn.setVisibility(View.VISIBLE);
            btCancel.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        if(retNeedRef){
            EducationClassifyFragment fragment = findFragment(EducationClassifyFragment.class);
            if(fragment !=null){
                fragment.refreshData();
            }
        }
        super.onDestroy();
    }
}

