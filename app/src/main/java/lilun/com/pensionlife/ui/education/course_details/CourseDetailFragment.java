package lilun.com.pensionlife.ui.education.course_details;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.app.User;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.adapter.CourseScheduleAdapter;
import lilun.com.pensionlife.module.bean.Contact;
import lilun.com.pensionlife.module.bean.CourseSchedule;
import lilun.com.pensionlife.module.bean.Information;
import lilun.com.pensionlife.module.bean.OrderLimit;
import lilun.com.pensionlife.module.bean.OrganizationProduct;
import lilun.com.pensionlife.module.bean.OrganizationProductCategory;
import lilun.com.pensionlife.module.utils.Preconditions;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.module.utils.ToastHelper;
import lilun.com.pensionlife.ui.contact.AddBasicContactFragment;
import lilun.com.pensionlife.ui.contact.ContactListFragment;
import lilun.com.pensionlife.ui.education.reservation.ReservationCourseFragment;
import lilun.com.pensionlife.ui.order.OrderListFragment;
import lilun.com.pensionlife.ui.protocol.ProtocolView;
import lilun.com.pensionlife.widget.NormalDialog;
import lilun.com.pensionlife.widget.NormalTitleBar;
import lilun.com.pensionlife.widget.slider.BannerPager;

public class CourseDetailFragment extends BaseFragment<CourseDetailContract.Presenter> implements CourseDetailContract.View {

    @Bind(R.id.titleBar)
    NormalTitleBar titleBar;
    @Bind(R.id.rl_course_schedules)
    RecyclerView rlCourseSchedules;
    @Bind(R.id.tv_bottom_price)
    TextView tvBottomPrice;
    @Bind(R.id.tv_reservation)
    TextView tvReservation;
    @Bind(R.id.ll_bottom_menu)
    LinearLayout llBottomMenu;

    @Bind(R.id.protocol)
    ProtocolView protocolView;

    private String mProductId;
    private OrganizationProduct mProduct;

    //不能预约 0-已经预约  1-时间冲突
    private int can_not_order_flag = -1;
    private CourseScheduleAdapter mCourseScheduleAdapter;
    private BannerPager banner;
    private TextView tvCourseTitle;
    private TextView tvCourseXueqi;
    private TextView tvCoursePrice;
    private TextView tvCourseStock;
    private TextView tvCourseSold;


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
            start(OrderListFragment.newInstance());
            setHadOrdered();
        }
    }

    @Subscribe
    public void refresh(ProtocolView.IsAgreeProtocol isAgreeProtocol) {
        protocolView.agreeProtocol(isAgreeProtocol.isAgreeProtocol);
    }


    @Override
    protected void getTransferData(Bundle arguments) {
        mProductId = arguments.getString("objectId");
        Preconditions.checkNull(mProductId);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new CourseDetailPresenter();
        mPresenter.bindView(this);

        mPresenter.getProductDetail(mProductId, null);
        mPresenter.getCourseSchedules(mProductId);
        mPresenter.getProtocol(mProductId);
        mPresenter.getIsOrder(mProductId);
    }

    @Override
    protected int getLayoutId() {

        return R.layout.fragment_course_detail;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        titleBar.setOnBackClickListener(this::pop);

        rlCourseSchedules.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false));


        mCourseScheduleAdapter = new CourseScheduleAdapter(new ArrayList<>());
        mCourseScheduleAdapter.setTeacherListener(organizationAccountId -> {
            //老师简介
            start(TeacherDetailFragment.newInstance(organizationAccountId));
        });
        setHead();
        rlCourseSchedules.setAdapter(mCourseScheduleAdapter);


    }

    private void setHead() {
        View headCourseDetail = LayoutInflater.from(App.context).inflate(R.layout.head_course_detail, null);
        banner = (BannerPager) headCourseDetail.findViewById(R.id.banner);
        tvCourseTitle = (TextView) headCourseDetail.findViewById(R.id.tv_course_title);
        tvCourseXueqi = (TextView) headCourseDetail.findViewById(R.id.tv_course_xueqi);
        tvCoursePrice = (TextView) headCourseDetail.findViewById(R.id.tv_course_price);
        tvCourseStock = (TextView) headCourseDetail.findViewById(R.id.tv_course_stock);
        tvCourseSold = (TextView) headCourseDetail.findViewById(R.id.tv_course_sold);
        mCourseScheduleAdapter.addHeaderView(headCourseDetail);
    }


    @Override
    public void showCourseDetail(OrganizationProduct product) {
        showProductDetail(product);
    }

    @Override
    public void showProtocol(Information protocol) {
        if (protocol != null) {
            protocolView.setVisibility(View.VISIBLE);
            protocolView.showProtocol(this, protocol);
        }
    }

    @Override
    public void showSchedules(List<CourseSchedule> schedules) {
        mCourseScheduleAdapter.replaceAll(schedules);
    }

    @Override
    public void showIsOrdered(OrderLimit orderLimit) {
        boolean ordered = orderLimit.isOrdered();
        boolean isLimit = orderLimit.isIsLimit();
        if (ordered) {
            setHadOrdered();
        } else if (isLimit) {
            //冲突
            can_not_order_flag = 1;
        }
    }


    private void showProductDetail(OrganizationProduct product) {

        this.mProduct = product;


        //图片
        showBanner(product);


        //标题
        tvCourseTitle.setText(product.getTitle());

        //学期
        //学期
        Map<String, Object> extend = product.getExtend();
        if (extend != null && extend.get("termStartDate") != null && extend.get("termEndDate") != null) {
            String termStartDate = extend.get("termStartDate").toString();
            String termEndDate = extend.get("termEndDate").toString();
            String semester = "学期：" + StringUtils.IOS2ToUTC(termStartDate, 5) + "~" + StringUtils.IOS2ToUTC(termEndDate, 5);
            tvCourseXueqi.setText(semester);
        }


        //报名人数
        tvCourseStock.setText("招生人数：" + StringUtils.filterNull(product.getStock() + product.getSold() + ""));


        //剩余名额
        tvCourseSold.setText("报名人数：" + StringUtils.filterNull(product.getSold() + ""));


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


    private void showBanner(OrganizationProduct product) {
        List<String> urls = new ArrayList<>();
        if (product.getImage() != null) {
            for (String url : product.getImage()) {
                urls.add(url);
            }
        } else if (product.getOrgCategory() != null) {
            OrganizationProductCategory orgCategory = product.getOrgCategory();
            urls.add(orgCategory.getIcon());
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
                orderCheck();
                break;


        }
    }


    //预约检查
    private void orderCheck() {
        if (mProduct == null) {
            return;
        }

        if (protocolView.getVisibility() != View.GONE) {
            boolean agreeProtocol = protocolView.isAgreeProtocol();
            if (!agreeProtocol) {
                ToastHelper.get().showWareShort("请先同意协议");
                return;
            }
        }

        if (mProduct.getCreatorId().equals(User.getUserId())) {
            ToastHelper.get().showWareShort("不能预约自己创建的课程");
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
        }


        if (can_not_order_flag == -1) {
            takeReservation();
        }
    }


    /**
     * 预约
     */
    private void takeReservation() {
        mPresenter.getContacts();
    }


    /**
     * 检查预约资料
     */
    @Override
    public void checkContact(List<Contact> contacts) {
        if (contacts.size() > 0) {
            //显示 预约者信息列表
            Contact defContact = getDefaultContact(contacts);
            if (defContact == null) {
                //没有默认信息，就进去信息列表
                start(ContactListFragment.newInstance(mProduct.getId(), ContactListFragment.RESERVATION_COURSE));
            } else if (TextUtils.isEmpty(defContact.getMobile()) || TextUtils.isEmpty(defContact.getName()) || TextUtils.isEmpty(defContact.getAddress())) {
                defContact.setProductId(mProductId);
                //必要信息不完善
                start(AddBasicContactFragment.newInstance(defContact, ContactListFragment.RESERVATION_COURSE));
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


}

