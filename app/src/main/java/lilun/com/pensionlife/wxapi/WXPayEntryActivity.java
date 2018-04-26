package lilun.com.pensionlife.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.orhanobut.logger.Logger;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

import lilun.com.pensionlife.app.ConfigUri;
import lilun.com.pensionlife.app.Event;
import lilun.com.pensionlife.module.utils.ToastHelper;
import lilun.com.pensionlife.pay.Cashier;

/**
 * 微信支付回调
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {


    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, ConfigUri.WXPAY_APPID, false);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(getIntent(), this);
    }


    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {

        Logger.d("微信回调");
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            Event.PayResult payResult = new Event.PayResult();
            payResult.payType = Cashier.WX_PAY;
            if (resp.errCode == 0) {
                payResult.code = 1;

            } else {
                payResult.code = 0;
                ToastHelper.get().showWareShort("支付失败");
            }
            EventBus.getDefault().post(payResult);
            finish();
        }
    }
}
