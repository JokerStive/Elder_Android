package lilun.com.pension.widget.filter_view;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import lilun.com.pension.R;
import lilun.com.pension.app.App;
import lilun.com.pension.module.adapter.NormalFilterAdapter;
import lilun.com.pension.module.bean.ConditionOption;

/**
 * 筛选栏
 *
 * @author yk
 *         create at 2017/3/27 10:21
 *         email : yk_developer@163.com
 */
public class FilterView extends LinearLayout implements View.OnTouchListener, View.OnClickListener {
    private int tabIconDUp;
    private int tabIconDownChecked;
    private int mTitleColor;
    private int mTitleColorChecked;
    private float mTitleSize;
    private int tabIconDown;
    private Context context;
    private FrameLayout popViews;
    //    private FrameLayout maskView;
    private FrameLayout pop;
    private LinearLayout filterTabContainer;
    private int checkedTabPosition = -1;
    private int maxWidth = -1;
    private int maxHeight = -1;
    private FilterTabView mCurrentClickTabView;
    private FrameLayout popContainer;
    private OnOptionClickListener listener;

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


        //分割线
        View view = new View(context);
        view.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
        view.setBackgroundColor(Color.BLACK);
        addView(view);

        pop = new FrameLayout(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setLayoutParams(lp);
        addView(pop);


    }

    public void setTitlesAndDatas(List<String> tabTitles, List<List<ConditionOption>> optionsList, View contentView) {
        List<View> pops = new ArrayList<>();
        for (int i = 0; i < optionsList.size(); i++) {
            RecyclerView recyclerView = new RecyclerView(App.context);
            recyclerView.setLayoutManager(new LinearLayoutManager(App.context, LinearLayoutManager.VERTICAL, false));
            NormalFilterAdapter adapter = new NormalFilterAdapter(optionsList.get(i));
            final int finalI = i;
            adapter.setOnItemClickListener((position, option) -> {

                setTabText(position == 0 ? tabTitles.get(finalI) : option.getConditionValue(), position == 0);
                if (listener != null) {
                    listener.onOptionClick(option);
                }
            });
            recyclerView.setAdapter(adapter);
            pops.add(recyclerView);
        }

        setTitlesAndPops(tabTitles, pops, contentView);
    }

    public interface OnOptionClickListener {
        void onOptionClick(ConditionOption option);
    }

    public void setOnOptionClickListener(OnOptionClickListener listener) {
        this.listener = listener;
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
        pop.addView(contentView);

        popContainer = new FrameLayout(context);
        popContainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        pop.addView(popContainer);
        popContainer.setBackgroundColor(0x33222222);
        popContainer.setVisibility(GONE);
        popContainer.setOnTouchListener(this);

        popViews = new FrameLayout(context);
        popViews.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        popContainer.addView(popViews);
        for (int i = 0; i < pops.size(); i++) {
            pops.get(i).setOnClickListener(this);
            popViews.addView(pops.get(i), i);
        }


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
                        popContainer.setVisibility(VISIBLE);
                        popContainer.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.pop_container_in));
                    }

                    View childAt = popViews.getChildAt(i);
                    childAt.setVisibility(VISIBLE);
                    maxWidth = childAt.getRight();
                    maxHeight = childAt.getBottom();
                    Log.d("yk", "当前可见的view的宽高 = " + maxWidth + "--" + maxHeight);
//                    Log.d("yk", "pops 中位置为" + i + "的布局被显示");

                    tabView.setTabStatus(true);
                    tabView.setColor(true);
                    checkedTabPosition = i;

                }


            } else {
//                Log.d("yk", "pops 中位置为" + i + "的布局被隐藏");
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
        popContainer.setVisibility(GONE);
        popContainer.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.pop_container_out));

        checkedTabPosition = -1;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            switchPop(mCurrentClickTabView);
        }
        return true;
    }

    @Override
    public void onClick(View v) {

    }
}
