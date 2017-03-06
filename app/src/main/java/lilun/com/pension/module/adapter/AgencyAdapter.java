package lilun.com.pension.module.adapter;

import android.widget.RatingBar;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pension.R;
import lilun.com.pension.app.IconUrl;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.base.QuickAdapter;
import lilun.com.pension.module.bean.Organization;
import lilun.com.pension.module.utils.UIUtils;
import lilun.com.pension.widget.image_loader.ImageLoaderUtil;

/**
 * 养老机构adapter
 *
 * @author yk
 *         create at 2017/2/21 18:43
 *         email : yk_developer@163.com
 */
public class AgencyAdapter extends QuickAdapter<Organization> {
    private BaseFragment fragment;
    private OnItemClickListener listener;

    public AgencyAdapter(BaseFragment fragment, List<Organization> data) {
        super(R.layout.item_agency, data);
        this.fragment = fragment;
    }

    @Override
    protected void convert(BaseViewHolder help, Organization organization) {
        Organization.DescriptionBean description = organization.getDescription();
        RatingBar ratingBar = help.getView(R.id.rb_bar);
        ratingBar.setRating(description.getRanking());

        UIUtils.setBold(help.getView(R.id.tv_item_title));
        help.setText(R.id.tv_item_title, organization.getName())
                .setText(R.id.tv_item_address, description.getAdress());

        help.setOnClickListener(R.id.ll_bg,v -> {
            if (listener!=null){
                listener.onItemClick(organization);
            }
        });

        ImageLoaderUtil.instance().loadImage(IconUrl.organization(organization.getId(), null), R.drawable.icon_def, help.getView(R.id.iv_icon));
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Organization agency);
    }
}
