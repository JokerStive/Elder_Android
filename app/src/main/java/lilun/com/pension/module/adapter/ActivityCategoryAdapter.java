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
    private OnItemClickListener listener;
    private int mSelectedPosition = -1;
    private boolean isRadioMode =false;

    public ActivityCategoryAdapter(BaseFragment fragment, List<ActivityCategory> data) {
        super(R.layout.item_elder_module, data);
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
        if (isRadioMode){
            helper.getView(R.id.ll_module_background).setSelected(mSelectedPosition == helper.getAdapterPosition());
        }
        helper.setText(R.id.tv_module_name, activityCategory.getName())
                .setBackgroundRes(R.id.ll_module_background,  R.drawable.selector_activity)
                .setOnClickListener(R.id.ll_module_background, v -> {
                    if (listener != null) {
                        listener.onItemClick(activityCategory);
                        setRadio(helper);
                    }
                });


        if (activityCategory.getIcon()!=null) {
            String firstIconName = StringUtils.getFirstIconNameFromIcon(activityCategory.getIcon());
            String iconUrl = IconUrl.activityCategory(activityCategory.getId(),firstIconName);
            Glide.with(fragment).load(iconUrl).into((ImageView) helper.getView(R.id.iv_module_icon));
        }
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
