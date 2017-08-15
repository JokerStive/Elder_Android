package lilun.com.pensionlife.module.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.base.QuickAdapter;
import lilun.com.pensionlife.module.bean.Contact;

/**
 * 用户预约登记信息adapter
 *
 * @author yk
 *         create at 2017/4/5 13:26
 *         email : yk_developer@163.com
 */
public class ContactListAdapter extends QuickAdapter<Contact> {
    private OnItemClickListener listener;

    public ContactListAdapter(List<Contact> data) {
        super(R.layout.item_contact, data);
        init();
    }

    private void init() {
        for (Contact contact : mData) {
            if (contact.getIndex() == 1) {
                contact.setDefault(true);
            }
        }
    }

//    public ContactListAdapter(int layoutResId, List<Contact> data) {
//        super(layoutResId, data);
//    }

    @Override
    protected void convert(BaseViewHolder helper, Contact info) {
        TextView tvSetDef = helper.getView(R.id.tv_contact_default);
        tvSetDef.setSelected(info.isDefault());

        helper.setText(R.id.tv_contact_name, info.getName())
                .setText(R.id.tv_contact_mobile, info.getMobile())
                .setText(R.id.tv_contact_address, info.getAddress())
                .setOnClickListener(R.id.tv_contact_edit, v -> {
                    if (listener != null) {
                        listener.onEdit(info);
                    }
                })
                .setOnClickListener(R.id.tv_contact_delete, v -> {
                    if (listener != null) {
                        listener.onDelete(info);
//                        if (mData.size() > 1) {
//                        } else {
//                            ToastHelper.get().showWareShort("再删就没有了哟");
//                        }
                    }
                })

                .setOnClickListener(R.id.tv_contact_default, v -> {
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
        void onDelete(Contact contact);

        void onEdit(Contact contact);

        void onSetDefault(String contactId);
    }
}
