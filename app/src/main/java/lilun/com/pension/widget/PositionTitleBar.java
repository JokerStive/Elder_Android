package lilun.com.pension.widget;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import lilun.com.pension.R;
import lilun.com.pension.app.User;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.callback.TitleBarClickCallBack;
import lilun.com.pension.ui.change_organization.ChangeOrganizationFragment;

/**
 * 标题栏
 *
 * @author yk
 *         create at 2017/2/7 13:03
 *         email : yk_developer@163.com
 */

public class PositionTitleBar extends RelativeLayout implements View.OnClickListener {

    private String rightText;
    private String title;
    public TextView tvPosition;
    private TextView tvTitle;
    private Button tvRight;
    private ImageView ivBack;
    private TitleBarClickCallBack listener;
    private BaseFragment fragment;

    public PositionTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PositionTitleBar);
        title = array.getString(R.styleable.PositionTitleBar_title);
        rightText = array.getString(R.styleable.PositionTitleBar_rightText);
        array.recycle();
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_position_title_bar, this);
        ivBack = (ImageView) view.findViewById(R.id.iv_back);
        tvPosition = (TextView) view.findViewById(R.id.tv_position);
        tvTitle = (TextView) view.findViewById(R.id.tv_product_name);
        tvRight = (Button) view.findViewById(R.id.tv_right);

        setTitle(title);
        setTvRightText(rightText);
        setPosition();


        ivBack.setOnClickListener(this);
        tvPosition.setOnClickListener(this);
        tvRight.setOnClickListener(this);

    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setPosition() {
        tvPosition.setText(User.getCurrentOrganizationName());
    }

    public void setTvRightText(String doWhat) {
        tvRight.setText(doWhat);
    }

    public void setNoPosition(boolean positionVisible) {
        tvPosition.setVisibility(positionVisible ? VISIBLE : GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                if (listener != null) {
                    listener.onBackClick();
                }
                break;

            case R.id.iv_position:
                if (fragment != null) {
                    fragment.startActivity(new Intent(fragment.getActivity(),ChangeOrganizationFragment.class));
                }
                if (listener != null) {
                    listener.onPositionClick();
                }
                break;


            case R.id.tv_right:
                if (listener != null) {
                    listener.onRightClick();
                }
                break;
        }
    }

    public void setFragment(BaseFragment fragment) {
        this.fragment = fragment;
    }


    public void setTitleBarClickListener(TitleBarClickCallBack listener) {
        this.listener = listener;
    }


    /**
     * 隐藏定位标志和事件右侧按钮
     */
    public void showOnlyTitle() {
        tvPosition.setVisibility(GONE);
        tvPosition.setVisibility(GONE);
        tvRight.setVisibility(GONE);
    }


    public void showAll() {
        tvPosition.setVisibility(VISIBLE);
        tvPosition.setVisibility(VISIBLE);
        tvRight.setVisibility(VISIBLE);
    }

}
