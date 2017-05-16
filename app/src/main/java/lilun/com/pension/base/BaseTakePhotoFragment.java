package lilun.com.pension.base;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.LubanOptions;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.ArrayList;

import lilun.com.pension.app.Config;
import lilun.com.pension.module.callback.TakePhotoClickListener;
import lilun.com.pension.widget.TakePhotoLayout;

public abstract class BaseTakePhotoFragment<T extends IPresenter> extends BaseFragment implements TakePhoto.TakeResultListener, InvokeListener, TakePhotoClickListener {

    private TakePhoto takePhoto;
    private InvokeParam invokeParam;
    protected T mPresenter;
    private Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "image.jpg"));
    private TakePhotoLayout takePhotoLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 获取TakePhoto实例
     *
     * @return
     */
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        LubanOptions option = new LubanOptions.Builder()
                .setMaxHeight(Config.uploadPhotoMaxHeight)
                .setMaxWidth(Config.uploadPhotoMaxWidth)
                .setMaxSize(Config.uploadPhotoMaxSize)
                .create();
        CompressConfig config = CompressConfig.ofLuban(option);
        takePhoto.onEnableCompress(config, true);
        return takePhoto;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(getActivity(), type, invokeParam, this);
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }


    @Override
    public void onCameraClick() {
        File file;
        file = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
        Uri uri = Uri.fromFile(file);
        Logger.d("拍照图片的存储地址" + uri.getAuthority());
        getTakePhoto().onPickFromCapture(uri);
    }

    @Override
    public void onAlbumClick() {
        if (takePhotoLayout != null) {
            getTakePhoto().onPickMultiple(takePhotoLayout.getEnableDataCount());
        }
    }


    @Override
    public void takeSuccess(TResult result) {
        onTakePhotoSuccess(result);
    }

    protected abstract void onTakePhotoSuccess(TResult tResult);

    @Override
    public void takeFail(TResult result, String msg) {
        Logger.d("take photo fail " + msg);
    }

    @Override
    public void takeCancel() {
        Logger.d("take photo cancel ");
    }

    protected void setTakePhotoLayout(TakePhotoLayout takePhotoLayout) {
        this.takePhotoLayout = takePhotoLayout;
    }

    protected ArrayList<String> getPhotoData() {
        if (takePhotoLayout != null) {
            return takePhotoLayout.getData();
        }

        return null;
    }
}
