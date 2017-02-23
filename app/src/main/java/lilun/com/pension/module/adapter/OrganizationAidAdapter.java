package lilun.com.pension.module.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pension.R;
import lilun.com.pension.app.App;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.base.QuickAdapter;
import lilun.com.pension.module.bean.OrganizationAid;
import lilun.com.pension.module.utils.UIUtils;

/**
 * 展示互助列表的adapter
 *
 * @author yk
 *         create at 2017/2/13 11:27
 *         email : yk_developer@163.com
 */
public class OrganizationAidAdapter extends QuickAdapter<OrganizationAid> {
    private BaseFragment fragment;
    private OnItemClickListener listener;

    public OrganizationAidAdapter(BaseFragment fragment, List<OrganizationAid> data) {
        super(R.layout.item_module_second, data);
        this.fragment = fragment;
    }

    @Override
    protected void convert(BaseViewHolder help, OrganizationAid aid) {

        //标题加粗
        TextView tvTitle = help.getView(R.id.tv_item_title);
        UIUtils.setBold(tvTitle);
        tvTitle.setText(aid.getTitle());

//        Glide.with(fragment).load(IconUrl.organizationAid(aid.getId())).into((ImageView) help.getView(R.id.iv_aid_icon));

        //是否显示补贴
        TextView tvItemPrice = help.getView(R.id.tv_item_time);
        tvItemPrice.setVisibility(aid.getPrice() == 0 ? View.GONE : View.VISIBLE);
        if (tvItemPrice.getVisibility() == View.VISIBLE) {
            tvItemPrice.setText(String.format(App.context.getString(R.string.help_price), aid.getPrice()));
        }


        //是否显示地址
        TextView tvItemAddress = help.getView(R.id.tv_item_address);
        tvItemAddress.setVisibility(aid.getKind() == 0 ? View.GONE : View.VISIBLE);
        if (tvItemAddress.getVisibility() == View.VISIBLE) {
            tvItemAddress.setText(aid.getAddress());
        }

        help.setOnClickListener(R.id.ll_module_background, v -> {
            if (listener != null) {
                listener.onItemClick(aid);
            }
        });

    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(OrganizationAid aid);
    }
}
