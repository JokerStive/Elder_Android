package lilun.com.pensionlife.ui.home.help;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.module.utils.PreUtils;

/**
 * 紧急求助协议dialog
 *
 * @author yk
 *         create at 2017/1/24 16:05
 *         email : yk_developer@163.com
 */
public class HelpProtocolDialogFragment extends DialogFragment implements View.OnClickListener {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_hepl_protocol, container);
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


        view.findViewById(R.id.btn_agree_protocol).setOnClickListener(this);
        view.findViewById(R.id.btn_refuse_protocol).setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
//        int screenWith = ScreenUtils.getScreenWith(App.context);
//        int screenHeight  = ScreenUtils.getScreenHeight(App.context);
//        Window window = getDialog().getWindow();
//        window .setLayout(UIUtils.dp2px(App.context,287), screenHeight/4*3);
//        window.setGravity(Gravity.CENTER);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_agree_protocol:
                chooseFirstHelper();
                break;
            case R.id.btn_refuse_protocol:
                dismiss();
                break;
        }
    }

    private void chooseFirstHelper() {
        PreUtils.putBoolean("helpProtocol", false);
        new FirstHelperDialogFragment().show(getActivity().getFragmentManager(), FirstHelperDialogFragment.class.getSimpleName());
        dismiss();
    }
}
