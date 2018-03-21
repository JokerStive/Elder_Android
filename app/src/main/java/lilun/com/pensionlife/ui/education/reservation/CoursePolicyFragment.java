package lilun.com.pensionlife.ui.education.reservation;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.User;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.bean.Contact;
import lilun.com.pensionlife.module.utils.Preconditions;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.module.utils.ToastHelper;
import lilun.com.pensionlife.net.NetHelper;
import lilun.com.pensionlife.net.RxSubscriber;
import lilun.com.pensionlife.ui.contact.AddBasicContactFragment;
import lilun.com.pensionlife.ui.contact.ContactListFragment;
import lilun.com.pensionlife.widget.NormalTitleBar;

/**
 * 协议界面
 *
 * @author yk
 *         create at 2017/9/14 11:29
 *         email : yk_developer@163.com
 */
public class CoursePolicyFragment extends BaseFragment {


    @Bind(R.id.wb_policy_content)
    WebView wbPolicyContent;

    @Bind(R.id.titleBar)
    NormalTitleBar titleBar;

    @Bind(R.id.tv_reservation)
    TextView tvReservation;

    @Bind(R.id.tv_agree_policy)
    TextView tvAgreePolicy;
    private String mProductId;
    private String mProductPolicy;

    @Override
    protected void initPresenter() {

    }

    public static CoursePolicyFragment newInstance(String productId, String policyContent) {
        CoursePolicyFragment fragment = new CoursePolicyFragment();
        Bundle args = new Bundle();
        args.putSerializable("objectId", productId);
        args.putSerializable("policyContent", policyContent);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void getTransferData(Bundle arguments) {
        super.getTransferData(arguments);
        mProductId = arguments.getString("objectId");
        mProductPolicy = arguments.getString("policyContent");
        Preconditions.checkNull(mProductId);
        Preconditions.checkNull(mProductPolicy);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_policy;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        titleBar.setOnBackClickListener(this::pop);

        String agreePolicy = "同意<font color='#108ee9'>《在线报名须知》</font>";
        tvAgreePolicy.setText(Html.fromHtml(agreePolicy));
        tvAgreePolicy.setOnClickListener(v -> tvAgreePolicy.setSelected(!tvAgreePolicy.isSelected()));

        wbPolicyContent.loadDataWithBaseURL(null, mProductPolicy, "text/html", "utf-8", null);

        tvReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agreePolicy();
            }
        });
    }

    private void agreePolicy() {
        boolean isAgree = tvAgreePolicy.isSelected();
        if (!isAgree) {
            ToastHelper.get().showWareShort("请勾选同意协议");
        } else {
            User.addCertificateLicense(mProductId);
            next();
        }
    }

    private void next() {
        String filter = "{\"where\":{\"accountId\":\"" + User.getUserId() + "\"}}";
        NetHelper.getApi().getContacts(filter)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<Contact>>(getActivity()) {
                    @Override
                    public void _next(List<Contact> contacts) {
                        checkContact(contacts);
                    }
                });
    }


    /**
     * 检查预约资料
     */
    private void checkContact(List<Contact> contacts) {
        if (contacts.size() > 0) {
            //显示 预约者信息列表
            Contact defContact = getDefaultContact(contacts);
            if (defContact == null) {
                //没有默认信息，就进去信息列表
                start(ContactListFragment.newInstance(mProductId, 1));
            } else if (TextUtils.isEmpty(defContact.getMobile()) || TextUtils.isEmpty(defContact.getName()) || TextUtils.isEmpty(defContact.getAddress())) {
                defContact.setProductId(mProductId);
                //必要信息不完善
                start(AddBasicContactFragment.newInstance(defContact, 1));
            } else {
                //有默认信息，并且必要信息完整，直接预约界面
                start(ReservationCourseFragment.newInstance(mProductId, defContact));
            }
        } else {
            //新增基础信息界面
            AddBasicContactFragment addBasicContactFragment = new AddBasicContactFragment();
            Bundle args = new Bundle();
            args.putString("objectId", mProductId);
            addBasicContactFragment.setArguments(args);
            addBasicContactFragment.setOnAddBasicContactListener(contact -> start(ReservationCourseFragment.newInstance(mProductId, contact)));
            start(addBasicContactFragment);
        }
    }

    /**
     * 获取默认信息
     */
    private Contact getDefaultContact(List<Contact> contacts) {
        for (Contact contact : contacts) {
            int index = contact.getIndex();
            if (index == 1) {
                return contact;
            }
        }
        return null;
    }

}
