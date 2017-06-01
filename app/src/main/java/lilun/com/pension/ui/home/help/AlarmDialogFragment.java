package lilun.com.pension.ui.home.help;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import java.util.Date;

import lilun.com.pension.R;
import lilun.com.pension.app.App;
import lilun.com.pension.app.User;
import lilun.com.pension.module.bean.OrganizationAid;
import lilun.com.pension.module.bean.PushMessage;
import lilun.com.pension.module.utils.BaiduLocation;
import lilun.com.pension.module.utils.PreUtils;
import lilun.com.pension.module.utils.StringUtils;
import lilun.com.pension.module.utils.ToastHelper;
import lilun.com.pension.module.utils.mqtt.MQTTManager;
import lilun.com.pension.widget.AlarmView;

/**
 * 倒计时dialog
 *
 * @author yk
 *         create at 2017/1/24 16:05
 *         email : yk_developer@163.com
 */
public class AlarmDialogFragment extends DialogFragment {

    private AlarmView alarm;
    private String phone;
    private double currentLongitude;
    private double currentLatitude;
    private String currentAddress;

    public static AlarmDialogFragment newInstance(String phone) {
        AlarmDialogFragment fragment = new AlarmDialogFragment();
        Bundle args = new Bundle();
        args.putString("phone", phone);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String phone = getArguments().getString("phone");
        this.phone = StringUtils.checkNotEmpty(phone);

        BaiduLocation.getLocation(getActivity());
        BaiduLocation.setMyLocationListener(new BaiduLocation.currentLocationListener() {
            @Override
            public void currentLocation(double mylongitude, double mylatitude, String address) {
                currentLongitude = mylongitude;
                currentLatitude = mylatitude;
                currentAddress = address;
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_count_down_help, container);
        initView(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null && App.widthDP > 820) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow().setLayout((int) (dm.widthPixels * 0.75), ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    private void initView(View view) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        alarm = (AlarmView) view.findViewById(R.id.alarm);
        alarm.setOnStopTimingListener(this::sendSOS);

        TextView tvDesc = (TextView) view.findViewById(R.id.tv_desc);
        String desc = getActivity().getResources().getString(R.string.count_down_help_desc);
        String format = String.format(desc, alarm.getTime());
        tvDesc.setText(format);


        Button btnCancelHelp = (Button) view.findViewById(R.id.btn_cancel_help);
        btnCancelHelp.setOnClickListener(v -> {
            dismiss();
        });
    }

    /**
     * 发送求助信号
     */
    private void sendSOS() {
        //TODO 推送求助信号
        postAid();

    }

    private OrganizationAid newAid() {
        OrganizationAid aid = new OrganizationAid();
        aid.setKind(2);
        aid.setPrice(10);
        aid.setTitle("紧急求助");
        Logger.d("当前aid的经纬度==地址" + currentLongitude + "---" + currentLatitude + "---" + currentAddress);
        if (currentLongitude != 0 && currentLatitude != 0 && !TextUtils.isEmpty(currentAddress)) {
            aid.setMemo(currentLongitude + "/" + currentLatitude);
            aid.setAddress(currentAddress);
        }
        aid.setMobile(User.getMobile());
        return aid;
    }

    private void postAid() {
//        NetHelper.getApi()
//                .newOrganizationAid(organizationAid)
//                .compose(RxUtils.handleResult())
//                .compose(RxUtils.applySchedule())
//                .subscribe(new RxSubscriber<OrganizationAid>() {
//                    @Override
//                    public void _next(OrganizationAid organizationAid) {
//                        ToastHelper.get().showShort("发送成功");
//                        dismiss();
//                    }
//                });
        String topic = StringUtils.encodeURL(User.getBelongToDistrict() + "/#aid/.help").replace("%2F", "/");
        PushMessage pushMessage = new PushMessage();
        pushMessage.setVerb(PushMessage.VERB_HELP)
                .setTitle(User.getName())
                .setTime(StringUtils.date2String(new Date()))
                .setMobile(User.getMobile())
                .setFrom(User.getUserId())
                .setAddress(currentAddress)
                .setLocation("{\"lat\":\"" + currentLatitude + "\",\"lng\":\"" + currentLongitude + "\"}")
                .setMessage("")
                .setPriority("critical");


        MQTTManager.getInstance().publish(topic, 2, pushMessage.getJsonStr());
        dismiss();
        String message = User.getName() + "向您紧急求助，他/她现在的位置在 " + currentAddress
                + ",联系电话：" + User.getMobile() + "，请尽快联系。";
        sendSmsWithBody(getContext(), PreUtils.getString("firstHelperPhone", ""), message);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        alarm.stopTiming();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        alarm.stopTiming();
    }

    /**
     * 调用系统界面，给指定的号码发送短信，并附带短信内容
     *
     * @param context
     * @param number
     * @param body
     */
    public void sendSmsWithBody(Context context, String number, String body) {
        try {
            Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
            sendIntent.setData(Uri.parse("smsto:" + number));
            sendIntent.putExtra("sms_body", body);
            context.startActivity(sendIntent);
        } catch (Exception e) {
            e.printStackTrace();
            ToastHelper.get(getContext()).showWareShort("短信调用失败");
        }
    }

}
