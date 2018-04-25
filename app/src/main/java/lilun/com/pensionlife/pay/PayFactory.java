package lilun.com.pensionlife.pay;

import android.app.Activity;

/**
 * 支付工厂
 */
public class PayFactory {
    public BasePay createPay(Activity activity, int payType) {
        BasePay pay=null;
        switch (payType) {
            case Cashier.ALI_PAY:
                pay = new AliPay(activity);
                break;
            case Cashier.WX_PAY:
                pay = new WXPay(activity);
                break;
        }

        return pay;
    }
}

