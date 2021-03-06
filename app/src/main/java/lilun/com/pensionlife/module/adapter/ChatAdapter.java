package lilun.com.pensionlife.module.adapter;

import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;
import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.User;
import lilun.com.pensionlife.base.QuickAdapter;
import lilun.com.pensionlife.module.bean.ds_bean.PushMessage;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.widget.image_loader.ImageLoaderUtil;

/**
 * 聊天界面adapter
 *
 * @author zp
 */
public class ChatAdapter extends QuickAdapter<PushMessage> {

    final String sg = System.currentTimeMillis() / (1000 * 60 * 60) + "" + User.getChangeAvatorCount();//缓存时间1小时
    private int chatType = 0; //默认为0，

    public ChatAdapter(List<PushMessage> data) {
        super(R.layout.item_chat, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PushMessage pushMessage) {
        String id = "", name = "";
        String[] kickId;
        String[] kickName;
        String kickMessage = "";

        try {
            if (!TextUtils.isEmpty(pushMessage.getFrom())) {
                JSONObject jsonObject = new JSONObject(pushMessage.getFrom());
                id = jsonObject.getString("id");
                name = jsonObject.getString("name");
            }
            if (!TextUtils.isEmpty(pushMessage.getTo())) {
                JSONArray jsonArray = new JSONArray(pushMessage.getTo());
                kickName = new String[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    kickName[i] = jsonObject.getString("name");
                    if (User.getUserId().equals(jsonObject.getString("id"))) {
                        kickMessage += "您被主持人请出了活动\n";
                    } else
                        kickMessage += kickName[i] + "被主持人请出了活动\n";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //if (helper.getAdapterPosition() == 0) {//如果只有一个列表数据
        if (helper.getAdapterPosition() - getHeaderLayoutCount() == 0) {
            helper.getView(R.id.tv_send_time).setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_send_time, StringUtils.thatTime(pushMessage.getTime()));
        } else {
            Date beforeLast = null;
//            for (int i = helper.getAdapterPosition() - 1; i >= 0; i--) {
            for (int i = helper.getAdapterPosition() - getHeaderLayoutCount() - 1; i >= 0; i--) {
                beforeLast = StringUtils.string2Date(getData().get(helper.getAdapterPosition() - getHeaderLayoutCount() - 1).getTime());
//                beforeLast = StringUtils.string2Date(getData().get(helper.getAdapterPosition() - 1).getTime());
                if (beforeLast != null)
                    break;
            }

            Date last = StringUtils.string2Date(getData().get(helper.getAdapterPosition() - getHeaderLayoutCount()).getTime());
            if (beforeLast != null && last != null && last.getTime() - beforeLast.getTime() > 3 * 60 * 1000)   //3分钟内
            {
                helper.getView(R.id.tv_send_time).setVisibility(View.VISIBLE);
                helper.setText(R.id.tv_send_time, StringUtils.thatTime(pushMessage.getTime()));
            } else
                helper.getView(R.id.tv_send_time).setVisibility(View.GONE);
        }


        if (PushMessage.VERB_CHAR.equals(pushMessage.getVerb())) {
            helper.getView(R.id.tv_notify_message).setVisibility(View.GONE);
            if (TextUtils.isEmpty(id)) {
                helper.getView(R.id.rl_my_message).setVisibility(View.GONE);
                helper.getView(R.id.rl_their_message).setVisibility(View.GONE);
                helper.getView(R.id.tv_notify_message).setVisibility(View.VISIBLE);
                helper.setText(R.id.tv_notify_message, pushMessage.getMessage());
            } else if (id.equals(User.getUserId())) {
                helper.getView(R.id.rl_my_message).setVisibility(View.VISIBLE);
                helper.getView(R.id.rl_their_message).setVisibility(View.GONE);
                helper.setText(R.id.tv_my_name, name)
                        .setText(R.id.tv_my_message, pushMessage.getMessage());
                ImageLoaderUtil.instance().loadAvatar(User.getUserId(), helper.getView(R.id.civ_my_ivatar), sg);

            } else {
                helper.getView(R.id.rl_my_message).setVisibility(View.GONE);
                helper.getView(R.id.rl_their_message).setVisibility(View.VISIBLE);
                helper.setText(R.id.tv_their_name, name)
                        .setText(R.id.tv_their_message, pushMessage.getMessage());
                if (chatType == 0)
                    ImageLoaderUtil.instance().loadAvatar(id, helper.getView(R.id.civ_their_ivatar), sg);
                else if (chatType == 1)
                    ImageLoaderUtil.instance().loadImage(R.drawable.def_comstor_serice, helper.getView(R.id.civ_their_ivatar));
            }
        } else {
            helper.getView(R.id.rl_my_message).setVisibility(View.GONE);
            helper.getView(R.id.rl_their_message).setVisibility(View.GONE);
            helper.getView(R.id.tv_notify_message).setVisibility(View.VISIBLE);
            if (PushMessage.VERB_KICK.equals(pushMessage.getVerb())) {
                helper.setText(R.id.tv_notify_message, kickMessage);
            } else if (PushMessage.VERB_QUIT.equals(pushMessage.getVerb())) {
                if (User.getUserId().equals(id))
                    helper.setText(R.id.tv_notify_message, kickMessage);
                else
                    helper.setText(R.id.tv_notify_message, pushMessage.getMessage());
            } else if (PushMessage.VERB_JOIN.equals(pushMessage.getVerb())) {
                if (User.getUserId().equals(id))
                    helper.setText(R.id.tv_notify_message, "您已加入本活动");
                else
                    helper.setText(R.id.tv_notify_message, pushMessage.getMessage());
            }
        }


    }

    /**
     * 设置聊天室类型
     *
     * @param chatType 0:普通聊天室， 1：订单聊天室
     */
    public void setChatType(int chatType) {
        this.chatType = chatType;
    }
}
