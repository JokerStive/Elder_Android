package lilun.com.pension.ui.education.edu_details;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import lilun.com.pension.R;
import lilun.com.pension.app.IconUrl;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.adapter.ColleageCourseAdapter;
import lilun.com.pension.module.bean.EdusColleageCourse;
import lilun.com.pension.module.bean.ElderEdus;
import lilun.com.pension.module.bean.IconModule;
import lilun.com.pension.module.utils.BitmapUtils;
import lilun.com.pension.module.utils.Preconditions;
import lilun.com.pension.widget.ElderModuleClassifyDecoration;
import lilun.com.pension.widget.NormalTitleBar;

/**
 * 大学详情
 * Created by zp on 2017/2/23.
 */

public class ColleageDetailFragment extends BaseFragment<ColleageDetailContract.Presenter>
        implements ColleageDetailContract.View {

    ElderEdus mColleage;
    ColleageCourseAdapter mCourseAdapter;

    @Bind(R.id.title_bar)
    NormalTitleBar titleBar;
    @Bind(R.id.ig_colleage_icon)
    ImageView igColleageIcon;
    @Bind(R.id.tv_colleage_name)
    TextView tvColleageName;
    @Bind(R.id.tv_connect_phone)
    TextView tvConnectPhone;
    @Bind(R.id.tv_connect_person)
    TextView tvConnectPerson;
    @Bind(R.id.tv_colleage_descript)
    TextView tvColleageDescript;
    @Bind(R.id.tv_colleage_addr)
    TextView tvColleageAddr;
    @Bind(R.id.rv_colleage_course)
    RecyclerView mRecyclerView;

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
        titleBar.setTitle(mColleage.getTitle());
        titleBar.setOnBackClickListener(new NormalTitleBar.OnBackClickListener() {
            @Override
            public void onBackClick() {
                pop();
            }
        });
        Glide.with(_mActivity)
                .load(IconUrl.organizationEdus(mColleage.getId(), BitmapUtils.picName((ArrayList<IconModule>) mColleage.getPicture())))
                .into(igColleageIcon);
        tvColleageName.setText(mColleage.getTitle());
        tvConnectPhone.setText(getString(R.string.connect_phone_, mColleage.getMobile()));
        tvConnectPerson.setText(getString(R.string.connect_person_, "刘先生"));
        tvColleageDescript.setText(mColleage.getDescription());
        tvColleageAddr.setText(mColleage.getAddress());

        mRecyclerView.setLayoutManager(new GridLayoutManager(_mActivity,3));
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.addItemDecoration(new ElderModuleClassifyDecoration());
        mPresenter.getColleageCouse(mColleage.getId(), "", 0);
    }

    @Override
    public void showColleageCouseList(List<EdusColleageCourse> courseList) {
        if (mCourseAdapter == null) {
            mCourseAdapter = new ColleageCourseAdapter(this, courseList);
            mRecyclerView.setAdapter(mCourseAdapter);
        }
    }

}
