package lilun.com.pension.ui.agency.merchant;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.Locale;

import butterknife.Bind;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DateTimePicker;
import cn.qqtheme.framework.picker.OptionPicker;
import cn.qqtheme.framework.picker.WheelPicker;
import lilun.com.pension.R;
import lilun.com.pension.app.App;
import lilun.com.pension.app.Event;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.bean.MerchantMemo;
import lilun.com.pension.module.bean.ProductOrder;
import lilun.com.pension.module.utils.Preconditions;
import lilun.com.pension.module.utils.RxUtils;
import lilun.com.pension.net.NetHelper;
import lilun.com.pension.net.RxSubscriber;

/**
 * 商家订单备注V
 *
 * @author yk
 *         create at 2017/4/7 8:42
 *         email : yk_developer@163.com
 */
public class MemoFragment extends BaseFragment {


//    @Bind(R.id.titleBar)
//    NormalTitleBar titleBar;

//    @Bind(R.id.rl_call_situation)
//    RelativeLayout rlCallSituation;

    @Bind(R.id.et_memo)
    EditText etMemo;

    @Bind(R.id.rb_assigned)
    RadioButton rbAssigned;

    @Bind(R.id.rb_delay)
    RadioButton rbDelay;

    @Bind(R.id.rb_cancel)
    RadioButton rbCancel;

    @Bind(R.id.rg_status)
    RadioGroup rgStatus;

    @Bind(R.id.tv_call_situation)
    TextView tvCallSituation;

    @Bind(R.id.rb_done)
    RadioButton rbDone;

//    @Bind(R.id.btn_confirm)
//    Button btnConfirm;

    private String[] callSituation;
    //    "reserved","assigned", "delay", "done","cancel"
    private String[] statusList = App.context.getResources().getStringArray(R.array.merchant_order_condition_value);

    private String mCurrentStatus;
    private int size = 17;
    private int selectColor = App.context.getResources().getColor(R.color.red);
    private String delayTime;
    private ProductOrder mOrder;
//    private boolean canEditStatus;

    public static MemoFragment newInstance(ProductOrder order) {
        MemoFragment fragment = new MemoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("order", order);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected void getTransferData(Bundle arguments) {
        mOrder = (ProductOrder) arguments.getSerializable("order");
        Preconditions.checkNull(mOrder);
    }

    @Override
    protected void initPresenter() {
        callSituation = getResources().getStringArray(R.array.merchant_call_situation);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_merchant_memo;
    }

    @Override
    protected void initView(LayoutInflater inflater) {

        rgStatus.check(R.id.rb_exit);
        rgStatus.setOnCheckedChangeListener((group, checkedId) -> {
            rbDelay.setText(getString(R.string.status_delay));
            switch (checkedId) {
                case R.id.rb_assigned:

                    mCurrentStatus = statusList[1];

                    break;

                case R.id.rb_done:

                    mCurrentStatus = statusList[3];

                    break;

                case R.id.rb_cancel:

                    mCurrentStatus = statusList[4];

                    break;
            }
        });

        setInitData();
    }

    private void setInitData() {
        tvCallSituation.setText(mOrder.getCallStatus());
        etMemo.setText(mOrder.getRemark());

        setStatus();
    }

    private void setStatus() {
        String status = mOrder.getStatus();
//        canEditStatus = TextUtils.equals(status, statusList[0]) || TextUtils.equals(status, statusList[2]);

        //预约
        if (TextUtils.equals(status, statusList[0])) {
            rbAssigned.setVisibility(View.VISIBLE);
            rbCancel.setVisibility(View.VISIBLE);
            rbDelay.setVisibility(View.VISIBLE);
        }

        //受理
        else if (TextUtils.equals(status, statusList[1])) {
            rgStatus.setVisibility(View.GONE);
//            rbDone.setVisibility(View.VISIBLE);
        }

        //延时
        else if (TextUtils.equals(status, statusList[2])) {
            rbAssigned.setVisibility(View.VISIBLE);
            rbCancel.setVisibility(View.VISIBLE);
        }

        //完成
        else if (TextUtils.equals(status, statusList[3])) {
            rgStatus.setVisibility(View.GONE);
        }

        //取消
        else if (TextUtils.equals(status, statusList[4])) {
            rgStatus.setVisibility(View.GONE);
        }
    }


    @OnClick({R.id.rl_call_situation, R.id.rb_delay})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_call_situation:
                callSituationChoose();
                break;

            case R.id.rb_delay:
                chooseDelayTime();
                break;
        }
    }


    /**
     * 延期时，选择延期时间
     */
    private void chooseDelayTime() {
        int month = Calendar.getInstance(Locale.CHINA).get(Calendar.MONTH) + 1;
        int day = Calendar.getInstance(Locale.CHINA).get(Calendar.DAY_OF_MONTH);
        DateTimePicker picker = new DateTimePicker(_mActivity, DateTimePicker.MONTH_DAY, DateTimePicker.NONE);
        setPickerConfig(picker);
        picker.setDateRangeStart(month, day);
        picker.setOnDateTimePickListener((DateTimePicker.OnMonthDayTimePickListener) (month1, day1, hour, minute) -> {
            int year = Calendar.getInstance().get(Calendar.YEAR);
            delayTime = year + "-" + month1 + "-" + day1;
            rbDelay.setText(delayTime);
            mCurrentStatus = statusList[2];
        });
        picker.show();
    }

    /**
     * 呼叫情况选择
     */
    private void callSituationChoose() {
        OptionPicker picker = new OptionPicker(_mActivity, callSituation);
        picker.setSelectedItem(tvCallSituation.getText().toString());
        setPickerConfig(picker);
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                tvCallSituation.setText(item);
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

    /**
     * 存储备注
     */
    public void saveMemo() {
        CharSequence callSituation = tvCallSituation.getText();
//        if (TextUtils.isEmpty(callSituation)) {
//            ToastHelper.get().showWareShort("请选择通话情况");
//            return;
//        }


//        if (TextUtils.isEmpty(mCurrentStatus) && mCurrentStatus.equals(statusList[2])) {
//            ToastHelper.get().showWareShort("请备注状态");
//
//            if (TextUtils.equals(mCurrentStatus, statusList[1]) && TextUtils.isEmpty(delayTime)) {
//                ToastHelper.get().showWareShort("请选择延期时间");
//                return;
//            }
//            return;
//        }





        String memo = etMemo.getText().toString();

        MerchantMemo order = new MerchantMemo();
        order.setCallStatus(callSituation.toString());
        order.setStatus(mCurrentStatus);
        order.setRemark(memo);
        order.setDelayTime(delayTime);

        putOrder(order);

    }

    private void putOrder(MerchantMemo order) {
        NetHelper.getApi().putMerchantMemoOrder(mOrder.getId(), order)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<Object>(_mActivity) {
                    @Override
                    public void _next(Object o) {
                        EventBus.getDefault().post(new Event.RefreshMerchantOrder());
                        getActivity().finish();
                    }
                });
    }


}
