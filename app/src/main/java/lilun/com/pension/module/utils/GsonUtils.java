package lilun.com.pension.module.utils;

import com.google.gson.Gson;

import lilun.com.pension.module.bean.Error;

/**
 * Created by yk on 2017/1/6.
 * gson解析器
 */
public class GsonUtils {
    public static Error string2Error(String s){
        return  new Gson().fromJson(s,Error.class);
    }
}
