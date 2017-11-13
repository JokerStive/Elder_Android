package lilun.com.pensionlife.module.adapter;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.base.QuickAdapter;
import lilun.com.pensionlife.module.bean.OrganizationProduct;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.widget.CustomRatingBar;
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
        CustomRatingBar ratingBar = help.getView(R.id.rb_score);
        ratingBar.setCountSelected(product.getRank());

        String contextType = product.getContextType();
        if (!TextUtils.isEmpty(contextType) && contextType.equals("2")) {
            help.setText(R.id.tv_product_title_extra, product.getSubTitle());
        } else {
            help.setText(R.id.tv_product_title_extra, StringUtils.filterNull(product.getContext()));
        }

        Double price = product.getPrice();
        String formatPrice = StringUtils.formatPrice(price);
        String topPriceResult = !TextUtils.isEmpty(formatPrice) ?"¥"+ formatPrice + product.getUnit() : StringUtils.formatPriceToFree(price);
        help.setText(R.id.tv_product_title, product.getTitle())
                .setText(R.id.tv_score, (double) product.getRank() + "")
                .setText(R.id.tv_product_price, topPriceResult);


        help.setOnClickListener(R.id.ll_bg, v -> {
            if (listener != null) {
                listener.onItemClick(product);
            }
        });

        String iconUrl =product.getImage().get(0);
        ImageLoaderUtil.instance().loadImage(iconUrl, R.drawable.icon_def, help.getView(R.id.iv_icon));
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(OrganizationProduct product);
    }
}
