package lilun.com.pensionlife.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import lilun.com.pensionlife.R;

/**
 * 带标题的输入框
 * 7.17 添加按钮
 *
 * @author yk
 *         create at 2017/2/16 18:50
 *         email : yk_developer@163.com
 */
public class InputView extends RelativeLayout {
    final int DEFIT = -1;

    private final int etTextColor;
    private final int titleTextColor;
    private final int maxLength;
    private final int maxLines;
    private final String inputTitle;
    private final String inputHint;
    private final boolean showButton;
    private final int ems;
    Context mContent;
    RelativeLayout relativeLayout;
    TextView tvInputTitle;
    EditText etInput;
    Button btView;

    private boolean allowInput;  //允许输入
    final int backgrandId;       //背景资源
    final int drawableLeftId;    //lable 的左图标


    //按钮的属性设置
    String buttonText;
    int buttonTextColor;
    int buttonTextSize;
    int buttonDrawable;


    public InputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContent = context;
        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.InputView);

        backgrandId = attr.getResourceId(R.styleable.InputView_this_backgrand, DEFIT);
        drawableLeftId = attr.getResourceId(R.styleable.InputView_drawableLeft, DEFIT);
        inputTitle = attr.getString(R.styleable.InputView_input_title);
        inputHint = attr.getString(R.styleable.InputView_input_hint);

        ems = attr.getInteger(R.styleable.InputView_this_ems, 0);

        allowInput = attr.getBoolean(R.styleable.InputView_allow_input, true);

        etTextColor = attr.getColor(R.styleable.InputView_et_text_color, DEFIT);
        titleTextColor = attr.getColor(R.styleable.InputView_title_text_color, DEFIT);

        maxLength = attr.getInt(R.styleable.InputView_et_max_length, DEFIT);
//        int lineColor = attr.getColor(R.styleable.InputView_line_color, -1);
        maxLines = attr.getInteger(R.styleable.InputView_et_max_lines, 1);

        //按钮属性
        showButton = attr.getBoolean(R.styleable.InputView_bt_show, false);
        buttonText = attr.getString(R.styleable.InputView_bt_text);
        buttonTextColor = attr.getColor(R.styleable.InputView_bt_text_color, DEFIT);
        buttonTextSize = attr.getDimensionPixelSize(R.styleable.InputView_bt_text_size, 0);
        buttonDrawable = attr.getResourceId(R.styleable.InputView_bt_background, DEFIT);

        init(context);
        attr.recycle();
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_input_view, this);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.rl_start_time_bg);
        tvInputTitle = (TextView) view.findViewById(R.id.tv_input_title);
        etInput = (EditText) view.findViewById(R.id.et_input);
        btView = (Button) view.findViewById(R.id.bt_view);
        etInput.setMaxLines(1);

        if (backgrandId != DEFIT) {
            relativeLayout.setBackgroundResource(backgrandId);
        }

        if (drawableLeftId != DEFIT) {
            Drawable drawable = context.getApplicationContext().getResources().getDrawable(drawableLeftId);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvInputTitle.setCompoundDrawables(drawable, null, null, null);
            tvInputTitle.setCompoundDrawablePadding(4);
        }

        if (inputTitle != null) {
            tvInputTitle.setText(inputTitle);
        }
        if (inputHint != null) {
            etInput.setHint(inputHint);
        }
        if (ems != 0) {
            tvInputTitle.setEms(ems);
            tvInputTitle.setMaxEms(ems);
        }

        if (maxLines != 1) {
            etInput.setMaxLines(maxLines);
        }


        etInput.setEnabled(allowInput);

        if (etTextColor != DEFIT) {
            etInput.setTextColor(etTextColor);
        }

        if (titleTextColor != DEFIT) {
            tvInputTitle.setTextColor(titleTextColor);
        }

        if (maxLength != DEFIT) {
            setMaxLength(maxLength);
        }

        if (showButton) btView.setVisibility(VISIBLE);
        else btView.setVisibility(GONE);
        btView.setText(buttonText);

        if (buttonTextSize != 0)
            btView.setTextSize(buttonTextSize);
        if (buttonDrawable != DEFIT) {
            btView.setBackgroundDrawable(mContent.getResources().getDrawable(buttonDrawable));
            btView.setTextColor(Color.WHITE);
        }
        if (buttonTextColor != DEFIT)
            btView.setTextColor(buttonTextColor);
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


    public void setButtonDrawable(int buttonDrawable) {
        btView.setBackgroundDrawable(mContent.getResources().getDrawable(buttonDrawable));
    }

    public void setButtonTextColor(int buttonTextColor) {
        btView.setTextColor(buttonTextColor);
    }

    public void setButtonTextSize(int buttonTextSize) {
        btView.setTextSize(buttonTextSize);
    }

    public void setButtonText(String buttonText) {
        btView.setText(buttonText);
    }

    /**
     * 设置按钮可点击
     *
     * @param clickable
     */
    public void setButtonClickable(boolean clickable) {
        btView.setClickable(clickable);
    }

    /**
     * 设置按钮监听
     *
     * @param btListener
     */
    public void setBtListener(OnClickListener btListener) {
        btView.setOnClickListener(btListener);
    }

    /**
     * 输入时软键盘按钮
     *
     * @param options
     */
    public void setImeOptions(int options) {
        etInput.setImeOptions(options);
    }

    /**
     * 设置软键盘按键监听
     *
     * @param listener
     */
    public void setOnKeyListener(OnKeyListener listener) {
        etInput.setOnKeyListener(listener);
    }

    public void setOnEditActionListener(TextView.OnEditorActionListener l) {
        etInput.setOnEditorActionListener(l);
    }
}
