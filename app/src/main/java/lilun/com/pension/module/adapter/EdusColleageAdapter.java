package lilun.com.pension.module.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pension.R;
import lilun.com.pension.app.IconUrl;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.base.QuickAdapter;
import lilun.com.pension.module.bean.ElderEdusColleage;
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

    public EdusColleageAdapter(List<ElderEdusColleage> data, int layoutId) {
        super(layoutId, data);
    }


    @Override
    protected void convert(BaseViewHolder help, ElderEdusColleage edu) {
        help.setText(R.id.tv_title, edu.getName())
                .setText(R.id.tv_address, edu.getAddress())

                .setOnClickListener(R.id.ll_bg, v -> {
                    if (listener != null) {
                        listener.onItemClick(edu);
                    }
                });
        Glide.with(help.getConvertView().getContext().getApplicationContext())
                .load(IconUrl.organizationEdus(edu.getId(), BitmapUtils.picName(edu.getPicture())))
                .dontAnimate()
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
