package lilun.com.pension.ui.home.help;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import lilun.com.pension.R;
import lilun.com.pension.module.utils.StringUtils;
import lilun.com.pension.widget.AlarmView;

/**
*倒计时dialog
*@author yk
*create at 2017/1/24 16:05
*email : yk_developer@163.com
*/
public class AlarmDialogFragment extends DialogFragment {

    private AlarmView alarm;
    private String phone;

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
        this.phone=StringUtils.checkNotEmpty(phone);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_count_down_help, container);
        initView(view);
        return view;
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
        btnCancelHelp.setOnClickListener(v -> dismiss());
    }

    /**
    *发送求助信号
    */
    private void sendSOS() {
        //TODO 发送短信和推送求助信号
        dismiss();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        alarm.stopTiming();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        Logger.d("onDestroyView");
        alarm.stopTiming();
    }
}
