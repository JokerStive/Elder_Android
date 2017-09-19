package lilun.com.pensionlife.module.adapter;

import com.chad.library.adapter.base.BaseViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.IconUrl;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.base.QuickAdapter;
import lilun.com.pensionlife.module.bean.Information;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.widget.image_loader.ImageLoaderUtil;

/**
 * 分类模块adapter
 *
 * @author yk
 *         create at 2017/2/13 11:14
 *         email : yk_developer@163.com
 */
public class HealthServiceAdapter extends QuickAdapter<Information> {
    //    private OnItemClickListener listener;
    ContextItem item = new ContextItem();

    public HealthServiceAdapter(BaseFragment fragment, List<Information> data) {
        super(R.layout.item_health, data);
    }

    @Override
    protected void convert(BaseViewHolder help, Information info) {

        if (5 == info.getContextType()) {
            try {
                JSONObject object = new JSONObject(info.getContext());
                item.setAddress(object.getString("address"));
                item.setMobile(object.getString("mobile"));
                item.setDescription(object.getString("description"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        help.setText(R.id.tv_item_title, info.getTitle());
        help.setText(R.id.tv_item_time, StringUtils.up2thisTime(info.getCreatedAt()));
        if (help.getAdapterPosition() == 0 && null != StringUtils.getFirstIconNameFromIcon(info.getImage())) {
            help.setVisible(R.id.iv_icon, true);
            String iconUrl = IconUrl.moduleIconUrl(IconUrl.OrganizationInformations, info.getId(), StringUtils.getFirstIconNameFromIcon(info.getImage()));
            ImageLoaderUtil.instance().loadImage(iconUrl, R.drawable.icon_def, help.getView(R.id.iv_icon));
        } else {
            help.setVisible(R.id.iv_icon, false);
        }

    }


    class ContextItem {
        String address;
        String mobile;
        String description;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
