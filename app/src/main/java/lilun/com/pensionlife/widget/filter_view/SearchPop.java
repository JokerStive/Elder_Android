package lilun.com.pensionlife.widget.filter_view;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.List;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.module.adapter.SearchRecordAdapter;
import lilun.com.pensionlife.module.db.RecordDbOpenHelper;

/**
 * 关键字搜索view
 *
 * @author yk
 *         create at 2017/3/28 14:51
 *         email : yk_developer@163.com
 */
public class SearchPop extends PopupWindow implements View.OnClickListener {

    private Context context;
    private String oldSerach;
    private EditText etSearch;
    private RecyclerView rvRecord;
    private RecordDbOpenHelper helper;
    private SQLiteDatabase db;
    private SearchRecordAdapter adapter;
    private OnSearchListener listener;
    private View clear;
    private InputMethodManager imm;

    public SearchPop(Context context, String string) {
        super(context);
        this.oldSerach = string;
        helper = new RecordDbOpenHelper(context);
        this.context = context;
        init();
    }


    private void init() {
        imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = LayoutInflater.from(App.context).inflate(R.layout.custom_search_view, null);
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        view.findViewById(R.id.tv_null).setOnClickListener(this);
        clear = view.findViewById(R.id.tv_clear_records);
        clear.setOnClickListener(this);
        etSearch = (EditText) view.findViewById(R.id.et_search);
        etSearch.setText(oldSerach);
        etSearch.setSelection(oldSerach.length());
        etSearch.setFocusable(true);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

//        setFocusable(true);
//        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//        setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);

        etSearch.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                //搜索
                search(etSearch.getText().toString());
            }
            return false;
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                queryRecord(s.toString());
            }
        });


        rvRecord = (RecyclerView) view.findViewById(R.id.rv_records);
        rvRecord.setLayoutManager(new LinearLayoutManager(App.context, LinearLayoutManager.VERTICAL, false));

        setContentView(view);
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        setFocusable(true);

        setBackgroundDrawable(context.getResources().getDrawable(R.drawable.shape_rect_white));
        showAsDropDown(etSearch);


        queryRecord("");
    }

    private void search(String str) {
        hintKeyBord();
        if (!TextUtils.isEmpty(str)) {
            str = str.replace(" ", "");
            insertData(str);
        }

        if (listener != null) {
            if (oldSerach != null) {
                if (!TextUtils.equals(str, oldSerach)) {
                    listener.onSearch(str);
                }
            } else {
                listener.onSearch(str);
            }
        }
        dismiss();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                dismiss();
                break;


            case R.id.tv_null:
                search("");
                break;
            case R.id.tv_clear_records:
                //清空
                clearRecords();
                break;

        }
    }


    private void queryRecord(String str) {
        Cursor cursor = helper.getReadableDatabase().rawQuery(
                "select id as _id,searchStr from records where searchStr like '%" + str + "%' order by id desc ", null);
        List<String> searchData;
        if (cursor != null && cursor.moveToFirst()) {
            searchData = new ArrayList<>();
            do {
                String searchStr = cursor.getString(cursor.getColumnIndex("searchStr"));
                searchData.add(searchStr);
            } while (cursor.moveToNext());

            if (searchData.size() != 0) {
                clear.setVisibility(View.VISIBLE);
            }
//            if (adapter == null) {
            rvRecord.setVisibility(View.VISIBLE);
            adapter = new SearchRecordAdapter(searchData);
            adapter.setOnItemClickListener(str1 -> {
                //搜索
                etSearch.setText(str1);
                search(str1);
            });
            rvRecord.setAdapter(adapter);
//            }
//            else {
//                adapter.replaceAll(searchData);
//            }
        }
    }


    private boolean hasData(String str) {
        //从Record这个表里找到name=tempName的id
        Cursor cursor = helper.getReadableDatabase().rawQuery(
                "select id as _id,searchStr from records where searchStr =?", new String[]{str});
        //判断是否有下一个
        return cursor.moveToNext();
    }


    private void insertData(String str) {
        if (!hasData(str)) {
            db = helper.getWritableDatabase();
            db.execSQL("insert into records(searchStr) values('" + str + "')");
            db.close();
        }
    }


    private void hintKeyBord() {
        boolean isOpen = imm.isActive();
        if (isOpen) {
            imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0); //强制隐藏键盘
        }
    }


    private void clearRecords() {
        hintKeyBord();
        db = helper.getWritableDatabase();
        db.execSQL("delete from records");
        db.close();

        clear.setVisibility(View.GONE);
//        if (adapter != null && adapter.getItemCount() != 0) {
//            adapter.replaceAll(new ArrayList<>());
//        }
        rvRecord.setVisibility(View.INVISIBLE);
        rvRecord.setAdapter(new SearchRecordAdapter(new ArrayList<>()));
    }


    public interface OnSearchListener {
        void onSearch(String str);

    }

    public void setOnSearchListenerListener(OnSearchListener listener) {
        this.listener = listener;
    }
}

