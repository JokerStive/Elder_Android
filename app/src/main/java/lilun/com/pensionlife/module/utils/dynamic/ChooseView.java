package lilun.com.pensionlife.module.utils.dynamic;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.App;

/**
 * 选择
 */
public class ChooseView extends BaseView {

    private Activity activity;
    private TextView tv_mate_value;

    public ChooseView(Activity context, JSONObject setting) {
        this.activity = context;
        setSetting(setting);
    }

    @Override
    public View createView() {
        return initView();
    }


    private View initView() {
        View chooseView = LayoutInflater.from(App.context).inflate(R.layout.dynamic_choose_view, null);

        TextView tv_mate_title = (TextView) chooseView.findViewById(R.id.mate_title);
        tv_mate_value = (TextView) chooseView.findViewById(R.id.mate_value);
        tv_mate_title.setText(mate_title);
//        tv_mate_value.setCompoundDrawables(null, null, App.context.getResources().getDrawable(R.drawable.add_contact_next), null);
        show();


        JSONObject display = setting.getJSONObject("display");
        Set<Map.Entry<String, Object>> entries = display.entrySet();
        ArrayList<String> data = new ArrayList<>();
        for (Map.Entry<String, Object> entry : entries) {
            String value = (String) entry.getValue();
            data.add(0,value);
        }
//        ArrayList<String> data = entries.stream().map(entry -> (String) entry.getValue()).collect(Collectors.toCollection(ArrayList::new));
        tv_mate_value.setOnClickListener(v -> showChooseDialog(data));
        return chooseView;

    }

    private void show() {
        if (mate_value != null) {
            JSONObject display = setting.getJSONObject("display");
            Object o = display.get(mate_value.toString());
            tv_mate_value.setText(o.toString());
        }
    }


    private void showChooseDialog(ArrayList<String> data) {
        //多项选择框
        if (mate_type.equals("array")) {
            Integer[] index = null;
            //显示初始化
            if (mate_value != null) {
                JSONArray mate_value_array = (JSONArray) this.mate_value;
                index = mate_value_array.toArray(null);
            }
            new MaterialDialog.Builder(activity)
                    .title(mate_title)
                    .items(data)
                    .itemsCallbackMultiChoice(index, new MaterialDialog.ListCallbackMultiChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                            setMultiData(which, text);
                            return false;
                        }
                    });

        }

        //单项选择框
        else {
            int index = -1;
            //初始化复制，boolean特殊类型做特殊处理
            if (mate_value != null) {
                JSONArray jsonArray = setting.getJSONArray("enum");
                index = jsonArray.indexOf(mate_value);
//                if (mate_type.equals("boolean")) {
//                } else {
//                    index = (int) mate_value;
//                }

            }
            new MaterialDialog.Builder(activity)
                    .title(mate_title)
                    .items(data)
                    .itemsCallbackSingleChoice(index, (dialog, view, which, text) -> {
                        setSingleData(which, text);
                        return true;
                    }).show();
        }


    }

    //多项选择-数据回填

    private void setMultiData(Integer[] which, CharSequence[] texts) {
        JSONArray jsonArray = setting.getJSONArray("enum");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < which.length; i++) {
            String text = (String) texts[i];
            jsonArray.remove(which[i]);
            if (i == 0) {
                builder.append(text);
            } else {
                builder.append("," + text);
            }
        }
        tv_mate_value.setText(builder.toString());
        setValue(jsonArray);

    }

    //单项选择-数据回填
    private void setSingleData(int which, CharSequence text) {
        JSONArray jsonArray = setting.getJSONArray("enum");
        tv_mate_value.setText(text);
        setValue(jsonArray.get(which));
    }
}
