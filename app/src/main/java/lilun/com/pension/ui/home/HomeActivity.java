package lilun.com.pension.ui.home;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import lilun.com.pension.R;
import lilun.com.pension.base.BaseActivity;
import lilun.com.pension.module.adapter.PushInfoAdapter;

public class HomeActivity extends BaseActivity {

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
        replaceLoadRootFragment(R.id.fl_root_container,new HomeFragment(),false);

    }

    @Override
    protected void initEvent() {

    }
}