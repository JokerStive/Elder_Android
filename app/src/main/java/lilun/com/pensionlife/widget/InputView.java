package lilun.com.pensionlife.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.App;

/**
 * 带标题的输入框
 *
 * @author yk
 *         create at 2017/2/16 18:50
 *         email : yk_developer@163.com
 */
public class InputView extends RelativeLayout {


    TextView tvInputTitle;
    EditText etInput;
    private  boolean allowInput;


    public InputView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.InputView);

        String inputTitle = attr.getString(R.styleable.InputView_input_title);
        String inputHint = attr.getString(R.styleable.InputView_input_hint);


        allowInput = attr.getBoolean(R.styleable.InputView_allow_input, true);

        int etTextColor = attr.getColor(R.styleable.InputView_et_text_color, -1);
        int titleTextColor = attr.getColor(R.styleable.InputView_title_text_color, -1);

        int maxLength = attr.getInt(R.styleable.InputView_et_max_length, -1);
//        int lineColor = attr.getColor(R.styleable.InputView_line_color, -1);
        int maxLines = attr.getInteger(R.styleable.InputView_et_max_lines,1);
        if (inputTitle != null) {
            tvInputTitle.setText(inputTitle);
        }
        if (inputHint != null) {
            etInput.setHint(inputHint);
        }
        if(maxLines != 1){
            etInput.setMaxLines(maxLines);
        }


        etInput.setEnabled(allowInput);

        if (etTextColor != -1) {
            etInput.setTextColor(etTextColor);
        }

        if (titleTextColor != -1) {
            tvInputTitle.setTextColor(titleTextColor);
        }

        if (maxLength != -1) {
            setMaxLength(maxLength);
        }

        attr.recycle();
    }

    private void init() {
        View view = LayoutInflater.from(App.context).inflate(R.layout.custom_input_view, this);
        tvInputTitle = (TextView) view.findViewById(R.id.tv_input_title);
        etInput = (EditText) view.findViewById(R.id.et_input);
        etInput.setMaxLines(1);

    }


    public String getInput() {
        return etInput.getText().toString();
    }


    public void setInput(String s) {
        etInput.setText(s);
    }

    public void setInputType(int type) {
        etInput.setInputType(type);
    }

    public void setMaxLength(int length) {
        InputFilter[] filters = {new InputFilter.LengthFilter(length)};
        etInput.setFilters(filters);
    }


    public void setTitle(String s) {
        tvInputTitle.setText(s);
    }










}
