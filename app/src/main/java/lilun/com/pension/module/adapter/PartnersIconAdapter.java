package lilun.com.pension.module.adapter;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pension.R;
import lilun.com.pension.app.IconUrl;
import lilun.com.pension.base.QuickAdapter;
import lilun.com.pension.widget.image_loader.ImageLoaderUtil;

/**
 * Created by zp on 2017/4/20.
 */

public class PartnersIconAdapter extends QuickAdapter<String> {

    public PartnersIconAdapter(List<String> data) {
        super(R.layout.item_partners_icon, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String accountId) {
        ImageLoaderUtil.instance().loadImage(
                IconUrl.moduleIconUrl(IconUrl.Accounts, accountId, null),
                R.drawable.icon_def, helper.getView(R.id.iv_avatar));
    }
}
