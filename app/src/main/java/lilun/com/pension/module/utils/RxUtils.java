package lilun.com.pension.module.utils;

import com.orhanobut.logger.Logger;

import java.io.IOException;

import lilun.com.pension.module.bean.Error;
import lilun.com.pension.net.ApiException;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by youke on 2016/12/29.
 * Observable转换器，数据预处理，Observable.compose(RxUtils.handleResult())得到真正需要的数据
 */

public class RxUtils {
    public static <T> Observable.Transformer<Response<T>, T> handleResult() {
        return toObservable -> toObservable.doOnSubscribe(() -> {
            if (!SystemUtils.checkHasNet()) {
                throw new ApiException(111, "无网络连接");
            }
        }).flatMap(tResponse -> {
            if (tResponse.isSuccessful()) {
                return dataObservable(tResponse.body());
            } else {
                String error_message = "错误异常数据";
                int error_code = 110;
                try {
                    error_message = tResponse.errorBody().string();
                    Error error = GsonUtils.string2Error(error_message);
                    if (error != null) {
                        Error.ErrorBean errorBean = error.getError();
                        if (errorBean != null) {
                            error_code = errorBean.getStatus();
                            error_message = errorBean.getMessage();
                        }
                    }else {
                        Logger.d(error_message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                tResponse.errorBody().close();
                return Observable.error(new ApiException(error_code, error_message));
            }

        });
    }


    public static <T> Observable<T> dataObservable(final T data) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                try {
                    subscriber.onNext(data);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }


    /**
     * 统一线程处理
     *
     * @param <T>
     * @return
     */
    public static <T> Observable.Transformer<T, T> applySchedule() {    //compose简化线程
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}

