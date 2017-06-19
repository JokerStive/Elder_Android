package lilun.com.pensionlife.ui.welcome;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.base.BaseActivity;
import lilun.com.pensionlife.module.adapter.PushInfoAdapter;

public class ChangePasswordActivity extends BaseActivity {

    private RecyclerView rvPushInfo;
    private PushInfoAdapter pushInfoAdapter;
    private ArrayList<String> data;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initPresenter() {
    }

    @Override
    protected void initView() {
        replaceLoadRootFragment(R.id.fl_root_container, new ChangePasswordFragment1(), false);

    }

    @Override
    protected void initEvent() {

    }



}
