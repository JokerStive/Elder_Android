package lilun.com.pension.module.adapter;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pension.R;
import lilun.com.pension.app.IconUrl;
import lilun.com.pension.base.QuickAdapter;
import lilun.com.pension.module.bean.Account;
import lilun.com.pension.widget.image_loader.ImageLoaderUtil;

/**
 * Created by zp on 2017/4/20.
 */

public class PartnersAdapter extends QuickAdapter<Account> {

    public PartnersAdapter(List<Account> data) {
        super(R.layout.item_partners, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Account account) {

        helper.setText(R.id.tv_partners_index, (helper.getLayoutPosition() - getHeaderLayoutCount() + 1) + "")
                .setText(R.id.tv_name, account.getName());
        //     .setText(R.id.tv_desp, StringUtils.timeFormat(question.getCreatedAt()))

        ImageLoaderUtil.instance().loadImage(
                IconUrl.moduleIconUrl(IconUrl.Accounts, account.getId(), null),
                R.drawable.icon_def, helper.getView(R.id.iv_avatar));
    }
}
