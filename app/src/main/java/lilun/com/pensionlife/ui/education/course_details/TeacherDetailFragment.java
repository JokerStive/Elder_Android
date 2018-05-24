package lilun.com.pensionlife.ui.education.course_details;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.bean.OrganizationAccount;
import lilun.com.pensionlife.module.bean.OrganizationAccountProfile;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.net.NetHelper;
import lilun.com.pensionlife.net.RxSubscriber;
import lilun.com.pensionlife.widget.NormalTitleBar;
import lilun.com.pensionlife.widget.image_loader.ImageLoaderUtil;

/**
 * 老师简介
 */
public class TeacherDetailFragment extends BaseFragment {
    @Bind(R.id.titleBar)
    NormalTitleBar titleBar;
    @Bind(R.id.iv_teacher_icon)
    ImageView ivTeacherIcon;
    @Bind(R.id.tv_teacher_name)
    TextView tvTeacherName;
    @Bind(R.id.tv_teacher_desc)
    TextView tvTeacherDesc;
    private String mOrganizationAccountId;


    public static TeacherDetailFragment newInstance(String organizationAccountId) {
        TeacherDetailFragment fragment = new TeacherDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("organizationAccountId", organizationAccountId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void getTransferData(Bundle arguments) {
        super.getTransferData(arguments);
        mOrganizationAccountId = arguments.getString("organizationAccountId");
    }

    @Override
    protected void initPresenter() {
        getTeacher();
    }

    private void getTeacher() {
        String filter = "{\"include\":\"organizationAccountProfile\",\"where\":{\"visible\":0}}";
        NetHelper.getApi().getOrganizationAccount(mOrganizationAccountId, filter)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<OrganizationAccount>() {
                    @Override
                    public void _next(OrganizationAccount teacher) {
                        showTeacher(teacher);
                    }
                });
    }

    private void showTeacher(OrganizationAccount teacher) {
        String title = teacher.getName() + "的个人简介";
        tvTeacherName.setText(title);
        OrganizationAccountProfile profile = teacher.getOrganizationAccountProfile();
        if (profile != null) {
            ImageLoaderUtil.instance().loadImage(profile.getImage(), ivTeacherIcon);
            int contextType = profile.getContextType();
            String context = profile.getContext();
            if (TextUtils.isEmpty(context)){return;}
            if (contextType==0) {
                tvTeacherDesc.setText(context);
            } else if (contextType==2) {
                tvTeacherDesc.setText(Html.fromHtml(context));
            }
        }

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_teacher_detail;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        titleBar.setOnBackClickListener(this::pop);
    }
}
