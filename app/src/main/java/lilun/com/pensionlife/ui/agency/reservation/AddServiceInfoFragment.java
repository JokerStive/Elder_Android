package lilun.com.pensionlife.ui.agency.reservation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.bean.AgencyContactExtension;
import lilun.com.pensionlife.module.bean.Contact;
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

    @Bind(R.id.et_memo)
    EditText etMemo;


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
//    @Bind(R.id.tv_memo)
//    TextView tvMemo;
    @Bind(R.id.tv_contact_extension_name)
    TextView tvContactExtensionName;


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
        etMemo.setEnabled(canEdit);
        btnConfirm.setVisibility(canEdit ? View.VISIBLE : View.GONE);

        setInitData();
    }


    private void setInitData() {
        if (mOrder != null) {

//            etMemo.setText(mOrder.getDescription());

//            tvMemo.setText(mOrder.getDescription());

            Contact contact = mOrder.getContact();
            if (contact != null) {
                etContactName.setText(contact.getName());

                etContactMobile.setText(contact.getMobile());

                tvContactAddress.setText(contact.getAddress());

                OrganizationProduct product = mOrder.getProduct();
                if (product != null) {
                    String categoryId = product.getCategoryId();
                    if (categoryId.contains("养老机构")) {
                        llContactExtent.setVisibility(View.VISIBLE);
                        AgencyContactExtension extend = contact.getExtend();
                        if (extend != null) {
                            tvContactExtensionName.setText(extend.getReservationName());
                            tvSex.setText(extend.getSex());
                            tvRelation.setText(extend.getRelation());
                            tvBirthday.setText(extend.getBirthday());
                            tvHealthStatus.setText(extend.getHealthStatus());

                            etHealthDesc.setText(extend.getHealthyDescription());
                        }
                    }
                }
            }


        }
    }


}
