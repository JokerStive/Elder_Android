package lilun.com.pension.module.adapter;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pension.R;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.base.QuickAdapter;
import lilun.com.pension.module.bean.OrganizationReply;
import lilun.com.pension.module.utils.StringUtils;

/**
*展示互助列表的adapter
*@author yk
*create at 2017/2/13 11:27
*email : yk_developer@163.com
*/
public class AidAskListAdapter extends QuickAdapter<OrganizationReply> {
    private  BaseFragment fragment;
    private  String icon;


    public AidAskListAdapter(BaseFragment fragment, List<OrganizationReply> data) {
        super(R.layout.item_aid_asker,data);
        this.fragment = fragment;
    }

    @Override
    protected void convert(BaseViewHolder helper, OrganizationReply reply) {
        helper.setText(R.id.tv_name,reply.getCreatorName())
                .setText(R.id.tv_time, StringUtils.timeFormat(reply.getCreatedAt()));
//                .set
    }
}
