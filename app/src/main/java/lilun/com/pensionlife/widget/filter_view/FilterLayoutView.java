package lilun.com.pensionlife.widget.filter_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.module.adapter.NormalFilterAdapter;
import lilun.com.pensionlife.module.bean.ConditionOption;
import lilun.com.pensionlife.module.utils.UIUtils;

/**
 * 筛选栏
 * zp 4.17 加入切换布局按钮
 *
 * @author yk
 *         create at 2017/3/27 10:21
 *         email : yk_developer@163.com
 */
public class FilterLayoutView extends LinearLayout implements View.OnTouchListener, View.OnClickListener {
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
    private OnLayoutClickListener layoutlistener;
    private int layoutTypeIndex = 0;
    private LayoutType[] layoutTypes = new LayoutType[]{LayoutType.BIG, LayoutType.SMALL};
    private int[] layoutTypeIcon = new int[]{R.drawable.big_icon, R.drawable.small_icon};
    private boolean noNullLayout;
    private boolean changeLayout;
    private ImageView imageView;

    public FilterLayoutView(Context context) {
        super(context, null);
    }

    public FilterLayoutView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FilterLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        changeLayout = false;
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.FilterLayoutView);
        changeLayout = arr.getBoolean(R.styleable.FilterLayoutView_has_change_layout, false);
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
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setLayoutParams(lp);
        addView(pop);


    }

    public void setTitlesAndDatas(List<String> tabTitles, List<ConditionOption> optionsList, View contentView) {
        List<View> pops = new ArrayList<>();
        for (int i = 0; i < optionsList.size(); i++) {
            RecyclerView recyclerView = new RecyclerView(App.context);
            recyclerView.setLayoutManager(new LinearLayoutManager(App.context, LinearLayoutManager.VERTICAL, false));
            NormalFilterAdapter adapter = new NormalFilterAdapter(optionsList.get(i));
            final int finalI = i;
            adapter.setOnItemClickListener((position, title, whereKey, whereValue) -> {
                setTabText(position == 0 ? tabTitles.get(finalI) : title, position == 0);
                if (listener != null) {
                    listener.onOptionClick(whereKey, whereValue);
                }
            });
            recyclerView.setAdapter(adapter);
            pops.add(recyclerView);
        }

        setTitlesAndPops(tabTitles, pops, contentView);
    }

    public interface OnOptionClickListener {
        void onOptionClick(String whereKey, String whereValue);
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
        popContainer.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
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
        if (changeLayout) {
            int padding = UIUtils.dp2px(context, 10);
            imageView = new ImageView(context);
            LinearLayoutCompat.LayoutParams layoutParams1 = new LinearLayoutCompat.LayoutParams(54, 54);
            layoutParams1.setMargins(padding, 0, padding, 0);
            imageView.setLayoutParams(layoutParams1);

            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("zp", v.getId() + "");
                    int indexCount = noNullLayout ? layoutTypes.length - 1 : layoutTypes.length;

                    if (layoutlistener != null) {
                        layoutTypeIndex++;

                        if (layoutTypeIndex >= layoutTypes.length) {
                            layoutTypeIndex = 0;
                        }

                        layoutlistener.onChangeLayout(layoutTypes[layoutTypeIndex]);

                        imageView.setImageResource(layoutTypeIcon[layoutTypeIndex]);
                    }

                }
            });

            imageView.setImageResource(R.drawable.big_icon);
            filterTabContainer.addView(imageView);
            filterTabContainer.setGravity(Gravity.CENTER);
            filterTabContainer.setPadding(padding, 0, padding, 0);
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
        int tabCont = filterTabContainer.getChildCount();
        if (imageView != null && tabCont > 0)
            tabCont -= 1;
        for (int i = 0; i < tabCont; i++) {
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
                    //   Log.d("yk", "当前可见的view的宽高 = " + maxWidth + "--" + maxHeight);
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

    public void setNoNullLayout() {
        this.noNullLayout = true;
    }

    public void isChangeLayout(boolean changeLayout) {
        this.changeLayout = changeLayout;
    }

    private void closePop() {
        popContainer.setVisibility(GONE);
        popContainer.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.pop_container_out));

        checkedTabPosition = -1;
    }

    public void setOnLayoutlistener(OnLayoutClickListener layoutlistener) {
        this.layoutlistener = layoutlistener;
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

    public enum LayoutType {
        BIG, SMALL
    }

    public interface OnLayoutClickListener {
        void onChangeLayout(FilterLayoutView.LayoutType layoutType);
    }
}
