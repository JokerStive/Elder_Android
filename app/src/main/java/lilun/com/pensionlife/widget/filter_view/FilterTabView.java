package lilun.com.pensionlife.widget.filter_view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.App;


/**
 * 筛选栏每一个条目
 *
 * @author yk
 *         create at 2017/3/24 11:10
 *         email : yk_developer@163.com
 */
public class FilterTabView extends LinearLayout {
    private String title;
    private boolean isDef = true;
    private TextView tvTitle;
    private int tabIconDUp = -1;
    private int tabIconDownChecked = -1;
    private int mTitleColor = getResources().getColor(R.color.gray2);
    private int mTitleColorChecked = getResources().getColor(R.color.filter_checked_color);
    private float mTitleSize = -1;
    private int tabIconDown = -1;
    private Drawable iconDownDrawable;
    private Drawable iconDownCheckedDrawable;
    private Drawable iconUpDrawable;


    public FilterTabView(Context context, String title) {
        super(context);
        this.title = title;
        initParameter();
        init();
    }

    private void initParameter() {
        iconDownDrawable = getResources().getDrawable(tabIconDown == -1 ? R.drawable.filter_down : tabIconDown);
        iconDownCheckedDrawable = getResources().getDrawable(tabIconDownChecked == -1 ? R.drawable.filter_down_checked : tabIconDownChecked);
        iconUpDrawable = getResources().getDrawable(tabIconDUp == -1 ? R.drawable.filter_up_checked : tabIconDUp);
    }


    public void setIsDef(boolean isDef) {
        this.isDef = isDef;
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.filter_tab, this);
        tvTitle = (TextView) view.findViewById(R.id.tv_sophisticated);
        setTitle(title);

        LayoutParams lp = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.weight = 1;
        setLayoutParams(lp);

        if (mTitleColor != -1) {
            tvTitle.setTextColor(mTitleColor);
        }

        if (mTitleSize != -1) {
            tvTitle.setTextSize(mTitleSize);
        } else {
            if (App.widthDP > 820)
                tvTitle.setTextSize(getContext().getResources().getDimension(R.dimen.sp_14));
        }
//        if (mTitleColorChecked!=-1 ){
//            tvTitle.setTextSize(mTitleSize);
//        }

    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setTabStatus(boolean checked) {
        if (!checked) {
            if (isDef) {
                tvTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, iconDownDrawable, null);
            } else {
                tvTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, iconDownCheckedDrawable, null);
//                drawable = getResources().getDrawable(R.drawable.filter_down_checked);
            }
        } else {
            tvTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, iconUpDrawable, null);
        }


    }

    public void setColor(boolean checked) {
        tvTitle.setTextColor(checked || !isDef ? mTitleColorChecked : mTitleColor);
    }

}
