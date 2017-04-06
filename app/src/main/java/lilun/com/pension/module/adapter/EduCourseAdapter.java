package lilun.com.pension.module.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import lilun.com.pension.R;
import lilun.com.pension.app.IconUrl;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.base.QuickAdapter;
import lilun.com.pension.module.bean.EdusColleageCourse;
import lilun.com.pension.module.bean.IconModule;
import lilun.com.pension.module.utils.BitmapUtils;
import lilun.com.pension.module.utils.StringUtils;
import lilun.com.pension.widget.image_loader.ImageLoaderUtil;

/**
 * 大学-课程adapter
 */
public class EduCourseAdapter extends QuickAdapter<EdusColleageCourse> {
    private BaseFragment fragment;
    private OnItemClickListener listener;

    public EduCourseAdapter(BaseFragment fragment, List<EdusColleageCourse> data) {
        super(R.layout.item_module_second, data);
        this.fragment = fragment;
    }

    @Override
    protected void convert(BaseViewHolder help, EdusColleageCourse course) {
        String signDateStr =  StringUtils.IOS2ToUTC(course.getStartSingnDate(), 0) +" ~ "+ StringUtils.IOS2ToUTC(course.getEndSingnDate(), 0);
        help.setText(R.id.tv_item_title, course.getName())
                .setText(R.id.tv_item_address, mContext.getString(R.string.course_sign_date_, signDateStr))
                .setOnClickListener(R.id.ll_module_background, v -> {
                    if (listener != null) {
                        listener.onItemClick(course);
                    }
                });

        ImageLoaderUtil.instance().loadImage(IconUrl.eduCourses(course.getId(), BitmapUtils.picName(course.getPicture())),
                R.drawable.icon_def, help.getView(R.id.iv_icon));


    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(EdusColleageCourse activity);
    }
}
