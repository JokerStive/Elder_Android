package lilun.com.pensionlife.module.utils.dynamic;

import android.app.Activity;

import com.alibaba.fastjson.JSONObject;

/**
 * 动态布局提供者，根据不同的类型，返回不同的布局
 */
public class DynamicLayoutProvider {


    private Activity activity;

    public DynamicLayoutProvider(Activity context) {
        this.activity = context;
    }

    public Result createInputView(JSONObject setting) {
        return new InputView(setting);
    }


    public Result createChooseView(JSONObject setting) {
        return new ChooseView(activity, setting);
    }


    public Result createTimeView(JSONObject setting) {
        return new DateView(activity, setting);
    }


//    public Result createPhotoView(JSONObject setting) {
//        return new InputView(App.context, setting);
//    }


}

