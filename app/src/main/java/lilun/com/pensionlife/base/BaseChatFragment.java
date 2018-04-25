package lilun.com.pensionlife.base;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;

import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.crud.DataSupport;
import org.litepal.crud.callback.FindMultiCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.Event;
import lilun.com.pensionlife.app.User;
import lilun.com.pensionlife.module.adapter.ChatAdapter;
import lilun.com.pensionlife.module.bean.ds_bean.PushMessage;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.module.utils.mqtt.MQTTManager;
import lilun.com.pensionlife.module.utils.mqtt.MQTTTopicUtils;
import lilun.com.pensionlife.ui.activity.activity_detail.ActivityDetailFragment;
import lilun.com.pensionlife.ui.activity.classify.ActivityClassifyFragment;
import lilun.com.pensionlife.widget.InputSendView;
import lilun.com.pensionlife.widget.NormalDialog;
import lilun.com.pensionlife.widget.NormalTitleBar;
import lilun.com.pensionlife.widget.chatView.ChatView;

/**
 * 基础聊天室 -- 主题  --
 * 注意：社区活动复用此聊天界面，但是社区活动没有setProductView的操作，是纯聊天室
 * 目前继承的有两个子类：互动式订单聊天室，社区活动聊天室
 * Created by zp on 2017/5/3.
 */

public abstract class BaseChatFragment<T extends IPresenter>  extends BaseFragment {
    protected static String objectId = ""; //记录社区活动id 或 产品订单id
    public String orgId = "";    // 组织id
    public int chatType; //聊天室类别

    public ChatAdapter chatAdapter;

    protected T mPresenter;
    private String topic;
    private int ops = 2;
    private int unReadCount;
    public static String Arg_ChatType = "chatType";

    public static int ChatType_Normal = 0;  //普通的
    public static int ChatType_Interactive = 1;  //互动的


    @Bind(R.id.title_bar)
    public NormalTitleBar titleBar;

    @Bind(R.id.cv_activity)
    ChatView cvActivity;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //弹出键盘，整个Layout重新编排,重新分配多余空间
        _mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    /**
     * 获取产品id，订阅产品所在组织的聊天频道
     * 注意topic----eg ： 订单主题：/社会组织/营利/商家/赵丽颖/#order/orderId/.chat
     * 社区活动主题：: /地球村/中国/重庆/重庆/渝中区/大坪街道/#activity/春季运动会活动ID/.chat
     * 订单不挂组织，但是其关联的产品挂组织，因此可以取产品的组织
     *
     * @param arguments
     */
    @Override
    protected void getTransferData(Bundle arguments) {
    }


    @Override
    protected void initData() {
        //对于订单时，topic 要将 order  替换成 order 模块
        topic = MQTTTopicUtils.getActivityTopic(orgId.replace("product", "order"), objectId);
        MQTTManager.getInstance().subscribe(topic, 2);
        // 更新数据库 未读标识
        ContentValues values = new ContentValues();
        values.put("unread", false);
        DataSupport.updateAllAsync(PushMessage.class, values, "activityId = ? and unread = 1 and chatType = ?", objectId, chatType + "")
                .listen(rowsAffected -> {
                    unReadCount = rowsAffected;
                    Logger.d("生效个数: " + rowsAffected);
                });
    }

    @Override
    public void onPause() {
        super.onPause();
        _mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //由于是全局变量，必须在该页面销毁后设置为空，否则判断不正确
        objectId = "";
    }

    @Override
    protected void initPresenter() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_activity_chart;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        titleBar.setOnBackClickListener(new NormalTitleBar.OnBackClickListener() {
            @Override
            public void onBackClick() {
                pop();
            }
        });
        titleBar.setOnTitleClickListener(() -> {
            if (cvActivity != null && cvActivity.getRecyclerView() != null)
                cvActivity.getRecyclerView().smoothScrollToPosition(0);
        });

        setChatAdapter();

        DataSupport.where("activityId = ? and chatType = ?", objectId, chatType + "").findAsync(PushMessage.class)
                .listen(new FindMultiCallback() {
                    @Override
                    public <T> void onFinish(List<T> t) {
                        chatAdapter.addAll((List<PushMessage>) t);
                        //在更新数据后，延迟一段时间滑到底部
                        titleBar.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                cvActivity.getRecyclerView().scrollToPosition(chatAdapter.getItemCount()-1);
                            }
                        },200);

                    }
                });
    }

    /**
     * 设置 聊天室 recycler及 adapter
     */
    public void setChatAdapter() {
        chatAdapter = new ChatAdapter(new ArrayList<>());
        inflateProductView();
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
     * 判断是本聊天室的消息
     *
     * @param id
     */
    public static boolean isCurrentMsg(String id) {
        return id.equals(objectId);
    }

    /**
     * 将产品信息添加到Header里
     */
    public abstract void inflateProductView();

    /**
     * 收到一个新的消息添加到聊天界面
     *
     * @param chat
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void RefreshNewMessage(Event.RefreshChatAddOne chat) {
        if (chat.getActivityId().equals(objectId)) {
            chatAdapter.add(chat.getPushMessage());
        }
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
