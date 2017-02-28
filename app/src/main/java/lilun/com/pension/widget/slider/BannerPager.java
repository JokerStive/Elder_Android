package lilun.com.pension.widget.slider;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import lilun.com.pension.R;
import lilun.com.pension.app.App;
import lilun.com.pension.widget.image_loader.ImageLoaderUtil;

/**
 * 滑动带indicator 的viewpager
 *
 * @author yk
 *         create at 2017/2/28 18:33
 *         email : yk_developer@163.com
 */
public class BannerPager extends RelativeLayout {

    private Context context;
    private ViewPager mViewPager;
    private LinearLayout container;

    public BannerPager(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public BannerPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        View view = LayoutInflater.from(App.context).inflate(R.layout.banner_pager, this);
        mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
        container = (LinearLayout) view.findViewById(R.id.indicator_container);
    }

    public void setData(List<String> urls) {
        List<ImageView> items = new ArrayList<>();
        for (int i = 0; i < urls.size(); i++) {
            ImageView item = new ImageView(getContext());
            item.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            ImageLoaderUtil.instance().loadImage(urls.get(i),0,item);
            Logger.d(urls.get(i));
//            item.setTag(111,urls.get(i));
            items.add(item);
        }

        mViewPager.setAdapter(new ViewPagerAdapter(items, context) {
        });

    }

    public class ViewPagerAdapter extends PagerAdapter {
        private List<ImageView> views;
        private Context context;

        public ViewPagerAdapter(List<ImageView> views, Context context) {
            this.views = views;
            this.context = context;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = views.get(position);
//            ImageLoaderUtil.instance().loadImage((String) imageView.getTag(111), 0, imageView);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return (view == o);
        }
    }
}
