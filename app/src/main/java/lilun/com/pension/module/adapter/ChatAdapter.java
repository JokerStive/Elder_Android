package lilun.com.pension.module.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

import lilun.com.pension.R;
import lilun.com.pension.app.App;
import lilun.com.pension.app.IconUrl;
import lilun.com.pension.app.User;
import lilun.com.pension.base.QuickAdapter;
import lilun.com.pension.module.bean.PushMessage;
import lilun.com.pension.module.utils.StringUtils;
import lilun.com.pension.widget.image_loader.ImageLoaderUtil;

/**
 * 聊天界面adapter
 *
 * @author zp
 */
public class ChatAdapter extends QuickAdapter<PushMessage> {


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


        if (helper.getAdapterPosition() == 0) {
            helper.getView(R.id.tv_send_time).setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_send_time, StringUtils.thatTime(pushMessage.getTime()));
        } else {
            Date beforeLast = StringUtils.string2Date(getData().get(helper.getAdapterPosition() - 1).getTime());
            Date last = StringUtils.string2Date(getData().get(helper.getAdapterPosition()).getTime());
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
                String iconUrl = IconUrl.moduleIconUrl(IconUrl.Accounts, User.getUserId(), null);

                Glide.with(App.context).load(iconUrl).dontAnimate()
                        .placeholder(R.drawable.icon_def)
                        .error(R.drawable.icon_def)
                        .into((ImageView) helper.getView(R.id.civ_my_ivatar));

            } else {
                helper.getView(R.id.rl_my_message).setVisibility(View.GONE);
                helper.getView(R.id.rl_their_message).setVisibility(View.VISIBLE);
                helper.setText(R.id.tv_their_name, name)
                        .setText(R.id.tv_their_message, pushMessage.getMessage());
                String iconUrl = IconUrl.moduleIconUrl(IconUrl.Accounts, id, null);
                Glide.with(App.context).load(iconUrl).dontAnimate()
                        .placeholder(R.drawable.icon_def)
                        .error(R.drawable.icon_def)
                        .into((ImageView) helper.getView(R.id.civ_their_ivatar));
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

}
