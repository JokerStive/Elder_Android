package lilun.com.pensionlife.module.adapter;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.base.QuickAdapter;
import lilun.com.pensionlife.module.bean.OrganizationAid;
import lilun.com.pensionlife.module.utils.StringUtils;

/**
*@author yk
*create at 2017/3/21 16:06
*email : yk_developer@163.com
*/
public class AidRootAdapter extends QuickAdapter<OrganizationAid> {
    public AidRootAdapter(List<OrganizationAid> data) {
        super(R.layout.item_aid_root, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrganizationAid aid) {
        String str;
        if (aid.getKind()==0){
            str="回答了";
        }else {
            str =  "帮助了";
        }
        String content = "\""+aid.getCreatorName()+"\""+str+"\""+aid.getCreatorName()+"\""+"   主题为"
                +"\""+aid.getTitle()+"\""+"的请求";

        helper.setText(R.id.tv_content,content)
                .setText(R.id.tv_creator, StringUtils.IOS2ToUTC(aid.getCreatedAt(),3));

    }
}
