package lilun.com.pension.module.adapter;

import com.chad.library.adapter.base.BaseViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import lilun.com.pension.R;
import lilun.com.pension.app.IconUrl;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.base.QuickAdapter;
import lilun.com.pension.module.bean.Information;
import lilun.com.pension.module.utils.StringUtils;
import lilun.com.pension.widget.image_loader.ImageLoaderUtil;

/**
 * 分类模块adapter
 *
 * @author yk
 *         create at 2017/2/13 11:14
 *         email : yk_developer@163.com
 */
public class HealthServiceAdapter extends QuickAdapter<Information> {
    private BaseFragment fragment;
    private OnItemClickListener listener;
    ContextItem item = new ContextItem();

    public HealthServiceAdapter(BaseFragment fragment, List<Information> data) {
        super(R.layout.item_module_second, data);
        this.fragment = fragment;
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
        help.setText(R.id.tv_item_title, info.getTitle())
                .setText(R.id.tv_item_address, item.getAddress())
                .setOnClickListener(R.id.ll_module_background, v -> {
                    if (listener != null) {
                        listener.onItemClick(info);
                    }
                });

        String iconUrl = IconUrl.moduleIconUrl(IconUrl.OrganizationInformations,info.getId(), StringUtils.getFirstIconNameFromIcon(info.getImage()));
        ImageLoaderUtil.instance().loadImage(iconUrl,R.drawable.icon_def,help.getView(R.id.iv_icon));
//        Glide.with(fragment)
//                .load(IconUrl.moduleIconUrl(IconUrl.OrganizationInformations,info.getId(), StringUtils.getFirstIconNameFromIcon(info.getImage()))
//                .placeholder(R.drawable.icon_def)
//                .error(R.drawable.icon_def)
//                .into((ImageView) help.getView(R.id.iv_icon));

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Information activity);
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
