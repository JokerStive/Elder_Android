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
import lilun.com.pension.module.bean.ElderEdus;
import lilun.com.pension.module.bean.IconModule;
import lilun.com.pension.module.bean.OrganizationActivity;
import lilun.com.pension.module.utils.BitmapUtils;
import lilun.com.pension.module.utils.StringUtils;

/**
 * 大学adapter
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

                .setOnClickListener(R.id.ll_module_background, v -> {
                    if (listener != null) {
                        listener.onItemClick(edu);
                    }
                });
        if (edu.getSource().equals(OrganizationActivity.TYPE)) {
            help.setText(R.id.tv_item_time, StringUtils.IOS2ToUTC(edu.getCreatedAt()));
        }


        Glide.with(fragment)
                .load(IconUrl.organizationEdus(edu.getId(), BitmapUtils.picName((ArrayList<IconModule>) edu.getPicture())))
                .into((ImageView) help.getView(R.id.banner));


    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(ElderEdus activity);
    }
}
