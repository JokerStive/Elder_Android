package lilun.com.pension.module.adapter;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pension.R;
import lilun.com.pension.app.IconUrl;
import lilun.com.pension.app.User;
import lilun.com.pension.base.QuickAdapter;
import lilun.com.pension.module.bean.OrganizationActivity;
import lilun.com.pension.module.utils.StringUtils;
import lilun.com.pension.module.utils.UIUtils;
import lilun.com.pension.widget.SearchTitleBar;
import lilun.com.pension.widget.image_loader.ImageLoaderUtil;

/**
 * 展示互助列表的adapter
 *
 * @author yk
 *         create at 2017/2/13 11:27
 *         email : yk_developer@163.com
 */
public class OrganizationActivityAdapter extends QuickAdapter<OrganizationActivity> {
    private String isjoined = "未参加";
    private OnItemClickListener listener;

    public OrganizationActivityAdapter(List<OrganizationActivity> data, int layoutRes, SearchTitleBar.LayoutType layoutType) {
        super(layoutRes, data);
    }

    @Override
    protected void convert(BaseViewHolder help, OrganizationActivity activity) {
        UIUtils.setBold(help.getView(R.id.tv_product_name));

        help.setText(R.id.tv_product_name, activity.getTitle())
                .setText(R.id.tv_environment, activity.getAddress())
                .setOnClickListener(R.id.ll_bg, v -> {
                    if (listener != null) {
                        listener.onItemClick(activity);
                    }
                });


        List<String> partners = activity.getPartners();
        String timeAndJoinCount = "/ " + StringUtils.IOS2ToUTC(activity.getCreatedAt(), 4);
        if (partners != null) {
            timeAndJoinCount = timeAndJoinCount + " / " + "参与人数(" + partners.size() + "/" + activity.getMaxPartner() + ")";
        }else {
            timeAndJoinCount = timeAndJoinCount + " / " + "参与人数(0 /" + activity.getMaxPartner() + ")";
        }

        //时间和参与的人
        help.setText(R.id.tv_time_joinCount,timeAndJoinCount);

        //是否参加
        if (partners != null && partners.size() != 0) {
            for (String partnerId : partners) {
                if (TextUtils.equals(partnerId, User.getUserId())) {
                    isjoined = "已参加";
                }
            }
        }
        help.setText(R.id.tv_isJoin, isjoined);


        String fileName = activity.getIcon() != null ? activity.getIcon().get(0).getFileName() : null;
        ImageLoaderUtil.instance().loadImage(IconUrl.moduleIconUrl(IconUrl.OrganizationActivities,activity.getId(), fileName),
                R.drawable.icon_def, help.getView(R.id.iv_icon));


    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(OrganizationActivity activity);
    }
}
