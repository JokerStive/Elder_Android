package lilun.com.pensionlife.module.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;
import com.orhanobut.logger.Logger;

import org.litepal.crud.DataSupport;

import java.util.Date;
import java.util.List;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.IconUrl;
import lilun.com.pensionlife.base.QuickAdapter;
import lilun.com.pensionlife.module.bean.OrganizationActivity;
import lilun.com.pensionlife.module.bean.PushMessage;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.widget.SearchTitleBar;
import lilun.com.pensionlife.widget.filter_view.FilterLayoutView;
import lilun.com.pensionlife.widget.image_loader.ImageLoaderUtil;

/**
 * 活动列表的adapter
 *
 * @author yk
 *         create at 2017/2/13 11:27
 *         email : yk_developer@163.com
 */
public class OrganizationActivityAdapter extends QuickAdapter<OrganizationActivity> {
    private boolean allowshowIcon = false; //小红点角标


    public OrganizationActivityAdapter(List<OrganizationActivity> data, int layoutRes, SearchTitleBar.LayoutType layoutType) {
        super(layoutRes, data);
    }


    /**
     * @param data
     * @param layoutRes
     * @param layoutType
     * @param allowshowIcon 是否允许显示小红点角标图标
     */
    public OrganizationActivityAdapter(List<OrganizationActivity> data, int layoutRes, FilterLayoutView.LayoutType layoutType, boolean allowshowIcon) {
        super(layoutRes, data);
        this.allowshowIcon = allowshowIcon;
    }

    /**
     * 更新所有未读角标
     */
    public void notityUnReadAll() {
        if (getData() == null) return;
        List<OrganizationActivity> list = getData();
        for (int i = 0; i < list.size(); i++) {
            int count = DataSupport.where("activityId = ? and unRead = ?", list.get(i).getId(), "1").count(PushMessage.class);
            list.get(i).setUnRead(count);
        }
        notifyDataChanged();
    }

    /**
     * 改变未一个未读角标
     *
     * @param pos
     */
    public void notityUnRead(int pos) {
        if (getData() == null && getData().size() > pos) return;
        int count = DataSupport.where("activityId = ? and unread = 1", getData().get(pos).getId()).count(PushMessage.class);
        getData().get(pos).setUnRead(count);
        notifyItemChanged(pos);
    }


    @Override
    protected void convert(BaseViewHolder help, OrganizationActivity activity) {
        Context context = help.getConvertView().getContext().getApplicationContext();
        help.setText(R.id.tv_sophisticated, activity.getTitle())
                .setText(R.id.tv_creator, activity.getCreatorName())
                .setText(R.id.tv_create_time, StringUtils.IOS2ToUTC(activity.getCreatedAt(), 4));
        help.getView(R.id.tv_activity_status).setVisibility(View.VISIBLE);
        help.setVisible(R.id.iv_msg_number, allowshowIcon && activity.getUnRead() != 0);
        if (allowshowIcon) {
            help.setText(R.id.iv_msg_number, activity.getUnRead() > 99 ? "..." : activity.getUnRead() + "");
        }
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
        if (activity.getMaxPartner() == null || 0 == activity.getMaxPartner())
            numPeople = "不限";
        else
            numPeople = activity.getMaxPartner() + "人";
        help.setText(R.id.tv_time_joinCount, context.getString(R.string.targart_partin_, numPeople));


        String fileName = (activity.getIcon() != null && activity.getIcon().size() > 0) ?
                activity.getIcon().get(0).getFileName() : null;
        final String url = IconUrl.moduleIconUrlOfActivity(IconUrl.OrganizationActivities, activity.getId(), fileName);

        if (TextUtils.isEmpty(fileName))
            help.getView(R.id.iv_icon).setVisibility(View.GONE);
        else {
            help.getView(R.id.iv_icon).setVisibility(View.VISIBLE);
            //活动图片加载
            Logger.d("mScrollIdle :" + mScrollIdle);
            //  String imgTag = (String) help.getView(R.id.iv_icon).getTag();
            int tag;
            if (help.getView(R.id.iv_icon).getTag(R.id.img_tag) == null)
                tag = -1;
            else tag = (int) help.getView(R.id.iv_icon).getTag(R.id.img_tag);
            if (tag != help.getAdapterPosition()) {
                ImageLoaderUtil.instance().loadImage(R.drawable.icon_def, help.getView(R.id.iv_icon));
            }
            if (mScrollIdle) {
                //http://test.j1home.com/api/OrganizationActivities/91da3e60-86f8-11e7-a1c6-556b4bbfef62/download/icon/1503378820208.png?access_token=XkcBrQzoo84KMy6sMQub5DGGsfjcjMu2G0LAPzv0wnjJlPlMcRZ2EAyDSxaUCiju
                Logger.d("in --> mScrollIdle :" + mScrollIdle);
                ImageLoaderUtil.instance().loadImage(url, R.drawable.icon_def, help.getView(R.id.iv_icon));
            }

            help.getView(R.id.iv_icon).setTag(R.id.img_tag, help.getAdapterPosition());
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
}
