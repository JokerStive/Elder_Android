package lilun.com.pension.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

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
public class FilterInputRangeView extends LinearLayout {

    private String title;
    private EditText etMin;
    private EditText etMax;
    private TextView tvUnit;
    private TextView tvConfirm;
    private OnConfirmListener listener;
    private Integer intMin;
    private Integer intMax;
    private InputMethodManager imm;
    private String unit;
    private List<Integer> range = new ArrayList<>();
    private TextView tvClear;

//    public FilterInputRangeView(Context context) {
//        super(context);
//        init(context);
//    }


    public FilterInputRangeView(Context context, String title) {
        super(context);
        init(context);
        this.title = title;
    }

    private void init(Context context) {
        imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        View view = LayoutInflater.from(context).inflate(R.layout.input_range, this);
        etMin = (EditText) view.findViewById(R.id.et_min);
        etMax = (EditText) view.findViewById(R.id.et_max);
        tvUnit = (TextView) view.findViewById(R.id.tv_unit);
        tvConfirm = (TextView) view.findViewById(R.id.btn_confirm);
        tvClear = (TextView) view.findViewById(R.id.btn_clear);

        tvClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                etMax.setText("");
                etMin.setText("");
                intMax=null;
                intMax=null;
                range.clear();
                if (listener != null) {
                    listener.onConfirm(null, title,true);
                    hintKeyBord();
                }

            }
        });

        tvConfirm.setOnClickListener(v -> {
            String min = etMin.getText().toString();
            String max = etMax.getText().toString();
            intMax=null;
            intMin=null;
            if (!TextUtils.isEmpty(min) && Preconditions.isNumeric(min)) {
                intMin = Integer.parseInt(min);
            }
            if (!TextUtils.isEmpty(max) && Preconditions.isNumeric(max)) {
                intMax = Integer.parseInt(max);
            }

            if (listener != null && (intMin != null || intMax != null)) {
                range.clear();
                if (intMax != null && intMin != null) {
                    if (intMax > intMin) {
                        range.add(intMin);
                        range.add(intMax);
                        listener.onConfirm(range, intMin + "-" + intMax,false);
                    } else {
                        ToastHelper.get().showWareShort("输入有误");
                    }
                } else if (intMax != null) {
                    range.add(0);
                    range.add(intMax);
                    listener.onConfirm(range, intMax + tvUnit.getText().toString() + "以下",false);
                } else {
                    range.add(intMin);
                    range.add(Integer.MAX_VALUE);
                    listener.onConfirm(range, intMin + tvUnit.getText().toString() + "以上",false);
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
        this.unit = unit;
        tvUnit.setText(unit);
    }

    public interface OnConfirmListener {
        void onConfirm(List<Integer> range, String show, boolean isDef);
    }

    public void setOnConfirmListener(OnConfirmListener listener) {
        this.listener = listener;
    }
}
