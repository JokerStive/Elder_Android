package lilun.com.pension.module.adapter;

import android.widget.RatingBar;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pension.R;
import lilun.com.pension.app.App;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.base.QuickAdapter;
import lilun.com.pension.module.bean.OrganizationProduct;

/**
 * 分类模块adapter
 *
 * @author yk
 *         create at 2017/2/13 11:14
 *         email : yk_developer@163.com
 */
public class ProductorAdapter extends QuickAdapter<OrganizationProduct> {
    private BaseFragment fragment;

    public ProductorAdapter(BaseFragment fragment, List<OrganizationProduct> data) {
        super(R.layout.item_productor, data);
        this.fragment = fragment;
    }

    @Override
    protected void convert(BaseViewHolder help, OrganizationProduct product) {
        RatingBar ratingBar = help.getView(R.id.rb_bar);
        ratingBar.setRating(product.getScore());
        help.setText(R.id.tv_title, product.getName())
                .setText(R.id.tv_mobile, String.format(App.context.getString(R.string.price_format), product.getPrice()))
        ;
    }
}
