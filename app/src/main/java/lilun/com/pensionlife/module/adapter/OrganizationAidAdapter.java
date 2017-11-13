package lilun.com.pensionlife.module.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.base.QuickAdapter;
import lilun.com.pensionlife.module.bean.OrganizationAid;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.widget.SearchTitleBar;
import lilun.com.pensionlife.widget.image_loader.ImageLoaderUtil;

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
        TextView tvTitle = help.getView(R.id.tv_sophisticated);
//        UIUtils.setBold(tvTitle);
        tvTitle.setText(aid.getTitle());

        //是否有图片需要加载
        if (layoutType != SearchTitleBar.LayoutType.NULL) {
            ImageLoaderUtil.instance().loadImage(aid.getImage().get(0), R.drawable.icon_def, help.getView(R.id.iv_icon));
        }


        //是否显示补贴和创建者
        if (layoutType != SearchTitleBar.LayoutType.BIG) {
            //创建者
            help.setText(R.id.tv_creatorName, aid.getCreatorName());
        }

        //补贴
        TextView tvItemPrice = help.getView(R.id.tv_aid_price);
        tvItemPrice.setText(aid.getPrice() + "元补贴");

        //参与者和时间
        TextView time_joinerCount = help.getView(R.id.tv_time_joinCount);
        int partnerCount = 0;
        List<String> partnerList = aid.getPartnerList();
        if (partnerList != null) {
            partnerCount = partnerList.size();
        }
        time_joinerCount.setText("/" + StringUtils.IOS2ToUTC(aid.getCreatedAt(), 4) + "/" + partnerCount + "人参与");

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
