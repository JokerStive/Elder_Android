package lilun.com.pension.module.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pension.R;
import lilun.com.pension.app.IconUrl;
import lilun.com.pension.app.User;
import lilun.com.pension.base.QuickAdapter;
import lilun.com.pension.module.bean.OrganizationActivity;
import lilun.com.pension.module.utils.StringUtils;
import lilun.com.pension.widget.filter_view.FilterLayoutView;
import lilun.com.pension.widget.image_loader.ImageLoaderUtil;

/**
 * 展示互助列表的adapter
 *
 * @author yk
 *         create at 2017/2/13 11:27
 *         email : yk_developer@163.com
 */
public class OrganizationActivityAdapter extends QuickAdapter<OrganizationActivity> {
    private OnItemClickListener listener;
    private boolean showIsBig = true;
    private boolean allowshowIcon = true;

    public OrganizationActivityAdapter(List<OrganizationActivity> data, int layoutRes, FilterLayoutView.LayoutType layoutType) {
        super(layoutRes, data);
        showIsBig = layoutRes == R.layout.item_activity_big;
    }

    /**
     * @param data
     * @param layoutRes
     * @param layoutType
     * @param allowshowIcon 是否允许显示参加图标
     */
    public OrganizationActivityAdapter(List<OrganizationActivity> data, int layoutRes, FilterLayoutView.LayoutType layoutType, boolean allowshowIcon) {
        super(layoutRes, data);
        showIsBig = layoutRes == R.layout.item_activity_big;
        this.allowshowIcon = allowshowIcon;
    }


    @Override
    protected void convert(BaseViewHolder help, OrganizationActivity activity) {
        Context context = help.getConvertView().getContext().getApplicationContext();
        help.setText(R.id.tv_product_name, activity.getTitle())
                .setText(R.id.tv_creator, activity.getCreatorName())
                .setText(R.id.tv_create_time, StringUtils.IOS2ToUTC(activity.getCreatedAt(), 4))
                .setOnClickListener(R.id.ll_bg, v -> {
                    if (listener != null) {
                        listener.onItemClick(activity);
                    }
                });


        List<String> partners = activity.getPartnerList();
        String partinNumber = "0";
        if (partners != null && partners.size() != 0) {
            partinNumber = partners.size() + "";
        }
        help.setText(R.id.tv_isJoin, context.getString(R.string.has_partin, partinNumber + ""));

        if (allowshowIcon) {
            //是否参加
            if (partners != null && partners.size() != 0) {
                for (String partnerId : partners) {
                    if (TextUtils.equals(partnerId, User.getUserId())) {
                        help.getView(R.id.iv_partin_flag).setVisibility(View.VISIBLE);
                        if (showIsBig) {
                            help.setText(R.id.iv_partin_flag, context.getString(R.string.has_joined));
                        }
                        break;
                    }
                }
            } else {
                if (showIsBig) {
                    help.setText(R.id.iv_partin_flag, context.getString(R.string.unjoined));
                } else
                    help.getView(R.id.iv_partin_flag).setVisibility(View.GONE);
            }
        } else help.getView(R.id.iv_partin_flag).setVisibility(View.GONE);

        help.setText(R.id.tv_time_joinCount, context.getString(R.string.targart_partin, activity.getMaxPartner() + "人"));


        String fileName = activity.getIcon() != null ? activity.getIcon().get(0).getFileName() : null;
        //活动图片加载
        ImageLoaderUtil.instance().loadImage(IconUrl.moduleIconUrlOfActivity(IconUrl.OrganizationActivities, activity.getId(), fileName),
                R.drawable.icon_def, help.getView(R.id.iv_icon));
        //创建者头像加载
        ImageLoaderUtil.instance().loadImage(IconUrl.account(IconUrl.Accounts, activity.getCreatorId()),
                R.drawable.icon_def, help.getView(R.id.iv_ivatar));

        String time = activity.getStartTime();
        String activityTime = "";
        if (activity.getRepeatedDesc() != null) {
            activityTime = activity.getRepeatedDesc();
            help.setText(R.id.iv_is_repeat, context.getApplicationContext().getString(R.string.cyclical));
        } else {
            if (activity.getStartTime() != null)
                activityTime += StringUtils.IOS2ToUTC(activity.getStartTime(), 3);
            if (activity.getEndTime() != null)
                activityTime = activityTime + "~" + StringUtils.IOS2ToUTC(activity.getEndTime(), 3);
            help.setText(R.id.iv_is_repeat, context.getApplicationContext().getString(R.string.single));
        }
        help.setText(R.id.tv_activity_time, context.getString(R.string.activity_time_, activityTime));

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(OrganizationActivity activity);
    }
}
