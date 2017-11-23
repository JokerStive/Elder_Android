package lilun.com.pensionlife.module.utils.qiniu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.widget.CircleProgressView;

/**
 * 七牛上传显示的view
 *
 * @author yk
 *         create at 2017/11/7 11:54
 *         email : yk_developer@163.com
 */
public class QiNiuUploadView extends RelativeLayout {

    public static final int LOCAL_SHOW = 0;
    public static final int UPLOADING = 2;
    public static final int UPLOAD_SUCCESS = 3;
    public static final int UPLOAD_FALSE = 4;

    private int mStatus = LOCAL_SHOW;
    private QiNiuUploadImageView mImageView;
    private ImageView mDelete;
    //    private FrameLayout mCover;
    private int status;
    private ImageView mError;
    private CircleProgressView progressView;

    public QiNiuUploadView(Context context) {
        super(context);
        init();
    }

    public QiNiuUploadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.qiniu_upload_view, this);
        mImageView = (QiNiuUploadImageView) view.findViewById(R.id.image);
        mDelete = (ImageView) view.findViewById(R.id.delete);
        mError = (ImageView) view.findViewById(R.id.error);
        progressView = (CircleProgressView) view.findViewById(R.id.progressBar);

//        mCover = (FrameLayout) view.findViewById(R.id.cover);

        setStatus(LOCAL_SHOW);
    }

    public void setStatus(int status) {
        this.mStatus = status;
        switch (mStatus) {

            //本地显示的时候
            case LOCAL_SHOW:
                mDelete.setVisibility(VISIBLE);
                mError.setVisibility(GONE);
                progressView.setVisibility(GONE);
                break;

            case UPLOADING:
                mDelete.setVisibility(GONE);
                mError.setVisibility(GONE);
                progressView.setVisibility(VISIBLE);
                progressView.runProgressAnim(2500);
                break;

            case UPLOAD_SUCCESS:
                setProgress(100);
                mDelete.setVisibility(GONE);
                mError.setVisibility(GONE);
                progressView.setVisibility(GONE);
                break;

            case UPLOAD_FALSE:
                mImageView.setProgress(0);
                mDelete.setVisibility(GONE);
                mError.setVisibility(VISIBLE);
                progressView.setVisibility(GONE);
                break;
        }
    }

    public void setProgress(double progress) {
        if (mStatus != UPLOADING) {
            setStatus(UPLOADING);
        }
        progressView.setProgress((int) progress * 100);
        mImageView.setProgress((int) progress);
    }


}
