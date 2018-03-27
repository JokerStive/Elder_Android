package lilun.com.pensionlife.module.utils.dynamic;

import android.view.View;

import com.alibaba.fastjson.JSONObject;

import lilun.com.pensionlife.module.utils.ToastHelper;

/**
 * 动态控件的父类
 */
public abstract class BaseView implements Result {

    protected JSONObject setting;
    protected String mate_id;
    protected String mate_title;
    protected String mate_description;
    protected String mate_type;
    protected Object mate_value;
    protected boolean isOnlyShow;

    protected void setSetting(JSONObject setting) {
        this.setting = setting;

        if (setting != null) {
            mate_id = setting.getString("id");
            mate_title = setting.getString("title");
            mate_description = setting.getString("description");
            mate_type = setting.getString("type");
            Object valueObj = setting.get("value");
            if (setting.containsKey("isOnlyShow")) {
                isOnlyShow = true;
            }
            if (valueObj instanceof Double) {
                Double valueDouble = (Double) valueObj;
                mate_value = valueDouble.intValue();
                return;
            }
            mate_value = setting.get("value");


        }
    }


    @Override
    public String resultKey() {
        return mate_id;
    }

    @Override
    public Object resultValue() {
        return mate_value;
    }

    @Override
    public View resultView() {
        return createView();
    }


    @Override
    public boolean isPassRequireCheck() {
        boolean result = true;
        boolean containsKey = setting.containsKey("required");
        if (containsKey) {
            Boolean required = setting.getBoolean("required");
            if (required) {
                result = mate_value != null;
                if (!result) {
                    ToastHelper.get().showWareShort(mate_title + "不能为空");
                }
            }
        }
        return result;
    }


    protected void setValue(Object value) {
        mate_value = value;
    }

    public abstract View createView();


}
