package lilun.com.pension.net;

import java.util.concurrent.TimeUnit;

import lilun.com.pension.app.Config;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by youke on 2016/12/29.
 * 网络管理器，用于初始化网络框架
 */
public class NetHelper {
    private static OkHttpClient okHttpClient = null;
    private static ApiService apis;


    public static ApiService getApi() {
        initOkhttpClient();
        if (apis == null) {
            apis = new Retrofit.Builder()
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .baseUrl(Config.BASE_URL)
                    .build().create(ApiService.class);
        }
        return apis;
    }

    private static void initOkhttpClient() {
        if (okHttpClient == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            okHttpClient = builder
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .addInterceptor(new HttpInterceptor())
                    .build();
        }
    }
}
