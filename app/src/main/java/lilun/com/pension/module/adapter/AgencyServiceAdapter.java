package lilun.com.pension.module.adapter;

import android.widget.RatingBar;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pension.R;
import lilun.com.pension.app.App;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.base.QuickAdapter;
import lilun.com.pension.module.bean.OrganizationProduct;
import lilun.com.pension.module.utils.UIUtils;

/**
 * 提供提供的服务adapter
 *
 * @author yk
 *         create at 2017/2/13 11:14
 *         email : yk_developer@163.com
 */
public class AgencyServiceAdapter extends QuickAdapter<OrganizationProduct> {
    private BaseFragment fragment;

    public AgencyServiceAdapter(BaseFragment fragment, List<OrganizationProduct> data) {
        super(R.layout.item_agency_service, data);
        this.fragment = fragment;
    }

    @Override
    protected void convert(BaseViewHolder help, OrganizationProduct product) {
        RatingBar ratingBar = help.getView(R.id.rb_bar);
        ratingBar.setRating(product.getScore());

        UIUtils.setBold(help.getView(R.id.tv_item_title));
        help.setText(R.id.tv_item_title, product.getTitle())
                .setText(R.id.tv_price, String.format(App.context.getString(R.string.price_format), product.getPrice()));
    }
}
