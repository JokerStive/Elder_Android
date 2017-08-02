/*
package lilun.com.pension.widget.slider;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import lilun.com.pension.module.bean.IconModule;

*/
/**
*viewPager指示器
*@author yk
*create at 2017/2/28 18:19
*email : yk_developer@163.com
*//*

public class OnPageChangeListenerForInfiniteIndicator implements ViewPager.OnPageChangeListener {
    private Activity topic_activity;
    private List<ImageView> pageIndicatorList = new ArrayList<>();
    private List<IconModule> bannerList;
    private LinearLayout containerIndicator;
    private int viewPagerActivePosition;
    private int positionToUse = 0;
    private int actualPosition;

    public OnPageChangeListenerForInfiniteIndicator(Activity topic_activity, List<IconModule> bannerList, int currentItem) {
        this.topic_activity = topic_activity;
        this.bannerList = bannerList;
        this.actualPosition = currentItem;
        this.viewPagerActivePosition = currentItem;
        loadIndicators();
    }

    private void loadIndicators() {
        containerIndicator = (LinearLayout) topic_activity.findViewById(R.id.container_home_page_indicator);
        if (pageIndicatorList.size() < 1) {
            for (IconModule IconModule : bannerList) {
                ImageView imageView = new ImageView(topic_activity);
                imageView.setImageResource(R.drawable.banner_pagination_normal);// normal indicator image
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                imageView.setLayoutParams(new ViewGroup.LayoutParams(topic_activity.getResources().getDimensionPixelOffset(R.dimen.home_banner_indicator_width),
                        ViewGroup.LayoutParams.MATCH_PARENT));
                pageIndicatorList.add(imageView);
            }
        }
        containerIndicator.removeAllViews();
        for (int x = 0; x < pageIndicatorList.size(); x++) {
            ImageView imageView = pageIndicatorList.get(x);
            imageView.setImageResource(x == positionToUse ? R.drawable.banner_pagination_active :
                    R.drawable.banner_pagination_normal); // active and notactive indicator
            containerIndicator.addView(imageView);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        actualPosition = position;
        int positionToUseOld = positionToUse;
        if (actualPosition < viewPagerActivePosition && positionOffset < 0.5f) {
            positionToUse = actualPosition % bannerList.size();
        } else {
            if (positionOffset > 0.5f) {
                positionToUse = (actualPosition + 1) % bannerList.size();
            } else {
                positionToUse = actualPosition % bannerList.size();
            }
        }
        if (positionToUseOld != positionToUse) {
            loadIndicators();
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == 0) {
            viewPagerActivePosition = actualPosition;
            positionToUse = viewPagerActivePosition % bannerList.size();
            loadIndicators();
        }
    }
}*/
