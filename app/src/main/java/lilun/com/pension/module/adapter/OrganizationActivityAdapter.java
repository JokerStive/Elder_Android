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
    private OnItemClickListener listener;
    private boolean showIsBig = true;
    private boolean allowshowIcon = true;

    public OrganizationActivityAdapter(List<OrganizationActivity> data, int layoutRes, SearchTitleBar.LayoutType layoutType) {
        super(layoutRes, data);
        showIsBig = layoutRes == R.layout.item_activity_big;
    }

    /**
     *
     * @param data
     * @param layoutRes
     * @param layoutType
     * @param allowshowIcon  是否允许显示参加图标
     */
    public OrganizationActivityAdapter(List<OrganizationActivity> data, int layoutRes, SearchTitleBar.LayoutType layoutType, boolean allowshowIcon) {
        super(layoutRes, data);
        showIsBig = layoutRes == R.layout.item_activity_big;
        this.allowshowIcon = allowshowIcon;
    }


    @Override
    protected void convert(BaseViewHolder help, OrganizationActivity activity) {
        Context context = help.getConvertView().getContext().getApplicationContext();
        help.setText(R.id.tv_product_name, activity.getTitle())
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
        ImageLoaderUtil.instance().loadImage(IconUrl.moduleIconUrl(IconUrl.OrganizationActivities, activity.getId(), fileName),
                R.drawable.icon_def, help.getView(R.id.iv_icon));

        String time = activity.getStartTime();
        help.setText(R.id.tv_activity_time, context.getString(R.string.activity_time_, time));

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(OrganizationActivity activity);
    }
}
