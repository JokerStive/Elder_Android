package lilun.com.pensionlife.module.adapter;

import android.widget.RatingBar;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.IconUrl;
import lilun.com.pensionlife.base.QuickAdapter;
import lilun.com.pensionlife.module.bean.Organization;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.module.utils.UIUtils;
import lilun.com.pensionlife.widget.SearchTitleBar;
import lilun.com.pensionlife.widget.image_loader.ImageLoaderUtil;

/**
 * 养老机构adapter
 *
 * @author yk
 *         create at 2017/2/21 18:43
 *         email : yk_developer@163.com
 */
public class AgencyAdapter extends QuickAdapter<Organization> {
    private SearchTitleBar.LayoutType layoutType;
    private OnItemClickListener listener;

    public AgencyAdapter(List<Organization>  data,int itemRes,  SearchTitleBar.LayoutType layoutType) {
        super(itemRes, data);
        this.layoutType = layoutType;
    }

    @Override
    protected void convert(BaseViewHolder help, Organization organization) {
        Organization.DescriptionBean description = organization.getDescription();
        RatingBar ratingBar = help.getView(R.id.rb_score);
        ratingBar.setRating(description.getRanking());

        UIUtils.setBold(help.getView(R.id.tv_product_title));
        help.setText(R.id.tv_product_title, organization.getName())
                .setText(R.id.tv_item_address, description.getAdress());

        help.setOnClickListener(R.id.ll_bg,v -> {
            if (listener!=null){
                listener.onItemClick(organization);
            }
        });

        ImageLoaderUtil.instance().loadImage(IconUrl.moduleIconUrl(IconUrl.Organizations,organization.getId(), StringUtils.getFirstIconNameFromIcon(organization.getIcon()),"icon"), R.drawable.icon_def, help.getView(R.id.iv_icon));
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Organization agency);
    }
}
