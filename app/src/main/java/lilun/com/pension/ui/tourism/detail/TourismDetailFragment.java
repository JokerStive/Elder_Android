package lilun.com.pension.ui.tourism.detail;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import lilun.com.pension.R;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.bean.Tourism;
import lilun.com.pension.module.utils.Preconditions;
import lilun.com.pension.widget.slider.BannerPager;

/**
 * 旅游详情V
 *
 * @author yk
 *         create at 2017/4/13 11:31
 *         email : yk_developer@163.com
 */
public class TourismDetailFragment extends BaseFragment {

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
    }

    private void setTabLayout() {
       for(int i = 0; i < titles.length; i++){
           tlTitle.addTab(tlTitle.newTab().setText(titles[i]));
       }
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

    }



}
