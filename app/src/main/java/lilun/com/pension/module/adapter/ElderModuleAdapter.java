package lilun.com.pension.module.adapter;

import android.content.res.Resources;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pension.R;
import lilun.com.pension.app.App;
import lilun.com.pension.app.IconUrl;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.base.QuickAdapter;
import lilun.com.pension.module.bean.ElderModule;
import lilun.com.pension.widget.image_loader.ImageLoaderUtil;

/**
 * 分类模块adapter
 *
 * @author yk
 *         create at 2017/2/13 11:14
 *         email : yk_developer@163.com
 */
public class ElderModuleAdapter extends QuickAdapter<ElderModule> {
    private BaseFragment fragment;
    private int backgroundRes;
    private OnItemClickListener listener;
    private int mSelectedPosition = -1;
    private boolean isRadioMode;


    public ElderModuleAdapter(BaseFragment fragment, List<ElderModule> data) {
        super(R.layout.item_elder_module, data);
        this.fragment = fragment;
        initData();
    }

    private void initData() {
        if (isRadioMode) {
            for (int i = 0; i < getData().size(); i++) {
                if (getData().get(i).isSelected()) {
                    mSelectedPosition = i;
                }
            }
        }
    }

    public void setIsRadioModule(boolean isRadioMode) {
        this.isRadioMode = isRadioMode;
    }


    @Override
    protected void convert(BaseViewHolder helper, ElderModule elderModule) {
        if (isRadioMode){
            helper.getView(R.id.ll_module_background).setSelected(mSelectedPosition == helper.getAdapterPosition());
        }
        helper.setText(R.id.tv_module_name, elderModule.getName())
                .setBackgroundRes(R.id.ll_module_background, getColor(elderModule.getParent()))
                .setOnClickListener(R.id.ll_module_background, v -> {
                    if (listener != null) {
                        listener.onItemClick(elderModule);
                        setRadio(helper);
                    }
                });


        if (elderModule.getIcon() != null) {
            String iconUrl = IconUrl.elderModule(elderModule.getId() + "", elderModule.getIcon().get(0).getFileName());
            ImageLoaderUtil.instance().loadImage(iconUrl,R.drawable.icon_def,helper.getView(R.id.iv_module_icon));
        }
    }

    private void setRadio(BaseViewHolder helper) {
        if (isRadioMode) {
            mSelectedPosition = helper.getAdapterPosition();
            notifyDataSetChanged();
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(ElderModule elderModule);
    }


    public int getBackgroundRes() {
        return backgroundRes;
    }

    private int getColor(String parent) {
        Resources resources = App.context.getResources();

        if (parent.equals(resources.getString(R.string.neighbor_help))) {
            backgroundRes = R.drawable.selector_help;
        } else if (parent.equals(resources.getString(R.string.community_activity))) {
            backgroundRes = R.drawable.selector_activity;
        } else if (parent.equals(resources.getString(R.string.pension_agency))) {
            backgroundRes = R.drawable.selector_agency;
        } else if (parent.equals(resources.getString(R.string.pension_education))) {
            backgroundRes = R.drawable.selector_education;
        } else if (parent.equals(resources.getString(R.string.health_service))) {
            backgroundRes = R.drawable.selector_health_service;
        } else if (parent.equals(resources.getString(R.string.residential_service))) {
            backgroundRes = R.drawable.selector_residential_service;
        }

        return backgroundRes;
    }
}
