package lilun.com.pensionlife.pay;

import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.module.bean.PrePayResponse;
import lilun.com.pensionlife.module.utils.DeviceUtils;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.net.NetHelper;
import lilun.com.pensionlife.net.RxSubscriber;

public abstract class BasePay {

    /**
     * 预支付下单
     */
    public void prePay(String orderId, int payType, PrePayCallBack callBack) {

        NetHelper.getApi()
                .prePay(orderId, DeviceUtils.getAndroidID(App.context), payType)
                .compose(RxUtils.applySchedule())
                .compose(RxUtils.handleResult())
                .subscribe(new RxSubscriber<PrePayResponse>() {
                    @Override
                    public void _next(PrePayResponse prePayResponse) {
                        callBack.preSuccess(prePayResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        callBack.preFalse();
                    }
                });
    }


    /**
     * 预支付下单回调
     */
    public interface PrePayCallBack {
        void preSuccess(PrePayResponse response);

        void preFalse();
    }

    public abstract void pay(String orderId);
}
