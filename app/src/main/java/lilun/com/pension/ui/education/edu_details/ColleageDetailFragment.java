package lilun.com.pension.ui.education.edu_details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import lilun.com.pension.R;
import lilun.com.pension.app.IconUrl;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.adapter.ColleageCourseAdapter;
import lilun.com.pension.module.bean.EdusColleageCourse;
import lilun.com.pension.module.bean.ElderEdus;
import lilun.com.pension.module.bean.IconModule;
import lilun.com.pension.module.utils.BitmapUtils;
import lilun.com.pension.module.utils.Preconditions;
import lilun.com.pension.ui.education.course_list.CourseListFragment;
import lilun.com.pension.widget.CircleImageView;
import lilun.com.pension.widget.slider.BannerPager;

/**
 * 大学详情
 * Created by zp on 2017/2/23.
 */

public class ColleageDetailFragment extends BaseFragment<ColleageDetailContract.Presenter>
        implements ColleageDetailContract.View {

    ElderEdus mColleage;
    ColleageCourseAdapter mCourseAdapter;


    @Bind(R.id.bp_colleage_icon)
    BannerPager bgColleageIcon;
    @Bind(R.id.tv_course_name)
    TextView tvColleageName;
    @Bind(R.id.tv_connect_phone)
    TextView tvConnectPhone;
    @Bind(R.id.tv_connect_person)
    TextView tvConnectPerson;
    @Bind(R.id.cig_connect_icon)
    CircleImageView tvConnectIcon;
    @Bind(R.id.tv_colleage_descript)
    TextView tvColleageDescript;
    @Bind(R.id.tv_colleage_addr)
    TextView tvColleageAddr;


    @OnClick({R.id.join_in, R.id.tv_course_list, R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.join_in:
            case R.id.tv_course_list:
                start(CourseListFragment.newInstance(mColleage));
                break;
            case R.id.iv_back:
                pop();
                break;
        }
    }


    public static ColleageDetailFragment newInstance(ElderEdus colleage) {
        ColleageDetailFragment fragment = new ColleageDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("mElderEdusColleage", colleage);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void getTransferData(Bundle arguments) {
        mColleage = (ElderEdus) arguments.getSerializable("mElderEdusColleage");
        Preconditions.checkNull(mColleage);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ColleageDetailPresenter();
        mPresenter.bindView(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_colleage_detail;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        //显示图片
        List<String> urls = new ArrayList<>();
        if (mColleage.getPicture() != null) {
            for (IconModule iconModule : mColleage.getPicture()) {
                String url = IconUrl.organizationEdus(mColleage.getId(), iconModule.getFileName());
                urls.add(url);
            }
        } else {
            String url = IconUrl.organizationEdus(mColleage.getId(), null);
            urls.add(url);
        }
        bgColleageIcon.setData(urls);
        tvColleageName.setText(mColleage.getTitle());
        if (mColleage.getContact() != null) {
            tvConnectPhone.setText(getString(R.string.connect_phone_, mColleage.getContact().getMobile()));
            tvConnectPerson.setText(getString(R.string.connect_person_, mColleage.getContact().getUsername()));
            Glide.with(_mActivity)
                    .load(IconUrl.account(mColleage.getOrganizationId(), BitmapUtils.picName((ArrayList<IconModule>) mColleage.getPicture())))
                    .placeholder(R.drawable.icon_def)
                    .error(R.drawable.icon_def)
                    .into(tvConnectIcon);
        }
        tvColleageDescript.setText(mColleage.getDescription());
        tvColleageAddr.setText(mColleage.getAddress());
    }

    @Override
    public void showColleageCouseList(List<EdusColleageCourse> courseList) {

    }

}
