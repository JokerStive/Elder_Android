package lilun.com.pensionlife.module.bean;

import com.alibaba.fastjson.JSONObject;

import lilun.com.pensionlife.base.BaseBean;

/**
 * Created by Admin on 2018/4/23.
 */
public class PrePayResponse extends BaseBean {

    /**
     * type : 2
     * options : {"return_code":"SUCCESS","return_msg":"OK","appid":"wx8862efa87d18d16f","mch_id":"1497311582","device_info":"none","nonce_str":"lqVI43ZKwdYKiJRa","sign":"489EC3232E51AE751B090939E585321B","result_code":"SUCCESS","prepay_id":"wx2410010749301994f104f4883215898679","trade_type":"APP"}
     */

    private int type;
    private JSONObject options;

    public int getType() {
        return type;
    }

    public PrePayResponse setType(int type) {
        this.type = type;
        return this;
    }

    public JSONObject getOptions() {
        return options;
    }

    public PrePayResponse setOptions(JSONObject options) {
        this.options = options;
        return this;
    }
}
