package lilun.com.pensionlife.module.utils.dynamic;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.module.bean.Contact;

/**
 * 联系人通用
 */
public class ContactView {

    private Activity activity;
    private TextView tvContactName;
    private TextView tvContactMobile;
    private TextView tvContactAddress;
    private RelativeLayout rlExtendContainer;
    private DynamicLayoutManager manager;
    private Contact contact;
    private boolean isOnlyShow;

    public ContactView(Activity activity) {
        this.activity = activity;
    }


    public void setOnlyShow(boolean isOnlyShow) {
        this.isOnlyShow = isOnlyShow;
    }

    public View getView() {
        LinearLayout container = (LinearLayout) LayoutInflater.from(App.context).inflate(R.layout.layout_contact, null);
        tvContactName = (TextView) container.findViewById(R.id.tv_contact_name);
        tvContactMobile = (TextView) container.findViewById(R.id.tv_contact_mobile);
        tvContactAddress = (TextView) container.findViewById(R.id.tv_contact_address);
        rlExtendContainer = (RelativeLayout) container.findViewById(R.id.rl_extend_container);


        return container;

    }

    public void reDraw(Contact contact, JSONObject template) {
        if (contact == null) {
            throw new IllegalStateException("contact can't be null");
        }
        this.contact = contact;
        tvContactName.setText(contact.getName());
        tvContactMobile.setText(contact.getMobile());
        tvContactAddress.setText(contact.getAddress());

        if (template == null) {
            return;
        }


        JSONObject extend = contact.getExtend();
        if (manager == null) {
            manager = new DynamicLayoutManager.Builder()
                    .setContext(activity)
                    .template(template)
                    .isOnlyShow(isOnlyShow)
                    .build();
        }
        LinearLayout dynamicLayout = manager.createDynamicLayout(extend);
        rlExtendContainer.removeAllViews();
        rlExtendContainer.addView(dynamicLayout);


    }


    public Contact getFinalContactData() {
        if (manager != null) {
            JSONObject extend = manager.getFinallyData();
            if (extend == null) {
                return null;
            }
            contact.setExtend(extend);
        }
        return contact;
    }
}
