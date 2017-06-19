package lilun.com.pensionlife.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.App;

/**
 * 聊天输入框
 *
 * @author yk
 *         create at 2017/3/14 11:52
 *         email : yk_developer@163.com
 */
public class ChatInputView extends LinearLayout {
    private OnConfirmClickListener listenet;
    private EditText etInput;

    public ChatInputView(Context context) {
        super(context);
        init();
    }


    public ChatInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {
        View view = LayoutInflater.from(App.context).inflate(R.layout.normal_buttom_input, this);
        TextView tvConfirm = (TextView) view.findViewById(R.id.tv_confirm);
        etInput = (EditText) view.findViewById(R.id.et_input);
        etInput.setSelection(etInput.getText().toString().length());

        tvConfirm.setOnClickListener(v -> {
            if (listenet != null && !TextUtils.isEmpty(etInput.getText().toString())) {
                listenet.onConfirm(etInput.getText().toString());
            }
        });
    }

    public void setOnConfirmClickListener(OnConfirmClickListener listener) {
        this.listenet = listener;
    }

    public interface OnConfirmClickListener {
        void onConfirm(String input);
    }

    public void clear(){
        etInput.setText("");
        etInput.setSelection(0);
    }
}
