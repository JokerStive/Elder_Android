package lilun.com.pension.net;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import lilun.com.pension.app.Event;
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

    private Response response;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        String token = User.getToken();

        if (!TextUtils.isEmpty(token)) {
            request = request.newBuilder().addHeader("Authorization", token).build();
        }

        response = chain.proceed(request);
        int code = response.code();
        //401并且不是登陆
        if (code == 401 && !TextUtils.isEmpty(User.getToken())) {
            if (!request.url().toString().contains("Accounts/me")) {
                Logger.d("出现了401需要去检查");
                EventBus.getDefault().post(new Event.PermissionDenied());
            } else {
                Logger.d("Accounts/me检查也是410，跳转登录界面");
                PreUtils.putString(User.token,"");
                EventBus.getDefault().post(new Event.TokenFailure());
            }
        }


        return response;
    }


}
