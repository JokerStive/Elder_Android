package lilun.com.pension.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import lilun.com.pension.R;
import lilun.com.pension.module.utils.ToastHelper;

/**
 * Created by zp on 2017/5/3.
 */

public class InputSendView extends LinearLayout {
    private final int inputBackgrand;
    private final int sendBackgrandOff, sendBackgrandOn;
    private final String inputHint;
    private final String sendText;
    Context context;
    OnSendListener listener;
    LinearLayout ll_layout;
    AppCompatEditText input;
    AppCompatButton send;

    public InputSendView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.InputSendView);
        inputBackgrand = array.getResourceId(R.styleable.InputSendView_input_backgrand, R.drawable.shape_rect_write_strock_corner_5);
        sendBackgrandOff = array.getResourceId(R.styleable.InputSendView_send_backgrand_off, R.drawable.shape_rect_write_corner);
        sendBackgrandOn = array.getResourceId(R.styleable.InputSendView_send_backgrand_on, R.drawable.shape_rect_red_corner_5);
        inputHint = array.getString(R.styleable.InputSendView_input_hint);
        sendText = array.getString(R.styleable.InputSendView_send_text);
        initUI(context);
    }

    private void initUI(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_input_send, this);
        ll_layout = (LinearLayout) view.findViewById(R.id.ll_layout);
        input = (AppCompatEditText) view.findViewById(R.id.acet_input);
        send = (AppCompatButton) view.findViewById(R.id.acbt_send);

        send.setBackgroundDrawable(ContextCompat.getDrawable(context, inputBackgrand));
        if (!TextUtils.isEmpty(inputHint))
            input.setHint(inputHint);


        send.setBackgroundDrawable(ContextCompat.getDrawable(context, sendBackgrandOff));

        if (!TextUtils.isEmpty(sendText))
            send.setText(sendText);

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
                    send.setBackgroundDrawable(ContextCompat.getDrawable(context, sendBackgrandOff));
                } else
                    send.setBackgroundDrawable(ContextCompat.getDrawable(context, sendBackgrandOn));
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(input.getText().toString().trim())) {
                    ToastHelper.get(context).showWareShort("您未输入内容");
                    return;
                }

                if (listener != null) {
                    listener.send(input.getText().toString().trim());
                    input.setText("");
                }
            }
        });
    }

    public void setOnSendListener(OnSendListener listener) {
        this.listener = listener;
    }

    public interface OnSendListener {
        void send(String sendStr);
    }
}
