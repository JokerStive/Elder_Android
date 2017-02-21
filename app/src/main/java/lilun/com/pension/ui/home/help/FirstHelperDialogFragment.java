package lilun.com.pension.ui.home.help;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import lilun.com.pension.R;
import lilun.com.pension.module.utils.StringUtils;

/**
 * 第一求助人dialog
 *
 * @author yk
 *         create at 2017/1/24 16:05
 *         email : yk_developer@163.com
 */
public class FirstHelperDialogFragment extends DialogFragment implements View.OnClickListener {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_first_helper, container);
        initView(view);
        return view;
    }

    private void initView(View view) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);


        view.findViewById(R.id.tv_family).setOnClickListener(this);
        view.findViewById(R.id.tv_neighbor).setOnClickListener(this);
        view.findViewById(R.id.tv_security).setOnClickListener(this);
        view.findViewById(R.id.btn_cancel_help).setOnClickListener(this);

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_family:
                firstHelperPhone(v);
                break;
            case R.id.tv_neighbor:
                firstHelperPhone(v);
                break;

            case R.id.tv_security:
                firstHelperPhone(v);
                break;

            case R.id.btn_cancel_help:
                dismiss();
                break;
        }
    }

    private void firstHelperPhone(View v) {
        TextView textView = (TextView) v;
        String string = textView.getText().toString();
        FirstHelperPhoneDialogFragment.newInstance(StringUtils.filterNull(string)).show(getActivity().getFragmentManager(),FirstHelperPhoneDialogFragment.class.getSimpleName());
        dismiss();
    }
}
