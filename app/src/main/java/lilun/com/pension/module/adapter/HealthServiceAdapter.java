package lilun.com.pension.module.adapter;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pension.R;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.base.QuickAdapter;
import lilun.com.pension.module.bean.HealtheaProduct;
import lilun.com.pension.module.bean.OrganizationActivity;

import static lilun.com.pension.R.color.activity;

/**
 * 分类模块adapter
 *
 * @author yk
 *         create at 2017/2/13 11:14
 *         email : yk_developer@163.com
 */
public class HealthServiceAdapter extends QuickAdapter<HealtheaProduct> {
    private BaseFragment fragment;
    private OnItemClickListener listener;

    public HealthServiceAdapter(BaseFragment fragment, List<HealtheaProduct> data) {
        super(R.layout.item_module_second, data);
        this.fragment = fragment;
    }

    @Override
    protected void convert(BaseViewHolder help, HealtheaProduct product) {
        help.setText(R.id.tv_item_title, product.getName())
                .setText(R.id.tv_item_address, product.getAddress())
                .setOnClickListener(R.id.ll_module_background, v -> {
                    if (listener != null) {
                        listener.onItemClick(product);
                    }
                });


//        Glide.with(fragment).load(IconUrl.organizationAid(activity.getId())).into((ImageView) help.getView(R.id.iv_aid_icon));


    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(HealtheaProduct activity);
    }
}
