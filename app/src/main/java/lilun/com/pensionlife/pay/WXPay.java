package lilun.com.pensionlife.pay;

import android.app.Activity;

import com.alibaba.fastjson.JSONObject;
import com.orhanobut.logger.Logger;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.app.ConfigUri;
import lilun.com.pensionlife.module.bean.PrePayResponse;
import lilun.com.pensionlife.module.utils.ToastHelper;

/**
 * 微信支付实现
 */
public class WXPay extends BasePay {


    private final IWXAPI wxAPI;

    public WXPay(Activity activity) {
        wxAPI = WXAPIFactory.createWXAPI(App.context, ConfigUri.WXPAY_APPID, false);
        wxAPI.registerApp(ConfigUri.WXPAY_APPID);
    }

    @Override
    public void pay(String orderId) {
        if (!initCheck()) {
            return;
        }
        prePay(orderId, Cashier.WX_PAY, new PrePayCallBack() {
            @Override
            public void preSuccess(PrePayResponse response) {
                wxPay(response);
            }

            @Override
            public void preFalse() {

            }
        });
    }

    private boolean initCheck() {
        //TODO 是否安装微信,是否登录微信等检查
        boolean result = true;
        boolean wxAppInstalled = wxAPI.isWXAppInstalled();
        boolean wxAppSupportAPI = wxAPI.isWXAppSupportAPI();
        boolean isPaySupported = wxAPI.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;

        if (!wxAppInstalled) {
            ToastHelper.get().showWareShort("您还未安装微信,无法使用微信支付");
            result = false;
        }


        if (!wxAppSupportAPI) {
            ToastHelper.get().showWareShort("当前手机不支持微信支付");
            result = false;
        }


        if (!isPaySupported) {
            ToastHelper.get().showWareShort("当前手机不支持微信支付");
            result = false;
        }

        return result;
    }

    private void wxPay(PrePayResponse response) {

        JSONObject options = response.getOptions();

        PayReq request = new PayReq();

        request.appId = options.getString("appid");

        request.partnerId = options.getString("partnerid");

        request.prepayId = options.getString("prepayid");

        request.packageValue = "Sign=WXPay";

        request.nonceStr = options.getString("noncestr");

        request.timeStamp = options.getIntValue("timestamp") + "";

        request.sign = options.getString("sign");

        boolean checkArgs = request.checkArgs();

        boolean b = wxAPI.sendReq(request);

        Logger.d(checkArgs);

        Logger.d(b);


    }
}
