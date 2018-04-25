package lilun.com.pensionlife.pay;

import android.app.DialogFragment;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.Event;

/**
 * 选择支付方式
 */
public class Cashier extends DialogFragment implements View.OnClickListener {
    public static final int ALI_PAY = 2;
    public static final int WX_PAY = 1;

    private TextView tvAmount;
    private TextView tvWXPay, tvAliPay, tvOfflinePay;
    private String orderId;
    private String price;
    private PayCallBack callBack;
    private ArrayList<String> paymentMethods;


    @Subscribe
    public void paySuccess(Event.PayResult payResult) {
        if (callBack != null && payResult.code == 0) {
            callBack.payFalse();
        }

        if (callBack != null && payResult.code == 1) {
            callBack.paySuccess();
        }

        unregister();
    }

    public static Cashier newInstance(String orderId, String price, ArrayList<String> paymentMethods) {
        Cashier fragment = new Cashier();
        Bundle args = new Bundle();
        args.putString("orderId", orderId);
        args.putString("price", price);
        args.putStringArrayList("paymentMethods", paymentMethods);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        orderId = getArguments().getString("orderId");
        price = getArguments().getString("price");
        paymentMethods = getArguments().getStringArrayList("paymentMethods");

        if (TextUtils.isEmpty(orderId) || TextUtils.isEmpty(price) || paymentMethods == null) {
            throw new RuntimeException("orderId-price -paymentMethods not be null");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setStyle(R.style.ActionSheetDialogStyle, 0);
        View view = inflater.inflate(R.layout.choose_pay_way, null);
        view.findViewById(R.id.iv_dismiss).setOnClickListener(this);
        tvAmount = (TextView) view.findViewById(R.id.tv_amount);
        tvAmount.setText("￥" + price);
        tvWXPay = (TextView) view.findViewById(R.id.tv_aliPay);
        tvAliPay = (TextView) view.findViewById(R.id.tv_weChatPay);
        tvOfflinePay = (TextView) view.findViewById(R.id.tv_offlinePay);
        tvAliPay.setOnClickListener(this);
        tvWXPay.setOnClickListener(this);
        tvOfflinePay.setOnClickListener(this);

        for (String paymentMethod : paymentMethods) {
            if (TextUtils.equals(paymentMethod, Order.OaymentMethods.alipay)) {
                tvAliPay.setVisibility(View.VISIBLE);
            }

            if (TextUtils.equals(paymentMethod, Order.OaymentMethods.weixin)) {
                tvWXPay.setVisibility(View.VISIBLE);
            }

            if (TextUtils.equals(paymentMethod, Order.OaymentMethods.offline)) {
                tvOfflinePay.setVisibility(View.VISIBLE);
            }
        }

        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        super.onActivityCreated(savedInstanceState);

        getDialog().getWindow().setGravity(Gravity.BOTTOM);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0x00000000));
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }


    @Override
    public void onClick(View v) {
        PayFactory payFactory = new PayFactory();
        switch (v.getId()) {
            case R.id.tv_aliPay:
                payFactory.createPay(getActivity(), ALI_PAY).pay(orderId);
                dismiss();
                break;
            case R.id.tv_weChatPay:
                payFactory.createPay(getActivity(), WX_PAY).pay(orderId);
                dismiss();
                break;

            case R.id.iv_dismiss:
                if (callBack != null) {
                    callBack.cancel();
                }
                unregister();
                dismiss();
        }
    }


    public void setPayCallBack(PayCallBack callBack) {
        this.callBack = callBack;
    }


    private void unregister() {
        EventBus.getDefault().unregister(this);
    }
}
