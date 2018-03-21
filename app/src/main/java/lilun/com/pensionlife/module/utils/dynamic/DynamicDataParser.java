package lilun.com.pensionlife.module.utils.dynamic;

import android.app.Activity;

import com.alibaba.fastjson.JSONObject;

/**
 * 动态数据解析器
 */
public class DynamicDataParser {


    private Activity activity;

    public DynamicDataParser(Activity activity) {
        this.activity = activity;
    }


    public Result getResult(JSONObject setting) {
        DynamicLayoutProvider provider = new DynamicLayoutProvider(activity);
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
                case "integer":
                    result = provider.createInputView(setting);
                    break;
            }
        }

        return result;
    }

}
