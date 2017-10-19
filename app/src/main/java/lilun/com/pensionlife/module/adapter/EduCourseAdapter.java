package lilun.com.pensionlife.module.adapter;

import android.text.Html;

import com.chad.library.adapter.base.BaseViewHolder;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.IconUrl;
import lilun.com.pensionlife.base.QuickAdapter;
import lilun.com.pensionlife.module.bean.OrganizationProduct;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.widget.image_loader.ImageLoaderUtil;

/**
 * 大学-课程adapter
 */
public class EduCourseAdapter extends QuickAdapter<OrganizationProduct> {

    public EduCourseAdapter(List<OrganizationProduct> data) {
        super(R.layout.item_course, data);
    }


    @Override
    protected void convert(BaseViewHolder help, OrganizationProduct course) {
        int totalCount = course.getSold() + course.getStock();
        String priceFormat = new DecimalFormat("######0.00").format(course.getPrice());
        String price = "合计: <font color='#ff4400'>" + "¥" + priceFormat + "</font>";

        help.setText(R.id.tv_course_title, course.getName())
                .setText(R.id.tv_course_allow_count, "报名人数:" + totalCount)
                .setText(R.id.tv_course_stock, "剩余人数:" + course.getStock())
                .setText(R.id.tv_course_price, Html.fromHtml(price));


        //扩展属性
        Map<String, Object> extend = course.getExtend();
        if (extend != null) {
            String teacher = (String) extend.get("teacher");
            String classStartTime = StringUtils.filterNull(StringUtils.IOS2ToUTCNot8((String) extend.get("classStartTime")));
            String classEndTime = StringUtils.filterNull(StringUtils.IOS2ToUTCNot8((String) extend.get("classEndTime")));
            String address = (String) extend.get("classPlace");
            help.setText(R.id.tv_course_teacher, "授课老师:" + StringUtils.filterNull(teacher))
                    .setText(R.id.tv_course_time, "上课时间 : " + StringUtils.filterNull(classStartTime+"-"+classEndTime))
                    .setText(R.id.tv_course_address, "上课地址 : " + StringUtils.filterNull(address));
        }


        //图片

        String iconUrl = IconUrl.moduleIconUrl(IconUrl.OrganizationProducts, course.getId(), StringUtils.getFirstIconNameFromIcon(course.getImage()));
        ImageLoaderUtil.instance().loadImage(iconUrl, help.getView(R.id.iv_course_icon));

    }


}
