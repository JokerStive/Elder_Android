package lilun.com.pensionlife.ui.contact;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.app.Event;
import lilun.com.pensionlife.app.User;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.adapter.ContactListAdapter;
import lilun.com.pensionlife.module.bean.Contact;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.module.utils.UIUtils;
import lilun.com.pensionlife.net.NetHelper;
import lilun.com.pensionlife.net.RxSubscriber;
import lilun.com.pensionlife.ui.agency.reservation.ReservationFragment;
import lilun.com.pensionlife.ui.education.reservation.ReservationCourseFragment;
import lilun.com.pensionlife.widget.DividerDecoration;
import lilun.com.pensionlife.widget.NormalDialog;
import lilun.com.pensionlife.widget.NormalTitleBar;


public class ContactListFragment extends BaseFragment {

    @Bind(R.id.rv_contact)
    RecyclerView rvInfo;

    @Bind(R.id.titleBar)
    NormalTitleBar titleBar;

    private ContactListAdapter adapter;
    private String mProductId;
    private int mFlag = -1;
//    private OrganizationProduct mProduct;

    @OnClick({R.id.tv_add_contact})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add_contact:
                start(new AddBasicContactFragment());
                break;
        }
    }

    @Subscribe
    public void refreshData(Event.RefreshContract event) {
        getContact();
    }

    public static ContactListFragment newInstance(String productId, int flag) {
        ContactListFragment fragment = new ContactListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("productId", productId);
        bundle.putInt("flag", flag);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static ContactListFragment newInstance() {
        return new ContactListFragment();
    }

    @Override
    protected void getTransferData(Bundle arguments) {
        mProductId = arguments.getString("productId");
        mFlag = arguments.getInt("flag");
    }

    @Override
    protected void initPresenter() {
        getContact();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_contact_list;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        titleBar.setOnBackClickListener(this::goBack);

        rvInfo.setLayoutManager(new LinearLayoutManager(App.context, LinearLayoutManager.VERTICAL, false));
        rvInfo.addItemDecoration(new DividerDecoration(App.context, LinearLayoutManager.VERTICAL, UIUtils.dp2px(App.context, 10), App.context.getResources().getColor(R.color.gray)));
    }

    private void goBack() {
        pop();
        if (getActivity() instanceof ContactListActivity) {
            getActivity().finish();
        }
    }

    private void getContact() {
        String filter = "{\"where\":{\"accountId\":\"" + User.getUserId() + "\"}}";
        NetHelper.getApi().getContacts(filter)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<Contact>>(getActivity()) {
                    @Override
                    public void _next(List<Contact> contacts) {
                        showContacts(contacts);
                    }
                });
    }

    private void showContacts(List<Contact> contacts) {
        adapter = new ContactListAdapter(contacts);
        adapter.setOnItemClickListener((ba, view, i) -> {
            Contact contact = adapter.getData().get(i);

            //携带产品id  预约流程
            if (!TextUtils.isEmpty(mProductId)) {
                if (TextUtils.isEmpty(contact.getMobile()) || TextUtils.isEmpty(contact.getName()) || TextUtils.isEmpty(contact.getAddress())) {
                    contact.setProductId(mProductId);
                    //必要信息不完善
                    start(AddBasicContactFragment.newInstance(contact, 1));
                } else {
                    statReservation(contact);
                }
            }

            // 纯粹的个人信息管理
            else {
                start(AddBasicContactFragment.newInstance(contact, 0));
            }
        });
        adapter.setOnItemClickListener(new ContactListAdapter.OnItemClickListener() {
            @Override
            public void onDelete(Contact contact) {
                new NormalDialog().createNormal(_mActivity, "是否删除", () -> {
                    deleteContact(contact);
                });

            }

            @Override
            public void onEdit(Contact contact) {
                start(AddBasicContactFragment.newInstance(contact, 0));
            }

            @Override
            public void onSetDefault(String contactId) {
                setDefault(contactId);
            }
        });
        rvInfo.setAdapter(adapter);
    }


    private void statReservation(Contact contact) {
        if (mFlag == 0) {
            startWithPop(ReservationFragment.newInstance(mProductId, contact));
        } else if (mFlag == 1) {
            startWithPop(ReservationCourseFragment.newInstance(mProductId, contact));
        }
    }

    /**
     * 设置默认
     */
    private void setDefault(String contactId) {
        NetHelper.getApi().putDefContact(contactId)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<Object>(_mActivity) {
                    @Override
                    public void _next(Object object) {
                        adapter.setDefault(contactId);
                    }
                });

    }


    /**
     * 删除contact
     */
    private void deleteContact(Contact contact) {
        NetHelper.getApi().deleteContact(contact.getId())
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<Object>(_mActivity) {
                    @Override
                    public void _next(Object object) {
                        adapter.remove(contact);
                        if (adapter.getItemCount() == 0) {
                            pop();
                        }
                    }
                });
    }


}
