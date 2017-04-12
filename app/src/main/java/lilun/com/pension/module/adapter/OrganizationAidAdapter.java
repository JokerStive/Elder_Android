package lilun.com.pension.module.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pension.R;
import lilun.com.pension.app.App;
import lilun.com.pension.app.IconUrl;
import lilun.com.pension.base.QuickAdapter;
import lilun.com.pension.module.bean.OrganizationAid;
import lilun.com.pension.module.utils.StringUtils;
import lilun.com.pension.widget.SearchTitleBar;
import lilun.com.pension.widget.image_loader.ImageLoaderUtil;

/**
 * 展示互助列表的adapter
 *
 * @author yk
 *         create at 2017/2/13 11:27
 *         email : yk_developer@163.com
 */
public class OrganizationAidAdapter extends QuickAdapter<OrganizationAid> {
    private SearchTitleBar.LayoutType layoutType;
    private OnItemClickListener listener;



    public OrganizationAidAdapter(List<OrganizationAid> data, int itemRes, SearchTitleBar.LayoutType layoutType) {
        super(itemRes, data);
        this.layoutType = layoutType;
    }

    @Override
    protected void convert(BaseViewHolder help, OrganizationAid aid) {

        //标题加粗
        TextView tvTitle = help.getView(R.id.tv_product_name);
//        UIUtils.setBold(tvTitle);
        tvTitle.setText(aid.getTitle());

        //是否有图片需要加载
        if (layoutType != SearchTitleBar.LayoutType.NULL) {
            ImageLoaderUtil.instance().loadImage(IconUrl.moduleIconUrl(IconUrl.OrganizationAids,aid.getId(), StringUtils.getFirstIconNameFromIcon(aid.getImage())), R.drawable.icon_def, help.getView(R.id.iv_icon));
        }


        //是否显示补贴和创建者
        if (layoutType != SearchTitleBar.LayoutType.BIG) {
            //创建者
            help.setText(R.id.tv_creatorName, aid.getCreatorName());
        }

        //补贴
        TextView tvItemPrice = help.getView(R.id.tv_mobile);
        tvItemPrice.setText(String.format(App.context.getString(R.string.help_price), aid.getPrice()));

        //参与者和时间
        TextView time_joinerCount = help.getView(R.id.tv_time_joinCount);
        time_joinerCount.setText("/" + StringUtils.IOS2ToUTC(aid.getCreatedAt(), 4) + "/" + "50人参与");

//        else {
//            help.setText(R.id.tv_time, StringUtils.IOS2ToUTC(aid.getCreatedAt(),3));
//            help.setText(R.id.tv_joinCount,"参与人数50人");
//
//        }

        //是否显示地址
        TextView tvItemAddress = help.getView(R.id.tv_environment);
        tvItemAddress.setVisibility(aid.getKind() == 0 ? View.GONE : View.VISIBLE);
        if (tvItemAddress.getVisibility() == View.VISIBLE) {
            tvItemAddress.setText(aid.getAddress());
        }


        help.setOnClickListener(R.id.ll_bg, v -> {
            if (listener != null) {
                listener.onItemClick(aid);
            }
        });

    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(OrganizationAid aid);
    }
}
