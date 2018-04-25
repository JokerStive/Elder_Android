package lilun.com.pensionlife.ui.order;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.CommonPagerTitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.adapter.ViewPagerFragmentAdapter;
import lilun.com.pensionlife.widget.BadgesView;
import lilun.com.pensionlife.widget.NormalTitleBar;

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
    @Bind(R.id.titleBar)
    NormalTitleBar titleBar;
    int selectedColor = 0xffef620f;


    private String[] statusTitle;
    private ArrayList<Integer> normalDrawablesRes = new ArrayList();
    private ArrayList<Integer> selectedDrawablesRes = new ArrayList();
    private int[] status;
    private ArrayList<Integer> unReadMsgs = new ArrayList<>();

    public static OrderListFragment newInstance() {
        OrderListFragment fragment = new OrderListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void getTransferData(Bundle arguments) {
        super.getTransferData(arguments);
    }

    @Override
    protected void initPresenter() {
        statusTitle = getResources().getStringArray(R.array.order_condition);
        status = getResources().getIntArray(R.array.order_condition_value);

        normalDrawablesRes.add(R.drawable.undeal_off);
        normalDrawablesRes.add(R.drawable.undeal_off);
        normalDrawablesRes.add(R.drawable.deal_off);
        normalDrawablesRes.add(R.drawable.complete_off);
        normalDrawablesRes.add(R.drawable.cancel_off);

        selectedDrawablesRes.add(R.drawable.undeal_on);
        selectedDrawablesRes.add(R.drawable.undeal_on);
        selectedDrawablesRes.add(R.drawable.deal_on);
        selectedDrawablesRes.add(R.drawable.complete_on);
        selectedDrawablesRes.add(R.drawable.cancel_on);
        unReadMsgs.add(0);
        unReadMsgs.add(0);
        unReadMsgs.add(0);
        unReadMsgs.add(0);
        unReadMsgs.add(0);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragmenta_order_list;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        titleBar.setOnBackClickListener(this::pop);
        titleBar.setTitle(getString(R.string.my_orders));


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
        navigator.setAdjustMode(true);
        navigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return statusTitle.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                CommonPagerTitleView commonPagerTitleView = new CommonPagerTitleView(_mActivity);
                commonPagerTitleView.setContentView(R.layout.item_order_list_indictor);

                ImageView image = (ImageView) commonPagerTitleView.findViewById(R.id.iv_image);
                TextView title = (TextView) commonPagerTitleView.findViewById(R.id.tv_title);
                BadgesView msgs = (BadgesView) commonPagerTitleView.findViewById(R.id.bv_msgs);

                title.setText(statusTitle[index]);
                image.setImageResource(normalDrawablesRes.get(index));
                msgs.setText(unReadMsgs.get(index) + "");

                commonPagerTitleView.setOnPagerTitleChangeListener(new CommonPagerTitleView.OnPagerTitleChangeListener() {

                    @Override
                    public void onSelected(int i, int i1) {
                        title.setTextColor(selectedColor);
                        image.setImageResource(selectedDrawablesRes.get(i));
                        msgs.setText(unReadMsgs.get(i) + "");
                    }

                    @Override
                    public void onDeselected(int i, int i1) {
                        title.setTextColor(Color.BLACK);
                        image.setImageResource(normalDrawablesRes.get(i));
                    }

                    @Override
                    public void onLeave(int i, int i1, float v, boolean b) {

                    }

                    @Override
                    public void onEnter(int i, int i1, float v, boolean b) {

                    }
                });

                commonPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                    }
                });

                return commonPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setColors(selectedColor);

                indicator.setMode(LinePagerIndicator.MODE_MATCH_EDGE);

//                indicator.setBackgroundColor(Color.parseColor("#f1f1f1"));
                return indicator;
            }
        });
        indicator.setNavigator(navigator);

    }

    public void startRank(String productId) {
//        start(RankFragment.newInstance(Constants.organizationProduct, objectId));
    }


}
