package lilun.com.pension.ui.tourism.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import lilun.com.pension.R;
import lilun.com.pension.app.IconUrl;
import lilun.com.pension.app.User;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.bean.Contact;
import lilun.com.pension.module.bean.IconModule;
import lilun.com.pension.module.bean.ProductOrder;
import lilun.com.pension.module.bean.Tourism;
import lilun.com.pension.module.utils.Preconditions;
import lilun.com.pension.module.utils.RxUtils;
import lilun.com.pension.net.NetHelper;
import lilun.com.pension.net.RxSubscriber;
import lilun.com.pension.ui.agency.reservation.ReservationActivity;
import lilun.com.pension.ui.agency.reservation.ReservationFragment;
import lilun.com.pension.ui.tourism.info.AddTourismInfoFragment;
import lilun.com.pension.widget.slider.BannerPager;

/**
 * 旅游详情V
 *
 * @author yk
 *         create at 2017/4/13 11:31
 *         email : yk_developer@163.com
 */
public class TourismDetailFragment extends BaseFragment implements TabLayout.OnTabSelectedListener {

    @Bind(R.id.banner)
    BannerPager banner;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_destination)
    TextView tvDestination;
    @Bind(R.id.tv_traffic_detail)
    TextView tvTrafficDetail;
    @Bind(R.id.tv_accommodation_standard)
    TextView tvAccommodationStandard;
    @Bind(R.id.tv_journey_attractions)
    TextView tvJourneyAttractions;
    @Bind(R.id.tv_gather_location)
    TextView tvGatherLocation;
    @Bind(R.id.tl_title)
    TabLayout tlTitle;
    @Bind(R.id.vp_pager)
    ViewPager vpPager;
    @Bind(R.id.btn_book)
    Button btnBook;
    @Bind(R.id.tv_context)
    TextView tvContext;
    @Bind(R.id.tv_price)
    TextView tvPrice;
    @Bind(R.id.tv_feature)
    TextView tvFeature;
    private Tourism tourism;
    private String[] titles = new String[]{"费用信息", "路线特色", "预定须知"};
    private String categoryId;

    public static TourismDetailFragment newInstance(Tourism tourism) {
        TourismDetailFragment fragment = new TourismDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("tourism", tourism);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void getTransferData(Bundle arguments) {
        super.getTransferData(arguments);
        tourism = (Tourism) arguments.getSerializable("tourism");
        Preconditions.checkNull(tourism);
        categoryId = tourism.getCategoryId();
        Preconditions.checkNull(categoryId);
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tourism_detail;
    }

    @Override
    protected void initView(LayoutInflater inflater) {

        setTabLayout();

        setInitData();

        setBanner();
    }


    @OnClick({R.id.iv_back, R.id.btn_book})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                pop();
                break;
            case R.id.btn_book:
                if (btnBook.getText().equals("预约")) {
                    bookTourism();
                }
                break;
        }
    }


    private void setBanner() {
        //展示product图片
        List<String> urls = new ArrayList<>();
        if (tourism.getImages() != null) {
            for (IconModule iconModule : tourism.getImages()) {
                String url = IconUrl.moduleIconUrl(IconUrl.OrganizationProducts, tourism.getId(), iconModule.getFileName());
                urls.add(url);
            }
        } else {
            String url = IconUrl.moduleIconUrl(IconUrl.OrganizationProducts, tourism.getId(), null);
            urls.add(url);
        }
        banner.setData(urls);
    }


    private void setTabLayout() {
        List<BaseFragment> views = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            tlTitle.addTab(tlTitle.newTab().setText(titles[i]));
        }
        tlTitle.addOnTabSelectedListener(this);

        Tourism.ExtendBean extend = tourism.getExtend();
        String fee = extend.getFee();
        String distance = extend.getDistance();
        String notice = extend.getNotice();

        TourismFeatureFragment feeFeature = TourismFeatureFragment.newInstance(fee);
        TourismFeatureFragment distanceFeature = TourismFeatureFragment.newInstance(distance);
        TourismFeatureFragment noticeFeature = TourismFeatureFragment.newInstance(notice);
        views.add(feeFeature);
        views.add(distanceFeature);
        views.add(noticeFeature);


        TourismFeatureAdapter adapter = new TourismFeatureAdapter(_mActivity.getSupportFragmentManager(), titles, views);
        vpPager.setAdapter(adapter);
        tlTitle.setupWithViewPager(vpPager);
    }

    private void setInitData() {
        tvContext.setText(tourism.getContext());
        tvPrice.setText(tourism.getPrice() + "");

        Tourism.ExtendBean extend = tourism.getExtend();
        tvDestination.setText(extend.getRoute());
        tvTrafficDetail.setText(extend.getTraffic());
        tvAccommodationStandard.setText(extend.getAccommodation());
        tvJourneyAttractions.setText(extend.getAttractions());
        tvGatherLocation.setText(extend.getGather());
        tvFeature.setText(extend.getFeature());

        canBook();
    }

    private void canBook() {
        //如果这个服务不是自己创建的，就要去判断是否能够预约
        if (!User.creatorIsOwn(tourism.getCreatorId())) {
            String filter = "{\"where\":{\"creatorId\":\"" + User.getUserId() + "\",\"or\":[{\"status\":\"reserved\"},{\"status\":\"assigned\"}]}}";
            NetHelper.getApi()
                    .getOrdersOfProduct(tourism.getId(), filter)
                    .compose(RxUtils.handleResult())
                    .compose(RxUtils.applySchedule())
                    .subscribe(new RxSubscriber<List<ProductOrder>>(_mActivity) {
                        @Override
                        public void _next(List<ProductOrder> orders) {
                            if (orders.size() != 0) {
                                setHadReservation();
                            }
                        }
                    });
        }

    }


    /**
     * 预约这个旅游
     */
    private void bookTourism() {
        String filter = "{\"limit\":\"1\",\"where\":{\"categoryId\":\"" + categoryId + "\",\"creatorId\":\"" + User.getUserId() + "\"}}";
        subscription.add(NetHelper.getApi().getContacts(filter)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<Contact>>(_mActivity) {
                    @Override
                    public void _next(List<Contact> contacts) {
                        if (contacts.size() > 0) {
                            Contact contact = contacts.get(0);
                            statReservation(contact);
//                            ReservationFragment fragment = ReservationFragment.newInstance(categoryId, tourism.getId(), contact);
//                            startForResult(fragment, ReservationFragment.requestCode);
                        } else {
                            //添加个人资料界面
                            AddTourismInfoFragment fragment = AddTourismInfoFragment.newInstance(categoryId, null);
                            fragment.setProductId(tourism.getId());
                            start(fragment);
                        }
                    }
                }));

    }


    private void statReservation(Contact contact) {
        Intent intent = new Intent(_mActivity,ReservationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("contact",contact);
        bundle.putString("productCategoryId",categoryId);
        bundle.putString("productId",tourism.getId());
        intent.putExtras(bundle);
        startActivityForResult(intent,ReservationFragment.requestCode);
    }

//    @Override
//    protected void onFragmentResult(int reqCode, int resultCode, Bundle data) {
//        if (reqCode == ReservationFragment.requestCode && resultCode == ReservationFragment.resultCode) {
//            setHadReservation();
//        }
//    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Logger.d("requestCode =  "+requestCode+"----"+"resultCode = "+resultCode);
        if (requestCode == ReservationFragment.requestCode && resultCode == ReservationFragment.resultCode) {
            setHadReservation();
        }
    }

    private void setHadReservation() {
        btnBook.setText("已预约");
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    //
    public class TourismFeatureAdapter extends FragmentPagerAdapter {
        private List<BaseFragment> fragments;
        private String[] titles;

        public TourismFeatureAdapter(FragmentManager fm, String[] titles, List<BaseFragment> fragments) {
            super(fm);
            this.titles = titles;
            this.fragments = fragments;
        }

        @Override
        public BaseFragment getItem(int arg0) {
            return fragments.get(arg0);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}
