package lilun.com.pensionlife.module.adapter;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.IconUrl;
import lilun.com.pensionlife.base.QuickAdapter;
import lilun.com.pensionlife.module.bean.EdusColleageCourse;
import lilun.com.pensionlife.module.bean.OrganizationProduct;
import lilun.com.pensionlife.module.utils.BitmapUtils;
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
        String signDateStr = StringUtils.IOS2ToUTC(course.getStartSingnDate(), 0) + " ~ " + StringUtils.IOS2ToUTC(course.getEndSingnDate(), 0);
        help.setText(R.id.tv_title, course.getName())
                .setText(R.id.tv_address, mContext.getString(R.string.course_sign_date_, signDateStr));

        ImageLoaderUtil.instance().loadImage(IconUrl.eduCourses(course.getId(), BitmapUtils.picName(course.getPicture())),
                R.drawable.icon_def, help.getView(R.id.iv_icon));
    }


}
