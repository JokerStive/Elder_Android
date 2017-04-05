package lilun.com.pension.module.adapter;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pension.R;
import lilun.com.pension.base.QuickAdapter;
import lilun.com.pension.module.bean.ServiceUserInformation;
import lilun.com.pension.module.utils.StringUtils;

/**
 * 用户预约登记信息adapter
 *
 * @author yk
 *         create at 2017/4/5 13:26
 *         email : yk_developer@163.com
 */
public class ServiceUserInfoAdapter extends QuickAdapter<ServiceUserInformation> {
    private OnItemClickListener listener;

    public ServiceUserInfoAdapter(List<ServiceUserInformation> data) {
        super(R.layout.item_reservation_info, data);
    }

    public ServiceUserInfoAdapter(int layoutResId, List<ServiceUserInformation> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ServiceUserInformation info) {
        helper.setText(R.id.tv_name, info.getName())
                .setText(R.id.tv_health_status, info.getHealthy())
                .setText(R.id.tv_health_desc, info.getHealthyContent())
                .setText(R.id.tv_time, StringUtils.IOS2ToUTC(info.getCreatedAt(), 4))
                .setOnClickListener(R.id.tv_edit, v -> {
                    if (listener != null) {
                        listener.onEdit();
                    }
                })
                .setOnClickListener(R.id.tv_delete, v -> {
                    if (listener != null) {
                        listener.onDelete();
                    }
                });
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onDelete();

        void onEdit();
    }
}
