package lilun.com.pensionlife.widget.image_loader;

import android.content.Context;
import android.widget.ImageView;

/**
*@author yk
*create at 2017/2/28 15:41
*email : yk_developer@163.com
*/
public interface BaseImageLoaderStrategy {
    void loadImage(String url, ImageView imageView);
    void loadImage(int resId, ImageView imageView);

    void loadImage(String url, int placeholder, ImageView imageView);

    void loadImage(Context context, String url, int placeholder, ImageView imageView);

    //清除硬盘缓存
    void clearDiskCache(final Context context);
    //清除内存缓存
    void clearMemoryCache(Context context);
}

