package lilun.com.pension.ui.residential.main;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

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
import lilun.com.pension.base.BaseActivity;
import lilun.com.pension.base.BaseFragment;

/**
 * Created by Administrator on 2017/3/7.
 */
public class OrderListActivity extends BaseActivity{


    @Bind(R.id.vp_container)
    ViewPager mViewPager;

    @Bind(R.id.indicator)
    MagicIndicator indicator;


    @Bind(R.id.iv_back)
    ImageView ivBack;


    private String[] statusTitle = {"已预约", "已受理", "已完成", "已取消"};
    private String[] status = {"reserved", "assigned", "done", "cancel"};

    @Override
    protected int getLayoutId() {
        return R.layout.fragmenta_order_list;
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initView() {
        initViewPager();
        initViewPager();
        initIndicator();
        ViewPagerHelper.bind(indicator, mViewPager);
        mViewPager.setCurrentItem(0);
        mViewPager.requestDisallowInterceptTouchEvent(true);

    }


    private void initViewPager() {
        List<BaseFragment> listFragments = new ArrayList<>();
        for (int i = 0; i < statusTitle.length; i++) {
            MyOderFragment fragment = MyOderFragment.newInstance(status[i]);
            listFragments.add(fragment);
        }
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return listFragments.get(position);
            }

            @Override
            public int getCount() {
                return listFragments.size();
            }
        });
    }


    /**
     * 初始化indicator并绑定viewpager
     */
    private void initIndicator() {
        CommonNavigator navigator = new CommonNavigator(this);
        navigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return statusTitle.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ColorTransitionPagerTitleView titleView = new ColorTransitionPagerTitleView(context);
                titleView.setNormalColor(Color.WHITE);
                titleView.setSelectedColor(Color.WHITE);
                titleView.setTextSize(17);
                titleView.setText(statusTitle[index]);
                titleView.setOnClickListener(view -> mViewPager.setCurrentItem(index));
                return titleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setColors(Color.WHITE);
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                return indicator;
            }
        });
        indicator.setNavigator(navigator);

    }

}
