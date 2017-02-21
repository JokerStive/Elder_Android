package lilun.com.pension.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportActivity;

/**
 * Created by yk on 2017/1/5.
 * 基类activity
 */
public abstract class BaseActivity<T extends IPresenter> extends SupportActivity {

    protected T mPresenter;
//    final FragmentController mFragments = FragmentController.createController(new HostCallbacks());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());


        getTransferData();

        initData();

        ButterKnife.bind(this);

        initPresenter();

        initView();

        initEvent();
    }




    @Override
    protected void onStart() {

        super.onStart();

    }

    //    ===============子类需要实现的方法=============================

    //获取传递过来的数据,可选,最先被调用
    protected void getTransferData() {

    }

    protected void initData() {
    }

    protected abstract int getLayoutId();

    //非Mvp就不用管
    protected abstract void initPresenter();

    //初始化布局
    protected abstract void initView();

    //初始化事件,listener...
    protected  void initEvent(){};

    @Override
    public void onBackPressedSupport() {
        if (getSupportFragmentManager().getBackStackEntryCount() >=1) {
            pop();
        } else {
            finish();
        }
    }

//    public void showHintToPop(){
//        FragmentTransaction ft = fragmentManager.beginTransaction().show(showFragment);
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        if (mPresenter != null) {
            mPresenter.unBindView();
            mPresenter=null;
        }
    }
}
