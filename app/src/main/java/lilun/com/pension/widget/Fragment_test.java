package lilun.com.pension.widget;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TextView;

import butterknife.Bind;
import lilun.com.pension.R;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.utils.Preconditions;

/**
 * ceshi
 *
 * @author yk
 *         create at 2017/3/6 19:45
 *         email : yk_developer@163.com
 */
public class Fragment_test extends BaseFragment {
    @Bind(R.id.tv_test)
    TextView tvTest;
    private String mStatus;

    @Override
    protected void initPresenter() {

    }

    public static Fragment_test newInstance(String status) {
        Fragment_test fragment = new Fragment_test();
        Bundle args = new Bundle();
        args.putString("status", status);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void getTransferData(Bundle arguments) {
        super.getTransferData(arguments);
        mStatus = arguments.getString("status");
        Preconditions.checkNull(mStatus);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        tvTest.setText(mStatus);
    }


}
