package lilun.com.pensionlife.module.utils.dynamic;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
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
    private boolean isOnlyShow;
    private LinearLayout mView;
    private ArrayList<Result> results;
    private JSONObject template, target;
    private final DynamicDataParser dataParser;


    private DynamicLayoutManager(Activity activity, JSONObject template, boolean isOnlyShow) {
        this.isOnlyShow = isOnlyShow;
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
        Set<Map.Entry<String, Object>> templateEntries = template.entrySet();

        mView.removeAllViews();
        for (Map.Entry<String, Object> templateEntry : templateEntries) {
            String toJSONString = JSON.toJSONString(templateEntry.getValue());
            if (TextUtils.isEmpty(toJSONString)) {
                continue;
            }
            JSONObject templateValueJsonObject = JSONObject.parseObject(toJSONString);
            if (target != null) {
                Set<Map.Entry<String, Object>> targetEntries = target.entrySet();
                compareAndSetValue(targetEntries, templateEntry, templateValueJsonObject);
            }

            if (isOnlyShow) {
                if (templateValueJsonObject.containsKey("isOnlyShow")) {
                    parserData(templateValueJsonObject);
                }
            }else {
                parserData(templateValueJsonObject);
            }

        }


        return mView;
    }

    private void parserData(JSONObject templateValueJsonObject) {
        Result result = dataParser.getResult(templateValueJsonObject);
        if (result != null) {
            View view = result.resultView();
            mView.addView(view);
            results.add(result);
        }
    }

    /**
     * 对比原数据，设置数据源
     */
    private void compareAndSetValue(Set<Map.Entry<String, Object>> targetEntries, Map.Entry<String, Object> templateEntry, JSONObject templateValueJsonObject) {
        for (Map.Entry<String, Object> targetEntry : targetEntries) {
            String templateKey = templateEntry.getKey();
            String targetKey = targetEntry.getKey();
            if (targetKey.equals(templateKey)) {
                Object targetValue = targetEntry.getValue();
                templateValueJsonObject.put("value", targetValue);
                if (isOnlyShow) {
                    templateValueJsonObject.put("isOnlyShow", true);
                }
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
        private JSONObject template;
        private Activity activity;
        private boolean isOnlyShow;


        public Builder setContext(Activity activity) {
            this.activity = activity;
            return this;
        }

        public Builder template(JSONObject template) {
            this.template = template;
            return this;
        }

        public Builder isOnlyShow(boolean isOnlyShow) {
            this.isOnlyShow = isOnlyShow;
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


            return new DynamicLayoutManager(activity, template, isOnlyShow);
        }
    }

}
