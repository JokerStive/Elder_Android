package lilun.com.pensionlife.ui.home.upload;

/**
 * Created by 陈丰尧 on 2017/2/2.
 * 下载信息
 */

public class DownloadInfo {
    /**
     * 文件大小
     */
    long total;
    /**
     * 已下载大小
     */
    long progress;

    public long getProgress() {
        return progress;
    }

    public long getTotal() {
        return total;
    }

    public DownloadInfo(long total, long progress) {
        this.total = total;
        this.progress = progress;
    }
}
