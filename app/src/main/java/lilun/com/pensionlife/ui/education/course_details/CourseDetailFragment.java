package lilun.com.pensionlife.ui.education.course_details;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.IconUrl;
import lilun.com.pensionlife.app.User;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.bean.Contact;
import lilun.com.pensionlife.module.bean.OrderLimit;
import lilun.com.pensionlife.module.bean.OrganizationProduct;
import lilun.com.pensionlife.module.utils.Preconditions;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.module.utils.ToastHelper;
import lilun.com.pensionlife.net.NetHelper;
import lilun.com.pensionlife.net.RxSubscriber;
import lilun.com.pensionlife.ui.contact.AddBasicContactFragment;
import lilun.com.pensionlife.ui.contact.ContactListFragment;
import lilun.com.pensionlife.ui.education.reservation.CoursePolicyFragment;
import lilun.com.pensionlife.ui.education.reservation.ReservationCourseFragment;
import lilun.com.pensionlife.widget.NormalDialog;
import lilun.com.pensionlife.widget.NormalTitleBar;
import lilun.com.pensionlife.widget.slider.BannerPager;

public class CourseDetailFragment extends BaseFragment {

    @Bind(R.id.titleBar)
    NormalTitleBar titleBar;
    @Bind(R.id.banner)
    BannerPager banner;
    @Bind(R.id.tv_course_title)
    TextView tvCourseTitle;
    @Bind(R.id.tv_course_xueqi)
    TextView tvCourseXueqi;
    @Bind(R.id.tv_course_price)
    TextView tvCoursePrice;
    @Bind(R.id.tv_course_stock)
    TextView tvCourseStock;
    @Bind(R.id.tv_course_remain)
    TextView tvCourseRemain;
    @Bind(R.id.tv_course_teacher)
    TextView tvCourseTeacher;
    @Bind(R.id.tv_course_startTime)
    TextView tvCourseStartTime;
    @Bind(R.id.tv_course_classRoom)
    TextView tvCourseClassRoom;
    @Bind(R.id.tv_course_address)
    TextView tvCourseAddress;
    @Bind(R.id.swipe_layout)
    SwipeRefreshLayout swipeLayout;
    @Bind(R.id.tv_bottom_price)
    TextView tvBottomPrice;
    @Bind(R.id.tv_reservation)
    TextView tvReservation;
    @Bind(R.id.tv_course_apply_time)
    TextView tvCourseApplyTime;
    @Bind(R.id.wb_course_content)
    WebView wbCourseContent;
    private String mProductId;
    private OrganizationProduct mProduct;

    //不能预约 0-已经预约  1-时间冲突
    private int can_not_order_flag = -1;


    public static CourseDetailFragment newInstance(String productId) {
        CourseDetailFragment fragment = new CourseDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("objectId", productId);
        fragment.setArguments(args);
        return fragment;
    }

    @Subscribe
    public void refresh(String tx) {
        if (tx.contains("hasOrder")) {
            setHadOrdered();
        }
    }


    @Override
    protected void getTransferData(Bundle arguments) {
        mProductId = arguments.getString("objectId");
        Preconditions.checkNull(mProductId);
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected int getLayoutId() {

        return R.layout.fragment_course_detail;
    }

    @Override
    protected void initView(LayoutInflater inflater) {

        titleBar.setOnBackClickListener(this::pop);

        swipeLayout.setEnabled(false);
    }


    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);


        getProduct();
//        getIsOrder();
    }


    private void getProduct() {
        NetHelper.getApi().getProduct(mProductId, null)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<OrganizationProduct>(getActivity()) {
                    @Override
                    public void _next(OrganizationProduct product) {
                        showProductDetail(product);
                        getIsOrder();
                    }
                });

    }


    private void getIsOrder() {
        NetHelper.getApi()
                .getOrderLimit(mProductId)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<OrderLimit>() {

                    @Override
                    public void _next(OrderLimit orderLimit) {
                        boolean ordered = orderLimit.isOrdered();
                        boolean isLimit = orderLimit.isIsLimit();
                        if (ordered) {
                            setHadOrdered();
                        } else if (isLimit) {
                            //冲突
                            can_not_order_flag = 1;
                        }
                    }
                });

    }


    private String null2empty(String s) {
        return TextUtils.isEmpty(s) ? "" : s;
    }

    private void showProductDetail(OrganizationProduct product) {

        this.mProduct = product;


        //图片
        showBanner(product);


        //标题
        tvCourseTitle.setText(product.getTitle());


        //报名人数
        tvCourseStock.setText("报名人数：" + StringUtils.filterNull(product.getStock() + product.getSold() + ""));


        //剩余名额
        tvCourseRemain.setText("剩余名额：" + StringUtils.filterNull(product.getStock() + ""));


        //报名开始时间和结束时间
        String startTime = product.getStartTime();
        String endTIme = product.getEndTIme();
        if (!TextUtils.isEmpty(startTime) || !TextUtils.isEmpty(endTIme)) {
            String applyText = "报名开始、结束时间：" + StringUtils.IOS2ToUTC(startTime, 5) + "--" + StringUtils.IOS2ToUTC(endTIme, 5);
            tvCourseApplyTime.setText(applyText);
        }


        //课程内容
        wbCourseContent.getSettings().setJavaScriptEnabled(true);
        wbCourseContent.loadDataWithBaseURL("", product.getContext(), "text/html", "UTF-8", "");

        Map<String, Object> extend = product.getExtend();
        if (extend != null) {
            //授课老师
            tvCourseTeacher.setText("授课老师：" + StringUtils.filterNull((String) extend.get("teacher")));


            //上课时间
            String classStartTime = StringUtils.filterNull(IOS2ToUTC((String) extend.get("classStartTime")));
            String classEndTime = StringUtils.filterNull(IOS2ToUTC((String) extend.get("classEndTime")));
            List<String> classWeekName = (List<String>) extend.get("classWeekName");
            String weekString = getWeekString(classWeekName);
            tvCourseStartTime.setText("上课时间：" + weekString + "  " + classStartTime + "-" + classEndTime);


            //上课教室
            tvCourseClassRoom.setText("上课教室：" + StringUtils.filterNull((String) extend.get("classRoom")));

            //上课地点
            tvCourseAddress.setText("上课地点：" + StringUtils.filterNull((String) extend.get("classPlace")));

            //显示学期
            String termStartDate = (String) extend.get("termStartDate");
            String termEndDate = (String) extend.get("termEndDate");
            if (!TextUtils.isEmpty(termStartDate) && !TextUtils.isEmpty(termEndDate)) {
                String semesterText = "学期：" + StringUtils.IOS2ToUTC(termStartDate, 5) + "--" + StringUtils.IOS2ToUTC(termEndDate, 5);
                tvCourseXueqi.setText(semesterText);
            }

        }


        //价格
        Double price = product.getPrice();
        String formatPriceToFree = StringUtils.formatPrice(price);
        String topPriceResult = !TextUtils.isEmpty(formatPriceToFree) ? "¥" + formatPriceToFree + "元" : StringUtils.formatPriceToFree(price);
        tvCoursePrice.setText(topPriceResult);

        //底部价格
        String buttonPriceResult = !TextUtils.isEmpty(formatPriceToFree) ? "¥" + formatPriceToFree : StringUtils.formatPriceToFree(price);
        tvBottomPrice.setText(Html.fromHtml("合计: <font color='#fe620f'>" + buttonPriceResult + "</font>"));


        //报名满额
        if (product.getStock() <= 0) {
            canNotOrderStatus("已经满员");
        }

    }

    private String getWeekString(List<String> classWeekNames) {
        String result = "";
        if (classWeekNames != null && classWeekNames.size() > 0) {
            result = "星期";
            for (int i = 0; i < classWeekNames.size(); i++) {
                String classWeekName = classWeekNames.get(i);
                if (classWeekName.contains("星期")) {
                    String classWeekNameNum = classWeekName.substring(classWeekName.lastIndexOf("星期") + 2);
                    result = result + " " + classWeekNameNum;
                }
            }
        }
        return result;
    }


    public String IOS2ToUTC(String isoTime1) {
        String ret = "";
        try {
            String[] ss = isoTime1.split("\\.");
            String isoTime = ss[0] + "+08:00";
            DateTimeFormatter parser2 = ISODateTimeFormat.dateTimeNoMillis();
            DateTime dateTime = parser2.parseDateTime(isoTime);
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            ret = format.format(new Date(dateTime.getMillis()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    private void showBanner(OrganizationProduct product) {
        List<String> urls = new ArrayList<>();
        if (product.getImage() != null) {
            for (String url : product.getImage()) {
                urls.add(url);
            }
        } else {
            String url = IconUrl.moduleIconUrl(IconUrl.OrganizationProducts, product.getId(), null);
            urls.add(url);
        }
        banner.setData(urls);
    }


    private void setHadOrdered() {
        canNotOrderStatus("已经预约");

    }

    private void canNotOrderStatus(String showStr) {
        tvReservation.setBackgroundColor(_mActivity.getResources().getColor(R.color.yellowish));
        tvReservation.setEnabled(false);
        tvReservation.setText(showStr);
    }


    @OnClick({R.id.tv_reservation})
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_reservation:
                //立即预约
                if (mProduct == null) {
                    return;
                }
                if (mProduct.getCreatorId().equals(User.getUserId())) {
                    ToastHelper.get().showWareShort("自己商品不可以预约");
                    return;
                }

                if (mProduct.getDraft() != null && mProduct.getDraft()) {
                    ToastHelper.get().showWareShort("该课程无法预约，请刷新页面后重试");
                    return;
                }


                if (can_not_order_flag == 1) {
                    new NormalDialog().createNormal(_mActivity, "该课程的上课时间与你报名过的时间有冲突,继续预约吗？", new NormalDialog.OnPositiveListener() {
                        @Override
                        public void onPositiveClick() {
                            takeReservation();
                        }
                    });
                } else if (can_not_order_flag == -1) {
                    takeReservation();
                }
                break;
        }
    }


    /**
     * 预约
     */
    private void takeReservation() {
        if (TextUtils.equals(mProduct.getCreatorId(), User.getUserId())) {
            ToastHelper.get().showWareShort("不能预约自己创建的课程");
            return;
        }

        if (can_not_order_flag == 1) {

        }

        //检查协议
        boolean needAgreePolicy = !TextUtils.isEmpty(mProduct.getLicenseGgreement());
        ArrayList<String> certificateLicenses = User.getCertificateLicense();
        if (certificateLicenses != null) {
            for (String productId : certificateLicenses) {
                if (TextUtils.equals(mProductId, productId)) {
                    needAgreePolicy = false;
                }
            }
        }
        //去签订协议界面
        if (needAgreePolicy) {
            start(CoursePolicyFragment.newInstance(mProductId, mProduct.getLicenseGgreement()));
            return;
        }


        String filter = "{\"where\":{\"accountId\":\"" + User.getUserId() + "\"}}";
        NetHelper.getApi().getContacts(filter)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<Contact>>(getActivity()) {
                    @Override
                    public void _next(List<Contact> contacts) {
                        checkContact(contacts);
                    }
                });
    }


    /**
     * 检查预约资料
     */
    private void checkContact(List<Contact> contacts) {
        if (contacts.size() > 0) {
            //显示 预约者信息列表
            Contact defContact = getDefaultContact(contacts);
            if (defContact == null) {
                //没有默认信息，就进去信息列表
                start(ContactListFragment.newInstance(mProduct.getId(), 1));
            } else if (TextUtils.isEmpty(defContact.getMobile()) || TextUtils.isEmpty(defContact.getName()) || TextUtils.isEmpty(defContact.getAddress())) {
                defContact.setProductId(mProductId);
                //必要信息不完善
                start(AddBasicContactFragment.newInstance(defContact, 1));
            } else {
                //有默认信息，并且必要信息完整，直接预约界面
                start(ReservationCourseFragment.newInstance(mProductId, defContact));
            }
        } else {
            //新增基础信息界面
            AddBasicContactFragment addBasicContactFragment = new AddBasicContactFragment();
            Bundle args = new Bundle();
            args.putString("objectId", mProductId);
            addBasicContactFragment.setArguments(args);
            addBasicContactFragment.setOnAddBasicContactListener(contact -> start(ReservationCourseFragment.newInstance(mProductId, contact)));
            start(addBasicContactFragment);
        }
    }


    /**
     * 获取默认信息
     */
    private Contact getDefaultContact(List<Contact> contacts) {
        for (Contact contact : contacts) {
            int index = contact.getIndex();
            if (index == 1) {
                return contact;
            }
        }
        return null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}

