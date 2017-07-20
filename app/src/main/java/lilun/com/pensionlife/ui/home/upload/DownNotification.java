package lilun.com.pensionlife.ui.home.upload;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.widget.RemoteViews;

import java.util.HashMap;
import java.util.Map;

import lilun.com.pensionlife.R;

/**
 * @author yk
 *         create at 2017/7/18 16:48
 *         email : yk_developer@163.com
 */
public class DownNotification {
    private Context mContext;
    private NotificationManager manager;
    private Map<Integer, Notification> map = null;

    public DownNotification(Context context) {
        this.mContext = context;
        manager = (NotificationManager) mContext
                .getSystemService(Context.NOTIFICATION_SERVICE);
        map = new HashMap<Integer, Notification>();
    }

    public void showNotification(int notificationId) {
        // 判断对应id的Notification是否已经显示， 以免同一个Notification出现多次
        if (!map.containsKey(notificationId)) {
            Notification notification = new Notification();
            notification.tickerText = "开始下载更新包";
            notification.when = System.currentTimeMillis();
            notification.icon = R.mipmap.icon;
            RemoteViews remoteViews = new RemoteViews(
                    mContext.getPackageName(),
                    R.layout.down_notification);
            notification.contentView = remoteViews;
            manager.notify(notificationId, notification);
            map.put(notificationId, notification);// 存入Map中
        }
    }

    public void cancel(int notificationId) {
        manager.cancel(notificationId);
        map.remove(notificationId);
    }

    public void updateProgress(int notificationId, int progress, int max) {
        Notification notify = map.get(notificationId);
        if (null != notify) {
            // 修改进度条
            float result = (float) progress / max * 100;
            notify.contentView.setProgressBar(R.id.progressBar,  max,  progress, false);
            notify.contentView.setTextViewText(R.id.tv_progress, "正在下载" + (int) result + "%");
            manager.notify(notificationId, notify);

        }
    }

}
