package lilun.com.pensionlife.module.utils.dynamic;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import java.util.Calendar;

import cn.qqtheme.framework.picker.DateTimePicker;
import cn.qqtheme.framework.picker.WheelPicker;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.App;

/**
 * 时间控件
 */
public class DateView extends BaseView {


    private Activity activity;
    private TextView tv_mate_value;

    public DateView(Activity activity, JSONObject setting) {

        this.activity = activity;
        setSetting(setting);
    }

    @Override
    public View createView() {
        return initView();
    }


    private View initView() {
        View dateView = LayoutInflater.from(App.context).inflate(R.layout.dynamic_choose_view, null);

        TextView tv_mate_title = (TextView) dateView.findViewById(R.id.mate_title);
        tv_mate_value = (TextView) dateView.findViewById(R.id.mate_value);
        tv_mate_title.setText(mate_title);
        tv_mate_value.setHint(mate_description);
        tv_mate_value.setText((String) mate_value);

        if (isOnlyShow) {
            tv_mate_value.setCompoundDrawables(null, null, null, null);
            return dateView;
        }
        tv_mate_value.setOnClickListener(v -> chooseData());

        return dateView;
    }


    /**
     * 时间选择器
     */
    private void chooseData() {
        DateTimePicker picker = new DateTimePicker(activity, DateTimePicker.YEAR_MONTH_DAY, DateTimePicker.NONE);
        picker.setDateRangeStart(1900, 1, 1);
        Calendar cal = Calendar.getInstance();
        int endYear = cal.get(Calendar.YEAR);
        int endMonth = cal.get(Calendar.MONTH);
        int endDay = cal.get(Calendar.DATE);
//        picker.setDateRangeEnd(endYear, endMonth, endDay);

        setPickerConfig(picker);
        picker.setOnDateTimePickListener((DateTimePicker.OnYearMonthDayTimePickListener) (year, month, day, hour, minute) -> {
            String time = year + "-" + month + "-" + day;
            setData(time);
        });
        picker.show();
    }

    private void setData(String time) {
        tv_mate_value.setText(time);
        setValue(time);
    }


    private void setPickerConfig(WheelPicker picker) {
        int size = 17;
        int selectColor = 0xff0090f9;
        picker.setTextSize(size);
        picker.setCancelTextSize(size);
        picker.setSubmitTextSize(size);
        picker.setLineColor(selectColor);
        picker.setTextColor(selectColor);
    }
}
