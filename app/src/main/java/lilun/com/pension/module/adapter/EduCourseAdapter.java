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
import lilun.com.pension.module.bean.ElderEdus;
import lilun.com.pension.module.bean.IconModule;
import lilun.com.pension.module.bean.OrganizationActivity;
import lilun.com.pension.module.utils.BitmapUtils;
import lilun.com.pension.module.utils.StringUtils;

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
        help.setText(R.id.tv_item_title, course.getName())
                .setText(R.id.tv_item_address, course.getContent())

                .setOnClickListener(R.id.ll_module_background, v -> {
                    if (listener != null) {
                        listener.onItemClick(course);
                    }
                });


        Glide.with(fragment)
                .load(IconUrl.organizationEdus(course.getId(), BitmapUtils.picName((ArrayList<IconModule>) course.getPicture())))
                .placeholder(R.drawable.icon_def)
                .error(R.drawable.icon_def)
                .into((ImageView) help.getView(R.id.banner));


    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(EdusColleageCourse activity);
    }
}
