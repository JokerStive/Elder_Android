package lilun.com.pensionlife.module.adapter;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.IconUrl;
import lilun.com.pensionlife.base.QuickAdapter;
import lilun.com.pensionlife.module.bean.Organization;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.widget.image_loader.ImageLoaderUtil;

/**
 * 大学adapter
 */
public class CollageAdapter extends QuickAdapter<Organization> {

    public CollageAdapter(List<Organization> data) {
        super(R.layout.item_collage, data);
    }


    @Override
    protected void convert(BaseViewHolder help, Organization organization) {
        help.setText(R.id.tv_organizationEdu_title, organization.getName());


        String iconUrl = IconUrl.moduleIconUrl(IconUrl.Organizations, organization.getId(), StringUtils.getFirstIconNameFromIcon(organization.getIcon()));
        ImageLoaderUtil.instance().loadImage(iconUrl, R.drawable.icon_def, help.getView(R.id.iv_organizationEdu_icon));

    }


}
