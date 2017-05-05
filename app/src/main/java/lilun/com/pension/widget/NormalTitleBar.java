package lilun.com.pension.widget;

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

import lilun.com.pension.R;

/**
 * 标题栏
 *
 * @author yk
 *         create at 2017/2/7 13:03
 *         email : yk_developer@163.com
 */

public class NormalTitleBar extends RelativeLayout implements View.OnClickListener {

    private final int maxEms;
    private final int rightIcon;
    private final int leftIcon;
    private final String leftString;
    private String title;
    private TextView tvTitle;
    private TextView tvLeftString;
    private ImageView ivBack;
    private OnBackClickListener listener;
    private TextView tvDoWhat;
    private OnRightClickListener listener1;

    public NormalTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PositionTitleBar);
        leftIcon = array.getResourceId(R.styleable.PositionTitleBar_leftIcon, 0);
        leftString = array.getString(R.styleable.PositionTitleBar_leftString);
        title = array.getString(R.styleable.PositionTitleBar_title);
        maxEms = array.getInteger(R.styleable.PositionTitleBar_titleMaxEms, 0);
        rightIcon = array.getResourceId(R.styleable.PositionTitleBar_rightIcon, 0);
        init(context);
        array.recycle();
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_normal_title_bar, this);
        ivBack = (ImageView) view.findViewById(R.id.iv_back);
        tvLeftString = (TextView) view.findViewById(R.id.tv_left_string);
        tvTitle = (TextView) view.findViewById(R.id.tv_product_name);
        tvDoWhat = (TextView) view.findViewById(R.id.tv_doWhat);
        if (leftIcon != 0) {
            ivBack.setImageResource(leftIcon);
        }
        if (!TextUtils.isEmpty(leftString)) {
            tvLeftString.setText(leftString);
        }

        if (rightIcon != 0) {
            ViewGroup.LayoutParams layoutParams = tvDoWhat.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            tvDoWhat.setLayoutParams(layoutParams);
            Drawable drawable = context.getApplicationContext().getResources().getDrawable(rightIcon);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvDoWhat.setCompoundDrawables(drawable, null, null, null);
            tvDoWhat.setCompoundDrawablePadding(4);
        }
        if (maxEms != 0) {
            tvTitle.setMaxEms(maxEms);
            tvTitle.setSingleLine();
            tvTitle.setEllipsize(TextUtils.TruncateAt.END);
        }
        setTitle(title);


        ivBack.setOnClickListener(this);
        tvDoWhat.setOnClickListener(this);

    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setRightText(String doWhat) {
        tvDoWhat.setVisibility(VISIBLE);
        tvDoWhat.setText(doWhat);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                if (listener != null) {
                    listener.onBackClick();
                }
                break;

            case R.id.tv_doWhat:
                if (listener1 != null) {
                    listener1.onRightClick();
                }
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

    public interface OnRightClickListener {
        void onRightClick();
    }


}
