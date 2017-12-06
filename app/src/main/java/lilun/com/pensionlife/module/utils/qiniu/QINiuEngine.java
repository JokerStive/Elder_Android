package lilun.com.pensionlife.module.utils.qiniu;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import com.orhanobut.logger.Logger;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import lilun.com.pensionlife.module.bean.QINiuToken;
import lilun.com.pensionlife.module.bean.TokenParams;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.module.utils.ToastHelper;
import lilun.com.pensionlife.net.NetHelper;
import lilun.com.pensionlife.net.RxSubscriber;
import lilun.com.pensionlife.widget.CircleProgressView;
import lilun.com.pensionlife.widget.QinNiuPop;

/**
 * 骑牛图片管理
 *
 * @author yk
 *         create at 2017/11/6 9:20
 *         email : yk_developer@163.com
 */
public class QINiuEngine {

    private TokenParams tokenParams;
    private ArrayList<String> postFileTokenFileNames;
    private ArrayList<String> filePaths;
    private QINiuToken token;
    private long tokenInvalidTime;
    private Activity activity;
    private UploadManager uploadManager;
    private UploadListener listener;
    private int needOperateCount = 0;
    private OperateStatistics operateStatistics;
    private QinNiuPop pop;
    public static String format = ".jpg";

    public QINiuEngine(Activity activity, TokenParams tokenParams, ArrayList<String> filePaths, ArrayList<QiNiuUploadView> qiNiuUploadViews, UploadListener listener) {
        this.tokenParams = tokenParams;
        this.filePaths = filePaths;
        this.needOperateCount = filePaths.size();
        this.listener = listener;
        this.activity = activity;
        operateStatistics = new OperateStatistics();
        uploadManager = new UploadManager();

        if (this.filePaths == null || qiNiuUploadViews == null || this.filePaths.size() != qiNiuUploadViews.size()) {
            throw new RuntimeException("传入的数据异常");
        }

        init(qiNiuUploadViews);


    }


    public QINiuEngine(Activity activity, int needOperateCount, QINiuToken token, UploadListener listener) {
        this.needOperateCount = needOperateCount;
        this.listener = listener;
        this.activity = activity;
        this.token = token;
        operateStatistics = new OperateStatistics();
        uploadManager = new UploadManager();
    }

    private void init(ArrayList<QiNiuUploadView> qiNiuUploadViews) {
        postFileTokenFileNames = new ArrayList<>();
        for (int i = 0; i < filePaths.size(); i++) {

            //初始化文件名
            String currentTimeMillis = System.currentTimeMillis() + "";
            String fileName = i + currentTimeMillis;
            postFileTokenFileNames.add(fileName);

            //
            String filePath = filePaths.get(i);
            QiNiuUploadView qiNiuUploadView = qiNiuUploadViews.get(i);
            Operate operate = new Operate(filePath, fileName, qiNiuUploadView);
            operateStatistics.addStatistic(operate);

        }
    }


    /**
     * 获取七牛post文件的token
     */
    private void getPostFileToken(GetTokenCallback callback) {
        NetHelper.getApi().
                getPostFileToken(tokenParams.getModelName(), tokenParams.getModelId(), tokenParams.getTag(), postFileTokenFileNames)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<QINiuToken>() {
                    @Override
                    public void _next(QINiuToken qiNiuToken) {
                        setToken(qiNiuToken);
                        callback.onGetToken();
                    }
                });
    }

    private void setToken(QINiuToken qiNiuToken) {
        this.token = qiNiuToken;
        tokenInvalidTime = System.currentTimeMillis() + qiNiuToken.getTtl() * 1000;
    }

    /**
     * 获取七牛put文件的token
     */
    private void getPutFileToken(GetTokenCallback callback) {
        NetHelper.getApi().
                getPutFileToken(tokenParams.getModelName(), tokenParams.getModelId(), tokenParams.getTag(), getUploadFileNames(), true)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<QINiuToken>() {
                    @Override
                    public void _next(QINiuToken qiNiuToken) {
                        setToken(qiNiuToken);
                        callback.onGetToken();
                    }
                });
    }

    public interface GetTokenCallback {
        void onGetToken();
    }


    /**
     * 上传多张图片
     */
    public void postMultipleFile() {
        getPostFileToken(this::postMultiple);
    }

    /**
     * 上传多张图片
     */
    private void postMultiple() {
        for (Operate operate : operateStatistics.operates) {
            uploadWithOperate(operate);
        }
    }


    private void uploadWithOperate(Operate operate) {
        UploadOptions options = new UploadOptions(null, null, false,
                new UpProgressHandler() {
                    public void progress(String key, double percent) {
                        Log.i("qiniu", key + ": " + percent);
                        operate.view.setProgress(percent);
                    }
                }, null);

        UpCompletionHandler upCompletionHandler = new UpCompletionHandler() {

            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                //计数
                operateStatistics.statis();

                //每一张操作
                if (info.isOK()) {
                    operate.view.setStatus(QiNiuUploadView.UPLOAD_SUCCESS);
                    operate.status = QiNiuUploadView.UPLOAD_SUCCESS;
                } else {
                    Logger.e(info.toString());
                    operate.view.setStatus(QiNiuUploadView.UPLOAD_FALSE);
                    operate.status = QiNiuUploadView.UPLOAD_FALSE;
                }

                //是否所有操作完成
                if (operateStatistics.isAllOperated() && listener != null) {
                    if (operateStatistics.isAllisAllOperatedSuccess()) {
                        listener.onAllSuccess();
                    } else {
                        uploadAgain();
                    }
                }

            }
        };
        byte[] bytes = fileToJPGByteData(operate.filePath);
        if (bytes != null) {
            uploadManager.put(bytes, operate.key, token.getToken(), upCompletionHandler, options);
        }
    }


    private boolean isTokenInvalid() {
        return System.currentTimeMillis() > tokenInvalidTime;
    }


    /**
     * 上传单张图片
     *
     * @param filePath  文件路径
     * @param filename  文件名
     * @param updateKey 更新使用的key，若第一次上传设为空
     * @param cpvUpload 圆形上传进度view
     */
    public void uploadOnlyOne(String filePath, String filename, String updateKey, CircleProgressView cpvUpload) {

        UploadOptions options = new UploadOptions(null, null, false, new UpProgressHandler() {
            @Override
            public void progress(String key, double percent) {
                cpvUpload.setProgress((int) (percent * 100));
            }
        }, null);
        UpCompletionHandler upCompletionHandler = new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                cpvUpload.setVisibility(View.GONE);
                if (info.isOK()) {
                    Logger.d(info.statusCode + ":上传成功");
                    if (listener != null) {
                        listener.onAllSuccess();
                    }
                } else {
                    ToastHelper.get().showWareShort(info.statusCode + ":上传失败，请稍后再试");
                }
            }
        };
        //库压缩成JPG格式
        byte[] bytes = fileToJPGByteData(filePath);
        if (bytes != null) {
            cpvUpload.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(updateKey))
                uploadManager.put(bytes, filename + format, token.getToken(), upCompletionHandler, options);
            else
                uploadManager.put(bytes, updateKey, token.getToken(), upCompletionHandler, options);
        }
    }


    private byte[] fileToJPGByteData(String filePath) {
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 100;
        boolean compress = bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
        if (compress) {
            return baos.toByteArray();
        } else {
            ToastHelper.get().showWareShort("转换失败");
        }

        return null;
    }

    private void uploadAgain() {
        if (pop == null) {
            pop = new QinNiuPop(activity);
            pop.setOnPushListener(v -> {
                pop.dismiss();
                if (listener != null) {
                    listener.onAllSuccess();
                }
            });
            pop.setOnUploadListener(v -> {
                pop.dismiss();
                if (isTokenInvalid()) {
                    refreshTokenFirst();
                } else {
                    continueUpload();
                }

            });
        }
        pop.showAtLocation(operateStatistics.operates.get(0).view, Gravity.CENTER, 0, 0);
    }

    private void refreshTokenFirst() {
        getPutFileToken(this::continueUpload);
    }


    private ArrayList<String> getUploadFileNames() {
        ArrayList<String> uploadFileNames = new ArrayList<>();
        for (int i = 0; i < operateStatistics.operates.size(); i++) {
            Operate operate = operateStatistics.operates.get(i);
            if (operate.status == QiNiuUploadView.UPLOAD_FALSE) {
                uploadFileNames.add(operate.key);
            } else {
                uploadFileNames.add("null");
            }
        }
        return uploadFileNames;
    }


    private void continueUpload() {
        List<Operate> operates = operateStatistics.operates;
        needOperateCount = 0;
        operateStatistics.hasOperatedCount = 0;
        for (int i = 0; i < operates.size(); i++) {
            Operate operate = operates.get(i);
            if (operate.status == QiNiuUploadView.UPLOAD_FALSE) {
                needOperateCount++;
                uploadWithOperate(operate);
            }
        }
    }


    public interface UploadListener {
        void onAllSuccess();
    }

    class OperateStatistics {
        public int hasOperatedCount = 0;
        public List<Operate> operates;

        public OperateStatistics() {
            operates = new ArrayList<>();
        }

        public void addStatistic(Operate operate) {
            operates.add(operate);
        }

        public boolean isAllOperated() {
            return hasOperatedCount == needOperateCount;
        }

        public void statis() {
            hasOperatedCount++;
        }

        public boolean isAllisAllOperatedSuccess() {
            boolean result = true;
            for (Operate operate : operates) {
                if (operate.status == QiNiuUploadView.UPLOAD_FALSE) {
                    result = false;
                    break;
                }
            }

            return result;
        }
    }

    class Operate {
        public String filePath;
        public int status = QiNiuUploadView.LOCAL_SHOW;
        public QiNiuUploadView view;
        public String key;

        public Operate(String filePath, String fileName, QiNiuUploadView view) {
            this.view = view;
            this.filePath = filePath;
            this.key = fileName;
        }
    }

}
