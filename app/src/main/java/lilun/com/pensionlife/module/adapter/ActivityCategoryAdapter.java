package lilun.com.pensionlife.module.adapter;

import android.content.res.Resources;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.base.QuickAdapter;
import lilun.com.pensionlife.module.bean.ActivityCategory;
import lilun.com.pensionlife.widget.image_loader.ImageLoaderUtil;

/**
 * 分类模块adapter
 *
 * @author yk
 *         create at 2017/2/13 11:14
 *         email : yk_developer@163.com
 */
public class ActivityCategoryAdapter extends QuickAdapter<ActivityCategory> {
    private BaseFragment fragment;
    private OnItemClickListener listener;
    private int mSelectedPosition = -1;
    private boolean isRadioMode = false;

    public ActivityCategoryAdapter(BaseFragment fragment, List<ActivityCategory> data) {
        super(R.layout.item_activity_module, data);
        this.fragment = fragment;
        initData();
    }

    private void initData() {
        if (isRadioMode) {
            for (int i = 0; i < getData().size(); i++) {
                if (getData().get(i).isSelected()) {
                    mSelectedPosition = i;
                }
            }
        }

    }

    public void setIsRadioModule(boolean isRadioMode) {
        this.isRadioMode = isRadioMode;
    }


    @Override
    protected void convert(BaseViewHolder helper, ActivityCategory activityCategory) {
        if (isRadioMode) {
            helper.getView(R.id.ll_module_background).setSelected(mSelectedPosition == helper.getAdapterPosition());
        }

        if (activityCategory.getUnRead() != 0) {
            helper.setText(R.id.tv_msg_number, activityCategory.getUnRead() > 99 ? "..." : activityCategory.getUnRead() + "")
                    .setVisible(R.id.tv_msg_number, true);
        } else
            helper.setVisible(R.id.tv_msg_number, false);

        helper.setText(R.id.tv_module_name, activityCategory.getName())
                .setOnClickListener(R.id.ll_module_background, v -> {
                    if (listener != null) {
                        listener.onItemClick(activityCategory);
                        setRadio(helper);
                    }
                });

        ImageLoaderUtil.instance().loadImage(activityCategory.getIcon(),helper.getView(R.id.iv_module_icon));
//        if (activityCategory.getIcon() != null && activityCategory.getIcon().size() > 0) {
//            String iconUrl = activityCategory.getIcon().get(0).getFileName();
//            Glide.with(App.context).load(iconUrl).dontAnimate()
//                    .placeholder(R.drawable.icon_def)
//                    .error(R.drawable.icon_def)
//                    .into((ImageView) helper.getView(R.id.iv_module_icon));
//        }
    }


    private void setRadio(BaseViewHolder helper) {
        if (isRadioMode) {
            mSelectedPosition = helper.getAdapterPosition();
            notifyDataSetChanged();
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(ActivityCategory activityCategory);
    }


    private int getColor() {
        Resources resources = App.context.getResources();
        return resources.getColor(R.color.activity);
    }
}
