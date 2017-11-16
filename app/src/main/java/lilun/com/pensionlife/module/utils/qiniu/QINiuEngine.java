package lilun.com.pensionlife.module.utils.qiniu;

import android.util.Log;

import com.orhanobut.logger.Logger;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONObject;

import java.util.ArrayList;

import lilun.com.pensionlife.app.ConfigUri;
import lilun.com.pensionlife.module.bean.QINiuToken;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.net.NetHelper;
import lilun.com.pensionlife.net.RxSubscriber;

/**
 * 骑牛图片管理
 *
 * @author yk
 *         create at 2017/11/6 9:20
 *         email : yk_developer@163.com
 */
public class QINiuEngine {

    private final UploadManager uploadManager;
    private ArrayList<String> filePaths;
    ArrayList<String> hasUploadFilePaths;
    ArrayList<String> errorUploadFilePaths;
    ArrayList<String> hasOperateUploadFilePaths;

    public QINiuEngine(ArrayList<String> filePaths) {
        this.filePaths = filePaths;
        hasUploadFilePaths = new ArrayList<>();
        errorUploadFilePaths = new ArrayList<>();
        hasOperateUploadFilePaths = new ArrayList<>();
        uploadManager = new UploadManager();
    }

    public String createUploadTokenUrl(String modelName, String modelId, String tag) {
        return ConfigUri.BASE_URL + "/" + modelName + "/" + "modelId" + "/" + "upload" + tag + "Accesstoken";
    }

    public void getUploadToken(String modelName, String modelId, String tag) {
        NetHelper.getApi().getUploadToken(modelName, modelId, tag)
                .compose(RxUtils.applySchedule())
                .compose(RxUtils.handleResult())
                .subscribe(new RxSubscriber<QINiuToken>() {
                    @Override
                    public void _next(QINiuToken qiNiuToken) {

                    }
                });
    }

    public interface getUploadTokenCallBack {
        void onGetUploadTokenSuccess(QINiuToken qiNiuToken);

        void onGetUploadTokenFail(String failInfo);
    }


    public void uploadImages(String filePath, String token, String modelName, String modelId, String tag, UploadCallBack callBack) {
        String substring = filePath.substring(filePath.lastIndexOf("/") + 1);
        String uploadKey = modelName + "/" + tag + "/" + modelId + "/" + substring;
        Logger.i("upload key --" + uploadKey);

        UploadOptions options = new UploadOptions(null, null, false,
                new UpProgressHandler() {
                    public void progress(String key, double percent) {
                        Log.i("qiniu", key + ": " + percent);
                        callBack.onUploadProgress(key, percent);
                    }
                }, null);

        UpCompletionHandler upCompletionHandler = new UpCompletionHandler() {


            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                hasOperateUploadFilePaths.add(filePath);
                if (info.isOK()) {
                    hasUploadFilePaths.add(filePath);
                    callBack.onUploadSuccess(key);
                } else {
                    errorUploadFilePaths.add(filePath);
                    callBack.onUploadFail(key, info.error);
                }
            }
        };


        uploadManager.put(filePath, uploadKey, token, upCompletionHandler, options);
    }


    public boolean isAllSuccess() {
        return hasUploadFilePaths.size() == filePaths.size();
    }

    public boolean needUploadAgain() {
        int size = filePaths.size();
        return hasOperateUploadFilePaths.size() == size && errorUploadFilePaths.size() > 0;
    }


    public ArrayList<String> getNeedUploadFilePaths() {
        return filePaths;
    }

    public ArrayList<String> uploadAgain() {
        filePaths = errorUploadFilePaths;
        Logger.i("需要重新上传的图片张数--" + errorUploadFilePaths.size());
        hasOperateUploadFilePaths.clear();
        errorUploadFilePaths = new ArrayList<>();
        hasUploadFilePaths.clear();
        return filePaths;
    }

    public interface UploadCallBack {
        void onUploadSuccess(String filePath);

        void onUploadFail(String filePath, String failInfo);

        void onUploadProgress(String filePath, double percent);
    }
}
