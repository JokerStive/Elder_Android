package lilun.com.pensionlife.module.utils.dynamic;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.App;

/**
 * 选择
 */
public class ChooseView extends BaseView {

    private Activity activity;
    private TextView tv_mate_value;
    private ArrayList<Integer> mulitpIndex = new ArrayList<>();

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
        show();


        if (!isOnlyShow) {
            JSONObject display = setting.getJSONObject("display");
            JSONArray enumArray = setting.getJSONArray("enum");
            ArrayList<String> data = new ArrayList<>();
            for (int i = 0; i < enumArray.size(); i++) {
                String key = enumArray.get(i).toString();
                String value = display.get(key).toString();
                data.add(value);
            }
            tv_mate_value.setOnClickListener(v -> showChooseDialog(data));
        } else {
            tv_mate_value.setCompoundDrawables(null, null, null, null);
        }

        return chooseView;

    }

    private void show() {
        if (mate_value != null) {
            JSONObject display = setting.getJSONObject("display");

            if (mate_type.equals("array")) {
                if (mate_value instanceof List) {
                    List valueList = (List) this.mate_value;
                    StringBuilder builder = new StringBuilder();
                    for (int i = 0; i < valueList.size(); i++) {
                        Object value = valueList.get(i);
                        if (value instanceof Double) {
                            int index = ((Double) value).intValue();
                            if (display.get(index + "") != null) {
                                String text = display.get(index + "").toString();
                                mulitpIndex.add(index);
                                if (i == 0) {
                                    builder.append(text);
                                } else {
                                    builder.append("," + text);
                                }
                            }
                        }
                    }
                    tv_mate_value.setText(builder.toString());

                }

            } else {
                Object o = display.get(mate_value.toString());
                if (o != null) {
                    tv_mate_value.setText(o.toString());
                }
            }
        }

    }


    private void showChooseDialog(ArrayList<String> data) {
        //多项选择框
        if (mate_type.equals("array")) {
            Integer[] index = mulitpIndex.toArray(new Integer[mulitpIndex.size()]);
            new MaterialDialog.Builder(activity)
                    .title(mate_title)
                    .items(data)
                    .itemsCallbackMultiChoice(index, new MaterialDialog.ListCallbackMultiChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                            setMultiData(which, text);
                            return true;
                        }
                    })
                    .positiveText(R.string.choose)
                    .show();

        }

        //单项选择框
        else {
            int index = -1;
            //初始化复制，boolean特殊类型做特殊处理
            if (mate_value != null) {
                JSONArray jsonArray = setting.getJSONArray("enum");
                index = jsonArray.indexOf(mate_value);

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
        JSONArray enumArray = setting.getJSONArray("enum");
        JSONArray jsonArray = new JSONArray();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < which.length; i++) {
            String text = (String) texts[i];
            jsonArray.add(enumArray.get(which[i]));
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
