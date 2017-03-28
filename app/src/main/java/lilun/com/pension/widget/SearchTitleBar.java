package lilun.com.pension.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import lilun.com.pension.R;

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
    private EditText etSearch;
    private ImageView ivSearch;
    private ImageView ivChangeLayout;
    private String lastSearchStr;
    private int layoutTypeIndex = 0;
    private LayoutType[] layoutTypes = new LayoutType[]{LayoutType.BIG, LayoutType.SMALL, LayoutType.NULL};
    private int[] layoutTypeIcon = new int[]{R.drawable.layout_type_big, R.drawable.layout_type_small, R.drawable.layout_type_null};
//    private RadioGroup rgContainer;
//    private List<ConditionModule> conditionModules;
    //    private HorizontalScrollView scroll;
//    private OnConditionClickListener conditionListener;
//    private FrameLayout popContainer;
//    private FilterView filterView;

    public SearchTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PositionTitleBar);
//        title = array.getString(R.styleable.PositionTitleBar_title);
        this.content = context;
        init(context);
        array.recycle();
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_title_bar, this);
        ivBack = (ImageView) view.findViewById(R.id.iv_back);
        etSearch = (EditText) view.findViewById(R.id.et_search);
        ivSearch = (ImageView) view.findViewById(R.id.iv_search);
        ivChangeLayout = (ImageView) view.findViewById(R.id.iv_change_layout);

//        filterView = (FilterView) view.findViewById(R.id.filter_view);


        ivBack.setOnClickListener(this);
        ivSearch.setOnClickListener(this);
        ivChangeLayout.setOnTouchListener(this);


//        initRadioGroup();
    }

//    private void initRadioGroup() {
//        if (conditionModules == null) {
//            return;
//        }
//        for (int i = 0; i < conditionModules.size(); i++) {
//            ConditionModule conditionModule = conditionModules.get(i);
//            RadioButton item = (RadioButton) LayoutInflater.from(content).inflate(R.layout.item_filter_radiobutton, null);
//            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
//            item.setText(conditionModule.getTitle());
//            item.setVisibility(INVISIBLE);
//
//            final int finalI = i;
//            item.post(() -> {
//                int margin = UIUtils.dp2px(content, 44);
//                if (finalI == 0) {
//                    margin = UIUtils.dp2px(content, 10);
//                }
//                lp.setMargins(margin, 0, 0, 0);
//                item.setLayoutParams(lp);
//                item.setVisibility(VISIBLE);
//            });
//
//            List<ConditionModule.ConditionBean> conditions = conditionModule.getConditions();
//            item.setOnClickListener(v -> {
//                if (conditions != null && conditions.size() != 0) {
//                    showConditionWindow(item, conditionModule.getKey(), conditions);
//
//                } else {
//                    if (conditionListener != null) {
//                        conditionListener.onConditionClick(conditionModule.getKey(), "");
//                    }
//                }
//            });
//            rgContainer.addView(item);
//        }
//    }
//
//    private void showConditionWindow(RadioButton radioButton, String key, List<ConditionModule.ConditionBean> conditions) {
//        ArrayList<String> conditionStr = new ArrayList<>();
//        for (ConditionModule.ConditionBean conditionBean : conditions) {
//            conditionStr.add(conditionBean.getConditionKey());
//        }
//        new MaterialDialog.Builder(content)
//                .items(conditionStr)
//                .itemsCallbackSingleChoice(-1, (dialog, view, which, text) -> {
//                    radioButton.setText(text);
//                    if (conditionListener != null) {
//                        conditionListener.onConditionClick(key, getChooseConditionValue(text, conditions));
//                    }
//                    return true;
//                })
//                .show();
//    }
//
//    private String getChooseConditionValue(CharSequence text, List<ConditionModule.ConditionBean> conditions) {
//        for (ConditionModule.ConditionBean conditionBean : conditions) {
//            if (TextUtils.equals(text, conditionBean.getConditionKey())) {
//                return conditionBean.getConditionValue();
//            }
//        }
//        return "";
//    }


//    public interface OnConditionClickListener {
//        void onConditionClick(String key, String value);
//    }
//
//    public void setOnConditionClickListener(OnConditionClickListener listener) {
//        this.conditionListener = listener;
//    }
//
//    public void setConditionModules(List<ConditionModule> conditionModules) {
//        this.conditionModules = conditionModules;
//        initRadioGroup();
//    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                if (listener != null) {
                    listener.onBack();
                }
                break;

            case R.id.iv_search:
//                String searchStr = etSearch.getText().toString();
//                if (conditionListener != null) {
//                    if (!TextUtils.isEmpty(searchStr) || !TextUtils.isEmpty(lastSearchStr)) {
//                        lastSearchStr = searchStr.replace(" ", "");
//                        conditionListener.onConditionClick("title", lastSearchStr);
//                    }
//                }
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

//        void onSearch(String searchStr);

        void onChangeLayout(LayoutType layoutType);
    }

    public enum LayoutType {
        BIG, SMALL, NULL
    }

}
