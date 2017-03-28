package lilun.com.pension.widget.filter_view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.List;

import lilun.com.pension.R;

/**
 * 筛选栏
 *
 * @author yk
 *         create at 2017/3/27 10:21
 *         email : yk_developer@163.com
 */
public class FilterView extends LinearLayout implements View.OnTouchListener {
    private int tabIconDUp;
    private int tabIconDownChecked;
    private int mTitleColor;
    private int mTitleColorChecked;
    private float mTitleSize;
    private int tabIconDown;
    private Context context;
    private FrameLayout popViews;
    private FrameLayout maskView;
    private FrameLayout popViewsContainer;
    private LinearLayout filterTabContainer;
    private int checkedTabPosition = -1;
    private int maxWidth = -1;
    private int maxHeight = -1;
    private FilterTabView mCurrentClickTabView;

    public FilterView(Context context) {
        super(context, null);
    }

    public FilterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FilterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

//        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.FilterView);
//        mTitleSize = arr.getDimension(R.styleable.FilterView_titleSize, -1);
//        mTitleColor = arr.getColor(R.styleable.FilterView_titleColor, -1);
//        mTitleColorChecked = arr.getColor(R.styleable.FilterView_titleColorChecked, -1);
//
//        tabIconDown = arr.getResourceId(R.styleable.FilterView_tabIconDown, -1);
//        tabIconDownChecked = arr.getResourceId(R.styleable.FilterView_tabIconDownChecked, -1);
//        tabIconDUp = arr.getResourceId(R.styleable.FilterView_tabIconUp, -1);
//
//        arr.recycle();

        init();
    }

    private void init() {
        LayoutParams params0 = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(params0);
        setOrientation(VERTICAL);

        filterTabContainer = new LinearLayout(context);
        filterTabContainer.setOrientation(HORIZONTAL);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        filterTabContainer.setLayoutParams(params);
        addView(filterTabContainer);

        popViewsContainer = new FrameLayout(context);
        popViewsContainer.setBackgroundColor(Color.WHITE);
//        popViewsContainer.setOrientation(VERTICAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        lp.topMargin = 20;
        popViewsContainer.setLayoutParams(lp);
        addView(popViewsContainer);


    }

    public void setTitlesAndPops(List<String> tabTitles, List<View> pops, View contentView) {

        if (tabTitles.size() != pops.size()) {
            throw new RuntimeException("数据和pop数量不一致");
        }

        for (int i = 0; i < tabTitles.size(); i++) {
            addTabView(tabTitles, i);
        }


        ViewGroup parent = (ViewGroup) contentView.getParent();
        parent.removeView(contentView);
        popViewsContainer.addView(contentView);


        maskView = new FrameLayout(context);
        maskView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        maskView.setBackgroundColor(0xaa222222);
        maskView.setOnTouchListener(this);
        maskView.setVisibility(GONE);
        popViewsContainer.addView(maskView);

        popViews = new FrameLayout(context);
        popViews.setVisibility(GONE);
        popViews.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        popViewsContainer.addView(popViews);
        for (int i = 0; i < pops.size(); i++) {
            popViews.addView(pops.get(i), i);
        }



        Log.d("yk", "筛选项个数  = " + filterTabContainer.getChildCount());
        Log.d("yk", "pop个数  = " + popViews.getChildCount());

    }

    private void addTabView(List<String> tabTitles, int position) {
        FilterTabView tabView = new FilterTabView(context, tabTitles.get(position));
        tabView.setOnClickListener(v -> {
            mCurrentClickTabView = tabView;
            switchPop(mCurrentClickTabView);
        });

        filterTabContainer.addView(tabView);
    }

    private void switchPop(FilterTabView targetView) {
        for (int i = 0; i < filterTabContainer.getChildCount(); i++) {
            FilterTabView tabView = (FilterTabView) filterTabContainer.getChildAt(i);
            if (targetView == tabView) {
                if (checkedTabPosition == i) {
                    tabView.setTabStatus(false);
                    tabView.setColor(false);
                    closePop();
                } else {
                    //如果没有选中任何的tab
                    if (checkedTabPosition == -1) {
                        popViews.setVisibility(VISIBLE);
                        maskView.setVisibility(VISIBLE);
                        popViews.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.pop_container_in));

                        maskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.pop_container_in));
                    }
                    View childAt = popViews.getChildAt(i);
                    childAt.setVisibility(VISIBLE);
                    maxWidth = childAt.getMeasuredWidth();
                    maxHeight = childAt.getMeasuredHeight();
//                    Log.d("yk", "当前可见的view的宽高 = " + maxWidth + "--" + maxHeight);
                    Log.d("yk", "pops 中位置为" + i + "的布局被显示");

                    tabView.setTabStatus(true);
                    tabView.setColor(true);
                    checkedTabPosition = i;

                }


            } else {
                Log.d("yk", "pops 中位置为" + i + "的布局被隐藏");
                tabView.setTabStatus(false);
                tabView.setColor(false);
                View childAt = popViews.getChildAt(i);
                childAt.setVisibility(GONE);

            }
        }
    }

    public void setTabText(String text, boolean isDef) {
        if (checkedTabPosition != -1) {
            FilterTabView tabView = (FilterTabView) filterTabContainer.getChildAt(checkedTabPosition);
            tabView.setTitle(text);
            tabView.setIsDef(isDef);
            switchPop(tabView);
        }
    }


    private void closePop() {
        popViews.setVisibility(View.GONE);
        popViews.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.pop_container_out));

        maskView.setVisibility(GONE);
        maskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.pop_container_out));

        checkedTabPosition = -1;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            float rawX = event.getX();
            float rawY = event.getY();
            Log.d("yk", "点击位置的坐标 ==" + rawX + "---" + rawY);
            if (!(rawX <= maxWidth && rawY <= maxHeight) && mCurrentClickTabView != null) {
                switchPop(mCurrentClickTabView);

            }
        }
        return true;
    }
}
