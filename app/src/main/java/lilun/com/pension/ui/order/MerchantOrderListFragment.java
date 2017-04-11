package lilun.com.pension.ui.order;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import lilun.com.pension.R;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.adapter.ViewPagerFragmentAdapter;
import lilun.com.pension.widget.NormalTitleBar;
import lilun.com.pension.widget.SearchTitleBar;

/**
 * 订单列表页面
 *
 * @author yk
 *         create at 2017/3/6 17:08
 *         email : yk_developer@163.com
 */
public class MerchantOrderListFragment extends BaseFragment {

    @Bind(R.id.indicator)
    MagicIndicator indicator;

    @Bind(R.id.vp_container)
    ViewPager mViewPager;
    @Bind(R.id.searchBar)
    SearchTitleBar searchBar;
    @Bind(R.id.titleBar)
    NormalTitleBar titleBar;


    private String[] statusTitle = {"已预约", "已受理", "已延期", "已完成", "已取消"};
    private String[] status = {"reserved", "assigned", "delay", "done", "cancel"};
    private String productId;

    public static MerchantOrderListFragment newInstance(String productId) {
        MerchantOrderListFragment fragment = new MerchantOrderListFragment();
        Bundle args = new Bundle();
        args.putString("productId", productId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void getTransferData(Bundle arguments) {
        productId = arguments.getString("productId");
    }

    @Override
    protected void initPresenter() {
        statusTitle = getResources().getStringArray(R.array.merchant_order_condition);
        status = getResources().getStringArray(R.array.merchant_order_condition_value);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragmenta_order_list;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        titleBar.setOnBackClickListener(this::pop);
        searchBar.isChangeLayout(false);
        searchBar.setOnItemClickListener(new SearchTitleBar.OnItemClickListener() {
            @Override
            public void onBack() {
                pop();
            }

            @Override
            public void onSearch(String searchStr) {

            }

            @Override
            public void onChangeLayout(SearchTitleBar.LayoutType layoutType) {

            }
        });


        initViewPager();
        initIndicator();
        ViewPagerHelper.bind(indicator, mViewPager);
        mViewPager.setCurrentItem(0);
        mViewPager.requestDisallowInterceptTouchEvent(true);

    }

    private void initViewPager() {
        List<BaseFragment> listFragments = new ArrayList<>();
        for (int i = 0; i < statusTitle.length; i++) {
            MerchantOrderPageFragment fragment = MerchantOrderPageFragment.newInstance(status[i],productId);
            listFragments.add(fragment);
        }
        mViewPager.setAdapter(new ViewPagerFragmentAdapter(getChildFragmentManager(), listFragments) {
        });
    }


    /**
     * 初始化indicator并绑定viewpager
     */
    private void initIndicator() {
        CommonNavigator navigator = new CommonNavigator(getActivity());
        navigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return statusTitle.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ColorTransitionPagerTitleView titleView = new ColorTransitionPagerTitleView(context);
                titleView.setNormalColor(Color.BLACK);
                titleView.setSelectedColor(getResources().getColor(R.color.red));
                titleView.setTextSize(17);
                titleView.setText(statusTitle[index]);
                titleView.setOnClickListener(view -> mViewPager.setCurrentItem(index));
                return titleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setColors(getResources().getColor(R.color.red));
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                return indicator;
            }
        });
        indicator.setNavigator(navigator);

    }

}
