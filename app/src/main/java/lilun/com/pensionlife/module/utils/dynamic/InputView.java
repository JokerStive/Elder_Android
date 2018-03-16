package lilun.com.pensionlife.module.utils.dynamic;

import android.content.Context;
import android.view.View;

import com.alibaba.fastjson.JSONObject;

/**
 * 数据类型的布局
 */
public class InputView extends BaseView  {


    public InputView(Context context, JSONObject setting) {
        super(context);
        super.setting = setting;
    }


    @Override
    public View createView() {
        return null;
    }
}
