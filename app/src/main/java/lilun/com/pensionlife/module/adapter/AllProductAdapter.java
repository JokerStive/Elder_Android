package lilun.com.pensionlife.module.adapter;

import com.chad.library.adapter.base.BaseViewHolder;

import java.text.DecimalFormat;
import java.util.List;

import lilun.com.pensionlife.R;
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
public class AllProductAdapter extends QuickAdapter<OrganizationProduct> {
    private OnItemClickListener listener;

    public AllProductAdapter(List<OrganizationProduct> data) {
        super(R.layout.item_all_product, data);
    }

    @Override
    protected void convert(BaseViewHolder help, OrganizationProduct product) {
        help.setOnClickListener(R.id.ll_bg, v -> {
            if (listener != null) {
                listener.onItemClick(product);
            }
        });

        help.setText(R.id.tv_product_title,product.getName())
                .setText(R.id.tv_product_title_extra,product.getName())
        .setText(R.id.tv_product_price,new DecimalFormat("######0.00").format(product.getPrice()) + "");

        String iconUrl = StringUtils.getFirstIcon(product.getImage());
        ImageLoaderUtil.instance().loadImage(iconUrl, R.drawable.icon_def, help.getView(R.id.iv_icon));
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(OrganizationProduct product);
    }
}
