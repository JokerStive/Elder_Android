package lilun.com.pension.ui.activity.activity_detail;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.crud.DataSupport;

import java.util.Date;

import butterknife.Bind;
import lilun.com.pension.R;
import lilun.com.pension.app.Event;
import lilun.com.pension.app.User;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.adapter.ChatAdapter;
import lilun.com.pension.module.bean.OrganizationActivity;
import lilun.com.pension.module.bean.PushMessage;
import lilun.com.pension.module.utils.StringUtils;
import lilun.com.pension.module.utils.mqtt.MQTTManager;
import lilun.com.pension.module.utils.mqtt.MQTTTopicUtils;
import lilun.com.pension.ui.activity.classify.ActivityClassifyFragment;
import lilun.com.pension.widget.InputSendView;
import lilun.com.pension.widget.NormalDialog;
import lilun.com.pension.widget.NormalItemDecoration;
import lilun.com.pension.widget.NormalTitleBar;

/**
 * Created by zp on 2017/5/3.
 */

public class ActivityChatFragment extends BaseFragment {
    private OrganizationActivity activity;
    ChatAdapter chatAdapter;

    private String topic;
    private int ops = 2;


    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.title_bar)
    NormalTitleBar titleBar;

    @Bind(R.id.isv_input_send)
    InputSendView isvSend;


    public static ActivityChatFragment newInstance(OrganizationActivity activity) {
        ActivityChatFragment fragment = new ActivityChatFragment();
        Bundle args = new Bundle();
        args.putSerializable("activity", activity);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void getTransferData(Bundle arguments) {
        super.getTransferData(arguments);
        activity = (OrganizationActivity) arguments.getSerializable("activity");

        topic = MQTTTopicUtils.getActivityTopic(activity.getOrganizationId(), activity.getId());
    }

    @Override
    protected void initPresenter() {
        MQTTManager.getInstance().subscribe(topic,2);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_activity_chart;
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
                    start(ActivityDetailFragment.newInstance(activity));
                } else
                    popTo(ActivityDetailFragment.class, false);
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new NormalItemDecoration(5));

        chatAdapter = new ChatAdapter(DataSupport.where("activityId = ?", activity.getId()).find(PushMessage.class));
        mRecyclerView.setAdapter(chatAdapter);
        if (chatAdapter.getItemCount() > 0)
            mRecyclerView.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
        isvSend.setOnSendListener(new InputSendView.OnSendListener() {
            @Override
            public void send(String sendStr) {
                sendData(sendStr);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void RefreshNewMessage(Event.RefreshChatAddOne chat) {
        chatAdapter.add(chat.getPushMessage());
        mRecyclerView.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
    }

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

}
