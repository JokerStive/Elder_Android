package lilun.com.pensionlife.module.utils.dynamic;

import com.alibaba.fastjson.JSONObject;

/**
 * 动态数据解析器
 */
public class DynamicDataParser {


    public DynamicDataParser() {
    }


    public Result getResult(JSONObject setting) {
        DynamicLayoutProvider provider = new DynamicLayoutProvider();
        Result result = null;
        boolean containsEnum = setting.containsKey("enum");
        if (containsEnum) {
            result = provider.createChooseView(setting);
        } else {
            String type = setting.getString("type");
            switch (type) {
                case "time":
                    result = provider.createTimeView(setting);
                    break;

                case "photo":
                    result = provider.createPhotoView(setting);
                    break;

                case "string":
                case "integer":
                    result = provider.createInputView(setting);
                    break;
            }
        }

        return result;
    }

}
