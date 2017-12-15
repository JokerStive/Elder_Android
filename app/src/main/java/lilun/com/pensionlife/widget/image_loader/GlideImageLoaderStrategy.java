package lilun.com.pensionlife.widget.image_loader;

import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.app.IconUrl;

/**
 * glide策略
 *
 * @author yk
 *         create at 2017/2/28 15:47
 *         email : yk_developer@163.com
 */
public class GlideImageLoaderStrategy implements BaseImageLoaderStrategy {


    @Override
    public void loadImage(String url, ImageView imageView) {
        Glide.with(imageView.getContext()).load(url).dontAnimate()
                .placeholder(App.context.getResources().getDrawable(R.drawable.icon_def))
                .error(App.context.getResources().getDrawable(R.drawable.icon_error))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .thumbnail(0.1f)
                .into(imageView);
    }

    @Override
    public void loadImageWithoutCache(String url, ImageView imageView) {
        Glide.with(imageView.getContext()).load(url).dontAnimate()
                .placeholder(App.context.getResources().getDrawable(R.drawable.icon_def))
                .error(App.context.getResources().getDrawable(R.drawable.icon_error))
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);
    }

    @Override
    public void loadImage(int resId, ImageView imageView) {
        Glide.with(imageView.getContext()).load(resId).dontAnimate()
                .placeholder(imageView.getBackground())
                .error(imageView.getBackground())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }


    @Override
    public void loadImage(String url, int placeholder, ImageView imageView) {
        loadNormal(imageView.getContext(), url, placeholder, imageView);
    }

    @Override
    public void loadImage(Context context, String url, int placeholder, ImageView imageView) {
        loadNormal(context, url, placeholder, imageView);
    }

    @Override
    public void clearDiskCache(Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(context.getApplicationContext()).clearDiskCache();
                    }
                }).start();
            } else {
                Glide.get(context.getApplicationContext()).clearDiskCache();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clearMemoryCache(Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                Glide.get(context.getApplicationContext()).clearMemory();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadNormal(final Context ctx, final String url, int placeholder, ImageView imageView) {
        Glide.with(ctx).load(url).dontAnimate()
                .placeholder(placeholder)
                .error(placeholder)
                .into(imageView);
    }

    /**
     * @param ctx
     * @param accountId
     * @param imageView
     * @param signature 缓存签名 为空不用缓存(采用时间段缓存策略)
     */
    public void loadAvatar(Context ctx, String accountId, ImageView imageView, String signature) {
        DrawableRequestBuilder<String> override = Glide.with(ctx).load(IconUrl.account(accountId)).dontAnimate()
                .thumbnail(0.1f)
                .error(R.drawable.icon_error)
                .override(400, 400);
        if (!TextUtils.isEmpty(signature)) {
            override.signature(new StringSignature(signature));
        } else {
            override.diskCacheStrategy(DiskCacheStrategy.NONE);
            override.skipMemoryCache(true);
        }
        override.into(imageView);
    }
}
