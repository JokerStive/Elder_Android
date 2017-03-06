package lilun.com.pension.module.adapter;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.base.QuickAdapter;
import lilun.com.pension.module.bean.ProductOrder;

/**
 * 分类模块adapter
 *
 * @author yk
 *         create at 2017/2/13 11:14
 *         email : yk_developer@163.com
 */
public class ProductOrderAdapter extends QuickAdapter<ProductOrder> {
    private BaseFragment fragment;
    private String icon;

    public ProductOrderAdapter(List<ProductOrder> data, int layout) {
        super(layout, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, ProductOrder productOrder) {

    }
}
