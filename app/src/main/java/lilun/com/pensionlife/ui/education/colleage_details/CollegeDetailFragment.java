package lilun.com.pensionlife.ui.education.colleage_details;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.bean.Organization;
import lilun.com.pensionlife.module.bean.Provider;
import lilun.com.pensionlife.module.utils.Preconditions;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.module.utils.ToastHelper;
import lilun.com.pensionlife.net.NetHelper;
import lilun.com.pensionlife.net.RxSubscriber;
import lilun.com.pensionlife.widget.NormalDialog;
import lilun.com.pensionlife.widget.NormalTitleBar;

/**
 * 大学简介
 *
 * @author yk
 *         create at 2017/9/21 9:39
 *         email : yk_developer@163.com
 */
public class CollegeDetailFragment extends BaseFragment {
    @Bind(R.id.titleBar)
    NormalTitleBar titleBar;
    @Bind(R.id.wb_college_context)
    WebView wbCollegeContext;
    @Bind(R.id.tv_college_address)
    TextView tvCollegeAddress;
    @Bind(R.id.tv_college_mobile)
    TextView tvCollegeMobile;
    @Bind(R.id.tv_college_phone)
    TextView tvCollegePhone;
    private String mOrganizationId;
    private String clickMobile;
    private String mobile;
    private String phone;


    public static CollegeDetailFragment newInstance(String organizationId) {
        CollegeDetailFragment fragment = new CollegeDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("organizationId", organizationId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void getTransferData(Bundle arguments) {
        mOrganizationId = arguments.getString("organizationId");
        Preconditions.checkNull(mOrganizationId);
    }


    @Override
    protected void initPresenter() {
        getCollegeDetail();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_college_detail;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        titleBar.setOnBackClickListener(this::pop);
    }


    private void getCollegeDetail() {
        String filter = "{\"include\":\"extension\"}";
        NetHelper.getApi().getOrganizationById(mOrganizationId, filter)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<Organization>(_mActivity) {
                    @Override
                    public void _next(Organization provider) {
                        showCollege(provider);
                    }
                });
    }

    private void showCollege(Organization college) {
        titleBar.setTitle(college.getName());

        Provider extension = college.getExtension();
        if (extension != null) {
            String context = extension.getContext();
            String address = extension.getAddress();
            mobile = extension.getMobile();
            phone = extension.getPhone();


            //显示简介
            if (!TextUtils.isEmpty(context)) {
                wbCollegeContext.loadDataWithBaseURL(null, context, "text/html", "utf-8", null);
            }

            //地址
            tvCollegeAddress.setText("学校地址：" + StringUtils.filterNull(address));

            //电话
            String mobileDes = "手机号码: <font color='#17c5b4'>" + formatMobile(mobile) + "</font>";
            String phoneDes = "座机电话: <font color='#17c5b4'>" + formatMobile(phone) + "</font>";
            tvCollegePhone.setText(Html.fromHtml(phoneDes));
            tvCollegeMobile.setText(Html.fromHtml(mobileDes));

        }
    }


    private String formatMobile(String mobile){
       return TextUtils.isEmpty(mobile)?"暂未提供":mobile;}

    @OnClick({R.id.tv_college_mobile, R.id.tv_college_phone})
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_college_mobile:
                connectProvider(1);
                break;

            case R.id.tv_college_phone:
                connectProvider(2);
                break;

        }
    }



    private void connectProvider(int flag) {
        switch (flag) {
            case 1:
                clickMobile = mobile;
                break;
            case 2:
                clickMobile = phone;
                break;
        }
        call();
    }


    private void call() {
        if (!TextUtils.isEmpty(clickMobile)) {
            boolean hasPermission = hasPermission(Manifest.permission.CALL_PHONE);
            if (hasPermission) {
                callMobile();
            } else {
                requestPermission(Manifest.permission.CALL_PHONE, 0X11);
            }
        } else {
            ToastHelper.get().showShort("未提供电话");
        }
    }

    private void callMobile() {
        new NormalDialog().createNormal(_mActivity, "是否联系：" + clickMobile, () -> {
            Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + clickMobile));
            startActivity(intent);
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0x11) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                ToastHelper.get().showShort("请给予权限");
            } else {
                callMobile();
            }
        }
    }

}
