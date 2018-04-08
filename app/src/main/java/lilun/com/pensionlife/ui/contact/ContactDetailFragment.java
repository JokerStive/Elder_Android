package lilun.com.pensionlife.ui.contact;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONObject;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.bean.Contact;
import lilun.com.pensionlife.module.bean.MetaServiceContact;
import lilun.com.pensionlife.module.utils.dynamic.ContactView;
import lilun.com.pensionlife.widget.NormalTitleBar;

/**
 * 联系人详情
 */
public class ContactDetailFragment extends BaseFragment {


    private Contact mContact;
    private ContactView contactView;
    private MetaServiceContact template;

    public static ContactDetailFragment newInstance(MetaServiceContact template, Contact contact) {
        ContactDetailFragment fragment = new ContactDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("template", template);
        bundle.putSerializable("contact", contact);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected void getTransferData(Bundle arguments) {
        super.getTransferData(arguments);
        template = (MetaServiceContact) arguments.getSerializable("template");
        mContact = (Contact) arguments.getSerializable("contact");
        if (mContact == null) {
            throw new IllegalStateException("contact can't be null");
        }
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_contact_detail;
    }

    @Override
    protected void initView(LayoutInflater inflater) {

        LinearLayout container = (LinearLayout) mRootView.findViewById(R.id.container);
        NormalTitleBar titleBar = (NormalTitleBar) mRootView.findViewById(R.id.titleBar);
        titleBar.setOnBackClickListener(this::pop);

        contactView = new ContactView(_mActivity);
        contactView.setOnlyShow(true);
        container.addView(contactView.getView());

        getTemplate();
    }

    private void getTemplate() {
        JSONObject settings = null;
        if (template != null) {
            settings = template.getSettings();
        }
        contactView.reDraw(mContact, settings);

    }
}
