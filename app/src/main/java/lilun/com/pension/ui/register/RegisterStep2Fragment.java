package lilun.com.pension.ui.register;

import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import lilun.com.pension.R;
import lilun.com.pension.base.BaseFragment;

/**
 * Created by zp on 2017/4/13.
 */

public class RegisterStep2Fragment extends BaseFragment {

    @Bind(R.id.ll_input)
    LinearLayout llRegisterName;
    @Bind(R.id.tv_input_title)
    TextView tvInputTitle;
    @Bind(R.id.acet_input)
    AppCompatEditText acetRegisterPhone;

    @Override
    protected void initPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_register_step1;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        tvInputTitle.setText(getString(R.string.phone_number));
        acetRegisterPhone.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
    }

    public String getRegisterPhone() {
        return acetRegisterPhone.getText().toString().trim();
    }


}
