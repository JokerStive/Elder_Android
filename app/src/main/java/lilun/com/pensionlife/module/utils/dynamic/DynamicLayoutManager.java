package lilun.com.pensionlife.module.utils.dynamic;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import lilun.com.pensionlife.app.App;

/*
* 动态布局管理
* 1.
*
* */
public class DynamicLayoutManager {
    private LinearLayout mView;
    private ArrayList<Result> results;
    private JSONObject template, target;
    private final DynamicDataParser dataParser;


    private DynamicLayoutManager(Activity activity, JSONObject template, JSONObject target) {
//        this.activity = activity;
        this.template = template;
//        this.target = target;


        mView = new LinearLayout(App.context);
        mView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mView.setOrientation(LinearLayout.VERTICAL);
        dataParser = new DynamicDataParser(activity);
        results = new ArrayList<>();
    }

    /**
     * 生成布局
     */
    public LinearLayout createDynamicLayout(JSONObject target) {

        this.target = target;
        Set<Map.Entry<String, Object>> targetEntries = template.entrySet();

        mView.removeAllViews();
        for (Map.Entry<String, Object> targetEntry : targetEntries) {
            JSONObject targetValue = (JSONObject) targetEntry.getValue();

            if (target != null) {
                Set<Map.Entry<String, Object>> oldEntries = target.entrySet();
                compareAndSetValue(oldEntries, targetEntry, targetValue);
            }

            Result result = dataParser.getResult(targetValue);
            results.add(result);
            mView.addView(result.resultView());
        }


        return mView;
    }

    /**
     * 对比原数据，设置数据源
     */
    private void compareAndSetValue(Set<Map.Entry<String, Object>> oldEntries, Map.Entry<String, Object> targetEntry, JSONObject targetValue) {
        for (Map.Entry<String, Object> oldEntry : oldEntries) {
            String targetKey = targetEntry.getKey();
            String oldKey = oldEntry.getKey();
            if (targetKey.equals(oldKey)) {
                Object oldValue = oldEntry.getValue();
                targetValue.put("value", oldValue);
            }
        }
    }

    /**
     * 获取最终的数据
     */
    public JSONObject getFinallyData() {
        JSONObject finallyData = target;
        if (finallyData == null) {
            finallyData = new JSONObject();
        }
        for (Result result : results) {
            if (result.isPassRequireCheck()) {
                finallyData.put(result.resultKey(), result.resultValue());
            } else {
                return null;
            }

        }
        return finallyData;
    }


    public static final class Builder {
        private JSONObject template, target;
        private Activity activity;


        public Builder setContext(Activity activity) {
            this.activity = activity;
            return this;
        }

        public Builder template(JSONObject template) {
            this.template = template;
            return this;
        }

//        public Builder target(JSONObject target) {
//            this.target = target;
//            return this;
//        }


        public DynamicLayoutManager build() {
            if (activity == null) {
                throw new IllegalStateException("activity required.");
            }

            if (template == null) {
                throw new IllegalStateException("template required.");
            }


            return new DynamicLayoutManager(activity, template, target);
        }
    }

}
