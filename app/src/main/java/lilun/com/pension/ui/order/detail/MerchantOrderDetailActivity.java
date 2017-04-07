package lilun.com.pension.ui.order.detail;

import lilun.com.pension.R;
import lilun.com.pension.base.BaseActivity;

/**
 * 商家订单详情V
 *
 * @author yk
 *         create at 2017/4/6 15:25
 *         email : yk_developer@163.com
 */
public class MerchantOrderDetailActivity extends BaseActivity<MerchantOrderDetailContract.Presenter> implements MerchantOrderDetailContract.View {
    @Override
    protected void initPresenter() {
        mPresenter = new MerchantOrderDetailPresenter();
        mPresenter.bindView(this);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_merchant_order_detail;
    }


}
