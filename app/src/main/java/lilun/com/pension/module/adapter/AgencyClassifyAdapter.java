package lilun.com.pension.module.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pension.R;
import lilun.com.pension.app.IconUrl;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.base.QuickAdapter;
import lilun.com.pension.module.bean.Organization;
import lilun.com.pension.module.utils.StringUtils;

/**
 * 分类模块adapter
 *
 * @author yk
 *         create at 2017/2/13 11:14
 *         email : yk_developer@163.com
 */
public class AgencyClassifyAdapter extends QuickAdapter<Organization> {
    private BaseFragment fragment;
    private OnItemClickListener listener;


    public AgencyClassifyAdapter(BaseFragment fragment, List<Organization> data) {
        super(R.layout.item_elder_module, data);
        this.fragment = fragment;
    }


//    public void setIsRadioModule(boolean isRadioMode) {
//        this.isRadioMode = isRadioMode;
//    }


    @Override
    protected void convert(BaseViewHolder helper, Organization organization) {
        helper.setText(R.id.tv_module_name, organization.getName())
                .setBackgroundRes(R.id.ll_module_background, R.drawable.selector_agency)
                .setOnClickListener(R.id.ll_module_background, v -> {
                    if (listener != null) {
                        listener.onItemClick(organization);
                    }
                });


        if (organization.getIcon() != null) {
            String iconUrl = IconUrl.moduleIconUrl(IconUrl.Organizations, organization.getId() , StringUtils.getFirstIconNameFromIcon(organization.getIcon()),"");
            Glide.with(fragment).load(iconUrl).into((ImageView) helper.getView(R.id.iv_module_icon));
        }
    }



    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Organization organization);
    }



}
