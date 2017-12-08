package lilun.com.pensionlife.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.widget.filter_view.SearchFragment;

/**
 * 带搜索框和布局切换的标题栏
 *
 * @author yk
 *         create at 2017/2/7 13:03
 *         email : yk_developer@163.com
 */

public class SearchTitleBar extends RelativeLayout implements View.OnClickListener, View.OnTouchListener {

    private Context content;
    private ImageView ivBack;
    private OnItemClickListener listener;
    private CustomTextView tvSearch;
    private ImageView ivChangeLayout;
    private String lastSearchStr;
    private int layoutTypeIndex = 0;
    private LayoutType[] layoutTypes = new LayoutType[]{LayoutType.SMALL, LayoutType.BIG, LayoutType.NULL};
    private int[] layoutTypeIcon = new int[]{R.drawable.layout_type_small, R.drawable.layout_type_big, R.drawable.layout_type_null};
    private boolean noNullLayout;
    private BaseFragment fragment;
    private RelativeLayout rl;

    public SearchTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PositionTitleBar);
        this.content = context;
        init(context);
        array.recycle();
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_title_bar, this);
        rl = (RelativeLayout) view.findViewById(R.id.rl);
        tvSearch = (CustomTextView) view.findViewById(R.id.tv_search);
        ivChangeLayout = (ImageView) view.findViewById(R.id.iv_change_layout);


        ivChangeLayout.setImageResource(layoutTypeIcon[layoutTypeIndex]);
        rl.setOnClickListener(this);
        tvSearch.setOnClickListener(this);
        ivChangeLayout.setOnTouchListener(this);


    }

    public void setFragment(BaseFragment fragment) {
        this.fragment = fragment;
    }

    public void setNoNullLayout() {
        this.noNullLayout = true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl:
                if (listener != null) {
                    listener.onBack();
                }
                break;

            case R.id.tv_search:
                if (fragment != null) {
                    SearchFragment searchPop = SearchFragment.newInstance(tvSearch.getText() + "");
                    searchPop.setOnSearchListenerListener(str -> {
                        tvSearch.setText(str);
                        if (listener != null) {
                            listener.onSearch(str);
                        }
                    });
                    fragment.start(searchPop);
                }
                break;

        }
    }


    public void isChangeLayout(boolean changeLayout) {
        ivChangeLayout.setVisibility(changeLayout ? VISIBLE : GONE);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    public void setLayoutTypeIcon(LayoutType layerType) {
        if (LayoutType.BIG == layerType) layoutTypeIndex = 1;
        else if (LayoutType.SMALL == layerType) layoutTypeIndex = 0;
        else layoutTypeIndex = 2;
        ivChangeLayout.setImageResource(layoutTypeIcon[layoutTypeIndex]);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int indexCount = noNullLayout ? layoutTypes.length - 2 : layoutTypes.length - 1;
            if (v.getId() == R.id.iv_change_layout) {
                if (listener != null) {
                    if (layoutTypeIndex == indexCount) {
                        layoutTypeIndex = 0;
                    } else {
                        layoutTypeIndex++;
                    }
                    listener.onChangeLayout(layoutTypes[layoutTypeIndex]);
                    ivChangeLayout.setImageResource(layoutTypeIcon[layoutTypeIndex]);
                }
            }
        }
        return true;
    }

    public interface OnItemClickListener {
        void onBack();

        void onSearch(String searchStr);

        void onChangeLayout(LayoutType layoutType);
    }

    public enum LayoutType {
        BIG, SMALL, NULL
    }

}
