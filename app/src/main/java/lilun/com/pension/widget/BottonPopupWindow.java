package lilun.com.pension.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import lilun.com.pension.R;
import lilun.com.pension.app.App;
import lilun.com.pension.base.BaseActivity;
import lilun.com.pension.module.utils.UIUtils;

/**
 * 底部弹出选择框
 * Created by zp on 2017/4/20.
 */

public class BottonPopupWindow extends PopupWindow {

    TextView delete, cancel;
    Context mContext;

    public BottonPopupWindow(Context context) {
        this(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, context);
    }

    public BottonPopupWindow(int width, int height, Context context) {
        this(LayoutInflater.from(context).inflate(R.layout.popwindow_botton, null), width, height, context);

    }

    public BottonPopupWindow(View contentView, int width, int height, Context context) {
        super(contentView, width, height);
        mContext = context;
        setFocusable(true);
        setOutsideTouchable(true);
        setAnimationStyle(R.style.pop_botton);
        initUI(contentView);
    }

    private void initUI(View contentView) {
        delete = (TextView) contentView.findViewById(R.id.tv_delete);
        cancel = (TextView) contentView.findViewById(R.id.tv_cancel);
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

    public void setOnDeleteListener(View.OnClickListener deleteListener) {
        delete.setOnClickListener(deleteListener);
    }

    public void setOnCancelListener(View.OnClickListener cancelListener) {
        cancel.setOnClickListener(cancelListener);
    }
}
