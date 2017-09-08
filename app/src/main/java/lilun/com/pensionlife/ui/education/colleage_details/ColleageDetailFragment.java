package lilun.com.pensionlife.ui.education.colleage_details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.IconUrl;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.bean.EdusColleageCourse;
import lilun.com.pensionlife.module.bean.OrganizationEdu;
import lilun.com.pensionlife.module.bean.IconModule;
import lilun.com.pensionlife.module.utils.BitmapUtils;
import lilun.com.pensionlife.module.utils.Preconditions;
import lilun.com.pensionlife.module.utils.StartOtherUtils;
import lilun.com.pensionlife.ui.education.course_list.CourseListFragment;
import lilun.com.pensionlife.widget.CircleImageView;
import lilun.com.pensionlife.widget.image_loader.ImageLoaderUtil;
import lilun.com.pensionlife.widget.slider.BannerPager;

/**
 * 大学详情
 * Created by zp on 2017/2/23.
 */

public class ColleageDetailFragment extends BaseFragment<ColleageDetailContract.Presenter>
        implements ColleageDetailContract.View {

    OrganizationEdu mColleage;


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


    @OnClick({R.id.join_in, R.id.tv_course_list, R.id.tv_connect_phone, R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.join_in:
            case R.id.tv_course_list:
                start(CourseListFragment.newInstance(mColleage));
                break;
            case R.id.tv_connect_phone:
                if (mColleage != null && mColleage.getContact() != null)
                    StartOtherUtils.cellPhone(_mActivity, mColleage.getContact().getMobile());
                break;
            case R.id.iv_back:
                pop();
                break;
        }
    }


    public static ColleageDetailFragment newInstance(OrganizationEdu colleage) {
        ColleageDetailFragment fragment = new ColleageDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("mElderEdusColleage", colleage);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void getTransferData(Bundle arguments) {
        mColleage = (OrganizationEdu) arguments.getSerializable("mElderEdusColleage");
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
        if (mColleage.getImage() != null) {
            for (IconModule iconModule : mColleage.getImage()) {
                String url = IconUrl.organizationEdus(mColleage.getId(), iconModule.getFileName());
                urls.add(url);
            }
        } else {
            String url = IconUrl.organizationEdus(mColleage.getId(), null);
            urls.add(url);
        }
        bgColleageIcon.setData(urls);
        tvColleageName.setText(mColleage.getName());
        if (mColleage.getContact() != null) {
            tvConnectPhone.setText(getString(R.string.connect_phone_, mColleage.getContact().getMobile()));
            tvConnectPerson.setText(getString(R.string.connect_person_, mColleage.getContact().getUsername()));
            //if(!TextUtils.isEmpty(BitmapUtils.picName((ArrayList<IconModule>) mColleage.getContact().getImage())))
            ImageLoaderUtil.instance().loadImage(
                    IconUrl.moduleIconUrl(IconUrl.Accounts, mColleage.getContact().getId(), BitmapUtils.picName((ArrayList<IconModule>) mColleage.getContact().getImage())),
                    R.drawable.icon_def, tvConnectIcon);
//            Glide.with(this)
//                    .load(IconUrl.account(mColleage.getContact().getId(), BitmapUtils.picName((ArrayList<IconModule>) mColleage.getContact().getImage())))
//                    .error(R.drawable.icon_def)
//                    .placeholder(R.drawable.icon_def)
//                    .into(tvConnectIcon);
        }
        tvColleageDescript.setText(mColleage.getDescription());
        tvColleageAddr.setText(mColleage.getAddress());
    }

    @Override
    public void showColleageCouseList(List<EdusColleageCourse> courseList) {

    }

}
