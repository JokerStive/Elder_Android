package lilun.com.pensionlife.module.adapter;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.IconUrl;
import lilun.com.pensionlife.base.QuickAdapter;
import lilun.com.pensionlife.widget.image_loader.ImageLoaderUtil;

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
