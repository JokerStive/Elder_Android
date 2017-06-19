package lilun.com.pensionlife.module.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.base.QuickAdapter;
import lilun.com.pensionlife.module.bean.Conversation;

/**
*聊天界面adapter
*@author yk
*create at 2017/3/7 12:01
*email : yk_developer@163.com
*/

public class ChatContentAdapter extends QuickAdapter<Conversation> {

    public ChatContentAdapter(List<Conversation> data) {
        super(R.layout.item_chat_content, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Conversation conversation) {
        View myContainer = helper.getView(R.id.rl_my_container);
        View otherContainer = helper.getView(R.id.rl_other_container);
        if (conversation.isOwn()){
            myContainer.setVisibility(View.VISIBLE);
            otherContainer.setVisibility(View.GONE);
            helper.setText(R.id.tv_my_content,conversation.getContent());
        }else {
            otherContainer.setVisibility(View.VISIBLE);
            myContainer.setVisibility(View.GONE);
            helper.setText(R.id.tv_other_content,conversation.getContent());
        }
    }
}
