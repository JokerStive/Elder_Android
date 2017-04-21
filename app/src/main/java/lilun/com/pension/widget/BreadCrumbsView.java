package lilun.com.pension.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import lilun.com.pension.R;
import lilun.com.pension.module.utils.UIUtils;

/**
 * 面包屑view
 *
 * @author yk
 *         create at 2017/4/21 9:17
 *         email : yk_developer@163.com
 */
public class BreadCrumbsView extends HorizontalScrollView {

    private Context content;
    private LinearLayout crumbContainer;
    private onCrumbClickListener listener;

    public BreadCrumbsView(Context context) {
        super(context);
//        BreadCrumbsView(context,null);
        this.content = context;
        init();
    }

    public BreadCrumbsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.content = context;
        init();
    }

    private void init() {
        setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setBackgroundColor(Color.WHITE);

        //面包屑容器
        crumbContainer = new LinearLayout(content);
        crumbContainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        crumbContainer.setOrientation(LinearLayout.HORIZONTAL);
        crumbContainer.setBackgroundColor(getResources().getColor(R.color.gray));

        addView(crumbContainer);
    }


    /**
     * 添加面包屑
     */
    public void addBreadCrumb(String title, String id) {
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(id)) {
            return;
        }

        int crumbCount = crumbContainer.getChildCount();
        int margin = UIUtils.dp2px(content, 10);

        TextView crumbView = new TextView(content);
        crumbView.setTextColor(Color.WHITE);
        crumbView.setTextSize(17);
        crumbView.setGravity(Gravity.CENTER);
        crumbView.setBackgroundColor(Color.WHITE);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (crumbCount == 0) {
            crumbView.setBackgroundResource(R.drawable.crumbs_first);
            params.setMargins(0, margin, margin, margin);
        } else {
            crumbView.setBackgroundResource(R.drawable.crumbs);
            params.setMargins(0, margin, margin, margin);
        }
        crumbView.setLayoutParams(params);


        crumbView.setText(title);
        crumbView.setTag(id);

        crumbView.setOnClickListener(v -> {
            clearBehindCrumbView((String) crumbView.getTag());
        });

        crumbContainer.addView(crumbView);

    }

    /**
     * 移除当前点击面包屑后面的所有面包屑
     */
    private void clearBehindCrumbView(String targetTag) {
//        int crumbCount = crumbContainer.getChildCount();
        boolean isClear = false;
        for (int i = 0; i < crumbContainer.getChildCount(); i++) {
            TextView childAt = (TextView) crumbContainer.getChildAt(i);
            String tag = (String) childAt.getTag();
            if (tag.equals(targetTag)) {
                isClear = true;
                continue;
            }
            if (isClear) {
//                crumbCount--;
                crumbContainer.removeView(childAt);
            }
        }

        if (listener != null) {
            listener.onCrumbClick(targetTag);
        }
    }


    public interface onCrumbClickListener {
        void onCrumbClick(String id);
    }

    public void setonCrumbClickListener(onCrumbClickListener listener) {
        this.listener = listener;
    }
}
