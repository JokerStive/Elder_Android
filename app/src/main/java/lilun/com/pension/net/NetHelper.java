package lilun.com.pension.net;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import lilun.com.pension.BuildConfig;
import lilun.com.pension.app.Config;
import lilun.com.pension.app.ConfigUri;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static lilun.com.pension.app.App.context;

/**
 * Created by youke on 2016/12/29.
 * 网络管理器，用于初始化网络框架
 */
public class NetHelper {
    private static OkHttpClient okHttpClient = null;
    private static ApiService apis;

    public static OkHttpClient getOkHttpClient() {
        initOkhttpClient();
        return okHttpClient;
    }


    public static ApiService getApi() {
        initOkhttpClient();
        if (apis == null) {
            apis = new Retrofit.Builder()
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .baseUrl(ConfigUri.BASE_URL)
                    .build().create(ApiService.class);
        }
        return apis;
    }

    private static void initOkhttpClient() {
        if (okHttpClient == null) {
            X509TrustManager trustManager = null;
            SSLSocketFactory sslSocketFactory = null;
            try {
                final InputStream inputStream;
                inputStream = context.getAssets().open("fcb619bd18c877b2.crt"); // 得到证书的输入流


                trustManager = trustManagerForCertificates(inputStream);//以流的方式读入证书
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, new TrustManager[]{trustManager}, null);
                sslSocketFactory = sslContext.getSocketFactory();

            } catch (GeneralSecurityException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                e.printStackTrace();
            }
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            if (BuildConfig.LOG_DEBUG) {
                LogInterceptor logInterceptor = new LogInterceptor();
                logInterceptor.setLevel(LogInterceptor.Level.BODY);
                builder.addInterceptor(logInterceptor);
            }
            okHttpClient = builder
                    .connectTimeout(Config.time_out, TimeUnit.SECONDS)
                    .readTimeout(Config.time_out, TimeUnit.SECONDS)
                    .writeTimeout(Config.time_out, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .addInterceptor(new HttpInterceptor())
                    .sslSocketFactory(sslSocketFactory, trustManager)
                    .build();
        }
    }

    private static X509TrustManager trustManagerForCertificates(InputStream in)
            throws GeneralSecurityException {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        Collection<? extends Certificate> certificates = certificateFactory.generateCertificates(in);
        if (certificates.isEmpty()) {
            throw new IllegalArgumentException("expected non-empty set of trusted certificates");
        }

        // Put the certificates a key store.
        char[] password = "password".toCharArray(); // Any password will work.
        KeyStore keyStore = newEmptyKeyStore(password);
        int index = 0;
        for (java.security.cert.Certificate certificate : certificates) {
            String certificateAlias = Integer.toString(index++);
            keyStore.setCertificateEntry(certificateAlias, certificate);
        }

        // Use it to build an X509 trust manager.
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(
                KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, password);
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:"
                    + Arrays.toString(trustManagers));
        }
        return (X509TrustManager) trustManagers[0];
    }

    /**
     * 添加password
     *
     * @param password
     * @return
     * @throws GeneralSecurityException
     */
    private static KeyStore newEmptyKeyStore(char[] password) throws GeneralSecurityException {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType()); // 这里添加自定义的密码，默认
            InputStream in = null; // By convention, 'null' creates an empty key store.
            keyStore.load(in, password);
            return keyStore;
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

}
