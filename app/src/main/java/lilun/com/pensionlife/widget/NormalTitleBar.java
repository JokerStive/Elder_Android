package lilun.com.pensionlife.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import lilun.com.pensionlife.R;

/**
 * 标题栏
 *
 * @author yk
 *         create at 2017/2/7 13:03
 *         email : yk_developer@163.com
 */

public class NormalTitleBar extends RelativeLayout implements View.OnClickListener {
    public static int NONE = 3; //都不显示
    public static int BOTH = 2;
    public static int ICON = 0;
    public static int TEXT = 1;
    private final int maxEms;
    private int rightIcon;
    private final int leftIcon;
    private final int tilteColor;
    private final int rightTextColor;
    private final String leftString;
    private final String rightString;
    private String title;
    private TextView tvTitle;
    //    private TextView tvLeftString;
    private ImageView ivBack;
    private OnBackClickListener listener;
    private TextView tvDoWhat;
    private OnRightClickListener listener1;
    private OnTitleClickListener titleListener;
    private Drawable mBackgrand;
    private int rightWitchShow = 0;
    private Context context;
    private RelativeLayout rl;

    public NormalTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.NormalTitleBar);
        leftIcon = array.getResourceId(R.styleable.NormalTitleBar_leftIcon, 0);
        leftString = array.getString(R.styleable.NormalTitleBar_leftString);
        title = array.getString(R.styleable.NormalTitleBar_title);
        tilteColor = array.getColor(R.styleable.NormalTitleBar_titleColor, 0);
        maxEms = array.getInteger(R.styleable.NormalTitleBar_titleMaxEms, 0);
        rightIcon = array.getResourceId(R.styleable.NormalTitleBar_rightIcon, 0);
        rightString = array.getString(R.styleable.NormalTitleBar_rightText);
        rightWitchShow = array.getInt(R.styleable.NormalTitleBar_rightWitchShow, 2);
        rightTextColor = array.getColor(R.styleable.NormalTitleBar_rightTextColor, 0);
        mBackgrand = getBackground();
        if (mBackgrand == null) {
            setBackgroundResource(R.color.white);
        }
        this.context = context;
        init(context);
        array.recycle();
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_normal_title_bar, this);
        ivBack = (ImageView) view.findViewById(R.id.iv_back);
        rl = (RelativeLayout) view.findViewById(R.id.rl);
//        tvLeftString = (TextView) view.findViewById(R.id.tv_left_string);
        tvTitle = (TextView) view.findViewById(R.id.tv_product_name);
        tvDoWhat = (TextView) view.findViewById(R.id.tv_doWhat);
        if (leftIcon != 0) {
            ivBack.setBackgroundResource(leftIcon);
        }

        if (!TextUtils.isEmpty(leftString)) {
//            tvLeftString.setText(leftString);
        }


        rightWitchShow();

        if (maxEms != 0) {
            tvTitle.setMaxEms(maxEms);
            tvTitle.setSingleLine();
            tvTitle.setEllipsize(TextUtils.TruncateAt.END);
        }
        setTitle(title);
        if (tilteColor != 0) {
            tvTitle.setTextColor(tilteColor);
        }

        rl.setOnClickListener(this);
        tvDoWhat.setOnClickListener(this);
        tvTitle.setOnClickListener(this);
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setRightText(String doWhat) {
        tvDoWhat.setVisibility(VISIBLE);
        tvDoWhat.setText(doWhat);
    }

    public void setRightIcon(int icon) {
        rightIcon = icon;
        rightWitchShow();
    }

    public void setRightWitchShow(int rightWitchShow) {
        this.rightWitchShow = rightWitchShow;
        rightWitchShow();
    }

    public int getRightWitchShow() {
        return rightWitchShow;
    }

    private void rightWitchShow() {
        if (rightIcon != 0) {
            ViewGroup.LayoutParams layoutParams = tvDoWhat.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            tvDoWhat.setLayoutParams(layoutParams);
            Drawable drawable = context.getApplicationContext().getResources().getDrawable(rightIcon);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvDoWhat.setCompoundDrawables(drawable, null, null, null);
            tvDoWhat.setCompoundDrawablePadding(4);
        }
        if (rightTextColor != 0) {
            tvDoWhat.setTextColor(rightTextColor);
        }
        if (!TextUtils.isEmpty(rightString)) {
            tvDoWhat.setText(rightString);
        }
        if (rightWitchShow == ICON) {
            tvDoWhat.setText("");
        } else if (rightWitchShow == TEXT) {
            tvDoWhat.setCompoundDrawables(null, null, null, null);
        } else if (rightWitchShow == NONE) {
            tvDoWhat.setText("");
            tvDoWhat.setCompoundDrawables(null, null, null, null);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl:
                if (listener != null) {
                    listener.onBackClick();
                }
                break;

            case R.id.tv_doWhat:
                if (listener1 != null) {
                    listener1.onRightClick();
                }
                break;
            case R.id.tv_product_name:
                if (titleListener != null) titleListener.onTitleClick();
                break;


        }
    }


    public void setOnBackClickListener(OnBackClickListener listener) {
        this.listener = listener;
    }

    public interface OnBackClickListener {
        void onBackClick();
    }

    public void setOnRightClickListener(OnRightClickListener listener) {
        this.listener1 = listener;
    }

    public void setOnTitleClickListener(OnTitleClickListener listener) {
        this.titleListener = listener;
    }

    public interface OnRightClickListener {
        void onRightClick();
    }

    public interface OnTitleClickListener {
        void onTitleClick();
    }


}
