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
import lilun.com.pension.module.bean.ElderEdusColleage;
import lilun.com.pension.module.bean.IconModule;
import lilun.com.pension.module.utils.BitmapUtils;

/**
 * 大学adapter
 */
public class EdusColleageAdapter extends QuickAdapter<ElderEdusColleage> {
    private BaseFragment fragment;
    private OnItemClickListener listener;

    public EdusColleageAdapter(BaseFragment fragment, List<ElderEdusColleage> data) {
        super(R.layout.item_module_second, data);
        this.fragment = fragment;
    }

    @Override
    protected void convert(BaseViewHolder help, ElderEdusColleage edu) {
        help.setText(R.id.tv_item_title, edu.getName())
                .setText(R.id.tv_item_address, edu.getAddress())

                .setOnClickListener(R.id.ll_module_background, v -> {
                    if (listener != null) {
                        listener.onItemClick(edu);
                    }
                });
        Glide.with(fragment)
                .load(IconUrl.organizationEdus(edu.getId(), BitmapUtils.picName((ArrayList<IconModule>) edu.getPicture())))
                .placeholder(R.drawable.icon_def)
                .error(R.drawable.icon_def)
                .into((ImageView) help.getView(R.id.iv_icon));


    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(ElderEdusColleage activity);
    }
}
