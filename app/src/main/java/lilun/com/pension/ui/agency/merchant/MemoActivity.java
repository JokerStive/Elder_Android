package lilun.com.pension.ui.agency.merchant;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import java.util.Calendar;
import java.util.Locale;

import butterknife.Bind;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DateTimePicker;
import cn.qqtheme.framework.picker.OptionPicker;
import cn.qqtheme.framework.picker.WheelPicker;
import lilun.com.pension.R;
import lilun.com.pension.app.App;
import lilun.com.pension.base.BaseActivity;
import lilun.com.pension.module.utils.ToastHelper;
import lilun.com.pension.widget.NormalTitleBar;

/**
 * 商家订单备注V
 *
 * @author yk
 *         create at 2017/4/7 8:42
 *         email : yk_developer@163.com
 */
public class MemoActivity extends BaseActivity {


    @Bind(R.id.titleBar)
    NormalTitleBar titleBar;

//    @Bind(R.id.rl_call_situation)
//    RelativeLayout rlCallSituation;

    @Bind(R.id.et_memo)
    EditText etMemo;

    @Bind(R.id.rb_complete)
    RadioButton rbComplete;

    @Bind(R.id.rb_delay)
    RadioButton rbDelay;

    @Bind(R.id.rb_cancel)
    RadioButton rbCancel;

    @Bind(R.id.rg_status)
    RadioGroup rgStatus;
    @Bind(R.id.tv_call_situation)
    TextView tvCallSituation;

//    @Bind(R.id.btn_confirm)
//    Button btnConfirm;

    private String[] callSituation;
    private String[] statusList = new String[]{"complete", "delay", "cancel"};

    private String status;
    private int size = 17;
    private int selectColor = App.context.getResources().getColor(R.color.red);

    @Override
    protected void initPresenter() {
        callSituation = getResources().getStringArray(R.array.merchant_call_situation);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_merchant_memo;
    }

    @Override
    protected void initView() {
        titleBar.setOnBackClickListener(this::finish);

        rgStatus.setOnCheckedChangeListener((group, checkedId) -> {
            rbDelay.setText(getString(R.string.status_delay));
            switch (checkedId) {
                case R.id.rb_complete:
                    status = statusList[0];
                    break;

                case R.id.rb_cancel:
                    status = statusList[2];
                    break;
            }
        });
    }


    @OnClick({R.id.rl_call_situation, R.id.btn_confirm, R.id.rb_delay})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_call_situation:
                callSituationChoose();
                break;

            case R.id.btn_confirm:
                saveMemo();

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
        DateTimePicker picker = new DateTimePicker(this, DateTimePicker.MONTH_DAY, DateTimePicker.NONE);
        setPickerConfig(picker);
        picker.setDateRangeStart(month, day);
        picker.setOnDateTimePickListener((DateTimePicker.OnMonthDayTimePickListener) (month1, day1, hour, minute) -> {
            int year = Calendar.getInstance().get(Calendar.YEAR);
            String time = year + "-" + month1 + "-" + day1;
            rbDelay.setText(time);
            status = statusList[1];
        });
        picker.show();
    }

    /**
     * 呼叫情况选择
     */
    private void callSituationChoose() {
        OptionPicker picker = new OptionPicker(this, callSituation);
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
    private void saveMemo() {
        CharSequence text = tvCallSituation.getText();
        if (TextUtils.isEmpty(text)) {
            ToastHelper.get().showWareShort("请选择通话情况");
            return;
        }


        if (TextUtils.isEmpty(status)) {
            ToastHelper.get().showWareShort("请备注状态");
            return;
        }


        String memo = etMemo.getText().toString();


        Logger.d("保存备注");
    }


}
