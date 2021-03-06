package lilun.com.pensionlife.module.adapter;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.base.QuickAdapter;
import lilun.com.pensionlife.module.bean.Organization;
import lilun.com.pensionlife.widget.image_loader.ImageLoaderUtil;

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
            ImageLoaderUtil.instance().loadImage(organization.getIcon(),helper.getView(R.id.iv_module_icon));
        }
    }



    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Organization organization);
    }



}
