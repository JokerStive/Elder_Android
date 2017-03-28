package lilun.com.pension.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import lilun.com.pension.R;
import lilun.com.pension.widget.filter_view.SearchPop;

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
    private TextView tvSearch;
    private ImageView ivChangeLayout;
    private String lastSearchStr;
    private int layoutTypeIndex = 0;
    private LayoutType[] layoutTypes = new LayoutType[]{LayoutType.BIG, LayoutType.SMALL, LayoutType.NULL};
    private int[] layoutTypeIcon = new int[]{R.drawable.layout_type_big, R.drawable.layout_type_small, R.drawable.layout_type_null};

    public SearchTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PositionTitleBar);
        this.content = context;
        init(context);
        array.recycle();
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_title_bar, this);
        ivBack = (ImageView) view.findViewById(R.id.iv_back);
        tvSearch = (TextView) view.findViewById(R.id.et_search);
        ivChangeLayout = (ImageView) view.findViewById(R.id.iv_change_layout);


        ivBack.setOnClickListener(this);
        tvSearch.setOnClickListener(this);
        ivChangeLayout.setOnTouchListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                if (listener != null) {
                    listener.onBack();
                }
                break;

            case R.id.et_search:
                SearchPop searchPop = new SearchPop(getContext(), this, tvSearch.getText() + "");
                searchPop.setOnSearchListenerListener(str -> {
                    tvSearch.setText(str);
                    if (listener != null) {
                        listener.onSearch(str);
                    }
                });
                break;

        }
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (v.getId() == R.id.iv_change_layout) {
                if (listener != null) {
                    if (layoutTypeIndex == layoutTypes.length - 1) {
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
