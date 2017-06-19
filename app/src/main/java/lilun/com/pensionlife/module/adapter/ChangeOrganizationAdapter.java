package lilun.com.pensionlife.module.adapter;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.base.QuickAdapter;
import lilun.com.pensionlife.module.bean.Organization;

/**
 * 切换社区
 *
 * @author yk
 *         create at 2017/4/11 11:23
 *         email : yk_developer@163.com
 */
public class ChangeOrganizationAdapter extends QuickAdapter<Organization> {

    public ChangeOrganizationAdapter(List<Organization> data) {
        super(R.layout.item_change_organization, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Organization organization) {
        holder.setText(R.id.tv_organization_name,organization.getName());
    }
}
