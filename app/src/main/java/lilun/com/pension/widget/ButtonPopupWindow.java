package lilun.com.pension.widget;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import lilun.com.pension.R;
import lilun.com.pension.module.utils.UIUtils;

/**
 * Created by zp on 2017/4/20.
 */

public class ButtonPopupWindow extends PopupWindow {
    private String[] buttonName;
    private OnChildListener listener;
    LinearLayout llView = null;
    Context mContext;

    public ButtonPopupWindow(Context context, String[] buttonName) {
        this(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, context, buttonName);
    }

    public ButtonPopupWindow(int width, int height, Context context, String[] buttonName) {
        this(LayoutInflater.from(context).inflate(android.R.layout.activity_list_item, null), width, height, context, buttonName);

    }

    public ButtonPopupWindow(View contentView, int width, int height, Context context, String[] buttonName) {
        super(contentView, width, height);
        mContext = context;
        this.buttonName = buttonName;
        setFocusable(true);
        setOutsideTouchable(true);
        initUI(contentView);
    }

    private View initUI(View contentView) {

        contentView.setBackgroundResource(R.drawable.popup_bg);

        if (contentView instanceof LinearLayout) {
            llView = (LinearLayout) contentView;
        }
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, UIUtils.dp2px(mContext, 40));
        layoutParams1.gravity = Gravity.CENTER;
        llView.setLayoutParams(layoutParams1);
        llView.setGravity(Gravity.CENTER);
        llView.findViewById(android.R.id.icon).setVisibility(View.GONE);
        llView.findViewById(android.R.id.text1).setVisibility(View.GONE);
        for (int i = 0; i < buttonName.length; i++) {
            TextView textView = new TextView(mContext);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            textView.setLayoutParams(layoutParams);
            textView.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            textView.setGravity(Gravity.CENTER);
            final int t = i;
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("zp", "ButtonPopupWIndow-" + t);
                    if (listener != null) {
                        listener.childClick((TextView) v, t);
                    }
                }
            });
            textView.setText(buttonName[i]);
            llView.addView(textView);
        }
        return llView;


    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {

        super.showAsDropDown(anchor, xoff, yoff, gravity);

    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    public void setOnChildListener(OnChildListener listener) {
        this.listener = listener;
    }

    public interface OnChildListener {
        void childClick(TextView view, int position);
    }
}
