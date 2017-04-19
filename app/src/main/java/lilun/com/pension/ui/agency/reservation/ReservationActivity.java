package lilun.com.pension.ui.agency.reservation;

import java.io.Serializable;

import lilun.com.pension.R;
import lilun.com.pension.base.BaseActivity;
import lilun.com.pension.module.bean.Contact;
import lilun.com.pension.module.utils.Preconditions;

/**
 * Created by Admin on 2017/4/19.
 */
public class ReservationActivity extends BaseActivity {


    private String productCategoryId;
    private String productId;
    private Contact contact;

    @Override
    protected void getTransferData() {
        productCategoryId = getIntent().getStringExtra("productCategoryId");
        productId =getIntent().getStringExtra("productId");
        Serializable contact = getIntent().getSerializableExtra("contact");
        if (contact!=null){
            this.contact = (Contact) contact;
        }
        Preconditions.checkNull(productCategoryId);
        Preconditions.checkNull(productId);
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
        loadRootFragment(R.id.ll_container, ReservationFragment.newInstance(productCategoryId,productId,contact));
    }

    @Override
    public void onBackPressedSupport() {
        finish();
    }
}
