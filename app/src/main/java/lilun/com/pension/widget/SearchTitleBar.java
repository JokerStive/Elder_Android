package lilun.com.pension.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.MaterialDialog;

import lilun.com.pension.R;

/**
 * 带搜索框和布局切换的标题栏
 *
 * @author yk
 *         create at 2017/2/7 13:03
 *         email : yk_developer@163.com
 */

public class SearchTitleBar extends RelativeLayout implements View.OnClickListener {

    private  Context content;
    private ImageView ivBack;
    private OnItemClickListener listener;
    private EditText etSearch;
    private ImageView ivSearch;
    private ImageView ivChangeLayout;
    private String[] layoutTypes = new String[]{"大图模式","小图模式","无图模式"};

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


        ivBack.setOnClickListener(this);
        ivSearch.setOnClickListener(this);
        ivChangeLayout.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                if (listener != null) {
                    listener.onBack();
                }
                break;

            case R.id.iv_search:
                String searchStr = etSearch.getText().toString();
                if (listener != null && TextUtils.isEmpty(searchStr)) {
                    listener.onSearch(searchStr);
                }
                break;
            case R.id.iv_change_layout:
                //TODO 弹出切换布局框
                new MaterialDialog.Builder(content)
                        .items(layoutTypes)
                        .itemsCallbackSingleChoice(0, (dialog, view, which, text) -> {
                            if (listener!=null){
                                listener.onChangeLayout(getType(text));
                            }
                            return true;
                        })
                        .positiveText(R.string.choose)
                        .show();
                break;

        }
    }

    private LayoutType getType(CharSequence text) {
        if (TextUtils.equals(text,layoutTypes[0])){
            return LayoutType.BIG;
        }else if (TextUtils.equals(text,layoutTypes[1])){
            return LayoutType.SMALL;
        }else {
            return LayoutType.NULL;
        }

    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
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
