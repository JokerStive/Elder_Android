package lilun.com.pensionlife.module.adapter;

import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;

import java.text.SimpleDateFormat;
import java.util.List;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.base.QuickAdapter;
import lilun.com.pensionlife.module.bean.CourseSchedule;
import lilun.com.pensionlife.module.bean.OrganizationAccount;
import lilun.com.pensionlife.module.utils.StringUtils;

/**
 * 课程表.
 */
public class CourseScheduleAdapter extends QuickAdapter<CourseSchedule> {

    private TeacherListener listener;

    public CourseScheduleAdapter(List<CourseSchedule> data) {
        super(R.layout.item_course_schedule, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CourseSchedule schedule) {
        helper.setText(R.id.tv_schedule_name, schedule.getName())
                .setText(R.id.tv_schedule_location, "上课地点：" + schedule.getLocation());

//        //上课时间
        String courseStartTime = StringUtils.IOS2ToUTC(schedule.getCourseStartTime(), 1);
        String courseEndTime = StringUtils.IOS2ToUTC(schedule.getCourseEndTime(), 1);
        String courseWeek =schedule.getWeek();
        String courseTime = "上课时间：" + courseWeek + "  " + courseStartTime + "~" + courseEndTime;
        helper.setText(R.id.tv_schedule_courseTime, courseTime);

        //上课时段
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        String startTime = StringUtils.IOS2ToUTC(schedule.getStartTime(), format);
        String endTime = StringUtils.IOS2ToUTC(schedule.getEndTime(), format);
        String time = "上课时段：" + startTime + "~" + endTime;
        helper.setText(R.id.tv_schedule_time, time);

        //专业老师
        LinearLayout view = helper.getView(R.id.ll_teachers);
        List<OrganizationAccount> teachers = schedule.getTeachers();

        for (OrganizationAccount teacher : teachers) {
            TextView tvTeacher = (TextView) LayoutInflater.from(App.context).inflate(R.layout.item_teacher, null);
            String teacherName = teacher.getName();
//            TextView tvTeacher = new TextView(App.context);
//            tvTeacher.setTextSize(UIUtils.sp2px(App.context, 8));
//            tvTeacher.setTextColor(Color.parseColor("#666666"));
//            tvTeacher.setPadding(0, 0, UIUtils.dp2px(App.context, 10), 0);
            tvTeacher.setText(teacherName);
            tvTeacher.setOnClickListener(v -> {
                //老师简介
                if (listener != null) {
                    listener.onTeacherClick(teacher.getId());
                }
            });
            view.addView(tvTeacher);
        }
    }

    public interface TeacherListener {
        void onTeacherClick(String organizationAccountId);
    }

    public void setTeacherListener(TeacherListener listener) {
        this.listener = listener;
    }
}
