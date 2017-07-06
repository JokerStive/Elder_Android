package lilun.com.pensionlife.ui.tourism.info;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.Event;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.bean.Contact;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.module.utils.ToastHelper;
import lilun.com.pensionlife.net.NetHelper;
import lilun.com.pensionlife.net.RxSubscriber;
import lilun.com.pensionlife.ui.agency.reservation.ReservationActivity;
import lilun.com.pensionlife.ui.agency.reservation.ReservationFragment;
import lilun.com.pensionlife.widget.NormalTitleBar;

public class AddTourismInfoFragment extends BaseFragment {
    @Bind(R.id.titleBar)
    NormalTitleBar titleBar;
    @Bind(R.id.et_occupant_name)
    EditText etName;
    @Bind(R.id.et_reservation_phone)
    EditText etPhone;
    @Bind(R.id.et_address)
    EditText etAddress;
    @Bind(R.id.btn_confirm)
    Button btnConfirm;
    private String productCategoryId;
    private Contact mContact;
    private String productId;
    private String contactId;
    private boolean canEdit;

    public static AddTourismInfoFragment newInstance(String productCategoryId) {
        AddTourismInfoFragment fragment = new AddTourismInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("productCategoryId", productCategoryId);
        fragment.setArguments(bundle);
        return fragment;
    }


    public static AddTourismInfoFragment newInstance(String contactId,boolean canEdit) {
        AddTourismInfoFragment fragment = new AddTourismInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("canEdit", canEdit);
        bundle.putString("contactId", contactId);
        fragment.setArguments(bundle);
        return fragment;
    }


    public static AddTourismInfoFragment newInstance(Contact contact,boolean canEdit) {
        AddTourismInfoFragment fragment = new AddTourismInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("canEdit", canEdit);
        bundle.putSerializable("contact", contact);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected void getTransferData(Bundle arguments) {
        productCategoryId = arguments.getString("productCategoryId");
        contactId = arguments.getString("contactId");
        canEdit = arguments.getBoolean("canEdit", true);
        mContact = (Contact) arguments.getSerializable("contact");
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }


    @Override
    protected void initPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_add_tourism_info;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        titleBar.setOnBackClickListener(this::pop);
        btnConfirm.setOnClickListener(v -> savePersonalInfo());

        etName.setEnabled(canEdit);
        etPhone.setEnabled(canEdit);
        etAddress.setEnabled(canEdit);
        btnConfirm.setVisibility(canEdit? View.VISIBLE:View.GONE);

        setInitData();
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        if (!TextUtils.isEmpty(contactId)) {
            subscription.add(NetHelper.getApi().getContact(contactId)
                    .compose(RxUtils.handleResult())
                    .compose(RxUtils.applySchedule())
                    .subscribe(new RxSubscriber<Contact>(_mActivity) {
                        @Override
                        public void _next(Contact contact) {
                            mContact=contact;
                            setInitData();
                        }
                    }));
        }
    }

    private void setInitData() {
        if (mContact != null) {
            etName.setText(mContact.getName());
            etPhone.setText(mContact.getMobile());
            etAddress.setText(mContact.getAddress());
        }
    }


    private void savePersonalInfo() {
        String name = etName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            ToastHelper.get().showWareShort("请输入姓名");
            return;
        }


        String phone = etPhone.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            ToastHelper.get().showWareShort("请输入联系电话");
            if (!StringUtils.isMobileNo(phone)){
                ToastHelper.get().showWareShort(getString(R.string.mobile_format_wrong));
            }
            return;
        }


        String address = etAddress.getText().toString();
        if (TextUtils.isEmpty(address)) {
            ToastHelper.get().showWareShort("请输入地址");
            return;
        }

        Contact contact = new Contact();
        contact.setName(name);
        contact.setMobile(phone);
        contact.setAddress(address);
        contact.setCategoryId(productCategoryId);

        if (this.mContact != null) {
            putContact(contact);
        } else {
            postContact(contact);
        }

    }


    /**
     * 添加个人信息
     */
    private void postContact(Contact contact) {
        subscription.add(NetHelper.getApi().newContact(contact)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<Contact>(_mActivity) {
                    @Override
                    public void _next(Contact contact) {
                        if (!TextUtils.isEmpty(productId)) {
                            statReservation(contact);
//                            start(ReservationFragment.newInstance(productCategoryId, productId, contact));
                        } else {
                            EventBus.getDefault().post(new Event.RefreshContract());
                        }
                        pop();
                    }
                }));
    }



    private void statReservation(Contact contact) {
        Intent intent = new Intent(_mActivity,ReservationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("contact",contact);
        bundle.putString("productCategoryId",productCategoryId);
        bundle.putString("productId",productId);
        intent.putExtras(bundle);
        startActivityForResult(intent,ReservationFragment.requestCode);
    }

    /**
     * 更新个人资料
     */
    private void putContact(Contact contact) {
        NetHelper.getApi().putContact(mContact.getId(), contact)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<Object>(_mActivity) {
                    @Override
                    public void _next(Object o) {
                        EventBus.getDefault().post(new Event.RefreshContract());
                        pop();
                    }
                });
    }


}