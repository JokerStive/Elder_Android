package lilun.com.pension.ui.agency.merchant;

import android.view.View;
import android.widget.FrameLayout;

import butterknife.Bind;
import butterknife.OnClick;
import lilun.com.pension.R;
import lilun.com.pension.base.BaseActivity;
import lilun.com.pension.module.bean.ProductOrder;
import lilun.com.pension.module.utils.Preconditions;
import lilun.com.pension.widget.NormalTitleBar;

/**
 * 商家订单备注V
 *
 * @author yk
 *         create at 2017/4/7 8:42
 *         email : yk_developer@163.com
 */
public class MemoActivity extends BaseActivity {


    @Bind(R.id.titleBar)
    NormalTitleBar titleBar;

    @Bind(R.id.fl_container)
    FrameLayout flContainer;

    private ProductOrder mOrder;
    private MemoFragment memoFragment;

    @Override
    protected void getTransferData() {
        super.getTransferData();
        mOrder = (ProductOrder) getIntent().getSerializableExtra("order");
        Preconditions.checkNull(mOrder);
    }

    @Override
    protected void initPresenter() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_merchant_memo;
    }

    @Override
    protected void initView() {
        titleBar.setOnBackClickListener(this::finish);

        memoFragment = MemoFragment.newInstance(mOrder);
        replaceLoadRootFragment(R.id.fl_container, memoFragment, false);
    }


    @OnClick({R.id.btn_confirm})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                memoFragment.saveMemo();
                break;
        }
    }


}
