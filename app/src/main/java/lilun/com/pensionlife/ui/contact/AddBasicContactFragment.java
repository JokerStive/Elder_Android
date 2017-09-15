package lilun.com.pensionlife.ui.contact;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vanzh.library.BaseBean;
import com.vanzh.library.BottomDialog;
import com.vanzh.library.DataInterface;
import com.vanzh.library.OnAddressSelectedListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.Event;
import lilun.com.pensionlife.app.User;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.bean.Area;
import lilun.com.pensionlife.module.bean.Contact;
import lilun.com.pensionlife.module.utils.RegexUtils;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.module.utils.ToastHelper;
import lilun.com.pensionlife.net.NetHelper;
import lilun.com.pensionlife.net.RxSubscriber;
import lilun.com.pensionlife.ui.agency.reservation.ReservationFragment;
import lilun.com.pensionlife.widget.CustomTextView;
import lilun.com.pensionlife.widget.NormalTitleBar;
import lilun.com.pensionlife.widget.SwitchButton;

/**
 * 新增contact基础信息
 *
 * @author yk
 *         create at 2017/8/9 9:34
 *         email : yk_developer@163.com
 */
public class AddBasicContactFragment extends BaseFragment implements DataInterface<BaseBean>, OnAddressSelectedListener {


    @Bind(R.id.titleBar)
    NormalTitleBar titleBar;
    @Bind(R.id.et_contact_name)
    EditText etContactName;
    @Bind(R.id.et_contact_mobile)
    EditText etContactMobile;
    @Bind(R.id.tv_choose_address)
    TextView tvChooseAddress;
    @Bind(R.id.et_contact_address)
    EditText etContactAddress;
    @Bind(R.id.sb_set_default)
    SwitchButton sbSetDefault;
    @Bind(R.id.ll_switch)
    RelativeLayout llSwitch;
    @Bind(R.id.tv_add_contact)
    CustomTextView tvAddContact;
    private String mProductId;
    private Contact mContact;
    private int limitSkip = 20;
    private BottomDialog dialog;
    BaseBean area;
    private int eachLevelCount = 3;
    int curLevel = -1;
    String AddressSepreator = "-";
    private boolean isLoseNecessary;
    private int flag = -1;
    private onAddBasicContactListener mListener;

    public AddBasicContactFragment newInstance(String productId, onAddBasicContactListener listener) {
        AddBasicContactFragment fragment = new AddBasicContactFragment();
        Bundle args = new Bundle();
        args.putString("productId", productId);
        fragment.setArguments(args);
        this.mListener = listener;
        return fragment;
    }
//
//    public static AddBasicContactFragment newInstance(Contact contact) {
//        AddBasicContactFragment fragment = new AddBasicContactFragment();
//        Bundle args = new Bundle();
//        args.putSerializable("contact", contact);
//        fragment.setArguments(args);
//        return fragment;
//    }


    public interface onAddBasicContactListener {
        void onAddBasicContact(Contact contact);
    }

    public void setOnAddBasicContactListener(onAddBasicContactListener listener) {
        this.mListener = listener;
    }

    /**
     * @param contact 需要编辑的信息
     */
    public static AddBasicContactFragment newInstance(Contact contact, int flag) {
        AddBasicContactFragment fragment = new AddBasicContactFragment();
        Bundle args = new Bundle();
        args.putSerializable("contact", contact);
        args.putInt("flag", flag);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void getTransferData(Bundle arguments) {
        super.getTransferData(arguments);
        mProductId = arguments.getString("productId");
        flag = arguments.getInt("flag", -1);
        mContact = (Contact) arguments.getSerializable("contact");
//        Preconditions.checkNull(mProductId);
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_add_basic_contact;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        titleBar.setOnBackClickListener(this::pop);

        if (flag == -1) {
            etContactMobile.setText(User.getMobile());
            etContactName.setText(User.getName());
            etContactAddress.setText(User.getAddress());

            String belongsOrganizationId = User.getBelongsOrganizationId();
            tvChooseAddress.setText(getAddressResultFromId(belongsOrganizationId));
        }

        //flag=-1(新增信息),flag=0(编辑信息),flag=1(补全信息)
        if (flag == 1) {
            ToastHelper.get().showWareShort("请先补全信息");
        }

        llSwitch.setVisibility(mContact == null ? View.VISIBLE : View.GONE);
    }


    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        //如果mContact初始化时不为空，就是编辑信息
        if (mContact != null) {
            mProductId = mContact.getProductId();

            etContactName.setText(mContact.getName());
            etContactMobile.setText(mContact.getMobile());
            sbSetDefault.setChecked(mContact.getIndex() == 1);


            String address = mContact.getAddress();
            if (address != null) {
                String area = address.substring(0, address.lastIndexOf(AddressSepreator));
                String local = address.substring(address.lastIndexOf(AddressSepreator) + 1);
                tvChooseAddress.setText(area);
                etContactAddress.setText(local);
            }
        }

    }

    @OnClick({R.id.tv_choose_address, R.id.tv_add_contact})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_choose_address:
                chooseArea();
                break;

            case R.id.tv_add_contact:
                addContact();
                break;
        }
    }

    private void chooseArea() {
        dialog = new BottomDialog(this, -1, limitSkip);
        dialog.setOnAddressSelectedListener(this);
        dialog.show();

    }


    /**
     * 新增contact信息
     */
    private void addContact() {
        String contactName = etContactName.getText().toString();
        String contactMobile = etContactMobile.getText().toString();
        String contactArea = tvChooseAddress.getText().toString();
        String contactAddress = etContactAddress.getText().toString();

        if (TextUtils.isEmpty(contactName)) {
            ToastHelper.get().showWareShort("请输入姓名");
            return;
        }

        if (TextUtils.isEmpty(contactMobile)) {
            ToastHelper.get().showWareShort("请输入联系电话");
            if (!RegexUtils.checkMobile(contactMobile)) {
                ToastHelper.get().showWareShort("请输入正确的电话号码");
            }
            return;
        }

        if (TextUtils.isEmpty(contactArea)) {
            ToastHelper.get().showWareShort("请选择区域");
            return;
        }

        if (TextUtils.isEmpty(contactAddress)) {
            ToastHelper.get().showWareShort("请输入详细地址");
            return;
        }

        Contact contact = new Contact();
        contact.setName(contactName);
        contact.setMobile(contactMobile);
        contact.setAddress(contactArea + AddressSepreator + contactAddress);
        contact.setAccountId(User.getUserId());
        contact.setUserId(User.getUserId());

        if (mContact != null) {
            NetHelper.getApi().putContact(mContact.getId(), contact)
                    .compose(RxUtils.handleResult())
                    .compose(RxUtils.applySchedule())
                    .subscribe(new RxSubscriber<Contact>(_mActivity) {
                        @Override
                        public void _next(Contact contact) {
                            mContact = contact;
                            isSetDefault();
                        }
                    });
        } else {
            NetHelper.getApi().newContact(contact)
                    .compose(RxUtils.handleResult())
                    .compose(RxUtils.applySchedule())
                    .subscribe(new RxSubscriber<Contact>(_mActivity) {
                        @Override
                        public void _next(Contact contact) {
                            mContact = contact;
                            isSetDefault();
                        }
                    });
        }
    }


    private void isSetDefault() {
        if (sbSetDefault.isChecked()) {
            putDefault();
        } else {
            next();
        }
    }

    private void putDefault() {
        NetHelper.getApi().putDefContact(mContact.getId())
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<Object>(_mActivity) {
                    @Override
                    public void _next(Object object) {
                        next();
                    }
                });
    }


    private void next() {
        //只有当mProductId不为空的时候，表示下一个动作是去预约界面
        if (!TextUtils.isEmpty(mProductId) && mListener != null) {
            pop();
            mListener.onAddBasicContact(mContact);
        } else {
            EventBus.getDefault().post(new Event.RefreshContract());
            pop();
        }
    }


    private void statReservation(Contact contact) {
        startWithPop(ReservationFragment.newInstance(mProductId, contact));
    }

    @Override
    public void requestData(BaseBean baseBean, Response<BaseBean> response, int level, int recyclerIndex, int startIndex, int reqCount) {
        if (baseBean == null) {
            //baseBean为空，表示首次获取数据
            if (area == null) {
                //cuLevel=0，表示第一层的第一层级
                getChildLocation("", response, level, recyclerIndex, startIndex, reqCount);
            } else {
                getChildLocation(area.getId().replace(getString(R.string.common_address), ""), response, level, recyclerIndex, startIndex, reqCount);
            }
        } else {
            if (level == eachLevelCount) {
                response.send(level, null, false);
            } else {
                getChildLocation(baseBean.getId().replace(getString(R.string.common_address), ""), response, level, recyclerIndex, startIndex, reqCount);
            }
        }
    }


    public void getChildLocation(String locationName, Response<BaseBean> response, int level, int recyclerIndex, int skip, int limitSkip) {
        NetHelper.getApi()
                .getChildLocation(locationName, skip, limitSkip)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<Area>>() {
                    @Override
                    public void _next(List<Area> areas) {
                        successOfChildLocation(areas, response, level, recyclerIndex);
                    }
                });
    }


    public void successOfChildLocation(List<Area> areas, Response<BaseBean> response, int level, int recyclerIndex) {
        curLevel++;
        ArrayList<BaseBean> data = new ArrayList<>();
        for (int i = 0; i < areas.size(); i++) {
            data.add(new BaseBean(areas.get(i).getId(), areas.get(i).getName()));
        }
        if (level == eachLevelCount || areas.size() == 0) {
            response.send(level, null, false);
            return;
        }

        response.send(level, data, data != null && data.size() == limitSkip);
    }

    @Override
    public void onAddressSelected(int index, BaseBean... baseBeen) {
        if (baseBeen.length == 0) {
            ToastHelper.get().showWareShort("size  = 0");
            return;
        }

        //当第一层完成的时候
        area = baseBeen[baseBeen.length - 1];
        if (curLevel <= eachLevelCount) {
            dialog.dismiss();
            tvChooseAddress.performClick();
        } else {
            tvChooseAddress.setText(getAddressResultFromId(area.getId()));
            resetInstance();
            dialog.dismiss();
        }
    }

    private String getAddressResultFromId(String id) {
        StringBuilder resultBuffer = new StringBuilder();
        String[] split = id.split("/");
        //从省开始
        for (int i = 3; i < split.length; i++) {
            if (i < split.length - 1) {
                resultBuffer.append(split[i]).append(AddressSepreator);
            } else {
                resultBuffer.append(split[i]);
            }
        }

        return resultBuffer.toString();
    }

    /**
     * 重置变量
     */
    private void resetInstance() {
        curLevel = -1;
        area = null;

    }

    @Override
    public void onConfirm(BaseBean baseBean) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
