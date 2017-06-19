package lilun.com.pensionlife.module.utils;

import java.util.HashMap;

import lilun.com.pensionlife.app.Config;
import lilun.com.pensionlife.base.BaseFragment;

/**
 * Created by yk on 2017/1/5.
 * 创建fragment的工厂类
 */
public class FragmentFactory {
    private static HashMap<String, BaseFragment> hashMap = new HashMap<>();

    public static BaseFragment createFragmentById(String id) {

        BaseFragment baseFragment = null;

        if (hashMap.containsKey(id)) {

            baseFragment = hashMap.get(id);

        } else {

            if (id.equals(Config.help)){

            }
            hashMap.put(id,baseFragment);
        }

        return baseFragment;
    }
}
