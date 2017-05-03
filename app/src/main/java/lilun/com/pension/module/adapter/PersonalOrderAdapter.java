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
import lilun.com.pension.module.utils.StringUtils;
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


//    @Override
//    public int getItemCount() {
//        return Integer.MAX_VALUE;
//    }

    @Override
    protected void convert(BaseViewHolder helper, ProductOrder order) {
        OrganizationProduct product = order.getProduct();
        if (product != null) {
            RatingBar rb = helper.getView(R.id.rb_product);
            rb.setRating(product.getScore());

            ImageLoaderUtil.instance().loadImage(IconUrl.moduleIconUrl(IconUrl.OrganizationProducts,product.getId(), null), R.drawable.icon_def, helper.getView(R.id.iv_product_icon));

            String agencyName = StringUtils.getOrganizationNameFromId(StringUtils.removeSpecialSuffix(product.getOrganizationId()));
            helper.setText(R.id.tv_product_name, product.getName())
                    .setText(R.id.tv_provider_name,agencyName)
                    .setText(R.id.tv_reservation_time,"预约时间:"+StringUtils.IOS2ToUTC(order.getRegisterDate(),0)+"  "+StringUtils.IOS2ToUTC(order.getRegisterDate(),1))
                    .setText(R.id.tv_product_price, String.format(App.context.getResources().getString(R.string.format_price), product.getPrice()))
                    .setVisible(R.id.tv_rank,order.getStatus().equals("done"))
                    .setOnClickListener(R.id.rl_item,v ->{
                       if (listener!=null){
                           listener.onItemClick(order);
                       }
                    })

                    .setOnClickListener(R.id.tv_rank,v ->{
                        if (listener!=null){
                            listener.onRank(order.getProductId());
                        }
                    })

            ;

        }
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(ProductOrder order);
        void onRank(String productId);
    }
}
