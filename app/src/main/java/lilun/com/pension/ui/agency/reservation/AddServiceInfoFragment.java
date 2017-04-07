package lilun.com.pension.ui.agency.reservation;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import butterknife.Bind;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DateTimePicker;
import cn.qqtheme.framework.picker.OptionPicker;
import cn.qqtheme.framework.picker.WheelPicker;
import lilun.com.pension.R;
import lilun.com.pension.app.App;
import lilun.com.pension.base.BaseFragment;
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

    @Bind(R.id.et_reservation_name)
    EditText etReservationName;

    @Bind(R.id.et_reservation_phone)
    EditText etReservationPhone;

    @Bind(R.id.tv_check_in_time)
    TextView tvCheckInTime;
    @Bind(R.id.titleBar)
    NormalTitleBar titleBar;


    private int size = 17;
    private int selectColor = App.context.getResources().getColor(R.color.red);
    private String[] optionSex = App.context.getResources().getStringArray(R.array.personal_info_sex);
    private String[] optionHealthStatus = App.context.getResources().getStringArray(R.array.personal_info_health_status);
    private String[] optionRelation = App.context.getResources().getStringArray(R.array.personal_info_relation);

    public static AddServiceInfoFragment newInstance() {
        AddServiceInfoFragment fragment = new AddServiceInfoFragment();
        return fragment;
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
    }


    @OnClick({R.id.tv_sex, R.id.tv_health_status, R.id.tv_relation, R.id.tv_birthday, R.id.btn_confirm})
    public void onClick(View view) {
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
        Logger.d("保存用户资料");
    }

    /**
     * 生日选择器
     */
    private void chooseBirthday() {
        DateTimePicker picker = new DateTimePicker(_mActivity, DateTimePicker.YEAR_MONTH_DAY, DateTimePicker.NONE);
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
