package lilun.com.pensionlife.widget;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.module.utils.ToastHelper;
import lilun.com.pensionlife.widget.numberKeyBoard.KeyboardUtil;
import lilun.com.pensionlife.widget.numberKeyBoard.MyKeyBoardView;

/**
 * 输入区间
 *
 * @author yk
 *         create at 2017/4/7 14:53
 *         email : yk_developer@163.com
 */
public class FilterPriceView extends LinearLayout {

    private String title;
    private EditText etMin;
    private EditText etMax;
    private TextView tvUnit;
    private TextView tvConfirm;
    private OnConfirmListener listener;
    private Double intMin;
    private Double intMax;
    private InputMethodManager imm;
    private String unit;
    private List<Double> range = new ArrayList<>();
    private TextView tvClear;
    private MyKeyBoardView myKeyBoard;
    private KeyboardUtil keyboardUtil;
    private LinearLayout llClear;

//    public FilterInputRangeView(Context context) {
//        super(context);
//        init(context);
//    }


    public FilterPriceView(Activity context, String title) {
        super(context);
        init(context);
        this.title = title;
    }

    private void init(Activity context) {
        keyboardUtil = new KeyboardUtil(context);

        imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        View view = LayoutInflater.from(context).inflate(R.layout.include_price_select, this);

        etMin = (EditText) view.findViewById(R.id.tv_min_price);
        etMax = (EditText) view.findViewById(R.id.tv_max_price);
        tvConfirm = (TextView) view.findViewById(R.id.tv_complete);
        tvClear = (TextView) view.findViewById(R.id.tv_clear);
        llClear = (LinearLayout) view.findViewById(R.id.ll_clear);
//        myKeyBoard = (MyKeyBoardView) view.findViewById(R.id.custom_key_board);

//        keyboardUtil.attachTo(etMin);e
//        etMin.performClick();

        etMin.setOnTouchListener((v, event) -> {
            llClear.setVisibility(VISIBLE);
            keyboardUtil.attachTo(etMin);
            return false;
        });


        etMax.setOnTouchListener((v, event) -> {
            llClear.setVisibility(VISIBLE);
            keyboardUtil.attachTo(etMax);
            return false;
        });


        tvClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hintKeyBoard();
                clear(false);
            }
        });

        tvConfirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm();
            }
        });

        keyboardUtil.setOnCancelClick(new KeyboardUtil.onCancelClick() {
            @Override
            public void onCancellClick() {
                clear(true);
            }
        });

        keyboardUtil.setOnOkClick(new KeyboardUtil.OnOkClick() {
            @Override
            public void onOkClick() {
                confirm();
            }
        });

    }

    private void confirm() {
        String min = etMin.getText().toString();
        String max = etMax.getText().toString();
        intMax = null;
        intMin = null;
//        if (!TextUtils.isEmpty(min) && RegexUtils.checkDecimals(min)) {
//            intMin = (int)Float.parseFloat(min);
//        }
//
        if (!TextUtils.isEmpty(min)) {
            intMin = Double.parseDouble(min);
        }
        if (!TextUtils.isEmpty(max)) {
            intMax = Double.parseDouble(max);
        }

        if (listener != null && (intMin != null || intMax != null)) {
            if (intMax != null && intMin != null) {
                if (intMax > intMin) {
                    range.add(intMin);
                    range.add(intMax);
                    listener.onConfirm(range, filter(intMin) + "-" + filter(intMax), false);
                    hintKeyBoard();
                } else {
                    ToastHelper.get().showWareShort("输入有误");
                }
            } else if (intMax != null) {
                range.add(0.00);
                range.add(intMax);
                listener.onConfirm(range, filter(intMax) + unit + "以下", false);
                hintKeyBoard();
            } else {
                range.add(intMin);
                range.add(Double.MAX_VALUE);
                listener.onConfirm(range, filter(intMin) + unit + "以上", false);
                hintKeyBoard();
            }
            range.clear();

        } else {
            hintKeyBoard();
            clear(true);
        }

    }

    private String filter(Double target) {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(target);
    }

    private void hintKeyBoard() {
//        keyboardUtil.hideKeyboard();
//        llClear.setVisibility(GONE);
    }

    private void clear(boolean isHint) {
        etMax.setText("");
        etMin.setText("");
        intMax = null;
        intMax = null;
        range.clear();
        if (listener != null) {
            listener.onConfirm(null, isHint ? null : title, true);
            hintKeyBoard();
//            hintKeyBord();
        }
    }


//    private void hintKeyBord() {
//        boolean isOpen = imm.isActive();
//        if (isOpen) {
//            imm.hideSoftInputFromWindow(etMax.getWindowToken(), 0); //强制隐藏键盘
//        }
//    }

    /**
     * 设置单位
     */
    public void setUnit(String unit) {
        this.unit = unit;
//        tvUnit.setText(unit);
    }

//    public interface OnConfirmListener {
//        void onConfirm(List<Integer> range, String show, boolean isDef);
//    }


    public interface OnConfirmListener {
        void onConfirm(List<Double> range, String show, boolean isDef);
    }

    public void setOnConfirmListener(OnConfirmListener listener) {
        this.listener = listener;
    }
}
