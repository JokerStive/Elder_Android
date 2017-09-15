package lilun.com.pensionlife.ui.education.reservation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DateTimePicker;
import cn.qqtheme.framework.picker.WheelPicker;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.app.IconUrl;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.bean.Contact;
import lilun.com.pensionlife.module.bean.ContactExtendKey;
import lilun.com.pensionlife.module.bean.OrganizationProduct;
import lilun.com.pensionlife.module.bean.ProductOrder;
import lilun.com.pensionlife.module.utils.Preconditions;
import lilun.com.pensionlife.module.utils.RegexUtils;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.module.utils.ToastHelper;
import lilun.com.pensionlife.net.NetHelper;
import lilun.com.pensionlife.net.RxSubscriber;
import lilun.com.pensionlife.ui.education.course_details.CourseDetailFragment;
import lilun.com.pensionlife.widget.NormalTitleBar;
import lilun.com.pensionlife.widget.image_loader.ImageLoaderUtil;
import retrofit2.Response;
import rx.Observable;

/**
 * @author yk
 *         create at 2017/4/11 16:11
 *         email : yk_developer@163.com
 */
public class ReservationCourseFragment extends BaseFragment {


    @Bind(R.id.titleBar)
    NormalTitleBar titleBar;
    @Bind(R.id.iv_product_image)
    ImageView ivProductImage;
    @Bind(R.id.tv_product_area)
    TextView tvProductArea;
    @Bind(R.id.tv_product_price)
    TextView tvProductPrice;
    @Bind(R.id.tv_product_title)
    TextView tvProductTitle;
    @Bind(R.id.rl_product)
    RelativeLayout rlProduct;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.tv_contact_name)
    TextView tvContactName;
    @Bind(R.id.mobile)
    TextView mobile;
    @Bind(R.id.tv_contact_mobile)
    TextView tvContactMobile;
    @Bind(R.id.address)
    TextView address;
    @Bind(R.id.tv_contact_address)
    EditText tvContactAddress;
    @Bind(R.id.extension_sex)
    TextView extensionSex;
    @Bind(R.id.tv_contact_extension_sex)
    TextView tvContactExtensionSex;
    @Bind(R.id.extension_birthday)
    TextView extensionBirthday;
    @Bind(R.id.tv_contact_extension_birthday)
    TextView tvContactExtensionBirthday;
    @Bind(R.id.extension_health_status)
    TextView extensionHealthStatus;
    @Bind(R.id.tv_contact_extension_health_status)
    TextView tvContactExtensionHealthStatus;
    @Bind(R.id.extension_health_politic_status)
    TextView extensionHealthPoliticStatus;

    @Bind(R.id.tv_contact_extension_politic_status)
    TextView tvContactExtensionPoliticStatus;


    @Bind(R.id.et_contact_post_work)
    EditText etContactExtensionPostWork;

    @Bind(R.id.et_contact_emergency_name)
    TextView etContactEmergencyName;

    @Bind(R.id.et_emergency_phone)
    TextView etEmergencyPhone;

    @Bind(R.id.tv_price)
    TextView tvPrice;

    @Bind(R.id.tv_reservation)
    TextView tvReservation;

    @Bind(R.id.ll_bottom_menu)
    LinearLayout llBottomMenu;

    private String[] optionSex = App.context.getResources().getStringArray(R.array.personal_info_sex);
    private String[] optionHealthStatus = App.context.getResources().getStringArray(R.array.personal_info_health_status);
    private String[] optionPoliticStatus = new String[]{"中共党员", "明主党派", "一般群众"};

    private int size = 17;
    private int selectColor = 0xff0090f9;
    private String reservationTime;
    private Contact mContact;
    public static int requestCode = 123;
    public static int resultCode = 321;
    private String mProductId;
    private OrganizationProduct mProduct;
//    private String mOrderMobile;


    public static ReservationCourseFragment newInstance(String productId, Contact contact) {
        ReservationCourseFragment fragment = new ReservationCourseFragment();
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
        return R.layout.fragment_reservation_course;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        titleBar.setOnBackClickListener(this::pop);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        getProduct();
    }


    @OnClick({R.id.tv_contact_extension_politic_status, R.id.tv_contact_extension_sex, R.id.tv_contact_extension_birthday,
            R.id.tv_contact_extension_health_status, R.id.tv_reservation,
    })
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tv_contact_extension_politic_status:
                //政治面貌
                optionPicker(optionPoliticStatus, tvContactExtensionPoliticStatus);
                break;

            case R.id.tv_contact_extension_sex:
                //选择性別
                optionPicker(optionSex, tvContactExtensionSex);
                break;

            case R.id.tv_contact_extension_health_status:
                //选择健康状态
                optionPicker(optionHealthStatus, tvContactExtensionHealthStatus);
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

        //课程需要的扩展属性
        HashMap<String, String> extend = mContact.getExtend();
        if (extend != null) {
            String sex = extend.get(ContactExtendKey.sex);
            String birthday = extend.get(ContactExtendKey.birthday);
            String healthStatus = extend.get(ContactExtendKey.healthStatus);
            String politicStatus = extend.get(ContactExtendKey.politicStatus);
            String postOfWorked = extend.get(ContactExtendKey.postOfWorked);
            String emergencyName = extend.get(ContactExtendKey.emergencyContact);
            String emergencyPhone = extend.get(ContactExtendKey.contactNumber);

            tvContactExtensionSex.setText(sex);
            tvContactExtensionBirthday.setText(birthday);
            tvContactExtensionHealthStatus.setText(healthStatus);
            tvContactExtensionPoliticStatus.setText(politicStatus);
            etContactExtensionPostWork.setText(postOfWorked);
            etContactEmergencyName.setText(emergencyName);
            etEmergencyPhone.setText(emergencyPhone);
        }


    }

    /**
     * 预约服务
     */
    private void reservation(String productId, String contactId) {
        if (mProduct == null) {
            return;
        }

//        String categoryId = mProduct.getCategoryId();
        if (!checkContactComplete()) {
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
                        popTo(CourseDetailFragment.class, false);
                        EventBus.getDefault().post("hasOrder");
                    }
                });
    }


    public Observable<Response<ProductOrder>> addOrderObservable(String productId, String contactId) {
//        String orderTime = tvOrderTime.getText().toString();
//        String orderMemo = tvOrderMemo.getText().toString();
        return NetHelper.getApi().createOrder(productId, contactId, null, null);
    }

    private Contact newContactModel() {
        Contact contact = new Contact();
        HashMap<String, String> extension = new HashMap<>();
        extension.put(ContactExtendKey.sex, tvContactExtensionSex.getText().toString());
        extension.put(ContactExtendKey.birthday, tvContactExtensionBirthday.getText().toString());
        extension.put(ContactExtendKey.healthStatus, tvContactExtensionHealthStatus.getText().toString());
        extension.put(ContactExtendKey.politicStatus, tvContactExtensionPoliticStatus.getText().toString());
        extension.put(ContactExtendKey.postOfWorked, etContactExtensionPostWork.getText().toString());
        extension.put(ContactExtendKey.emergencyContact, etContactEmergencyName.getText().toString());
        extension.put(ContactExtendKey.contactNumber, etEmergencyPhone.getText().toString());
        contact.setExtend(extension);
        contact.setIndex(mContact.getIndex());
        return contact;
    }

    private boolean checkContactComplete() {

        if (TextUtils.isEmpty(tvContactExtensionSex.getText())) {
            ToastHelper.get().showWareShort("请选择的性别");
            return false;
        }

        if (TextUtils.isEmpty(tvContactExtensionBirthday.getText())) {
            ToastHelper.get().showWareShort("请选择出生日期");
            return false;
        }

        if (TextUtils.isEmpty(tvContactExtensionSex.getText())) {
            ToastHelper.get().showWareShort("请选择健康状况");
            return false;
        }

        if (TextUtils.isEmpty(tvContactExtensionPoliticStatus.getText())) {
            ToastHelper.get().showWareShort("请选择政治面貌");
            return false;
        }

        if (TextUtils.isEmpty(etContactExtensionPostWork.getText())) {
            ToastHelper.get().showWareShort("请输入原工作单位");
            return false;
        }

        if (TextUtils.isEmpty(etContactExtensionPostWork.getText())) {
            ToastHelper.get().showWareShort("请输入紧急联系人姓名");
            return false;
        }


        if (TextUtils.isEmpty(etContactExtensionPostWork.getText())) {
            ToastHelper.get().showWareShort("请输入紧急联系人电话");

            return false;
        } else if (!RegexUtils.checkMobile(etContactExtensionPostWork.getText().toString())) {
            ToastHelper.get().showWareShort("紧急联系人电话有误");
        }

        return true;
    }

//    /**
//     * 选择服务时间
//     */
//    private void chooseReservationTime() {
//        int month = Calendar.getInstance(Locale.CHINA).get(Calendar.MONTH) + 1;
//        int day = Calendar.getInstance(Locale.CHINA).get(Calendar.DAY_OF_MONTH);
//        DateTimePicker picker = new DateTimePicker(_mActivity, DateTimePicker.MONTH_DAY, DateTimePicker.NONE);
//        setPickerConfig(picker);
//        if (App.widthDP > 820) {
//            picker.setTextSize(12 * 2);
//            picker.setCancelTextSize(12 * 2);
//            picker.setSubmitTextSize(12 * 2);
//            picker.setTopPadding(15 * 3);
//            picker.setTopHeight(40 * 2);
//        }
//        picker.setDateRangeStart(month, day);
//        picker.setOnDateTimePickListener((DateTimePicker.OnMonthDayTimePickListener) (month1, day1, hour, minute) -> {
//            int year = Calendar.getInstance().get(Calendar.YEAR);
//            reservationTime = year + "-" + month1 + "-" + day1;
//            tvOrderTime.setText(reservationTime);
//        });
//        picker.show();
//    }


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

}
