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
 * 标题栏
 *
 * @author yk
 *         create at 2017/2/7 13:03
 *         email : yk_developer@163.com
 */

public class NormalTitleBar extends RelativeLayout implements View.OnClickListener {

    private String title;
    private TextView tvTitle;
    private ImageView ivBack;
    private OnBackClickListener listener;

    public NormalTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PositionTitleBar);
        title = array.getString(R.styleable.PositionTitleBar_title);
        array.recycle();
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_normal_title_bar, this);
        ivBack = (ImageView) view.findViewById(R.id.iv_back);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);

        setTitle(title);


        ivBack.setOnClickListener(this);

    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                if (listener != null) {
                    listener.onBackClick();
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




}