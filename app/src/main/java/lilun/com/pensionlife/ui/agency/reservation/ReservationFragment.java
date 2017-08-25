package lilun.com.pensionlife.ui.agency.reservation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DateTimePicker;
import cn.qqtheme.framework.picker.WheelPicker;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.app.IconUrl;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.bean.AgencyContactExtension;
import lilun.com.pensionlife.module.bean.Contact;
import lilun.com.pensionlife.module.bean.OrganizationProduct;
import lilun.com.pensionlife.module.bean.ProductOrder;
import lilun.com.pensionlife.module.utils.Preconditions;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.module.utils.ToastHelper;
import lilun.com.pensionlife.net.NetHelper;
import lilun.com.pensionlife.net.RxSubscriber;
import lilun.com.pensionlife.ui.agency.detail.ProductDetailFragment;
import lilun.com.pensionlife.widget.NormalTitleBar;
import lilun.com.pensionlife.widget.image_loader.ImageLoaderUtil;
import retrofit2.Response;
import rx.Observable;

/**
 * @author yk
 *         create at 2017/4/11 16:11
 *         email : yk_developer@163.com
 */
public class ReservationFragment extends BaseFragment {
    @Bind(R.id.titleBar)
    NormalTitleBar titleBar;
    @Bind(R.id.iv_product_image)
    ImageView ivProductImage;
    @Bind(R.id.tv_product_title)
    TextView tvProductTitle;
    @Bind(R.id.tv_product_area)
    TextView tvProductArea;
    @Bind(R.id.tv_product_price)
    TextView tvProductPrice;
    @Bind(R.id.tv_contact_name)
    TextView tvContactName;
    @Bind(R.id.tv_contact_mobile)
    TextView tvContactMobile;
    @Bind(R.id.tv_contact_address)
    EditText tvContactAddress;
    @Bind(R.id.tv_contact_extension_relation)
    TextView tvContactExtensionRelation;
    @Bind(R.id.et_contact_extension_name)
    EditText tvContactExtensionName;
    @Bind(R.id.tv_contact_extension_sex)
    TextView tvContactExtensionSex;
    @Bind(R.id.tv_contact_extension_birthday)
    TextView tvContactExtensionBirthday;
    @Bind(R.id.tv_contact_extension_health_status)
    TextView tvContactExtensionHealthStatus;
    @Bind(R.id.tv_contact_extension_health_desc)
    EditText tvContactExtensionHealthDesc;
    @Bind(R.id.tv_order_time)
    TextView tvOrderTime;
    @Bind(R.id.tv_price)
    TextView tvPrice;
    @Bind(R.id.tv_order_memo)
    EditText tvOrderMemo;

    @Bind(R.id.agency_contact_extension)
    LinearLayout llAgancyExtension;
    @Bind(R.id.tv_health_desc_count)
    TextView tvHealthDescCount;
    @Bind(R.id.tv_memo_count)
    TextView tvMemoCount;

    @Bind(R.id.tv_reservation)
    TextView tvReservation;


    private String[] optionSex = App.context.getResources().getStringArray(R.array.personal_info_sex);
    private String[] optionHealthStatus = App.context.getResources().getStringArray(R.array.personal_info_health_status);
    private String[] optionRelation = App.context.getResources().getStringArray(R.array.personal_info_relation);

    private int size = 17;
    private int selectColor = 0xff0090f9;
    private String reservationTime;
    private Contact mContact;
    public static int requestCode = 123;
    public static int resultCode = 321;
    private String mProductId;
    private OrganizationProduct mProduct;
//    private String mOrderMobile;


    public static ReservationFragment newInstance(String productId, Contact contact) {
        ReservationFragment fragment = new ReservationFragment();
        Bundle bundle = new Bundle();
        bundle.putString("mProductId", productId);
        bundle.putSerializable("contact", contact);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected void getTransferData(Bundle arguments) {
        mContact = (Contact) arguments.getSerializable("contact");
        mProductId = arguments.getString("mProductId");
        Preconditions.checkNull(mProductId);
        Preconditions.checkNull(mContact);
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

        titleBar.setOnBackClickListener(this::pop);

        tvContactExtensionHealthDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvHealthDescCount.setText(count + "/60");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        tvOrderMemo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvMemoCount.setText(count + "/60");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        getProduct();
    }


    @OnClick({R.id.tv_contact_extension_relation, R.id.tv_contact_extension_sex, R.id.tv_contact_extension_birthday,
            R.id.tv_contact_extension_health_status, R.id.tv_order_time, R.id.tv_reservation,
    })
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tv_contact_extension_relation:
                //选择关系
//                startForResult(ContactListFragment.newInstance(productCategoryId, mProductId), requestCode);
                optionPicker(optionRelation, tvContactExtensionRelation);
                break;

            case R.id.tv_contact_extension_sex:
                //选择性別
                optionPicker(optionSex, tvContactExtensionSex);
                break;

            case R.id.tv_contact_extension_health_status:
                //选择健康状态
                optionPicker(optionHealthStatus, tvContactExtensionHealthStatus);
                break;

            case R.id.tv_order_time:
                //选择服务时间
                chooseReservationTime();
                break;

            case R.id.tv_contact_extension_birthday:
                //选择生日
                chooseBirthday();
                break;

            case R.id.tv_reservation:
                //预约
                reservation(mProductId, mContact.getId());
                break;

        }
    }


    private void getProduct() {
        NetHelper.getApi().getProduct(mProductId, null)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<OrganizationProduct>(getActivity()) {
                    @Override
                    public void _next(OrganizationProduct product) {
                        mProduct = product;
                        showProduct();
                        showContact();
                    }
                });
    }

    private void showProduct() {
        //产品第一张图
        String iconUrl = IconUrl.moduleIconUrl(IconUrl.OrganizationProducts, mProduct.getId(), StringUtils.getFirstIconNameFromIcon(mProduct.getImage()));
        ImageLoaderUtil.instance().loadImage(iconUrl, R.drawable.icon_def, ivProductImage);

        tvProductTitle.setText(mProduct.getTitle());

        tvProductPrice.setText("￥" + new DecimalFormat("######0.00").format(mProduct.getPrice()) + "");

        showProductArea();
    }


    /**
     * 服务范围
     */
    private void showProductArea() {
        List<String> areas = mProduct.getAreaIds();
        String result = "无";
        if (areas != null) {
            for (int i = 0; i < areas.size(); i++) {
                String area = StringUtils.getOrganizationNameFromId(areas.get(i));
                if (!TextUtils.isEmpty(area)) {
                    if (i == 0) {
                        result = result + area;
                    } else {
                        result = result + "、" + area;
                    }
                }
            }
        }
        tvProductArea.setText(String.format("服务范围: %1$s", result));

        tvPrice.setText(Html.fromHtml("价格: <font color='#fe620f'>" + new DecimalFormat("######0.00").format(mProduct.getPrice()) + "元" + "</font>"));
    }

    /**
     * 显示个人资料
     */
    private void showContact() {

        tvContactName.setText(mContact.getName());

        tvContactMobile.setText(mContact.getMobile());

        tvContactAddress.setText(mContact.getAddress());

        String categoryId = mProduct.getCategoryId();
//        AgencyContactExtension extend = mContact.getExtend();
        if (categoryId.contains("养老机构")) {
            llAgancyExtension.setVisibility(View.VISIBLE);

//            if (extend != null) {
//
//                String relation = extend.getRelation();
//                String reservationName = extend.getReservationName();
//                String sex = extend.getSex();
//                String birthday = extend.getBirthday();
//                String healthStatus = extend.getHealthStatus();
//                String healthyDescription = extend.getHealthyDescription();
//
//                tvContactExtensionRelation.setText(relation);
//                tvContactExtensionName.setText(reservationName);
//                tvContactExtensionSex.setText(sex);
//                tvContactExtensionBirthday.setText(birthday);
//                tvContactExtensionHealthStatus.setText(healthStatus);
//                tvContactExtensionHealthDesc.setText(healthyDescription);
//            }


        }
    }

    /**
     * 预约服务
     */
    private void reservation(String productId, String contactId) {
        if (mProduct == null) {
            return;
        }

        String categoryId = mProduct.getCategoryId();
        if (!checkContactComplete(categoryId)) {
            return;
        }

        Contact contact = newContactModel();

        NetHelper.getApi()
                .putContact(contactId, contact)
                .flatMap(o -> addOrderObservable(productId, contactId))
                .compose(RxUtils.applySchedule())
                .compose(RxUtils.handleResult())
                .subscribe(new RxSubscriber<ProductOrder>(_mActivity) {
                    @Override
                    public void _next(ProductOrder productOrder) {
//                        mOrderMobile = productOrder.getMobile();
//                        call();
                        popTo(ProductDetailFragment.class, false);
                        EventBus.getDefault().post("hasOrder-" + productOrder.getMobile());
//                        setHadOrdered();
                    }
                });
    }



    public Observable<Response<ProductOrder>> addOrderObservable(String productId, String contactId) {
        String orderTime = tvOrderTime.getText().toString();
        String orderMemo = tvOrderMemo.getText().toString();
        return NetHelper.getApi().createOrder(productId, contactId, orderTime, orderMemo);
    }

    private Contact newContactModel() {
        Contact contact = new Contact();
        AgencyContactExtension extension = new AgencyContactExtension();
        extension.setRelation(tvContactExtensionRelation.getText().toString());
        extension.setReservationName(tvContactExtensionName.getText().toString());
        extension.setSex(tvContactExtensionSex.getText().toString());
        extension.setBirthday(tvContactExtensionBirthday.getText().toString());
        extension.setHealthStatus(tvContactExtensionHealthStatus.getText().toString());
        extension.setHealthyDescription(tvContactExtensionHealthDesc.getText().toString());
        contact.setExtend(extension);
        return contact;
    }

    private boolean checkContactComplete(String categoryId) {
        if (categoryId.contains("养老机构")) {
            if (TextUtils.isEmpty(tvContactExtensionRelation.getText())) {
                ToastHelper.get().showWareShort("请选择您与入住人的关系");
                return false;
            }

            if (TextUtils.isEmpty(tvContactExtensionName.getText())) {
                ToastHelper.get().showWareShort("请输入入住人的姓名");
                return false;
            }

            if (TextUtils.isEmpty(tvContactExtensionSex.getText())) {
                ToastHelper.get().showWareShort("请选择入住人的性别");
                return false;
            }

            if (TextUtils.isEmpty(tvContactExtensionBirthday.getText())) {
                ToastHelper.get().showWareShort("请选择入住人的出生日期");
                return false;
            }

            if (TextUtils.isEmpty(tvContactExtensionSex.getText())) {
                ToastHelper.get().showWareShort("请选择入住人的健康状况");
                return false;
            }

            if (TextUtils.isEmpty(tvContactExtensionSex.getText())) {
                ToastHelper.get().showWareShort("请输入入住人的身体情况,方便我们服务");
                return false;
            }
        }

        if (TextUtils.isEmpty(tvOrderTime.getText())) {
            ToastHelper.get().showWareShort("请选择服务时间");
            return false;
        }
        return true;
    }

    /**
     * 选择服务时间
     */
    private void chooseReservationTime() {
        int month = Calendar.getInstance(Locale.CHINA).get(Calendar.MONTH) + 1;
        int day = Calendar.getInstance(Locale.CHINA).get(Calendar.DAY_OF_MONTH);
        DateTimePicker picker = new DateTimePicker(_mActivity, DateTimePicker.MONTH_DAY, DateTimePicker.NONE);
        setPickerConfig(picker);
        if (App.widthDP > 820) {
            picker.setTextSize(12 * 2);
            picker.setCancelTextSize(12 * 2);
            picker.setSubmitTextSize(12 * 2);
            picker.setTopPadding(15 * 3);
            picker.setTopHeight(40 * 2);
        }
        picker.setDateRangeStart(month, day);
        picker.setOnDateTimePickListener((DateTimePicker.OnMonthDayTimePickListener) (month1, day1, hour, minute) -> {
            int year = Calendar.getInstance().get(Calendar.YEAR);
            reservationTime = year + "-" + month1 + "-" + day1;
            tvOrderTime.setText(reservationTime);
        });
        picker.show();
    }


    /**
     * 生日选择器
     */
    private void chooseBirthday() {
        DateTimePicker picker = new DateTimePicker(_mActivity, DateTimePicker.YEAR_MONTH_DAY, DateTimePicker.NONE);
        picker.setDateRangeStart(1900, 1, 1);
        if (App.widthDP > 820) {
            picker.setTextSize(12 * 2);
            picker.setCancelTextSize(12 * 2);
            picker.setSubmitTextSize(12 * 2);
            picker.setTopPadding(15 * 3);
            picker.setTopHeight(40 * 2);
        }
        setPickerConfig(picker);
        picker.setOnDateTimePickListener((DateTimePicker.OnYearMonthDayTimePickListener) (year, month, day, hour, minute) -> {
            String time = year + "-" + month + "-" + day;
            tvContactExtensionBirthday.setText(time);
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


    /**
     * 显示一个选择器
     */
    private void optionPicker(String[] options, TextView targetTv) {
        new MaterialDialog.Builder(_mActivity)
                .title("-选择类型-")
                .items(options)
                .itemsCallbackSingleChoice(-1, (dialog, view, which, text) -> {
                    targetTv.setText(text);
                    return true;
                }).show();

    }

    @Override
    protected void onFragmentResult(int reCode, int resultCode, Bundle data) {
        if (reCode == requestCode && resultCode == 0 && data != null) {
            Serializable serializable = data.getSerializable("mContact");
            if (mContact != null) {
                mContact = (Contact) serializable;
                showContact();
            }
        }
    }

//    private void call() {
//        if (!TextUtils.isEmpty(mOrderMobile)) {
//            boolean hasPermission = hasPermission(Manifest.permission.CALL_PHONE);
//            if (hasPermission) {
//                callMobile();
//            } else {
//                requestPermission(Manifest.permission.CALL_PHONE, 0X11);
//            }
//        } else {
//            ToastHelper.get().showShort("此服务商没有提供电话");
//        }
//    }
//
//    private void callMobile() {
//        Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + mOrderMobile.replace("-", "")));
//        startActivity(intent);
//    }
//
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == 0x11) {
//            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
//                ToastHelper.get().showShort("请给予权限");
//            } else {
//                callMobile();
//            }
//        }
//    }

}
