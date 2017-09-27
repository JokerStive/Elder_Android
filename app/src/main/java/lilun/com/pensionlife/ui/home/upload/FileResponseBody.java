package lilun.com.pensionlife.ui.home.upload;


import java.io.IOException;

import lilun.com.pensionlife.app.Config;
import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;

/**
 * @author yk
 *         create at 2017/7/20 16:41
 *         email : yk_developer@163.com
 */
public class FileResponseBody extends ResponseBody {

    Response originalResponse;
    long total = 0;

    public FileResponseBody(Response originalResponse) {
        this.originalResponse = originalResponse;
    }

    @Override
    public MediaType contentType() {
        return originalResponse.body().contentType();
    }

    @Override
    public long contentLength() {
//        if (total <= 0) {
//            InputStream inputStream = originalResponse.body().byteStream();
//            try {
//                ByteArrayOutputStream os = new ByteArrayOutputStream();
//                int b;
//                while ((b = inputStream.read()) != -1)
//                    os.write(b);
//                total = os.size();
//                Logger.d("总长度 ---" + total);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        return Config.apkTotalSize;
    }

    @Override
    public BufferedSource source() {
        return Okio.buffer(new ForwardingSource(originalResponse.body().source()) {
            long bytesReaded = 0;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                bytesReaded += bytesRead == -1 ? 0 : bytesRead;
                RxBus.getDefault().post(new DownloadInfo(contentLength(), bytesReaded));
                return bytesRead;
            }
        });
    }
}
