package lilun.com.pensionlife.module.utils;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import lilun.com.pensionlife.app.Event;
import lilun.com.pensionlife.app.User;
import lilun.com.pensionlife.net.ApiException;
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
                int code = tResponse.code();

                //401并且不是登陆
                if (code == 401 && !TextUtils.isEmpty(User.getToken())) {
                    if (!tResponse.raw().request().url().toString().contains("Accounts/me")) {
                        Logger.d("出现了401需要去检查");
                        EventBus.getDefault().post(new Event.PermissionDenied());
                    } else {
                        Logger.d("Accounts/me检查也是410，跳转登录界面");
                        PreUtils.putString(User.token, "");
                        EventBus.getDefault().post(new Event.TokenFailure());
                    }
                }

                String errorString = "错误的结构有误";
                try {
                    errorString = tResponse.errorBody().string();
                    JSONObject jsonObject = JSONObject.parseObject(errorString);
                    JSONObject error = jsonObject.getJSONObject("error");
                    errorString = error.getString("message");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //503不可用
                if (code == 503) {
                    EventBus.getDefault().post(new Event.ServiceUnable(errorString));
                }




//                int error_code = 110;
//                try {
//                    errorString = tResponse.errorBody().string();
//                    JSONObject jsonObject = JSONObject.parseObject(errorString);
//                    String message = jsonObject.get("message").toString();
//                    JSONObject jsonObject = new JSONObject();
//                    jsonObject.
//                    Error error = GsonUtils.string2Error(error_message);
//                    if (error != null) {
//                        Error.ErrorBean errorBean = error.getError();
//                        if (errorBean != null) {
//                            error_code = errorBean.getStatus();
//                            error_message = errorBean.getMessage();
//                        }
//                    }else {
//                        Logger.d(error_message);
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                tResponse.errorBody().close();
                return Observable.error(new ApiException(code, errorString));
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

