package lilun.com.pensionlife.module.adapter;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.IconUrl;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.base.QuickAdapter;
import lilun.com.pensionlife.module.bean.ElderEdus;
import lilun.com.pensionlife.module.utils.BitmapUtils;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.widget.image_loader.ImageLoaderUtil;

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
        help.setText(R.id.tv_product_title, edu.getTitle())
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
//                .load(IconUrl.eduCourses(edu.getId(), BitmapUtils.picName(edu.getImage())))
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
