package lilun.com.pensionlife.ui.contact;

import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.Bind;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.User;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.widget.NormalTitleBar;
import lilun.com.pensionlife.widget.SwitchButton;

/**
 * 新增contact基础信息
 *
 * @author yk
 *         create at 2017/8/9 9:34
 *         email : yk_developer@163.com
 */
public class AddBasicContactFragment extends BaseFragment {


    @Bind(R.id.titleBar)
    NormalTitleBar titleBar;
    @Bind(R.id.et_contact_name)
    EditText etContactName;
    @Bind(R.id.et_contact_mobile)
    EditText etContactMobile;
    @Bind(R.id.tv_choose_address)
    TextView tvChooseAddress;
    @Bind(R.id.et_contact_address)
    EditText etContactAddress;
    @Bind(R.id.sb_set_default)
    SwitchButton sbSetDefault;

    public static AddBasicContactFragment newInstance() {
        AddBasicContactFragment fragment = new AddBasicContactFragment();
//        Bundle args = new Bundle();
//        args.putSerializable("productId", productId);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_add_basic_contact;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        titleBar.setOnBackClickListener(this::pop);
        etContactMobile.setText(User.getMobile());
    }

}
