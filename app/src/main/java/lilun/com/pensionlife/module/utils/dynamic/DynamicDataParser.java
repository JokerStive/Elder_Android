package lilun.com.pensionlife.module.utils.dynamic;

import android.app.Activity;

import com.alibaba.fastjson.JSONObject;

/**
 * 动态数据解析器
 */
public class DynamicDataParser {


    private Activity activity;
    private final DynamicLayoutProvider provider;

    public DynamicDataParser(Activity activity) {
        provider = new DynamicLayoutProvider(activity);
        this.activity = activity;
    }


    public Result getResult(JSONObject setting) {
        Result result = null;
        boolean containsEnum = setting.containsKey("enum");
        if (containsEnum) {
            result = provider.createChooseView(setting);
        } else {
            String type = setting.getString("type");
            switch (type) {
                case "date":
                case "datetime":
                case "time":
                    result = provider.createTimeView(setting);
                    break;

//                case "photo":
//                    result = provider.createPhotoView(setting);
//                    break;

                case "string":
                case "number":
                    result = provider.createInputView(setting);
                    break;
            }
        }

        return result;
    }

}
