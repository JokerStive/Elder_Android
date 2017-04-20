package lilun.com.pension.widget;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import lilun.com.pension.R;
import lilun.com.pension.module.utils.ToastHelper;

/**
 * 编辑 带按钮的PopupWIndown
 * Created by zp on 2017/4/19.
 */

public class InputSendPopupWindow extends PopupWindow {
    Context mContext;
    OnSendListener listener;
    LinearLayout ll_layout;
    AppCompatEditText input;
    AppCompatButton send;

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
//        ColorDrawable dw = new ColorDrawable(0x40000000);
//        setBackgroundDrawable(dw);
        setOutsideTouchable(true);
        initUI(contentView);
    }

    private void initUI(View contentView) {
        ll_layout = (LinearLayout) contentView.findViewById(R.id.ll_layout);
        input = (AppCompatEditText) contentView.findViewById(R.id.acet_input);
        send = (AppCompatButton) contentView.findViewById(R.id.acbt_send);
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() == 0) {
                    send.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.shape_rect_write_corner));
                }
                send.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.shape_rect_red_corner));
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(input.getText().toString().trim())) {
                    ToastHelper.get(mContext).showWareShort("您未输入回回复内容");
                    return;
                }

                if (listener != null) {
                    listener.send(input.getText().toString().trim());
                }
            }
        });

    }

    public void clearInput() {
        input.setText("");
    }

    public InputSendPopupWindow setOnSendListener(OnSendListener listener) {
        this.listener = listener;
        return this;
    }

    public void setBackgrand(float alpha) {
        WindowManager.LayoutParams params = ((Activity) mContext).getWindow().getAttributes();
        params.alpha = alpha;

        ((Activity) mContext).getWindow().setAttributes(params);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        setBackgrand(0.7f);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        setBackgrand(1f);
    }


    public interface OnSendListener {
        void send(String sendStr);
    }


}
