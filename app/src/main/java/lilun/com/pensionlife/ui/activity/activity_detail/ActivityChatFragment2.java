package lilun.com.pensionlife.ui.activity.activity_detail;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.crud.DataSupport;

import java.util.Date;

import butterknife.Bind;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.Event;
import lilun.com.pensionlife.app.User;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.adapter.ChatAdapter;
import lilun.com.pensionlife.module.bean.OrganizationActivity;
import lilun.com.pensionlife.module.bean.PushMessage;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.module.utils.mqtt.MQTTManager;
import lilun.com.pensionlife.module.utils.mqtt.MQTTTopicUtils;
import lilun.com.pensionlife.ui.activity.classify.ActivityClassifyFragment;
import lilun.com.pensionlife.widget.InputSendView;
import lilun.com.pensionlife.widget.NormalDialog;
import lilun.com.pensionlife.widget.NormalItemDecoration;
import lilun.com.pensionlife.widget.NormalTitleBar;
import lilun.com.pensionlife.widget.chatView.ChatView;

/**
 * Created by zp on 2017/5/3.
 */

public class ActivityChatFragment2 extends BaseFragment {
    public static String curActId = ""; //记录当前聊天的活动id
    private OrganizationActivity activity;
    ChatAdapter chatAdapter;

    private String topic;
    private int ops = 2;

    @Bind(R.id.title_bar)
    NormalTitleBar titleBar;

    @Bind(R.id.cv_activity)
    ChatView cvActivity;

    private int unReadCount;


    public static ActivityChatFragment2 newInstance(OrganizationActivity activity) {
        ActivityChatFragment2 fragment = new ActivityChatFragment2();
        Bundle args = new Bundle();
        args.putSerializable("activity", activity);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void getTransferData(Bundle arguments) {
        super.getTransferData(arguments);
        activity = (OrganizationActivity) arguments.getSerializable("activity");
        unReadCount = DataSupport.where("activityId = ? and unread = 1", activity.getId()).count(PushMessage.class);
        curActId = activity.getId();
        // 更新数据库 未读标识
        ContentValues values = new ContentValues();
        values.put("unread", false);
        int affected = DataSupport.updateAll(PushMessage.class, values, "activityId = ? and unread = 1", activity.getId());
        Logger.d("生效个数: " + affected);
        topic = MQTTTopicUtils.getActivityTopic(activity.getOrganizationId(), activity.getId());
    }

    @Override
    public void onPause() {
        super.onPause();
        _mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        curActId = "";

    }

    @Override
    protected void initPresenter() {
        MQTTManager.getInstance().subscribe(topic, 2);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_activity_chart2;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        titleBar.setTitle(activity.getTitle());
        titleBar.setOnBackClickListener(new NormalTitleBar.OnBackClickListener() {
            @Override
            public void onBackClick() {
                pop();
            }
        });
        titleBar.setOnRightClickListener(new NormalTitleBar.OnRightClickListener() {
            @Override
            public void onRightClick() {
                ActivityDetailFragment fragment = findFragment(ActivityDetailFragment.class);
                if (fragment == null) {
                    hideSoftInput();
                    start(ActivityDetailFragment.newInstance(activity));
                } else
                    popTo(ActivityDetailFragment.class, false);
            }
        });

        chatAdapter = new ChatAdapter(DataSupport.where("activityId = ?", activity.getId()).find(PushMessage.class));
        cvActivity.setChatAdapter(chatAdapter);
        cvActivity.setOnSendListener(new InputSendView.OnSendListener() {
            @Override
            public void send(String sendStr) {
                sendData(sendStr);
            }
        });
        cvActivity.setUnreadCount(unReadCount);
    }

    /**
     * 收到一个新的消息添加到聊天界面
     *
     * @param chat
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void RefreshNewMessage(Event.RefreshChatAddOne chat) {
        if (chat.getActivityId().equals(activity.getId()))
            chatAdapter.add(chat.getPushMessage());
        cvActivity.getRecyclerView().smoothScrollToPosition(chatAdapter.getItemCount() - 1);
    }

    /**
     * 强制退出聊天界面
     *
     * @param forcedQuitChat
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void forcedquitChat(Event.ForcedQuitChat forcedQuitChat) {
        ActivityDetailFragment detailFragment = findFragment(ActivityDetailFragment.class);
        if (detailFragment == null) {
            new NormalDialog().createShowMessage(_mActivity, forcedQuitChat.getShowMessage(), new NormalDialog.OnPositiveListener() {
                @Override
                public void onPositiveClick() {
                    ActivityClassifyFragment fragment = findFragment(ActivityClassifyFragment.class);
                    if (fragment != null)
                        popTo(ActivityClassifyFragment.class, false);
                }
            }, false);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void clearChat(Event.ClearChat clearChat) {
        chatAdapter.clear();
    }

    public void sendData(String str) {
        Date date = new Date();
        // date.setTime(new Date().getTime() - 8 * 60 * 60 * 1000);
        String from = "{\"id\": \"" + User.getUserId() + "\", \"name\": \"" + User.getName() + "\"}";
        PushMessage pushMessage = new PushMessage().setVerb(PushMessage.VERB_CHAR)
                .setFrom(from)
                .setMessage(str)
                .setTime(StringUtils.date2String(date));
        MQTTManager.getInstance().publish(topic, ops, pushMessage.getJsonStr());
    }

    public Animation startUnreadAnimotion() {
        AnimationSet set = new AnimationSet(false);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.5f, 1f);
        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
        set.setDuration(500);
        set.addAnimation(alphaAnimation);
        set.addAnimation(translateAnimation);
        return set;
    }

    public Animation hideUnreadAnimotion() {
        AnimationSet set = new AnimationSet(false);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);
        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
        set.setDuration(500);
        set.addAnimation(alphaAnimation);
        set.addAnimation(translateAnimation);
        return set;
    }

}
