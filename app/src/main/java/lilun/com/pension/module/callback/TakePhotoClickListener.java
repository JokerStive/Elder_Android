package lilun.com.pension.module.callback;

import java.util.List;

import lilun.com.pension.module.bean.TakePhotoResult;

/**
*返回图片后的回调
*@author yk
*create at 2017/2/27 11:33
*email : yk_developer@163.com
*/
public interface TakePhotoClickListener {
    void showPhotos(List<TakePhotoResult> results);
    void onAlbumClick();
}
