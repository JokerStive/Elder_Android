package lilun.com.pensionlife.pay;

/**
 * 支付结果回调
 */
public abstract class PayCallBack {
    public abstract void paySuccess();

    public void payFalse() {
    }

    public void cancel() {
    }

}
