package lilun.com.pensionlife.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.base.BaseActivity;
import lilun.com.pensionlife.module.utils.UIUtils;

/**
 * 图片上传失败弹框
 *
 * @author yk
 *         create at 2017/11/14 11:40
 *         email : yk_developer@163.com
 */
public class QinNiuPop extends PopupWindow {

    TextView directPush, continueUpload;
    Context mContext;

    public QinNiuPop(Context context) {
        this(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, context);
    }

    public QinNiuPop(int width, int height, Context context) {
        this(LayoutInflater.from(context).inflate(R.layout.popwindow_qiniu, null), width, height, context);

    }

    public QinNiuPop(View contentView, int width, int height, Context context) {
        super(contentView, width, height);
        mContext = context;
        setFocusable(true);
        setOutsideTouchable(true);
        setAnimationStyle(R.style.pop_botton);
        initUI(contentView);
    }

    private void initUI(View contentView) {
        directPush = (TextView) contentView.findViewById(R.id.tv_direct_push);
        continueUpload = (TextView) contentView.findViewById(R.id.tv_continue_upload);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        UIUtils.setBackgroundAlpha((BaseActivity) mContext, 0.7f);
        super.showAsDropDown(anchor, xoff, yoff, gravity);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        UIUtils.setBackgroundAlpha((BaseActivity) mContext, 0.7f);
        super.showAtLocation(parent, gravity, x, y);
    }

    @Override
    public void dismiss() {
        UIUtils.setBackgroundAlpha((BaseActivity) mContext, 1f);
        super.dismiss();
    }

    public void setOnPushListener(View.OnClickListener deleteListener) {
        directPush.setOnClickListener(deleteListener);
    }

    public void setOnUploadListener(View.OnClickListener cancelListener) {
        continueUpload.setOnClickListener(cancelListener);
    }
}
