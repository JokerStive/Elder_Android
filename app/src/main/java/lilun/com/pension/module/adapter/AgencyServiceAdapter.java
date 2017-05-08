package lilun.com.pension.module.adapter;

import android.widget.RatingBar;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pension.R;
import lilun.com.pension.app.App;
import lilun.com.pension.app.IconUrl;
import lilun.com.pension.base.QuickAdapter;
import lilun.com.pension.module.bean.OrganizationProduct;
import lilun.com.pension.module.utils.StringUtils;
import lilun.com.pension.module.utils.UIUtils;
import lilun.com.pension.widget.image_loader.ImageLoaderUtil;

/**
 * 提供提供的服务adapter
 *
 * @author yk
 *         create at 2017/2/13 11:14
 *         email : yk_developer@163.com
 */
public class AgencyServiceAdapter extends QuickAdapter<OrganizationProduct> {
    private OnItemClickListener listener;

    public AgencyServiceAdapter(List<OrganizationProduct> data, int itemRes) {
        super(itemRes, data);
    }

    @Override
    protected void convert(BaseViewHolder help, OrganizationProduct product) {
        RatingBar ratingBar = help.getView(R.id.rb_bar);
        ratingBar.setRating(product.getScore());

        UIUtils.setBold(help.getView(R.id.tv_item_title));
        help.setText(R.id.tv_item_title, product.getTitle())
                .setText(R.id.tv_mobile, String.format(App.context.getString(R.string.price_format), product.getPrice()));
        help.setOnClickListener(R.id.ll_bg, v -> {
            if (listener != null) {
                listener.onItemClick(product);
            }
        });

        String iconUrl = IconUrl.moduleIconUrl(IconUrl.OrganizationProducts, product.getId(), StringUtils.getFirstIconNameFromIcon(product.getImages()));
        ImageLoaderUtil.instance().loadImage(iconUrl, R.drawable.icon_def, help.getView(R.id.iv_icon));
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(OrganizationProduct product);
    }
}
