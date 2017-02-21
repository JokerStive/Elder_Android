package lilun.com.pension.net;

import android.text.TextUtils;

import java.io.IOException;

import lilun.com.pension.app.User;
import lilun.com.pension.module.utils.PreUtils;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by youke on 2016/12/29.
 * okhttp3拦截器。token、401权限检查
 */
public class HttpInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        String token = PreUtils.getString(User.token,"");

        if (!TextUtils.isEmpty(token)) {
            request = request.newBuilder().addHeader("Authorization", token).build();
        }

        Response response = chain.proceed(request);
        int code = response.code();
        //401并且不是登陆
        if (code == 401 && !TextUtils.isEmpty(token)) {
            if (!request.url().toString().contains("Accounts/me")) {
//                    RxBus.getDefault().send(new  RxbusEvent.Check401());
            } else {
//                Logger.d("是token过期跳转登录界面");
//                    RxBus.getDefault().send(new  RxbusEvent.BackToLogin());
            }
        }

//
//            if (!TextUtils.isEmpty(cacheControl.toString()) && response.code() == 200) {
//                if (NetUtil.checkNet(App.app)) {
//                    response = response.newBuilder()
//                            .header("Cache-Control", "public, max-age=" + cacheControl.maxAgeSeconds())
//                            .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
//                            .build();
//                } else {
//                    response = response.newBuilder()
//                            .header("Cache-Control", "public, only-if-cached, max-stale=" + cacheControl.maxStaleSeconds())
//                            .removeHeader("Pragma")
//                            .build();
//                }
//
//            }


        return response;
    }


}
