package lilun.com.pensionlife.ui.education.reservation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.bean.Contact;
import lilun.com.pensionlife.module.bean.MetaServiceContact;
import lilun.com.pensionlife.module.bean.Organization;
import lilun.com.pensionlife.module.bean.OrganizationProduct;
import lilun.com.pensionlife.module.bean.ProductOrder;
import lilun.com.pensionlife.module.utils.Preconditions;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.module.utils.StringUtils;
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
    @Bind(R.id.rl_contact_container)
    RelativeLayout rlContactContainer;
    @Bind(R.id.ll_container)
    LinearLayout llContainer;
    @Bind(R.id.tv_price)
    TextView tvPrice;
    @Bind(R.id.tv_reservation)
    TextView tvReservation;
    @Bind(R.id.ll_bottom_menu)
    LinearLayout llBottomMenu;
    private Contact mContact;
    public static int requestCode = 123;
    private String mProductId;
    private OrganizationProduct mProduct;
    private ContactView mContactView;
    private JSONObject template;
    private ArrayList<String> paymentMethods;
    private String orderText;


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


    @OnClick({R.id.rl_change_contact, R.id.tv_reservation
    })
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.rl_change_contact:
                //切换联系人
                start(ContactListFragment.newInstance(mProduct.getId(), ContactListFragment.RESERVATION_COURSE));
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
                        initProduct(product);
                    }
                });
    }


    private void initProduct(OrganizationProduct product) {
        mProduct = product;

        //显示铲平
        showProduct();

        //动态联系人资料
        String templateId = product.getMetaServiceContactId();
        if (!TextUtils.isEmpty(templateId)) {
            getTemplate(templateId);
        } else {
            showContact();
        }

        //是否需要支付
        String orderType = product.getOrderType();
        if (!TextUtils.isEmpty(orderType) && TextUtils.equals(orderType, Order.Type.payment)) {
            tvReservation.setEnabled(false);
            String filter = "{\"fields\":\"paymentMethods\"}";
            String organizationId = StringUtils.removeSpecialSuffix(product.getOrganizationId());
            NetHelper.getApi()
                    .getOrganizationById(organizationId, filter)
                    .compose(RxUtils.handleResult())
                    .compose(RxUtils.applySchedule())
                    .subscribe(new RxSubscriber<Organization>() {
                        @Override
                        public void _next(Organization organization) {
                            tvReservation.setEnabled(true);
                            paymentMethods = organization.getPaymentMethods();
                        }
                    });
        }
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

        tvPrice.setText(Html.fromHtml("价格: <font color='#fe620f'>" + new DecimalFormat("######0.00").format(mProduct.getPrice()) + "元" + "</font>"));

        tvProductArea.setText(showSemester(mProduct.getExtend()));

        String orderType = mProduct.getOrderType();
        if (!TextUtils.isEmpty(orderType)) {
            orderText = TextUtils.equals(orderType, Order.Type.payment) ? "立即下单" : "立即预约";
        }
        tvReservation.setText(orderText);

//        checkPayment();
    }

    private void checkPayment() {
        String orderType = mProduct.getOrderType();
        if (!TextUtils.isEmpty(orderType) && TextUtils.equals(orderType, "payment")) {
            tvReservation.setText("");
        }
    }


    private String showSemester(Map<String, Object> extend) {
        //显示学期
        String semester = "无";
        if (extend != null) {
            String termStartDate = (String) extend.get("termStartDate");
            String termEndDate = (String) extend.get("termEndDate");
            if (!TextUtils.isEmpty(termStartDate) && !TextUtils.isEmpty(termEndDate)) {
                semester = "开始时间：" + StringUtils.IOS2ToUTC(termStartDate, 5) + "\n"
                        + "结束时间：" + StringUtils.IOS2ToUTC(termEndDate, 5);
            }
        }
        return semester;
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

        NetHelper.getApi()
                .putContact(contactId, contact)
                .flatMap(o -> addOrderObservable(productId, contactId)).compose(RxUtils.handleResult())
                .flatMap(order -> addOrderDetaislObservable(order.getId()))
                .compose(RxUtils.applySchedule())
                .compose(RxUtils.handleResult())
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
        if (!TextUtils.isEmpty(orderType) && TextUtils.equals(orderType, Order.Type.payment)) {
            if (paymentMethods == null || paymentMethods.size() == 0) {
                paymentMethods = new ArrayList<>();
                paymentMethods.add(Order.paymentMethods.alipay);
                paymentMethods.add(Order.paymentMethods.alipay);
                paymentMethods.add(Order.paymentMethods.alipay);
            }
            Cashier cashier = Cashier.newInstance(productOrder.getId(), productOrder.getPrice().toString(), paymentMethods);
            cashier.setPayCallBack(new PayCallBack() {
                @Override
                public void paySuccess() {
                    next();
                }

                @Override
                public void payFalse() {
                    next();
                }

                @Override
                public void cancel() {
                    next();
                }
            });

            cashier.show(_mActivity.getFragmentManager(), null);
        } else {
            next();
        }


    }

    private void next() {
        popTo(CourseDetailFragment.class, false);
        EventBus.getDefault().post("hasOrder");
    }


    public Observable<Response<ProductOrder>> addOrderObservable(String productId, String contactId) {
//        String date2String = StringUtils.date2String(new Date());
        return NetHelper.getApi().createOrder(productId, contactId, null, null);
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
