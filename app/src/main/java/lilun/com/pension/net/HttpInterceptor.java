package lilun.com.pension.net;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import lilun.com.pension.app.Event;
import lilun.com.pension.app.User;
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
                Logger.d("401 permission denied");
                EventBus.getDefault().post(new Event.PermissionDenied());
            } else {
                Logger.d("TokenFailure");
                EventBus.getDefault().post(new Event.TokenFailure());
            }
        }


        return response;
    }


}
