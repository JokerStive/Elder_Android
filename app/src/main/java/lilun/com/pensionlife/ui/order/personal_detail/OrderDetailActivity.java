package lilun.com.pensionlife.ui.order.personal_detail;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.base.BaseActivity;
import lilun.com.pensionlife.module.utils.Preconditions;

/**
 * @author yk
 *         create at 2017/3/7 10:35
 *         email : yk_developer@163.com
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
        loadRootFragment(R.id.ll_container, OrderDetailFragment.newInstance(mOrderId));
    }

    @Override
    public void onBackPressedSupport() {
        finish();
    }
}
