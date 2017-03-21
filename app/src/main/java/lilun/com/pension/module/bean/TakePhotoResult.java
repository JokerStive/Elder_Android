package lilun.com.pension.module.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.jph.takephoto.model.TImage;

/**
*选择或拍照返回数据模型
*@author yk
*create at 2017/2/27 10:43
*email : yk_developer@163.com
*/
public class TakePhotoResult extends MultiItemEntity{
    public static  final int TYPE_PHOTO=000;
    public static  final int TYPE_ADD=111;
    private String compressPath;
    private TImage.FromType from;

    public TImage.FromType getFrom() {
        return from;
    }

    public TakePhotoResult setFrom(TImage.FromType from) {
        this.from = from;
        return this;
    }

    private String originalPath;

    public String getCompressPath() {
        return compressPath;
    }

    public TakePhotoResult setCompressPath(String compressPath) {
        this.compressPath = compressPath;
        return this;
    }

    public String getOriginalPath() {
        return originalPath;
    }

    public TakePhotoResult setOriginalPath(String originalPath) {
        this.originalPath = originalPath;
        return this;
    }

    public TakePhotoResult(String originalPath, String compressPath, TImage.FromType from, int type) {
        setOriginalPath(originalPath);
        setCompressPath(compressPath);
        setItemType(type);
        setFrom(from);
    }

    public static TakePhotoResult of(String originalPath, String compressPath, TImage.FromType from, int type){
        return new TakePhotoResult(originalPath, compressPath,from,type);
    }

//    public enum FromType {
//        CAMERA, OTHER
//    }

}
