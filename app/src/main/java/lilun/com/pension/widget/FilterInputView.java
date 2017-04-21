package lilun.com.pension.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import lilun.com.pension.R;

/**
 * 输入区间
 *
 * @author yk
 *         create at 2017/4/7 14:53
 *         email : yk_developer@163.com
 */
public class FilterInputView extends LinearLayout {

    private String title;
    private EditText etInput;
    private TextView tvConfirm;
    private OnConfirmListener listener;
    private InputMethodManager imm;
    private TextView tvClear;

    public FilterInputView(Context context) {
        super(context);
        init(context);
    }


    public FilterInputView(Context context, String title) {
        super(context);
        init(context);
        this.title = title;
    }

    private void init(Context context) {
        imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        View view = LayoutInflater.from(context).inflate(R.layout.filter_input, this);
        etInput = (EditText) view.findViewById(R.id.et_input);
        tvConfirm = (TextView) view.findViewById(R.id.btn_confirm);
        tvClear = (TextView) view.findViewById(R.id.btn_clear);


        tvClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                etInput.setText("");
                listener.onConfirm(title, true);
                hintKeyBord();
            }
        });

        tvConfirm.setOnClickListener(v -> {
            String input = etInput.getText().toString();
            if (listener != null && !TextUtils.isEmpty(input)) {
                listener.onConfirm(input, false);
                hintKeyBord();
            }

        });
    }


    private void hintKeyBord() {
        boolean isOpen = imm.isActive();
        if (isOpen) {
            imm.hideSoftInputFromWindow(etInput.getWindowToken(), 0); //强制隐藏键盘
        }
    }

    /**
     * 设置单位
     */
    public void setHint(String hint) {
        etInput.setHint(hint);
    }

    public interface OnConfirmListener {
        void onConfirm(String input, boolean isDef);
    }

    public void setOnConfirmListener(OnConfirmListener listener) {
        this.listener = listener;
    }
}
