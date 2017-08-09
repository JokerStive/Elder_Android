package lilun.com.pensionlife.ui.contact;

import android.view.LayoutInflater;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.base.BaseFragment;

/**
*新增contact基础信息
*@author yk
*create at 2017/8/9 9:34
*email : yk_developer@163.com
*/
public class AddBasicContactFragment extends BaseFragment {


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

    }
}
