package lilun.com.pension.ui.order.detail;

import lilun.com.pension.R;
import lilun.com.pension.base.BaseActivity;
import lilun.com.pension.module.bean.ProductOrder;
import lilun.com.pension.module.utils.Preconditions;

/**
 * Created by Admin on 2017/4/10.
 */
public class MerchantOrderDetailActivity extends BaseActivity {


    private ProductOrder mOrder;

    @Override
    protected void getTransferData() {
        mOrder = (ProductOrder) getIntent().getSerializableExtra("order");
        Preconditions.checkNull(mOrder);
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
        loadRootFragment(R.id.ll_container, MerchantOrderDetailFragment.newInstance(mOrder));
    }

    @Override
    public void onBackPressedSupport() {
        finish();
    }
}
