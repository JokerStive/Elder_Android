package lilun.com.pension.widget.image_loader;

import android.widget.ImageView;

/**
*图片加载封装
*@author yk
*create at 2017/2/28 15:38
*email : yk_developer@163.com
*/
public class ImageLoaderUtil {
    private static ImageLoaderUtil mInstance;
    private  BaseImageLoaderStrategy mStrategy;

    public ImageLoaderUtil() {
        mStrategy = new GlideImageLoaderStrategy();
    }


    public static ImageLoaderUtil instance(){
        if (mInstance==null){
            synchronized (ImageLoaderUtil.class){
                if (mInstance==null){
                    mInstance = new ImageLoaderUtil();
                }
            }
        }
        return mInstance;
    }

    public void loadImage(String url, int placeholder, ImageView imageView) {
        mStrategy.loadImage(imageView.getContext(), url, placeholder, imageView);
    }

    public void loadImage(String url,  ImageView imageView) {
        mStrategy.loadImage(url, imageView);
    }

    public void setStrategy(BaseImageLoaderStrategy strategy){
        this.mStrategy = strategy;
    }

}
