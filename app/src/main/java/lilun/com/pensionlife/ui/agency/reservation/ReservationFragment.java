package lilun.com.pensionlife.ui.agency.reservation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DateTimePicker;
import cn.qqtheme.framework.picker.WheelPicker;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.app.Config;
import lilun.com.pensionlife.app.User;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.bean.Contact;
import lilun.com.pensionlife.module.bean.ProductOrder;
import lilun.com.pensionlife.module.utils.Preconditions;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.module.utils.ToastHelper;
import lilun.com.pensionlife.net.NetHelper;
import lilun.com.pensionlife.net.RxSubscriber;
import lilun.com.pensionlife.ui.residential.detail.OrderDetailActivity;
import lilun.com.pensionlife.widget.NormalDialog;
import lilun.com.pensionlife.widget.NormalTitleBar;

/**
 * @author yk
 *         create at 2017/4/11 16:11
 *         email : yk_developer@163.com
 */
public class ReservationFragment extends BaseFragment {
    @Bind(R.id.titleBar)
    NormalTitleBar titleBar;

    @Bind(R.id.tv_name)
    TextView tvName;

    @Bind(R.id.tv_health_status)
    TextView tvHealthStatus;

    @Bind(R.id.tv_phone)
    TextView tvPhone;

    @Bind(R.id.tv_health_desc)
    TextView tvHealthDesc;
    @Bind(R.id.tv_reservation_time)
    TextView tvReservationTime;
    private String productCategoryId;
    private String productId;

//    @Bind(R.id.rl_check_contact)
//    RelativeLayout rlCheckContact;

//    @Bind(R.id.rl_reservation_time)
//    RelativeLayout rlReservationTime;

//    @Bind(R.id.btn_confirm)
//    Button btnConfirm;


    private String contactId;
    private int size = 17;
    private int selectColor = App.context.getResources().getColor(R.color.red);
    private String reservationTime;
    private Contact contact;
    public static int requestCode = 123;
    public static int resultCode = 321;


    public static ReservationFragment newInstance(String productCategoryId, String productId, Contact contact) {
        ReservationFragment fragment = new ReservationFragment();
        Bundle bundle = new Bundle();
        bundle.putString("productCategoryId", productCategoryId);
        bundle.putString("productId", productId);
        bundle.putSerializable("contact", contact);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected void getTransferData(Bundle arguments) {
        productCategoryId = arguments.getString("productCategoryId");
        productId = arguments.getString("productId");
        contact = (Contact) arguments.getSerializable("contact");
        Preconditions.checkNull(productCategoryId);
        Preconditions.checkNull(productId);
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_reservation;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        titleBar.setOnBackClickListener(() -> _mActivity.finish());
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        if (contact == null) {
            getDefContact();
        } else {
            showContact(contact);
        }
    }


    @OnClick({R.id.btn_confirm, R.id.rl_check_contact, R.id.rl_reservation_time})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:
                if (!TextUtils.isEmpty(contactId)) {
                    reservation(productId, contactId, reservationTime);
                }
                break;

            case R.id.rl_check_contact:
                startForResult(ServiceUserInfoFragment.newInstance(productCategoryId, productId), requestCode);
                break;


            case R.id.rl_reservation_time:
                chooseReservationTime();
                break;
        }
    }


    private void getDefContact() {
        String filter = "{\"where\":{\"categoryId\":\"" + productCategoryId + "\",\"creatorId\":\"" + User.getUserId() + "\",\"index\":\"1\"}}";
        NetHelper.getApi().getContacts(filter)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<Contact>>(_mActivity) {
                    @Override
                    public void _next(List<Contact> contacts) {
                        if (contacts.size() > 0) {
                            showContact(contacts.get(0));
                        }
                    }
                });
    }


    /**
     * 现实个人资料
     */
    private void showContact(Contact contact) {
        contactId = contact.getId();
        tvName.setText(contact.getName());
        tvPhone.setText(contact.getMobile());

        if (productCategoryId.contains(Config.agency_product_categoryId)&& contact.getExtend()!=null) {
            tvHealthStatus.setText(contact.getExtend().get("healthyStatus"));
            tvHealthDesc.setText(contact.getExtend().get("healthyDescription"));
        } else {
            tvHealthDesc.setText(contact.getAddress());
        }
    }

    /**
     * 预约服务
     */
    private void reservation(String productId, String contactId, String reservationTime) {
        if (TextUtils.isEmpty(reservationTime)) {
            ToastHelper.get().showWareShort("请选择预约时间");
            return;
        }

        new NormalDialog().createNormal(_mActivity, getString(R.string.reservation_desc), () -> {
            NetHelper.getApi()
                    .createOrder(productId, contactId, reservationTime)
                    .compose(RxUtils.handleResult())
                    .compose(RxUtils.applySchedule())
                    .subscribe(new RxSubscriber<ProductOrder>() {
                        @Override
                        public void _next(ProductOrder order) {
                            Intent intent = new Intent(_mActivity, OrderDetailActivity.class);
                            intent.putExtra("orderId", order.getId());
                            startActivity(intent);
                            _mActivity.setResult(resultCode, null);
                            _mActivity.finish();
                        }
                    });
        });

    }

    /**
     * 延期时，选择延期时间
     */
    private void chooseReservationTime() {
        int month = Calendar.getInstance(Locale.CHINA).get(Calendar.MONTH) + 1;
        int day = Calendar.getInstance(Locale.CHINA).get(Calendar.DAY_OF_MONTH);
        DateTimePicker picker = new DateTimePicker(_mActivity, DateTimePicker.MONTH_DAY, DateTimePicker.NONE);
        setPickerConfig(picker);
        if (App.widthDP > 820) {
            picker.setTextSize(12 *2);
            picker.setCancelTextSize(12 *2);
            picker.setSubmitTextSize(12 *2);
            picker.setTopPadding(15*3);
            picker.setTopHeight(40*2);
        }
        picker.setDateRangeStart(month, day);
        picker.setOnDateTimePickListener((DateTimePicker.OnMonthDayTimePickListener) (month1, day1, hour, minute) -> {
            int year = Calendar.getInstance().get(Calendar.YEAR);
            reservationTime = year + "-" + month1 + "-" + day1;
            tvReservationTime.setText(reservationTime);
//            rbDelay.setText(delayTime);
        });
        picker.show();
    }


    /**
     * 选择器的配置
     */
    private void setPickerConfig(WheelPicker picker) {
        picker.setTextSize(size);
        picker.setCancelTextSize(size);
        picker.setSubmitTextSize(size);
        picker.setLineColor(selectColor);
        picker.setTextColor(selectColor);
    }


    @Override
    protected void onFragmentResult(int reCode, int resultCode, Bundle data) {
        if (reCode == requestCode && resultCode == 0 && data != null) {
            Serializable serializable = data.getSerializable("contact");
            if (contact != null) {
                Contact contact = (Contact) serializable;
                showContact(contact);
            }
        }
    }
}
