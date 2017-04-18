package lilun.com.pension.module.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;
import java.util.Map;

import lilun.com.pension.R;
import lilun.com.pension.app.Config;
import lilun.com.pension.base.QuickAdapter;
import lilun.com.pension.module.bean.Contact;

/**
 * 用户预约登记信息adapter
 *
 * @author yk
 *         create at 2017/4/5 13:26
 *         email : yk_developer@163.com
 */
public class ServiceUserInfoAdapter extends QuickAdapter<Contact> {
    private OnItemClickListener listener;

    public ServiceUserInfoAdapter(List<Contact> data) {
        super(R.layout.item_reservation_info, data);
        data.get(0).setDefault(true);
    }

    public ServiceUserInfoAdapter(int layoutResId, List<Contact> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Contact info) {
        TextView tvSetDef = helper.getView(R.id.tv_set_def);
        tvSetDef.setSelected(info.isDefault());

        Map<String, String> extend = info.getExtend();

        if (extend != null && info.getCategoryId().equals(Config.agency_product_categoryId)) {
            helper.setText(R.id.tv_health_status, info.getExtend().get("healthyStatus"))
                    .setText(R.id.tv_health_desc, info.getExtend().get("healthyDescription"));
        } else {
            helper.setText(R.id.tv_health_desc, info.getAddress());
        }

        helper.setText(R.id.tv_name, info.getName())
                .setText(R.id.tv_phone, info.getMobile())
                .setOnClickListener(R.id.tv_edit, v -> {
                    if (listener != null) {
                        listener.onEdit(info);
                    }
                })
                .setOnClickListener(R.id.tv_delete, v -> {
                    if (listener != null) {
                        listener.onDelete();
                    }
                })

                .setOnClickListener(R.id.tv_set_def, v -> {
                    if (listener != null && !info.isDefault()) {
                        listener.onSetDefault(info.getId());
                    }
                })

        ;
    }


    public void setDefault(String contactId) {
        for (Contact contact : getData()) {
            contact.setDefault(contactId.endsWith(contact.getId()));
        }
        notifyDataChanged();
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onDelete();

        void onEdit(Contact contact);

        void onSetDefault(String contactId);
    }
}
