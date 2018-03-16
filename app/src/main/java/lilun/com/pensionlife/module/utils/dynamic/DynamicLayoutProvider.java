package lilun.com.pensionlife.module.utils.dynamic;

import com.alibaba.fastjson.JSONObject;

import lilun.com.pensionlife.app.App;

/**
 * 动态布局提供者，根据不同的类型，返回不同的布局
 */
public class DynamicLayoutProvider {


    public Result createInputView(JSONObject setting) {
        return new InputView(App.context, setting);
    }


    public Result createChooseView(JSONObject setting) {
        return new InputView(App.context, setting);
    }


    public Result createTimeView(JSONObject setting) {
        return new InputView(App.context, setting);
    }


    public Result createPhotoView(JSONObject setting) {
        return new InputView(App.context, setting);
    }


}

