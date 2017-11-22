package lilun.com.pensionlife.ui.agency.reservation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;

import butterknife.Bind;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.bean.Contact;
import lilun.com.pensionlife.module.bean.ContactExtendKey;
import lilun.com.pensionlife.module.bean.OrganizationProduct;
import lilun.com.pensionlife.module.bean.ProductOrder;
import lilun.com.pensionlife.widget.NormalTitleBar;

/**
 * 新增预约信息列表V
 *
 * @author yk
 *         create at 2017/3/29 18:47
 *         email : yk_developer@163.com
 */
public class AddServiceInfoFragment extends BaseFragment {

    @Bind(R.id.et_occupant_name)
    EditText etContactName;

    @Bind(R.id.tv_sex)
    TextView tvSex;

    @Bind(R.id.tv_birthday)
    TextView tvBirthday;

    @Bind(R.id.tv_health_status)
    TextView tvHealthStatus;

    @Bind(R.id.tv_relation)
    TextView tvRelation;


    @Bind(R.id.et_health_desc)
    EditText etHealthDesc;


    @Bind(R.id.et_reservation_phone)
    EditText etContactMobile;

    @Bind(R.id.titleBar)
    NormalTitleBar titleBar;

    @Bind(R.id.btn_confirm)
    Button btnConfirm;
    @Bind(R.id.tv_contact_address)
    TextView tvContactAddress;
    @Bind(R.id.ll_contact_extent)
    LinearLayout llContactExtent;
    @Bind(R.id.contact_course_extend)
    LinearLayout llContactCourseExtend;
    @Bind(R.id.tv_contact_extension_name)
    TextView tvContactExtensionName;
    @Bind(R.id.tv_contact_extension_sex)
    TextView tvContactExtensionSex;
    @Bind(R.id.tv_contact_extension_birthday)
    TextView tvContactExtensionBirthday;
    @Bind(R.id.tv_contact_extension_health_status)
    TextView tvContactExtensionHealthStatus;
    @Bind(R.id.tv_contact_extension_politic_status)
    TextView tvContactExtensionPoliticStatus;
    @Bind(R.id.et_contact_post_work)
    EditText etContactPostWork;
    @Bind(R.id.et_contact_emergency_name)
    EditText etContactEmergencyName;
    @Bind(R.id.et_emergency_phone)
    EditText etEmergencyPhone;

    @Bind(R.id.rl_extend)
    RelativeLayout rlExtend;


    private ProductOrder mOrder;
    private boolean canEdit;

//    public static AddServiceInfoFragment newInstance(String productCategoryId) {
//        AddServiceInfoFragment fragment = new AddServiceInfoFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString("productCategoryId", productCategoryId);
//        fragment.setArguments(bundle);
//        return fragment;
//    }


    public static AddServiceInfoFragment newInstance(ProductOrder order, boolean canEdit) {
        AddServiceInfoFragment fragment = new AddServiceInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("canEdit", canEdit);
        bundle.putSerializable("order", order);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected void getTransferData(Bundle arguments) {
        canEdit = arguments.getBoolean("canEdit", true);
        mOrder = (ProductOrder) arguments.getSerializable("order");
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_add_reservation_info;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        titleBar.setOnBackClickListener(this::pop);

        tvContactAddress.setEnabled(canEdit);
        etContactName.setEnabled(canEdit);
        etHealthDesc.setEnabled(canEdit);
        etContactMobile.setEnabled(canEdit);
        btnConfirm.setVisibility(canEdit ? View.VISIBLE : View.GONE);

        setInitData();
    }


    private void setInitData() {
        if (mOrder != null) {
            rlExtend.setVisibility(View.GONE);
//            etMemo.setText(mOrder.getDescription());

//            tvMemo.setText(mOrder.getDescription());

            Contact contact = mOrder.getContact();
            if (contact != null) {
                etContactName.setText(contact.getName());

                etContactMobile.setText(contact.getMobile());

                tvContactAddress.setText(contact.getAddress());

                OrganizationProduct product = mOrder.getProduct();
                if (product != null) {
                    String categoryId = product.getOrgCategoryId();
                    HashMap<String, String> extend = contact.getExtend();
                    if (extend == null) {
                        return;
                    }
                    if (categoryId.contains("养老机构")) {
                        rlExtend.setVisibility(View.VISIBLE);
                        llContactExtent.setVisibility(View.VISIBLE);
                        llContactCourseExtend.setVisibility(View.GONE);

                        tvContactExtensionName.setText(extend.get(ContactExtendKey.reservationName));
                        tvRelation.setText(extend.get(ContactExtendKey.relation));
                        tvSex.setText(extend.get(ContactExtendKey.sex));
                        tvBirthday.setText(extend.get(ContactExtendKey.birthday));
                        tvHealthStatus.setText(extend.get(ContactExtendKey.healthStatus));
                        etHealthDesc.setText(extend.get(ContactExtendKey.healthyDescription));
                    } else if (categoryId.contains("教育服务")) {
                        rlExtend.setVisibility(View.VISIBLE);
                        llContactExtent.setVisibility(View.GONE);
                        llContactCourseExtend.setVisibility(View.VISIBLE);

                        tvContactExtensionSex.setText(extend.get(ContactExtendKey.sex));
                        tvContactExtensionBirthday.setText(extend.get(ContactExtendKey.birthday));
                        tvContactExtensionHealthStatus.setText(extend.get(ContactExtendKey.healthStatus));
                        tvContactExtensionPoliticStatus.setText(extend.get(ContactExtendKey.politicStatus));
                        etContactPostWork.setText(extend.get(ContactExtendKey.postOfWorked));
                        etEmergencyPhone.setText(extend.get(ContactExtendKey.contactNumber));
                        etContactEmergencyName.setText(extend.get(ContactExtendKey.emergencyContact));
                    }
                }
            }


        }
    }


}
