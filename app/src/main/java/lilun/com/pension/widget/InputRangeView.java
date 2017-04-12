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
import lilun.com.pension.module.utils.Preconditions;
import lilun.com.pension.module.utils.ToastHelper;

/**
 * 输入区间
 *
 * @author yk
 *         create at 2017/4/7 14:53
 *         email : yk_developer@163.com
 */
public class InputRangeView extends LinearLayout {

    private EditText etMin;
    private EditText etMax;
    private TextView tvUnit;
    private TextView tvConfirm;
    private OnConfirmListener listener;
    private Integer intMin;
    private Integer intMax;
    private InputMethodManager imm;

    public InputRangeView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        View view = LayoutInflater.from(context).inflate(R.layout.input_range, this);
        etMin = (EditText) view.findViewById(R.id.et_min);
        etMax = (EditText) view.findViewById(R.id.et_max);
        tvUnit = (TextView) view.findViewById(R.id.tv_unit);
        tvConfirm = (TextView) view.findViewById(R.id.btn_confirm);

        tvConfirm.setOnClickListener(v -> {
            String min = etMin.getText().toString();
            String max = etMax.getText().toString();
            if (!TextUtils.isEmpty(min) && Preconditions.isNumeric(min)) {
                intMin = Integer.parseInt(min);
            }
            if (!TextUtils.isEmpty(max) && Preconditions.isNumeric(max)) {
                intMax = Integer.parseInt(max);
            }

            if (listener != null && (intMin != null || intMax != null)) {
                if (intMax != null && intMin != null) {
                    if (intMax > intMin) {
                        listener.onConfirm(intMin, intMax);
                    } else {
                        ToastHelper.get().showWareShort("输入有误");
                    }
                } else {
                    listener.onConfirm(intMin == null ? 0 : intMin, intMax);
                }

                hintKeyBord();
            }

        });
    }


    private void hintKeyBord() {
        boolean isOpen = imm.isActive();
        if (isOpen) {
            imm.hideSoftInputFromWindow(etMax.getWindowToken(), 0); //强制隐藏键盘
        }
    }

    /**
     * 设置单位
     */
    public void setUnit(String unit) {
        tvUnit.setText(unit);
    }

    public interface OnConfirmListener {
        void onConfirm(Integer min, Integer max);
    }

    public void setOnConfirmListener(OnConfirmListener listener) {
        this.listener = listener;
    }
}
