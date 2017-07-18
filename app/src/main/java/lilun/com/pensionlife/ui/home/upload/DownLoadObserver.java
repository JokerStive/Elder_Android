package lilun.com.pensionlife.ui.home.upload;

import rx.Observer;

/**
* 
*@author yk
*create at 2017/7/18 10:08
*email : yk_developer@163.com
*/
public abstract class DownLoadObserver implements Observer<DownloadInfo> {

    @Override
    public void onNext(DownloadInfo downloadInfo) {
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }


    @Override
    public void onCompleted() {
//        onCompleted(DownloadInfo downloadInfo);
    }
}
