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
import lilun.com.pensionlife.widget.DividerDecoration;
import lilun.com.pensionlife.widget.NormalTitleBar;


public class ContactListFragment extends BaseFragment {

    @Bind(R.id.rv_contact)
    RecyclerView rvInfo;

    @Bind(R.id.titleBar)
    NormalTitleBar titleBar;

    private ContactListAdapter adapter;
    private String mProductId;
//    private OrganizationProduct mProduct;

    @OnClick({R.id.tv_add_contact})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add_contact:
                start(AddBasicContactFragment.newInstance(""));
                break;
        }
    }

    @Subscribe
    public void refreshData(Event.RefreshContract event) {
        getContact();
    }

    public static ContactListFragment newInstance(String productId) {
        ContactListFragment fragment = new ContactListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("productId", productId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void getTransferData(Bundle arguments) {
        mProductId = arguments.getString("productId");
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
        titleBar.setOnBackClickListener(this::pop);

        rvInfo.setLayoutManager(new LinearLayoutManager(App.context, LinearLayoutManager.VERTICAL, false));
        rvInfo.addItemDecoration(new DividerDecoration(App.context, LinearLayoutManager.VERTICAL, UIUtils.dp2px(App.context, 10), App.context.getResources().getColor(R.color.gray)));
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
        adapter.setOnRecyclerViewItemClickListener((view, i) -> {
            if (!TextUtils.isEmpty(mProductId)) {
                Contact contact = adapter.getData().get(i);
                statReservation(contact);
//                pop();
            }
        });
        adapter.setOnItemClickListener(new ContactListAdapter.OnItemClickListener() {
            @Override
            public void onDelete(Contact contact) {
                deleteContact(contact);
            }

            @Override
            public void onEdit(Contact contact) {
                start(AddBasicContactFragment.newInstance(contact));
//                if (productCategoryId.equals(Config.tourism_product_categoryId)) {
//                    start(AddTourismInfoFragment.newInstance(contact.getId(), true));
//                } else {
//                    start(AddServiceInfoFragment.newInstance(contact.getId(), true));
//                }
//                Logger.d("编辑个人信息");
            }

            @Override
            public void onSetDefault(String contactId) {
                setDefault(contactId);
            }
        });
        rvInfo.setAdapter(adapter);
    }


    private void statReservation(Contact contact) {
//        Intent intent = new Intent(_mActivity, ReservationActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("contact", contact);
//        bundle.putString("productId", mProductId);
//        intent.putExtras(bundle);
//        startActivityForResult(intent, ReservationFragment.requestCode);

        startWithPop(ReservationFragment.newInstance(mProductId, contact));
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
//                        Contact data = null;
//                        for (Contact contact : adapter.getData()) {
//                            if (contact.getId().equals(contactId)) {
//                                data = contact;
//                            }
//                        }
//                        Bundle bundle = new Bundle();
//                        bundle.putSerializable("contact", data);
//                        setFragmentResult(0, bundle);
//                        pop();
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
