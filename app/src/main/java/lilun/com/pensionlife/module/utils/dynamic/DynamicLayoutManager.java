package lilun.com.pensionlife.module.utils.dynamic;

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
    private JSONObject target, old;
    private ArrayList<Result> results;


    public DynamicLayoutManager() {

    }

    /**
     * 生成布局
     */
    public LinearLayout createDynamicLayout() {
        if (target == null) {
            return null;
        }

        mView = new LinearLayout(App.context);
        mView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mView.setOrientation(LinearLayout.VERTICAL);

        DynamicDataParser parser = new DynamicDataParser();
        results = new ArrayList<>();


        Set<Map.Entry<String, Object>> targetEntries = target.entrySet();


        for (Map.Entry<String, Object> targetEntry : targetEntries) {
            JSONObject targetValue = JSONObject.parseObject((String) targetEntry.getValue());

            if (old != null) {
                Set<Map.Entry<String, Object>> oldEntries = old.entrySet();
                compareAndSetValue(oldEntries, targetEntry, targetValue);
            }

            Result result = parser.getResult(targetValue);
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
        JSONObject finallyData = old;
        if (finallyData == null) {
            finallyData = new JSONObject();
        }
        for (Result result : results) {
            finallyData.put(result.resultKey(), result.resultValue());
        }
        return finallyData;
    }


    public static final class Builder {

    }

}
