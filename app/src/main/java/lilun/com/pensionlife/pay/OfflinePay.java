package lilun.com.pensionlife.pay;

import org.greenrobot.eventbus.EventBus;

import lilun.com.pensionlife.app.Event;
import lilun.com.pensionlife.module.bean.PrePayResponse;

/**
 * 线下支付
 */
public class OfflinePay extends BasePay {
    @Override
    public void pay(String orderId) {
        prePay(orderId, Cashier.OFFLINE_PAY, new PrePayCallBack() {
            @Override
            public void preSuccess(PrePayResponse response) {
                Event.PayResult payResult = new Event.PayResult();
                payResult.payType = Cashier.OFFLINE_PAY;
                payResult.code = 1;
                EventBus.getDefault().post(payResult);
            }

            @Override
            public void preFalse() {

            }
        });
    }
}
