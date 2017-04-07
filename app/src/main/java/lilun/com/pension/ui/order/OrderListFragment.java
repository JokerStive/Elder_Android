package lilun.com.pension.ui.order;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import butterknife.ButterKnife;
import lilun.com.pension.R;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.adapter.ViewPagerFragmentAdapter;
import lilun.com.pension.widget.SearchTitleBar;

/**
 * 订单列表页面
 *
 * @author yk
 *         create at 2017/3/6 17:08
 *         email : yk_developer@163.com
 */
public class OrderListFragment extends BaseFragment {

    @Bind(R.id.indicator)
    MagicIndicator indicator;

    @Bind(R.id.vp_container)
    ViewPager mViewPager;
    @Bind(R.id.searchBar)
    SearchTitleBar searchBar;


    private String[] statusTitle = {"已预约", "已受理", "已完成", "已取消"};
    private String[] status = {"reserved", "assigned", "done", "cancel"};

    @Override
    protected void initPresenter() {
        statusTitle = getResources().getStringArray(R.array.order_condition);
        status = getResources().getStringArray(R.array.order_condition_value);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragmenta_order_list;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
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
            OrderPageFragment fragment = OrderPageFragment.newInstance(status[i]);
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
