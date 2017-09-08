package lilun.com.pensionlife.module.adapter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.DecimalFormat;
import java.util.List;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.IconUrl;
import lilun.com.pensionlife.base.QuickAdapter;
import lilun.com.pensionlife.module.bean.Course;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.widget.image_loader.ImageLoaderUtil;

/**
 * 大学-课程adapter
 */
public class EduCourseAdapter extends QuickAdapter<Course> {

    public EduCourseAdapter(List<Course> data) {
        super(R.layout.item_course, data);
    }


    @Override
    protected void convert(BaseViewHolder help, Course course) {
        int totalCount = course.getSold() + course.getStock();
        String priceFormat = new DecimalFormat("######0.00").format(course.getPrice());
        String price = "合计: <font color='#ff9d09'>" + "¥" + priceFormat + "</font>";

        help.setText(R.id.tv_course_title, course.getName())
                .setText(R.id.tv_course_allow_count, "报名人数:" + totalCount)
                .setText(R.id.tv_course_stock, "剩余人数:" + course.getStock())
                .setText(R.id.tv_course_price, price);


        //扩展属性
        String extend = course.getExtend();
        JSONObject extendJson = JSON.parseObject(extend);
        if (extendJson != null) {
            String teacher = extendJson.getString("teacher");
            String time = extendJson.getString("time");
            String address = extendJson.getString("address");
            help.setText(R.id.tv_course_teacher, "授课老师:" + StringUtils.filterNull(teacher))
                    .setText(R.id.tv_course_time, "上课时间:" + StringUtils.filterNull(time))
                    .setText(R.id.tv_course_address, "上课地址:" + StringUtils.filterNull(address));
        }


        //图片
        String iconUrl = IconUrl.moduleIconUrl(IconUrl.OrganizationProducts, course.getId(), StringUtils.getFirstIconNameFromIcon(course.getImages()));
        ImageLoaderUtil.instance().loadImage(iconUrl, help.getView(R.id.iv_icon));

    }


}
