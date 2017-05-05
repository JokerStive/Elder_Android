package lilun.com.pension.module.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.Date;
import java.util.List;

import lilun.com.pension.R;
import lilun.com.pension.app.IconUrl;
import lilun.com.pension.app.User;
import lilun.com.pension.base.QuickAdapter;
import lilun.com.pension.module.bean.OrganizationActivity;
import lilun.com.pension.module.utils.StringUtils;
import lilun.com.pension.widget.SearchTitleBar;
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

    private boolean allowshowIcon = true;

    public OrganizationActivityAdapter(List<OrganizationActivity> data, int layoutRes, SearchTitleBar.LayoutType layoutType) {
        super(layoutRes, data);
    }

    /**
     * @param data
     * @param layoutRes
     * @param layoutType
     * @param allowshowIcon 是否允许显示参加图标
     */
    public OrganizationActivityAdapter(List<OrganizationActivity> data, int layoutRes, FilterLayoutView.LayoutType layoutType, boolean allowshowIcon) {
        super(layoutRes, data);
        this.allowshowIcon = allowshowIcon;
    }


    @Override
    protected void convert(BaseViewHolder help, OrganizationActivity activity) {
        Context context = help.getConvertView().getContext().getApplicationContext();
        help.setText(R.id.tv_sophisticated, activity.getTitle())
                .setText(R.id.tv_creator, activity.getCreatorName())
                .setText(R.id.tv_create_time, StringUtils.IOS2ToUTC(activity.getCreatedAt(), 4))
                .setOnClickListener(R.id.ll_bg, v -> {
                    if (listener != null) {
                        listener.onItemClick(activity);
                    }
                });
        help.getView(R.id.tv_activity_status).setVisibility(View.VISIBLE);
        //不是周期活动
        if (TextUtils.isEmpty(activity.getRepeatedDesc())) {
            Date start = StringUtils.IOS2DateTime(activity.getStartTime());
            Date end = StringUtils.IOS2DateTime(activity.getEndTime());

            if (start != null && start.after(new Date()))  //未进行
                help.getView(R.id.tv_activity_status).setVisibility(View.GONE);
            else if (start != null && start.before(new Date()) && (end != null && end.after(new Date()))) {
                help.setText(R.id.tv_activity_status, context.getString(R.string.ongoing));
            } else if (end != null && end.before(new Date())) {
                help.setText(R.id.tv_activity_status, context.getString(R.string.finished));
            }
        }


        List<String> partners = activity.getPartnerList();
        String partinNumber = "0";
        if (partners != null && partners.size() != 0) {
            partinNumber = partners.size() + "";
        }
        help.setText(R.id.tv_isJoin, context.getString(R.string.has_partin, partinNumber + ""));

        String numPeople;
        if ("null".equals(String.valueOf(activity.getMaxPartner())) || "0".equals(String.valueOf(activity.getMaxPartner())))
            numPeople = "不限";
        else
            numPeople = activity.getMaxPartner() + "人";
        help.setText(R.id.tv_time_joinCount, context.getString(R.string.targart_partin_, numPeople));


        String fileName = (activity.getIcon() != null && activity.getIcon().size() > 0) ?
                activity.getIcon().get(0).getFileName() : null;
        if (TextUtils.isEmpty(fileName))
            help.getView(R.id.iv_icon).setVisibility(View.GONE);
        else {
            help.getView(R.id.iv_icon).setVisibility(View.VISIBLE);
            //活动图片加载
            ImageLoaderUtil.instance().loadImage(IconUrl.moduleIconUrlOfActivity(IconUrl.OrganizationActivities, activity.getId(), fileName),
                    R.drawable.icon_def, help.getView(R.id.iv_icon));
        }

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

        help.setText(R.id.tv_creator_time, StringUtils.up2thisTime(activity.getCreatedAt()));

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(OrganizationActivity activity);
    }
}
