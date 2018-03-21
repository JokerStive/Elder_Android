package lilun.com.pensionlife.ui.education.reservation;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.base.BaseActivity;
import lilun.com.pensionlife.module.bean.Contact;
import lilun.com.pensionlife.module.utils.Preconditions;

/**
 * Created by Admin on 2017/4/19.
 */
public class ReservationCourseActivity extends BaseActivity {


    private String productId;
    private Contact contact;

    @Override
    protected void getTransferData() {
        productId = getIntent().getStringExtra("objectId");
        contact = (Contact) getIntent().getSerializableExtra("contact");
        Preconditions.checkNull(contact);
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
        loadRootFragment(R.id.ll_container, ReservationCourseFragment.newInstance(productId, contact));
    }

    @Override
    public void onBackPressedSupport() {
        finish();
    }
}
