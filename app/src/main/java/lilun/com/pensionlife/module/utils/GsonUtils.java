package lilun.com.pensionlife.module.utils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import lilun.com.pensionlife.module.bean.Error;

/**
 * Created by yk on 2017/1/6.
 * gson解析器
 */
public class GsonUtils {
    public static Error string2Error(String s){
        Error error=null;
        try {
            error = new Gson().fromJson(s, Error.class);
        }catch (JsonSyntaxException exception) {
            return error;
        }
        return error;
    }


    public static JSONObject objectToJSONObject(Object object) throws JSONException {
        Gson gson = new Gson();
        String result = gson.toJson(object);
        return  new JSONObject(result);
    }
}
