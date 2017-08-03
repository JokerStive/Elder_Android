package lilun.com.pensionlife.module.adapter;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.base.QuickAdapter;
import lilun.com.pensionlife.module.bean.OrganizationAid;

/**
 * @author yk
 *         create at 2017/4/26 10:33
 *         email : yk_developer@163.com
 */
public class UrgentInfoAdapter extends QuickAdapter<OrganizationAid> {

    public UrgentInfoAdapter(List<OrganizationAid> data) {
        super(R.layout.item_express_info, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, OrganizationAid aid) {

        holder.setText(R.id.tv_address, "位置：" + aid.getAddress())
                .setText(R.id.tv_time, "发生时间：" + aid.getCreatedAt())
                .setText(R.id.tv_mobile, "联系电话：" + aid.getMobile())
                .setText(R.id.tv_name, "姓名：" + aid.getCreatorName());
    }
}
