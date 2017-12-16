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
    void loadImageWithoutCache(String url, ImageView imageView);
    void loadImage(int resId, ImageView imageView);

    void loadImage(String url, int placeholder, ImageView imageView);

    void loadImage(Context context, String url, int placeholder, ImageView imageView);
    /**
     * @param context
     * @param accountId
     * @param imageView
     * @param signature 缓存签名 为空不用缓存(采用时间段缓存策略)
     */
    void loadAvatar(Context context, String accountId, ImageView imageView, String signature);

    //清除硬盘缓存
    void clearDiskCache(final Context context);
    //清除内存缓存
    void clearMemoryCache(Context context);
}

