package lilun.com.pensionlife.widget;

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

import java.util.ArrayList;
import java.util.List;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.module.utils.UIUtils;

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
    private List<String> ids = new ArrayList<>();

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

        setHorizontalScrollBarEnabled(false);
        setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setBackgroundColor(Color.WHITE);

        //面包屑容器
        crumbContainer = new LinearLayout(content);
        crumbContainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        crumbContainer.setOrientation(LinearLayout.HORIZONTAL);
        crumbContainer.setBackgroundColor(getResources().getColor(R.color.white));

        addView(crumbContainer);
    }


    /**
     * 添加面包屑
     */
    public void addBreadCrumb(String id) {
        if (TextUtils.isEmpty(id)) {
            return;
        }

        if ( hasRepeatId(id)){
            return;
        }

        ids.add(id);

        int crumbCount = crumbContainer.getChildCount();
        int margin = UIUtils.dp2px(content, App.context.getResources().getDimension(R.dimen.dp_10));

        TextView crumbView = new TextView(content);
        crumbView.setTextColor(Color.WHITE);
        crumbView.setTextSize(14);
        crumbView.setGravity(Gravity.CENTER);
        crumbView.setBackgroundColor(Color.WHITE);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (crumbCount == 0) {
            crumbView.setBackgroundResource(R.drawable.crumbs_first);
            params.setMargins(0, margin, 0, margin);
        } else {
            crumbView.setBackgroundResource(R.drawable.crumbs);
            params.setMargins(UIUtils.dp2px(content,  App.context.getResources().getDimension(R.dimen.dp_5)), margin, 0, margin);
        }
        crumbView.setLayoutParams(params);


        crumbView.setText(StringUtils.getOrganizationNameFromId(id));
        crumbView.setTag(id);

        crumbView.setOnClickListener(v -> {
            clearBehindCrumbView((String) crumbView.getTag());
        });

        crumbContainer.addView(crumbView);

    }


    /**
    *是否有重复的id
    */
    private boolean hasRepeatId(String targetId) {
        for(String id:ids){
            if (TextUtils.equals(targetId,id)){
                return true;
            }
        }
        return false;
    }



    /**
    *获取id列表
    */
    public List<String> getIds() {
        return ids;
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
                ids.remove(i);
                crumbContainer.removeView(childAt);
                i--;
            }
        }

        listener.onCrumbClick(targetTag);

    }


    public interface onCrumbClickListener {
        void onCrumbClick(String id);
    }

    public void setonCrumbClickListener(onCrumbClickListener listener) {
        this.listener = listener;
    }
}
