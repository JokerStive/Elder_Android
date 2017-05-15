package lilun.com.pension.ui.agency.reservation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DateTimePicker;
import cn.qqtheme.framework.picker.OptionPicker;
import cn.qqtheme.framework.picker.WheelPicker;
import lilun.com.pension.R;
import lilun.com.pension.app.App;
import lilun.com.pension.app.Event;
import lilun.com.pension.app.User;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.bean.Contact;
import lilun.com.pension.module.bean.Setting;
import lilun.com.pension.module.utils.ACache;
import lilun.com.pension.module.utils.RxUtils;
import lilun.com.pension.module.utils.ToastHelper;
import lilun.com.pension.net.NetHelper;
import lilun.com.pension.net.RxSubscriber;
import lilun.com.pension.widget.NormalTitleBar;

/**
 * 新增预约信息列表V
 *
 * @author yk
 *         create at 2017/3/29 18:47
 *         email : yk_developer@163.com
 */
public class AddServiceInfoFragment extends BaseFragment {

    @Bind(R.id.et_occupant_name)
    EditText etOccupantName;

    @Bind(R.id.tv_sex)
    TextView tvSex;

    @Bind(R.id.tv_birthday)
    TextView tvBirthday;

    @Bind(R.id.tv_health_status)
    TextView tvHealthStatus;

    @Bind(R.id.tv_relation)
    TextView tvRelation;


    @Bind(R.id.et_health_desc)
    EditText etHealthDesc;

    @Bind(R.id.et_memo)
    EditText etMemo;


    @Bind(R.id.et_reservation_phone)
    EditText etReservationPhone;

    @Bind(R.id.titleBar)
    NormalTitleBar titleBar;

    @Bind(R.id.btn_confirm)
    Button btnConfirm;


    private int size = 17;
    private int selectColor = App.context.getResources().getColor(R.color.red);
    private String[] optionSex = App.context.getResources().getStringArray(R.array.personal_info_sex);
    private String[] optionHealthStatus = App.context.getResources().getStringArray(R.array.personal_info_health_status);
    private String[] optionRelation = App.context.getResources().getStringArray(R.array.personal_info_relation);
    private String productCategoryId;
    //    private String productId;
    private Contact mContact;
    private List<Setting> expandKeys;
    private boolean canEdit;
    private String contactId;
    private String productId;

    public static AddServiceInfoFragment newInstance(String productCategoryId) {
        AddServiceInfoFragment fragment = new AddServiceInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("productCategoryId", productCategoryId);
        fragment.setArguments(bundle);
        return fragment;
    }


    public static AddServiceInfoFragment newInstance(String contactId, boolean canEdit) {
        AddServiceInfoFragment fragment = new AddServiceInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("canEdit", canEdit);
        bundle.putString("contactId", contactId);
        fragment.setArguments(bundle);
        return fragment;
    }

//    public static AddServiceInfoFragment newInstance(String productCategoryId,String contactId, Boolean canEdit) {
//        AddServiceInfoFragment fragment = new AddServiceInfoFragment();
//        Bundle bundle = new Bundle();
//        bundle.putBoolean("canEdit", canEdit);
//        bundle.putString("productCategoryId",productCategoryId);
//        bundle.putString("contactId", contactId);
//        fragment.setArguments(bundle);
//        return fragment;
//    }


    @Override
    protected void getTransferData(Bundle arguments) {
        productCategoryId = arguments.getString("productCategoryId");
        contactId = arguments.getString("contactId");
        canEdit = arguments.getBoolean("canEdit", true);
//        mContact = (Contact) arguments.getSerializable("mContact");
        if (!TextUtils.isEmpty(productCategoryId)) {
            expandKeys = (List<Setting>) ACache.get().getAsObject(productCategoryId);
        }
//        Preconditions.checkNull(productCategoryId);
//        Preconditions.checkNull(productId);
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_add_reservation_info;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        titleBar.setOnBackClickListener(this::pop);

        etOccupantName.setEnabled(canEdit);
        etHealthDesc.setEnabled(canEdit);
        etReservationPhone.setEnabled(canEdit);
        etMemo.setEnabled(canEdit);
        btnConfirm.setVisibility(canEdit ? View.VISIBLE : View.GONE);

        setInitData();
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        if (!TextUtils.isEmpty(contactId)) {
            subscription.add(NetHelper.getApi().getContact(contactId)
                    .compose(RxUtils.handleResult())
                    .compose(RxUtils.applySchedule())
                    .subscribe(new RxSubscriber<Contact>(_mActivity) {
                        @Override
                        public void _next(Contact contact) {
                            expandKeys = (List<Setting>) ACache.get().getAsObject(contact.getCategoryId());
                            mContact = contact;
                            setInitData();
                        }
                    }));
        }
    }


    public void setProductd(String productId) {
        this.productId = productId;
    }

    private void setInitData() {
        if (mContact != null) {
            etOccupantName.setText(mContact.getName());

            tvSex.setText(optionSex[mContact.getGender()]);

            tvRelation.setText(mContact.getRelation());

//            etReservationName.setText(mContact.getCreatorName());


            etReservationPhone.setText(mContact.getMobile());

            if (expandKeys != null && expandKeys.size() >= 3) {
                Map<String, String> extend = mContact.getExtend();
                if (extend != null) {
                    tvBirthday.setText(extend.get(expandKeys.get(0).getKey()));
                    tvHealthStatus.setText(extend.get(expandKeys.get(1).getKey()));
                    etHealthDesc.setText(extend.get(expandKeys.get(2).getKey()));
                }
            }

        }
    }


    @OnClick({R.id.tv_sex, R.id.tv_health_status, R.id.tv_relation, R.id.tv_birthday, R.id.btn_confirm})
    public void onClick(View view) {
        if (!canEdit) {
            return;
        }

        switch (view.getId()) {

            case R.id.tv_sex:
                optionPicker(optionSex, tvSex.getText().toString(), tvSex);
                break;

            case R.id.tv_health_status:
                optionPicker(optionHealthStatus, tvHealthStatus.getText().toString(), tvHealthStatus);
                break;


            case R.id.tv_relation:
                optionPicker(optionRelation, tvRelation.getText().toString(), tvRelation);
                break;

            case R.id.tv_birthday:
                chooseBirthday();
                break;

            case R.id.btn_confirm:
                savePersonalInfo();
                break;
        }
    }


    private void savePersonalInfo() {
        String name = etOccupantName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            ToastHelper.get().showWareShort("请输入入住者姓名");
            return;
        }

        CharSequence sex = tvSex.getText();
        if (TextUtils.isEmpty(sex)) {
            ToastHelper.get().showWareShort("请选择性别");
            return;
        }

        CharSequence birthday = tvBirthday.getText();
//        if (TextUtils.isEmpty(birthday)) {
//            ToastHelper.get().showWareShort("请选择生日");
//            return;
//        }

        CharSequence healthStatus = tvHealthStatus.getText();
        if (TextUtils.isEmpty(healthStatus)) {
            ToastHelper.get().showWareShort("请选择健康状况");
            return;
        }

        String healthDesc = etHealthDesc.getText().toString();

        CharSequence relation = tvRelation.getText();
        if (TextUtils.isEmpty(relation)) {
            ToastHelper.get().showWareShort("请选择您和入住者的关系");
            return;
        }

//        String reservationName = etReservationName.getText().toString();

        String reservationPhone = etReservationPhone.getText().toString();
        if (TextUtils.isEmpty(reservationPhone)) {
            ToastHelper.get().showWareShort("请输入电话");
            return;
        }

        Contact contact = new Contact();
        contact.setName(name);
        contact.setGender(sex.equals(optionSex[0]) ? 1 : 0);
        contact.setRelation(relation.toString());
        contact.setCategoryId(productCategoryId);
//        contact.setCreatorName(reservationName);
        contact.setMobile(reservationPhone);
        if (expandKeys != null) {
            Map<String, String> expand = new HashMap<>();
            Setting setting = expandKeys.get(0);
            String key = setting.getKey();
            expand.put(key, birthday.toString());
            expand.put(expandKeys.get(1).getKey(), healthStatus.toString());
            expand.put(expandKeys.get(2).getKey(), healthDesc);
            contact.setExtend(expand);

            if (mContact != null) {
                putContact(contact);
            } else {
                postContact(contact);
            }
        } else {
            Logger.d("配置项为空");
        }


    }


    /**
     * 更新个人资料
     */
    private void putContact(Contact contact) {
        NetHelper.getApi().putContact(mContact.getId(), contact)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<Object>(_mActivity) {
                    @Override
                    public void _next(Object o) {
                        EventBus.getDefault().post(new Event.RefreshContract());
                        pop();
                    }
                });
    }

    /**
     * 添加个人信息
     */
    private void postContact(Contact contact) {
        NetHelper.getApi().newContact(contact)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<Contact>(_mActivity) {
                    @Override
                    public void _next(Contact contact) {
                        if (!TextUtils.isEmpty(productId)) {
                            statReservation(contact);
//                            start(ReservationFragment.newInstance(productCategoryId, productId, contact));
                        } else {
                            EventBus.getDefault().post(new Event.RefreshContract());
                        }
                        pop();
//                        ToastHelper.get().showWareShort("新增个人资料成功");
                    }
                });
    }


    private void statReservation(Contact contact) {
        Intent intent = new Intent(_mActivity, ReservationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("contact", contact);
        bundle.putString("productCategoryId", productCategoryId);
        bundle.putString("productId", productId);
        intent.putExtras(bundle);
        startActivityForResult(intent, ReservationFragment.requestCode);
    }

    /**
     * 生日选择器
     */
    private void chooseBirthday() {
        DateTimePicker picker = new DateTimePicker(_mActivity, DateTimePicker.YEAR_MONTH_DAY, DateTimePicker.NONE);
        picker.setDateRangeStart(1900, 1, 1);
        setPickerConfig(picker);
        picker.setOnDateTimePickListener((DateTimePicker.OnYearMonthDayTimePickListener) (year, month, day, hour, minute) -> {
            String time = year + "-" + month + "-" + day;
            tvBirthday.setText(time);
        });
        picker.show();
    }

    /**
     * 显示一个选择器
     */
    private void optionPicker(String[] options, String selectItem, TextView view) {
        OptionPicker picker = new OptionPicker(_mActivity, options);
        picker.setSelectedItem(selectItem);
        setPickerConfig(picker);
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                if (view.getId() == R.id.tv_relation) {
                    if (TextUtils.equals(item, optionRelation[0])) {
                        //TODO 设置电话
                        etReservationPhone.setText(User.getMobile());
                    } else {
                        etReservationPhone.setText("");
                    }
                }
                view.setText(item);
            }
        });
        picker.show();
    }


    /**
     * 选择器的配置
     */
    private void setPickerConfig(WheelPicker picker) {
        picker.setTextSize(size);
        picker.setCancelTextSize(size);
        picker.setSubmitTextSize(size);
        picker.setLineColor(selectColor);
        picker.setTextColor(selectColor);
    }

}
