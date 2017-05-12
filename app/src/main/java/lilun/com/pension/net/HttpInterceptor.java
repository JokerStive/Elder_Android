package lilun.com.pension.net;

import android.text.TextUtils;
import android.util.Log;

import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import lilun.com.pension.BuildConfig;
import lilun.com.pension.app.Event;
import lilun.com.pension.app.User;
import lilun.com.pension.module.utils.PreUtils;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;

/**
 * Created by youke on 2016/12/29.
 * okhttp3拦截器。token、401权限检查
 */
public class HttpInterceptor implements Interceptor {

    public static String TAG = "okhttp";
    private Response response;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        String token = User.getToken();

        if (!TextUtils.isEmpty(token)) {
            request = request.newBuilder().addHeader("Authorization", token).build();
        }

        Response response = chain.proceed(request);
        int code = response.code();
        //401并且不是登陆
        if (code == 401 && !TextUtils.isEmpty(User.getToken())) {
            if (!request.url().toString().contains("Accounts/me")) {
                Logger.d("出现了401需要去检查");
                EventBus.getDefault().post(new Event.PermissionDenied());
            } else {
                Logger.d("Accounts/me检查也是410，跳转登录界面");
                PreUtils.putString(User.token, "");
                EventBus.getDefault().post(new Event.TokenFailure());
            }
        }

        if (BuildConfig.LOG_DEBUG) {
//            log(response);
        }
        return response;
    }


    /**
     * 日志打印
     */
    private void log(Response response) throws IOException {
        String requestUrl = response.request().url().toString();
        String content = response.body().string();
        Log.d(TAG, "\n");
        Log.d(TAG, "\n");
        Log.d(TAG, "----------Start----------------");
        String method =  response.request().method();
        Log.d(TAG, "| request  | " + method + "  |  " + java.net.URLDecoder.decode(requestUrl, "UTF-8")+"   ");
        if ("POST".equals(method) || "PUT".equals(method)) {
            Log.d(TAG, "\n");
            try {
                final Request copy =  response.request().newBuilder().build();
                final Buffer buffer = new Buffer();
                copy.body().writeTo(buffer);
                Log.d(TAG, "| request | " + buffer.readUtf8());
            } catch (final IOException e) {
                Log.d(TAG, "catch error");
            }
        }

        long startNs = System.nanoTime();
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
        Log.d(TAG, "| response | " + content+"   ");
        Log.d(TAG, "----------End:" + tookMs + "ms----------");
    }

}
