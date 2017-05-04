package lilun.com.pension.ui.agency.reservation;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import lilun.com.pension.R;
import lilun.com.pension.app.App;
import lilun.com.pension.app.Config;
import lilun.com.pension.app.Event;
import lilun.com.pension.app.User;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.adapter.ServiceUserInfoAdapter;
import lilun.com.pension.module.bean.Contact;
import lilun.com.pension.module.utils.Preconditions;
import lilun.com.pension.module.utils.RxUtils;
import lilun.com.pension.net.NetHelper;
import lilun.com.pension.net.RxSubscriber;
import lilun.com.pension.ui.tourism.info.AddTourismInfoFragment;
import lilun.com.pension.widget.NormalItemDecoration;
import lilun.com.pension.widget.NormalTitleBar;

/**
 * 自己预约信息列表V
 *
 * @author yk
 *         create at 2017/3/29 18:47
 *         email : yk_developer@163.com
 */
public class ServiceUserInfoFragment extends BaseFragment {

    @Bind(R.id.rv_info)
    RecyclerView rvInfo;

    @Bind(R.id.titleBar)
    NormalTitleBar titleBar;
    private String productCategoryId;
    private String productId;
    private ServiceUserInfoAdapter adapter;

    @OnClick({R.id.btn_add_info})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_info:
                if (productCategoryId.equals(Config.tourism_product_categoryId)) {
                    start(AddTourismInfoFragment.newInstance(productCategoryId));
                } else {
                    start(AddServiceInfoFragment.newInstance(productCategoryId));
                }
                break;
        }
    }

    @Subscribe
    public void refreshData(Event.RefreshContract event) {
        getContract();
    }

    public static ServiceUserInfoFragment newInstance(String productCategoryId, String productId) {
        ServiceUserInfoFragment fragment = new ServiceUserInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("productCategoryId", productCategoryId);
        bundle.putString("productId", productId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void getTransferData(Bundle arguments) {
        productCategoryId = arguments.getString("productCategoryId");
        productId = arguments.getString("productId");
        Preconditions.checkNull(productCategoryId);
        Preconditions.checkNull(productId);
    }

    @Override
    protected void initPresenter() {
        //TODO 获取个人健康信息列表
        getContract();
    }

    private void getContract() {
        String filter = "{\"where\":{\"creatorId\":\"" + User.getUserId() + "\",\"categoryId\":\"" + productCategoryId + "\"}}";
        NetHelper.getApi().getContacts(filter)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<Contact>>(_mActivity) {
                    @Override
                    public void _next(List<Contact> contacts) {
                        showUserInfo(contacts);
                    }
                });
    }

    private void showUserInfo(List<Contact> contacts) {
        adapter = new ServiceUserInfoAdapter(contacts);
        adapter.setOnRecyclerViewItemClickListener((view, i) -> {
            //TODO 设置默认
            Bundle bundle = new Bundle();
            bundle.putSerializable("contact", adapter.getData().get(i));
            setFragmentResult(0, bundle);
            pop();
        });
        adapter.setOnItemClickListener(new ServiceUserInfoAdapter.OnItemClickListener() {
            @Override
            public void onDelete(Contact contact) {
                Logger.d("删除个人信息");
                deleteContact(contact);
            }

            @Override
            public void onEdit(Contact contact) {
                if (productCategoryId.equals(Config.tourism_product_categoryId)) {
                    start(AddTourismInfoFragment.newInstance(contact.getId(),true));
                } else {
                    start(AddServiceInfoFragment.newInstance(contact.getId(),true));
                }
                Logger.d("编辑个人信息");
            }

            @Override
            public void onSetDefault(String contactId) {
                setDefault(contactId);
            }
        });
        rvInfo.setAdapter(adapter);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_personal_info_list;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        titleBar.setOnBackClickListener(this::pop);

        rvInfo.setLayoutManager(new LinearLayoutManager(App.context, LinearLayoutManager.VERTICAL, false));
        rvInfo.addItemDecoration(new NormalItemDecoration(10));
    }

    /**
     * 设置为默认
     */
    private void setDefault(String contactId) {
        NetHelper.getApi().putDefContact(contactId)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<Object>(_mActivity) {
                    @Override
                    public void _next(Object object) {
                        adapter.setDefault(contactId);
                        Contact data = null;
                        for (Contact contact : adapter.getData()) {
                            if (contact.getId().equals(contactId)) {
                                data = contact;
                            }
                        }
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("contact", data);
                        setFragmentResult(0, bundle);
                        pop();
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
                    }
                });
    }


}
