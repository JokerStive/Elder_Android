package lilun.com.pension.module.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pension.R;
import lilun.com.pension.app.IconUrl;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.base.QuickAdapter;
import lilun.com.pension.module.bean.ElderEdus;
import lilun.com.pension.module.bean.OrganizationActivity;
import lilun.com.pension.module.utils.BitmapUtils;
import lilun.com.pension.module.utils.StringUtils;
import lilun.com.pension.widget.image_loader.ImageLoaderUtil;

/**
 * 老年教育  我参加的课程（讲堂 /课程）  adapter
 */
public class OrganizationEdusAdapter extends QuickAdapter<ElderEdus> {
    private BaseFragment fragment;
    private OnItemClickListener listener;

    public OrganizationEdusAdapter(BaseFragment fragment, List<ElderEdus> data) {
        super(R.layout.item_module_second, data);
        this.fragment = fragment;
    }

    @Override
    protected void convert(BaseViewHolder help, ElderEdus edu) {
        help.setText(R.id.tv_item_title, edu.getTitle())
                .setText(R.id.tv_item_address, edu.getAddress())
                .setText(R.id.tv_item_time, StringUtils.IOS2ToUTC(edu.getStartDate(),0))
                .setOnClickListener(R.id.ll_module_background, v -> {
                    if (listener != null) {
                        listener.onItemClick(edu);
                    }
                });
//        if (edu.getType() == OrganizationActivity.TYPE) {
//            help.setText(R.id.tv_item_time, StringUtils.IOS2ToUTC(edu.getStartDate(),0));
//        }

        ImageLoaderUtil.instance().loadImage(IconUrl.eduCourses(edu.getId(), BitmapUtils.picName(edu.getPicture())),
                R.drawable.icon_def, help.getView(R.id.iv_icon));
//        Glide.with(fragment)
//                .load(IconUrl.eduCourses(edu.getId(), BitmapUtils.picName(edu.getPicture())))
//                .placeholder(R.drawable.icon_def)
//                .error(R.drawable.icon_def)
//                .into((ImageView) help.getView(R.id.iv_icon));


    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(ElderEdus activity);
    }
}
