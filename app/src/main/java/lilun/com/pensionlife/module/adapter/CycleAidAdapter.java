package lilun.com.pensionlife.module.adapter;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.IconUrl;
import lilun.com.pensionlife.app.User;
import lilun.com.pensionlife.base.QuickAdapter;
import lilun.com.pensionlife.module.bean.OrganizationAid;
import lilun.com.pensionlife.widget.image_loader.ImageLoaderUtil;

/**
 * Created by Admin on 2017/4/19.
 */
public class CycleAidAdapter extends QuickAdapter<OrganizationAid> {


    public CycleAidAdapter(List<OrganizationAid> data) {
        super(R.layout.item_aid_cycle, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, OrganizationAid aid) {
        String creatorId = aid.getCreatorId();
        String creatorName = aid.getCreatorName();
        if (User.getUserId().equals(creatorId)) {
            creatorName = "我";
        }

        holder.setText(R.id.tv_creator_name, creatorName);
        holder.setText(R.id.tv_title, aid.getTitle());

        ImageLoaderUtil.instance().loadImage(IconUrl.moduleIconUrl(IconUrl.Accounts, aid.getCreatorId(), null), R.drawable.icon_def, holder.getView(R.id.iv_icon));
    }
}
