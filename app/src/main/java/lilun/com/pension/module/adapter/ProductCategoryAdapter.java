package lilun.com.pension.module.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pension.R;
import lilun.com.pension.app.IconUrl;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.base.QuickAdapter;
import lilun.com.pension.module.bean.ProductCategory;
import lilun.com.pension.module.utils.StringUtils;

/**
 * 产品分类模块adapter
 *
 * @author yk
 *         create at 2017/2/13 11:14
 *         email : yk_developer@163.com
 */
public class ProductCategoryAdapter extends QuickAdapter<ProductCategory> {
    private BaseFragment fragment;
    private int color;
    private OnItemClickListener listener;

    public ProductCategoryAdapter(BaseFragment fragment, List<ProductCategory> data,int color) {
        super(R.layout.item_elder_module, data);
        this.fragment = fragment;
        this.color = color;
    }

    @Override
    protected void convert(BaseViewHolder helper, ProductCategory productCategory) {
        helper.setText(R.id.tv_module_name, productCategory.getName())
                .setBackgroundColor(R.id.ll_module_background, color)
                .setOnClickListener(R.id.ll_module_background, v -> {
                    if (listener != null) {
                        listener.onItemClick(productCategory);
                    }
                });


        if (productCategory.getIcon()!=null) {
            String firstIconName = StringUtils.getFirstIconNameFromIcon(productCategory.getIcon());
            String iconUrl = IconUrl.productCategory(productCategory.getId(),firstIconName);
            Glide.with(fragment).load(iconUrl).into((ImageView) helper.getView(R.id.iv_module_icon));
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(ProductCategory productCategory);
    }



}
