package lilun.com.pension.module.adapter;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pension.R;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.base.QuickAdapter;
import lilun.com.pension.module.bean.OrganizationReply;

/**
*展示互助列表的adapter
*@author yk
*create at 2017/2/13 11:27
*email : yk_developer@163.com
*/
public class AidJoinerAdapter extends QuickAdapter<OrganizationReply> {
    private  BaseFragment fragment;
    private  String icon;

    public AidJoinerAdapter(BaseFragment fragment, List<OrganizationReply> data) {
        super(R.layout.item_aid_answers,data);
        this.fragment = fragment;
    }

    @Override
    protected void convert(BaseViewHolder helper, OrganizationReply reply) {

    }
}
