package lilun.com.pensionlife.ui.home.upload;

import android.os.Environment;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * @author yk
 *         create at 2017/7/18 9:22
 *         email : yk_developer@163.com
 */

public class DownloadManager {

    /**
     * 目标文件存储的文件夹路径
     */
    private String destFileDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File
            .separator + "M_DEFAULT_DIR";
    /**
     * 目标文件存储的文件名
     */
    private String fileName = "test.apk";

    private Retrofit.Builder retrofit;

    private static DownloadManager instance;


    //获得一个单例类
    public static synchronized DownloadManager getInstance() {
        if (instance == null) {
            instance = new DownloadManager();
        }
        return instance;
    }


    /**
     * 下载文件
     */
    public void download(String apkName, DownLoadCallBack callBack) {
        if (TextUtils.isEmpty(apkName) || callBack == null) {
            throw new NullPointerException("文件名或回掉不能為空");
        }
        if (retrofit == null) {
            retrofit = new Retrofit.Builder();
        }
        retrofit.baseUrl("http://download.j1home.com/")
                .client(initOkHttpClient())
                .build()
                .create(IFileLoad.class)
                .loadFile(apkName)
                .enqueue(new FileCallback(destFileDir, fileName) {

                    @Override
                    public void onSuccess(File file) {
                        Logger.i("下載成功");
                        callBack.onSuccess(file);
                    }

                    @Override
                    public void onLoading(long progress, long total) {
                        Logger.i(progress + "----" + total);
                        callBack.onProgress((int) progress, (int) total);
                    }

                    @Override
                    public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                        Logger.i("请求失败");
                    }
                });
    }

    public interface IFileLoad {
        @GET
        retrofit2.Call<ResponseBody> loadFile(@Url String apkName);
    }


    /**
     * 初始化OkHttpClient
     *
     * @return
     */
    private OkHttpClient initOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(100000, TimeUnit.SECONDS);
        builder.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse
                        .newBuilder()
                        .body(new FileResponseBody(originalResponse))
                        .build();
            }
        });
        return builder.build();
    }

}
