package lilun.com.pension.module.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
*选择或拍照返回数据模型
*@author yk
*create at 2017/2/27 10:43
*email : yk_developer@163.com
*/
public class TakePhotoResult extends MultiItemEntity{
    public static  final int TYPE_PHOTO=0;
    public static  final int TYPE_ADD=1;
    private String compressPath;

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

    public TakePhotoResult(String originalPath, String compressPath,int type) {
        setOriginalPath(originalPath);
        setCompressPath(compressPath);
        setItemType(type);
    }

    public static TakePhotoResult of(String originalPath,String compressPath,int type){
        return new TakePhotoResult(originalPath, compressPath,type);
    }


}
