package lilun.com.pension.ui.residential.detail;

import lilun.com.pension.R;
import lilun.com.pension.base.BaseActivity;
import lilun.com.pension.module.utils.Preconditions;

/**
*
*@author yk
*create at 2017/3/7 10:35
*email : yk_developer@163.com
*/
public class OrderDetailActivity extends BaseActivity {

    private String mOrderId;

    @Override
    protected void getTransferData() {
        super.getTransferData();
        mOrderId = getIntent().getStringExtra("orderId");
        Preconditions.checkNull(mOrderId);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_detail_root;
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initView() {
        loadRootFragment(R.id.ll_container,OrderDetailFragment.newInstance(mOrderId));
    }

    @Override
    public void onBackPressedSupport() {
        finish();
    }
}
