package lilun.com.pension.module.adapter;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pension.R;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.base.QuickAdapter;
import lilun.com.pension.module.bean.OrganizationActivity;

/**
 * 展示互助列表的adapter
 *
 * @author yk
 *         create at 2017/2/13 11:27
 *         email : yk_developer@163.com
 */
public class OrganizationActivityAdapter extends QuickAdapter<OrganizationActivity> {
    private BaseFragment fragment;
    private OnItemClickListener listener;

    public OrganizationActivityAdapter(BaseFragment fragment, List<OrganizationActivity> data) {
        super(R.layout.item_module_second, data);
        this.fragment = fragment;
    }

    @Override
    protected void convert(BaseViewHolder help, OrganizationActivity activity) {
        help.setText(R.id.tv_item_title, activity.getTitle())
                .setText(R.id.tv_item_time, activity.getCreatedAt())
                .setText(R.id.tv_item_address, activity.getAddress())
                .setOnClickListener(R.id.ll_module_background, v -> {
                    if (listener != null) {
                        listener.onItemClick(activity);
                    }
                });


//        Glide.with(fragment).load(IconUrl.organizationAid(activity.getId())).into((ImageView) help.getView(R.id.iv_aid_icon));


    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(OrganizationActivity activity);
    }
}
