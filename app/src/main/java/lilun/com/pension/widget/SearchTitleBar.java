package lilun.com.pension.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import lilun.com.pension.R;

/**
 * 带搜索框和布局切换的标题栏
 *
 * @author yk
 *         create at 2017/2/7 13:03
 *         email : yk_developer@163.com
 */

public class SearchTitleBar extends RelativeLayout implements View.OnClickListener {

    private String title;
    private TextView tvTitle;
    private ImageView ivBack;
    private OnBackClickListener listener;
    private TextView tvDoWhat;
    private OnRightClickListener listener1;

    public SearchTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PositionTitleBar);
        title = array.getString(R.styleable.PositionTitleBar_title);
        init(context);
        array.recycle();
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_normal_title_bar, this);
        ivBack = (ImageView) view.findViewById(R.id.iv_back);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvDoWhat= (TextView) view.findViewById(R.id.tv_doWhat);

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

    public interface  OnBackClickListener{
        void onBackClick();
    }

    public void setOnRightClickListener(OnRightClickListener listener) {
        this.listener1 = listener;
    }

    public interface  OnRightClickListener{
        void onRightClick();
    }




}
