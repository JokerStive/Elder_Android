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

import lilun.com.pensionlife.module.utils.ToastHelper;
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

    private String token;
    private Activity activity;
    private UploadManager uploadManager;
    private UploadListener listener;
    private int needOperateCount = 0;
    private OperateStatistics operateStatistics;
    private QinNiuPop pop;
    private String format = ".jpg";

    public QINiuEngine(Activity activity, int needOperateCount, String token, UploadListener listener) {
        this.needOperateCount = needOperateCount;
        this.listener = listener;
        this.activity = activity;
        this.token = token;
        operateStatistics = new OperateStatistics();
        uploadManager = new UploadManager();
    }


    public void upload(Operate operate) {
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
            uploadManager.put(bytes, operate.key, token, upCompletionHandler, options);
        }
//        uploadManager.put(operate.filePath, operate.key, token, upCompletionHandler, options);
    }

    public void upload(String filePath, int index, QiNiuUploadView view) {
        Operate operate = new Operate(filePath, index, view);
        operateStatistics.addStatistic(operate);
        upload(operate);
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
                uploadManager.put(bytes, filename + format, token, upCompletionHandler, options);
            else
                uploadManager.put(bytes, updateKey, token, upCompletionHandler, options);
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

    //
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
                continueUpload();
            });
        }
        pop.showAtLocation(operateStatistics.operates.get(0).view, Gravity.CENTER, 0, 0);
    }


    public void continueUpload() {
        List<Operate> operates = operateStatistics.operates;
        needOperateCount = 0;
        operateStatistics.hasOperatedCount = 0;
        for (int i = 0; i < operates.size(); i++) {
            Operate operate = operates.get(i);
            if (operate.status == QiNiuUploadView.UPLOAD_FALSE) {
                needOperateCount++;
                upload(operate);
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
        public int index;
        public String key;

        public Operate(String filePath, int index, QiNiuUploadView view) {
            this.view = view;
            this.filePath = filePath;
            this.index = index;
            if (index <= 10) {
                index = Integer.parseInt("0" + index);
            }
            this.key = index + format;
        }
    }

}
