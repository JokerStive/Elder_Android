package lilun.com.pension.module.utils;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.orhanobut.logger.Logger;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lilun.com.pension.app.App;
import lilun.com.pension.module.bean.IconModule;

/**
 * Created by youke on 2016/7/26.
 * 图片压缩
 */
public class BitmapUtils {

    // 根据路径获得图片并压缩，返回bitmap用于显示
    public static Bitmap getSmallBitmap(String filePath) {
        if (filePath.startsWith("content")){
            filePath = filePath.replace("content","file");
        }
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

        return bitmap;
    }


    //计算图片的缩放值
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    //把bitmap转换成String
    public static byte[] bitmapToString(String filePath) {
        byte[] b =null;
        Bitmap bm = getSmallBitmap(filePath);
        if (bm!=null){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 40, baos);
            b= baos.toByteArray();
            Logger.d("上传图片大小 = " + b.length / 1024 + "k");
        }
        return b;
    }


    //把bitmap转换成String
    public static byte[] bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 40, baos);
        byte[] b = baos.toByteArray();
        return b;
    }

    /**
     * 从模型中的picture字段获取单张pictureName
     */
    public static String picName(String pictureName) {
        if (pictureName!=null){
            List<IconModule> imgList = StringUtils.string2IconModule(pictureName);
            return imgList.get(0).getFileName();
        }
        return "";
    }
    /**
     * 从模型中的picture字段获取单张pictureName
     */
    public static String picName(ArrayList<IconModule> pictureName) {
        if (pictureName!=null && pictureName.size()>0){
            return pictureName.get(0).getFileName();
        }
        return "";
    }

    /**
     * 从模型中的picture字段获取多张张pictureName
     */
    public static List<String> picNames(String pictureName) {
        List<String> picNames = new ArrayList<>();
        if (pictureName!=null){
            List<IconModule> imgList = StringUtils.string2IconModule(pictureName);
            for (IconModule img : imgList) {
                picNames.add(img.getFileName());
            }
            return picNames;
        }
        return picNames;
    }


    /**
    *清空drawee对一条uri的缓存
    */
    public static void clearDraweeCache(Uri uri){
        Glide.with(App.context)
                .load(uri)
                .signature(new StringSignature(UUID.randomUUID().toString()));
//        ImagePipeline imagePipeline = Fresco.getImagePipeline();
//        imagePipeline.evictFromMemoryCache(uri);
//        imagePipeline.ev
//        imagePipeline.clearMemoryCaches();
//        imagePipeline.evictFromCache(uri);
    }

    /**
    *根据文件的uri的到文件真实的路径
    */
    public static String getRealPathFromURI(Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = App.context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);

            return path;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

}
