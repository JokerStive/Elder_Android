package lilun.com.pension.module.adapter;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pension.R;
import lilun.com.pension.base.QuickAdapter;
import lilun.com.pension.module.bean.OrganizationAid;
import lilun.com.pension.module.utils.StringUtils;

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

        holder.setText(R.id.tv_address, "定位：" + aid.getAddress())
                .setText(R.id.tv_time, "发生时间：" + StringUtils.IOS2ToUTC(aid.getCreatedAt(), 1))
                .setText(R.id.tv_mobile, "联系电话：" + aid.getMobile())
                .setText(R.id.tv_name, "姓名：" + aid.getCreatorName());
    }
}
