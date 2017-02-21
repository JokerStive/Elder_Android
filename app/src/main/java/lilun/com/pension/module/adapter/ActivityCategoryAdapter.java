package lilun.com.pension.module.adapter;

import android.content.res.Resources;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pension.R;
import lilun.com.pension.app.App;
import lilun.com.pension.app.IconUrl;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.base.QuickAdapter;
import lilun.com.pension.module.bean.ActivityCategory;
import lilun.com.pension.module.utils.StringUtils;

/**
 * 分类模块adapter
 *
 * @author yk
 *         create at 2017/2/13 11:14
 *         email : yk_developer@163.com
 */
public class ActivityCategoryAdapter extends QuickAdapter<ActivityCategory> {
    private BaseFragment fragment;
    private int color;
    private OnItemClickListener listener;

    public ActivityCategoryAdapter(BaseFragment fragment, List<ActivityCategory> data) {
        super(R.layout.item_elder_module, data);
        this.fragment = fragment;
    }

    @Override
    protected void convert(BaseViewHolder helper, ActivityCategory activityCategory) {
        helper.setText(R.id.tv_module_name, activityCategory.getName())
                .setBackgroundColor(R.id.ll_module_background, getColor())
                .setOnClickListener(R.id.ll_module_background, v -> {
                    if (listener != null) {
                        listener.onItemClick(activityCategory);
                    }
                });


        if (activityCategory.getIcon()!=null) {
            String firstIconName = StringUtils.getFirstIconNameFromIcon(activityCategory.getIcon());
            String iconUrl = IconUrl.activityCategory(activityCategory.getId(),firstIconName);
            Glide.with(fragment).load(iconUrl).into((ImageView) helper.getView(R.id.iv_module_icon));
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
