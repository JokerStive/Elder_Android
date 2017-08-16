package lilun.com.pensionlife.ui.contact;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.vanzh.library.BaseBean;
import com.vanzh.library.BottomDialog;
import com.vanzh.library.DataInterface;
import com.vanzh.library.OnAddressSelectedListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
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
import lilun.com.pensionlife.widget.NormalTitleBar;
import lilun.com.pensionlife.widget.SwitchButton;

/**
 * 新增contact基础信息
 *
 * @author yk
 *         create at 2017/8/9 9:34
 *         email : yk_developer@163.com
 */
public class AddBasicContactFragment extends BaseFragment implements DataInterface<BaseBean> {


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
    private String mProductId;
    private Contact mContact;
    private int limitSkip = 20;
    private int[] skipArray;
    private BottomDialog dialog;
    BaseBean area, distrect;
    private int RECYCLERLEVEL = 5;
    int curLevel = -1;

    public static AddBasicContactFragment newInstance(String productId) {
        AddBasicContactFragment fragment = new AddBasicContactFragment();
        Bundle args = new Bundle();
        args.putString("productId", productId);
        fragment.setArguments(args);
        return fragment;
    }

    public static AddBasicContactFragment newInstance(Contact contact) {
        AddBasicContactFragment fragment = new AddBasicContactFragment();
        Bundle args = new Bundle();
        args.putSerializable("contact", contact);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void getTransferData(Bundle arguments) {
        super.getTransferData(arguments);
        mProductId = arguments.getString("productId");
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

        etContactMobile.setText(User.getMobile());


    }


    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        //如果mContact初始化时不为空，就是编辑信息
        if (mContact != null) {
            etContactName.setText(mContact.getName());
            etContactMobile.setText(mContact.getMobile());
            etContactAddress.setText(mContact.getAddress());
            sbSetDefault.setChecked(mContact.getIndex() == 1);
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
        skipArray = new int[RECYCLERLEVEL + 1];
        dialog = new BottomDialog(this, -1, limitSkip);
//        dialog.setButtonVisiableLevels(new int[]{6}, RECYCLERLEVEL);
        dialog.setOnAddressSelectedListener(new OnAddressSelectedListener() {
            @Override
            public void onAddressSelected(int recyclerIndex, BaseBean... baseBeen) {
                if (baseBeen.length == 0) {
                    ToastHelper.get(getContext()).showWareShort("该地区未开通服务，请重新选择");
                    return;
                }
                if (recyclerIndex != RECYCLERLEVEL) {
                    area = baseBeen[baseBeen.length - 1];
                    tvChooseAddress.setText(area.getName());
                    distrect = null;
                    curLevel = baseBeen.length - 1;
                } else {
                    distrect = baseBeen[baseBeen.length - 1];
                    tvChooseAddress.setText(distrect.getName());
                    curLevel = baseBeen.length - 1 + RECYCLERLEVEL;
                }
                dialog.dismiss();
            }

            @Override
            public void onConfirm(BaseBean baseBean) {
                distrect = baseBean;
                tvChooseAddress.setText(distrect.getName());
                dialog.dismiss();
            }
        });
        dialog.show();

    }


    /**
     * 新增contact信息
     */
    private void addContact() {
        String contactName = etContactName.getText().toString();
        String contactMobile = etContactMobile.getText().toString();
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


        if (TextUtils.isEmpty(contactAddress)) {
            ToastHelper.get().showWareShort("请输入详细地址");
            return;
        }

        Contact contact = new Contact();
        contact.setName(contactName);
        contact.setMobile(contactMobile);
        contact.setAddress(contactAddress);
        contact.setAccountId(User.getUserId());
        contact.setUserId(User.getUserId());

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
        if (!TextUtils.isEmpty(mProductId)) {
            statReservation(mContact);
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
            if (level == 0) {
                getChildLocation("", response, level, recyclerIndex, startIndex, reqCount);
            } else {
                getChildLocation(area.getId().replace(getString(R.string.common_address), ""), response, level, recyclerIndex, startIndex, reqCount);
            }
        } else {
            getChildLocation(baseBean.getId().replace(getString(R.string.common_address), ""), response, level, recyclerIndex, startIndex, reqCount);
//            if (level < RECYCLERLEVEL) {
//            } else {
//                response.send(level, null, false);
//            }
//            if (level< RECYCLERLEVEL){
//            }else {
//
//            }
        }
    }


    public void getChildLocation(String locationName, DataInterface.Response<BaseBean> response, int level, int recyclerIndex, int skip, int limitSkip) {
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
        skipArray[level] += areas.size();
        ArrayList<BaseBean> data = new ArrayList<>();
        for (int i = 0; i < areas.size(); i++) {
            data.add(new BaseBean(areas.get(i).getId(), areas.get(i).getName()));
        }
//        if (level>0){
//            level = level-1;
//        }
        if (level == recyclerIndex || level == RECYCLERLEVEL) {
            response.send(level, null, false);
            return;
        }

        response.send(level, data, data != null && data.size() == limitSkip);
    }
}
