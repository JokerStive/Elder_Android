package lilun.com.pensionlife.pay;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import lilun.com.pensionlife.app.Event;
import lilun.com.pensionlife.module.bean.PrePayResponse;
import lilun.com.pensionlife.module.utils.RxUtils;
import rx.Observable;
import rx.Subscriber;

/**
 * 支付宝支付实现
 */
public class AliPay extends BasePay {


    private Activity mActivity;

    public AliPay(Activity activity) {
//        if (SystemUtils.isApkInDebug()) {
//            EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
//        }
        this.mActivity = activity;
    }

    @Override
    public void pay(String orderId) {
        prePay(orderId, Cashier.ALI_PAY, new PrePayCallBack() {
            @Override
            public void preSuccess(PrePayResponse response) {
                aliPay(response);
            }

            @Override
            public void preFalse() {

            }
        });


        Intent intent = new Intent();
        String 标准data = "<scheme>://<host>:<port>/<path>";
        intent.setData(Uri.parse("content://youke.net:200/zhang"));
    }


    private void aliPay(PrePayResponse response) {
        JSONObject options = response.getOptions();
        String orderInfo = options.getString("orderInfo");
        Observable.create(new Observable.OnSubscribe<Map<String, String>>() {
            @Override
            public void call(Subscriber<? super Map<String, String>> subscriber) {
                PayTask aliPay = new PayTask(mActivity);
                Map<String, String> result = aliPay.payV2(orderInfo, true);
                subscriber.onNext(result);
            }
        })
                .compose(RxUtils.applySchedule())
                .subscribe(new Subscriber<Map<String, String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Map<String, String> result) {
                        String resultStatus = result.get("resultStatus");
                        Event.PayResult payResult = new Event.PayResult();
                        payResult.payType = Cashier.ALI_PAY;
                        if (TextUtils.equals(resultStatus, "9000")) {
                            payResult.code = 1;
//                            ToastHelper.get().showWareShort("支付成功");
                        } else {
                            payResult.code = 0;
//                            ToastHelper.get().showWareShort("支付失败");
                        }
                        EventBus.getDefault().post(payResult);
                    }
                });
    }
}
