package lilun.com.pensionlife.ui.activity.activity_detail;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

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
        mRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (chatAdapter.getItemCount() > 0)
                    mRecyclerView.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
            }
        });
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
