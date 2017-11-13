package lilun.com.pensionlife.module.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.base.QuickAdapter;
import lilun.com.pensionlife.module.bean.OrganizationProductCategory;
import lilun.com.pensionlife.module.utils.StringUtils;

/**
 * 产品分类模块adapter
 *
 * @author yk
 *         create at 2017/2/13 11:14
 *         email : yk_developer@163.com
 */
public class ProductCategoryAdapter extends QuickAdapter<OrganizationProductCategory> {
    private BaseFragment fragment;
    private int color;
//    private OnItemClickListener listener;

    public ProductCategoryAdapter(BaseFragment fragment, List<OrganizationProductCategory> data, int color) {
        super(R.layout.item_elder_module, data);
        this.fragment = fragment;
        this.color = color;
    }

    @Override
    protected void convert(BaseViewHolder helper, OrganizationProductCategory OrganizationProductCategory) {
        helper.setText(R.id.tv_module_name, OrganizationProductCategory.getName())
                .setBackgroundColor(R.id.ll_module_background, color);
//                .setOnClickListener(R.id.ll_module_background, v -> {
//                    if (listener != null) {
//                        listener.onItemClick(OrganizationProductCategory);
//                    }
//                });


        if (OrganizationProductCategory.getIcon() != null) {
//            String firstIconName = StringUtils.getFirstIconNameFromIcon(OrganizationProductCategory.getIcon());
            String iconUrl = StringUtils.getFirstIcon(OrganizationProductCategory.getIcon());
            Glide.with(fragment).load(iconUrl).into((ImageView) helper.getView(R.id.iv_module_icon));
        }
    }

//    public void setOnItemClickListener(OnItemClickListener listener) {
//        this.listener = listener;
//    }
//
//    public interface OnItemClickListener {
//        void onItemClick(OrganizationProductCategory OrganizationProductCategory);
//    }


}
