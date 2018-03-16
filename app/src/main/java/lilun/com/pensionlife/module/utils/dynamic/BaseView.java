package lilun.com.pensionlife.module.utils.dynamic;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSONObject;

/**
 * 动态控件的父类
 */
public abstract class BaseView extends RelativeLayout implements Result {

    protected JSONObject setting;

    public BaseView(Context context) {
        super(context);
    }


    @Override
    public String resultKey() {
        return setting.getString("id");
    }

    @Override
    public String resultValue() {
        return setting.getString("value");
    }

    @Override
    public View resultView() {
        return createView();
    }


    protected void setValue(Object value) {
        setting.put("value", value);
    }

    public abstract View createView();


}
