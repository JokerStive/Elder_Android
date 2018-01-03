package lilun.com.pensionlife.ui.home.upload;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.File;

import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.module.utils.SystemUtils;

/**
 * 更新的service
 *
 * @author yk
 *         create at 2017/7/18 14:21
 *         email : yk_developer@163.com
 */
public class DownLoadService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String url = intent.getStringExtra("url");
        startLoadApk(url);
        return super.onStartCommand(intent, flags, startId);

    }

    private void startLoadApk(String url) {
        DownNotification notification = new DownNotification(App.context);
        notification.showNotification(100);
        DownloadManager.getInstance().download(url, new DownLoadCallBack() {
            @Override
            public void onSuccess(File file) {
                SystemUtils.installApk(App.context, file);
                notification.cancel();
            }

            @Override
            public void onProgress(int progress, int total) {
                notification.updateProgress( progress, total);
            }

            @Override
            public void onFail(Throwable t) {

            }
        });
    }


}
