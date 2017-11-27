package lilun.com.pensionlife.widget.slider;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.App;
import me.relex.circleindicator.CircleIndicator;

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
    private CircleIndicator indictor;

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
        indictor = (CircleIndicator) view.findViewById(R.id.indicator);
    }

    public void setData(List<String> oldUrls) {
        List<String> urls = sortUrls(oldUrls);
        List<ImageView> items = new ArrayList<>();
        if (urls.size() == 0) {
            ImageView item = (ImageView) LayoutInflater.from(App.context).inflate(R.layout.image_view, null);
            Glide.with(getContext()).load(R.drawable.icon_def).dontAnimate()
                    .error(R.drawable.icon_def)
                    .placeholder(R.drawable.icon_def)
                    .fitCenter()
                    .into(item);
            items.add(item);
        }
        for (int i = 0; i < urls.size(); i++) {
            ImageView item = (ImageView) LayoutInflater.from(App.context).inflate(R.layout.image_view, null);
            Glide.with(getContext()).load(urls.get(i)).dontAnimate()
                    .error(R.drawable.icon_def)
                    .placeholder(R.drawable.icon_def)
                    .fitCenter()
                    .into(item);
            items.add(item);
        }

        mViewPager.setAdapter(new ViewPagerAdapter(items, context) {
        });
        if (urls.size() > 1) {
            indictor.setViewPager(mViewPager);
        }

    }

    /**
    *对图片的下载地址排序，也是垃圾代码
    */
    private List<String> sortUrls(List<String> oldUrls) {
        ArrayList<String> result = null;
        Map<Integer, String> resultMather;
        if (oldUrls != null && oldUrls.size() > 0) {
            result = new ArrayList<>();
            resultMather = new HashMap<>();
            for (String url : oldUrls) {
                String indexString = getUrlFileNameIndex(url);
                if (!TextUtils.isEmpty(indexString)) {
                    int index = Integer.parseInt(indexString);
                    resultMather.put(index, url);
                }

            }
            List<Map.Entry<Integer, String>> infoIds = new ArrayList<>(resultMather.entrySet());
            Collections.sort(infoIds, (o1, o2) -> (o1.getKey()).toString().compareTo(o2.getKey().toString()));
            for (Map.Entry<Integer, String> entry : resultMather.entrySet()) {
                result.add(entry.getValue());
            }
        }

        if (result == null) {
            result = new ArrayList<>();
        }
        return result;
    }

    /**
     * 获取图片排序的index
     */
    private String getUrlFileNameIndex(String url) {
        String result;
        result = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("."));
        Logger.i("图片index--" + result);
        return result;
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
