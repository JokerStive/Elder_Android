package lilun.com.pensionlife.widget;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.inputmethod.EditorInfo;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.module.utils.UIUtils;

/**
 * 小红点控件，用于显示未读消息条数
 * 默认背景颜色为红、字体为白色
 * Created by zp on 2018/3/16.
 */

public class BadgesView extends android.support.v7.widget.AppCompatTextView {

    //最小宽高
    int minWidth = 20;
    int minHeight = 20;
    //默认填充颜色
    int sloidColor = getResources().getColor(android.R.color.holo_red_light);

    public BadgesView(Context context) {
        super(context);
        init();
    }

    public BadgesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BadgesView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        setBackground(getDefaultDrawable(sloidColor));
        setMinWidth(UIUtil.dip2px(getContext(), minWidth));
        setMinHeight(UIUtil.dip2px(getContext(), minHeight));
        setTextColor(getResources().getColor(R.color.white));
        setTextColor(getResources().getColor(R.color.white));
        setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        setGravity(Gravity.CENTER);
    }


    public GradientDrawable getDefaultDrawable( int sloidColor) {
        GradientDrawable gradientDw = new GradientDrawable();
        gradientDw.setCornerRadius(UIUtils.dp2px(getContext(), getResources().getDimension(R.dimen.dp_20)));
        gradientDw.setStroke(UIUtils.dp2px(getContext(), 0),
                getResources().getColor(R.color.white));
        gradientDw.setColor(sloidColor);
        gradientDw.setShape(GradientDrawable.RECTANGLE);
        return gradientDw;
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        if (getText().toString().isEmpty() || "0".equals(getText().toString())) {
            setVisibility(GONE);
        } else
            setVisibility(VISIBLE);
    }
}
