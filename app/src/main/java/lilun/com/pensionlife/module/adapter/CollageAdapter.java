package lilun.com.pensionlife.module.adapter;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.IconUrl;
import lilun.com.pensionlife.base.QuickAdapter;
import lilun.com.pensionlife.module.bean.OrganizationEdu;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.widget.image_loader.ImageLoaderUtil;

/**
 * 大学adapter
 */
public class CollageAdapter extends QuickAdapter<OrganizationEdu> {

    public CollageAdapter(List<OrganizationEdu> data) {
        super(R.layout.item_collage, data);
    }


    @Override
    protected void convert(BaseViewHolder help, OrganizationEdu organizationEdu) {
        help.setText(R.id.tv_organizationEdu_title, organizationEdu.getName());


        String iconUrl = IconUrl.moduleIconUrl(IconUrl.OrganizationEdus, organizationEdu.getId(), StringUtils.getFirstIconNameFromIcon(organizationEdu.getImage()));
        ImageLoaderUtil.instance().loadImage(iconUrl, R.drawable.icon_def, help.getView(R.id.iv_organizationEdu_icon));

    }


}
