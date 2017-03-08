package lilun.com.pension.module.adapter;

import android.widget.RatingBar;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pension.R;
import lilun.com.pension.app.App;
import lilun.com.pension.app.IconUrl;
import lilun.com.pension.base.QuickAdapter;
import lilun.com.pension.module.bean.OrganizationProduct;
import lilun.com.pension.module.bean.ProductOrder;
import lilun.com.pension.widget.image_loader.ImageLoaderUtil;

/**
 * 个人订单adapter
 *
 * @author yk
 *         create at 2017/3/6 11:10
 *         email : yk_developer@163.com
 */
public class PersonalOrderAdapter extends QuickAdapter<ProductOrder> {

    private OnItemClickListener listener;

    public PersonalOrderAdapter(List<ProductOrder> data) {
        super(R.layout.item_personal_order, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ProductOrder order) {
        OrganizationProduct product = order.getProduct();
        if (product != null) {
            RatingBar rb = helper.getView(R.id.rb_product);
            rb.setRating(product.getScore());

            ImageLoaderUtil.instance().loadImage(IconUrl.organizationProduct(product.getId(), null), R.drawable.icon_def, helper.getView(R.id.iv_product_icon));
            helper.setText(R.id.tv_product_name, product.getName())
                    .setText(R.id.tv_product_price, String.format(App.context.getResources().getString(R.string.format_price), product.getPrice()))
                    .setOnClickListener(R.id.rl_item,v ->{
                       if (listener!=null){
                           listener.onItemClick(order);
                       }
                    });

        }
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(ProductOrder order);
    }
}