package lilun.com.pensionlife.ui.home.upload;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.orhanobut.logger.Logger;

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
        DownloadManager.getInstance().download(url, new DownLoadObserver() {
            @Override
            public void onNext(DownloadInfo downloadInfo) {
                super.onNext(downloadInfo);
                int progress = (int) downloadInfo.getProgress();
                int total = (int) downloadInfo.getTotal();
                notification.updateProgress(100, progress, total);



                if (progress == total) {
                    Logger.d("下载完成");
                    SystemUtils.installApk(DownLoadService.this, downloadInfo.getFilePath());
                    notification.cancel(100);
                }
            }

            @Override
            public void onCompleted() {
                super.onCompleted();

            }
        });
    }


}
