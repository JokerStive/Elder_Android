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
        String organizationId = organization.getId();
        String removeSpecialSuffix = StringUtils.removeSpecialSuffix(organizationId);
        String name = removeSpecialSuffix.substring(removeSpecialSuffix.lastIndexOf("/")+1);
        help.setText(R.id.tv_organizationEdu_title, name);


        String iconUrl = IconUrl.moduleIconUrl(IconUrl.Organizations, removeSpecialSuffix, StringUtils.getFirstIconNameFromIcon(organization.getIcon()),"icon");
        ImageLoaderUtil.instance().loadImage(iconUrl, R.drawable.icon_def, help.getView(R.id.iv_organizationEdu_icon));

    }


}
