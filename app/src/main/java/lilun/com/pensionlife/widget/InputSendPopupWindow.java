package lilun.com.pensionlife.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import lilun.com.pensionlife.R;

/**
 * 编辑 带按钮的PopupWIndown
 * Created by zp on 2017/4/19.
 */

public class InputSendPopupWindow extends PopupWindow {
    Context mContext;
    InputSendView isvView;

    public InputSendPopupWindow(Context context) {
        this(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, context);
    }

    public InputSendPopupWindow(int width, int height, Context context) {
        this(LayoutInflater.from(context).inflate(R.layout.window_input_send, null), width, height, context);
    }

    public InputSendPopupWindow(View contentView, int width, int height, Context mContext) {
        super(contentView, width, height);
        this.mContext = mContext;
        setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x40000000);
        setBackgroundDrawable(dw);
        setAnimationStyle(R.style.pop_botton);
        setOutsideTouchable(true);
        initUI(contentView);
    }

    private void initUI(View contentView) {
        isvView = (InputSendView) contentView.findViewById(R.id.input_send_view);
    }


    public InputSendPopupWindow setOnSendListener(InputSendView.OnSendListener listener) {
        isvView.setOnSendListener(listener);
        return this;
    }



    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
    }


    @Override
    public void dismiss() {
        super.dismiss();
    }
}
