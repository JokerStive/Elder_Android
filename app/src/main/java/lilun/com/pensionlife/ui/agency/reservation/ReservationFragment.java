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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DateTimePicker;
import cn.qqtheme.framework.picker.WheelPicker;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.bean.Contact;
import lilun.com.pensionlife.module.bean.MetaServiceContact;
import lilun.com.pensionlife.module.bean.OrganizationProduct;
import lilun.com.pensionlife.module.bean.ProductOrder;
import lilun.com.pensionlife.module.utils.Preconditions;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.module.utils.ToastHelper;
import lilun.com.pensionlife.module.utils.dynamic.ContactView;
import lilun.com.pensionlife.module.utils.mqtt.MQTTManager;
import lilun.com.pensionlife.module.utils.mqtt.MQTTTopicUtils;
import lilun.com.pensionlife.net.NetHelper;
import lilun.com.pensionlife.net.RxSubscriber;
import lilun.com.pensionlife.pay.Cashier;
import lilun.com.pensionlife.pay.Order;
import lilun.com.pensionlife.pay.PayCallBack;
import lilun.com.pensionlife.ui.contact.ContactListFragment;
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
public class ReservationFragment extends BaseFragment {
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
    @Bind(R.id.rl_contact_container)
    RelativeLayout rlContactContainer;
    @Bind(R.id.time)
    TextView time;
    @Bind(R.id.tv_order_time)
    TextView tvOrderTime;
    @Bind(R.id.memo)
    TextView memo;
    @Bind(R.id.tv_order_memo)
    EditText tvOrderMemo;
    @Bind(R.id.tv_memo_count)
    TextView tvMemoCount;
    @Bind(R.id.tv_price)
    TextView tvPrice;
    @Bind(R.id.tv_reservation)
    TextView tvReservation;
    private String reservationTime;
    private Contact mContact;
    public static int requestCode = 123;
    public static int resultCode = 321;
    private String mProductId;
    private OrganizationProduct mProduct;
    private ContactView mContactView;
    private JSONObject template;


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
        tvOrderMemo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tvMemoCount.setText(s.toString().length() + "/60");
            }
        });
    }


    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        getProduct();
    }


    @OnClick({R.id.tv_order_time, R.id.tv_reservation, R.id.rl_change_contact})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.rl_change_contact:
                //切换联系人信息
                changeContact();
                break;


            case R.id.tv_order_time:
                //选择服务时间
                chooseReservationTime();
                break;


            case R.id.tv_reservation:
                //预约
                reservation(mProductId, mContact.getId());
                break;

        }
    }

    private void changeContact() {
        start(ContactListFragment.newInstance(mProduct.getId(), ContactListFragment.RESERVATION_PRODUCT));
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
                        String templateId = product.getMetaServiceContactId();
                        if (!TextUtils.isEmpty(templateId)) {
                            getTemplate(templateId);
                        } else {
                            showContact();
                        }
                    }
                });
    }


    private void getTemplate(String templateId) {
        if (!TextUtils.isEmpty(templateId)) {
            NetHelper.getApi().getTemplate(templateId)
                    .compose(RxUtils.handleResult())
                    .compose(RxUtils.applySchedule())
                    .subscribe(new RxSubscriber<MetaServiceContact>(getActivity()) {
                        @Override
                        public void _next(MetaServiceContact metaServiceContact) {
                            template = metaServiceContact.getSettings();
                            showContact();
                        }
                    });
        }
    }


    private void showProduct() {
        //产品第一张图
        String iconUrl = StringUtils.getFirstIcon(mProduct.getImage());
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
        String result = StringUtils.getProductArea(areas);
        tvProductArea.setText(String.format("服务范围: %1$s", result));

        tvPrice.setText(Html.fromHtml("价格: <font color='#fe620f'>" + new DecimalFormat("######0.00").format(mProduct.getPrice()) + "元" + "</font>"));
    }

    /**
     * 显示个人资料
     */
    private void showContact() {
        if (mContactView == null) {
            mContactView = new ContactView(_mActivity);
            rlContactContainer.addView(mContactView.getView());
        }
        mContactView.reDraw(mContact, template);
    }

    /**
     * 预约服务
     */
    private void reservation(String productId, String contactId) {
        if (mProduct == null) {
            return;
        }


        Contact contact = mContactView.getFinalContactData();
        if (contact == null) {
            return;
        }


        if (!checkContactComplete()) {
            return;
        }

        NetHelper.getApi()
                .putContact(contactId, contact)
                .flatMap(o -> addOrderObservable(productId, contactId))
                .compose(RxUtils.handleResult())
                .flatMap(order -> addOrderDetaislObservable(order.getId()))
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<ProductOrder>(_mActivity) {
                    @Override
                    public void _next(ProductOrder productOrder) {
                        then(productOrder);
                    }
                });
    }


    //生成订单的后续
    private void then(ProductOrder productOrder) {
        if (productOrder.getProductBackup() != null && productOrder.getProductBackup().getOrganizationId() != null) {
            String topic = MQTTTopicUtils.getActivityTopic(productOrder.getProductBackup().getOrganizationId().replace("product", "order"), productOrder.getId());
            MQTTManager.getInstance().subscribe(topic, 2);
        }

        String orderType = mProduct.getOrderType();
        if (!TextUtils.isEmpty(orderType) && TextUtils.equals(orderType, "payment")) {
            ArrayList<String> paymentMethods = new ArrayList<>();
            paymentMethods.add(Order.OaymentMethods.alipay);
            paymentMethods.add(Order.OaymentMethods.weixin);
            Cashier cashier = Cashier.newInstance(productOrder.getId(), productOrder.getPrice().toString(),paymentMethods);
            cashier.setPayCallBack(new PayCallBack() {
                @Override
                public void paySuccess() {
                    po();
                }

                @Override
                public void payFalse() {
                    po();
                }

                @Override
                public void cancel() {
                    po();
                }
            });
            cashier.show(_mActivity.getFragmentManager(), null);
        } else {
            po();
        }


    }

    private void po() {
        popTo(CourseDetailFragment.class, false);
        EventBus.getDefault().post("hasOrder");
    }

    public Observable<Response<ProductOrder>> addOrderObservable(String productId, String contactId) {
        String orderTime = tvOrderTime.getText().toString();
        String orderMemo = tvOrderMemo.getText().toString();
        return NetHelper.getApi().createOrder(productId, contactId, orderTime, orderMemo);
    }

    /**
     * 订单详情需要获取到组织id 来订阅mqtt
     *
     * @param orderId 订单id
     * @return
     */
    public Observable<Response<ProductOrder>> addOrderDetaislObservable(String orderId) {
        String filter = "{\"include\":\"productBackup\"}";
        return NetHelper.getApi().getOrder(orderId, filter);
    }


    private boolean checkContactComplete() {
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


    private void setPickerConfig(WheelPicker picker) {
//        picker.setTextSize(size);
//        picker.setCancelTextSize(size);
//        picker.setSubmitTextSize(size);
//        picker.setLineColor(selectColor);
//        picker.setTextColor(selectColor);
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
