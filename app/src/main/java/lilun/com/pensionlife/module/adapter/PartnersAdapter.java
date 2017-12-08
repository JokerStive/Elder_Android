package lilun.com.pensionlife.module.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.IconUrl;
import lilun.com.pensionlife.base.QuickAdapter;
import lilun.com.pensionlife.module.bean.Account;
import lilun.com.pensionlife.widget.image_loader.ImageLoaderUtil;

/**
 * Created by zp on 2017/4/20.
 */

public class PartnersAdapter extends QuickAdapter<Account> {
    boolean selectedStatus = false;
    ArrayList<Integer> selectedList = new ArrayList<>();

    public PartnersAdapter(List<Account> data) {
        super(R.layout.item_partners, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Account account) {

        helper.setText(R.id.tv_name, account.getName());
        if (selectedList.contains(helper.getAdapterPosition())) {
            helper.setImageResource(R.id.iv_selected, R.drawable.partners_selected);
        } else helper.setImageResource(R.id.iv_selected, R.drawable.partners_unselected);

        if (selectedStatus) helper.getView(R.id.iv_selected).setVisibility(View.VISIBLE);
        else helper.getView(R.id.iv_selected).setVisibility(View.GONE);

        ImageLoaderUtil.instance().loadAvatar(
                account.getId(), helper.getView(R.id.iv_avatar));
    }

    public void setShowSelectedStatus(boolean show) {
        selectedStatus = show;
    }

    public void dealSelectedStatus(int pos) {
        if (!selectedList.contains(pos))
            selectedList.add(pos);
        else
            selectedList.remove(pos);
    }

    public ArrayList<Integer> getSelectedList() {
        return selectedList;
    }
}
