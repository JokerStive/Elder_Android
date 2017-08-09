package lilun.com.pensionlife.module.adapter;

import android.widget.RatingBar;

import com.chad.library.adapter.base.BaseViewHolder;

import java.text.DecimalFormat;
import java.util.List;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.IconUrl;
import lilun.com.pensionlife.base.QuickAdapter;
import lilun.com.pensionlife.module.bean.OrganizationProduct;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.widget.image_loader.ImageLoaderUtil;

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
        RatingBar ratingBar = help.getView(R.id.rb_score);
        ratingBar.setRating(product.getScore() == 0 ? 5 : product.getScore());

        help.setText(R.id.tv_product_title, product.getTitle())
                .setText(R.id.tv_product_title_extra, StringUtils.filterNull(product.getContext()))
                .setText(R.id.tv_score, product.getScore() == 0 ? "5.0" : (double)product.getScore()+"")
                .setText(R.id.tv_product_price,   new DecimalFormat("######0.00").format(product.getPrice())+"");
        help.setOnClickListener(R.id.ll_bg, v -> {
            if (listener != null) {
                listener.onItemClick(product);
            }
        });

        String iconUrl = IconUrl.moduleIconUrl(IconUrl.OrganizationProducts, product.getId(), StringUtils.getFirstIconNameFromIcon(product.getImage()));
        ImageLoaderUtil.instance().loadImage(iconUrl, R.drawable.icon_def, help.getView(R.id.iv_icon));
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(OrganizationProduct product);
    }
}
