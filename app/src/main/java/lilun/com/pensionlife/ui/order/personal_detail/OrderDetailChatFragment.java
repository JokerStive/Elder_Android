package lilun.com.pensionlife.ui.order.personal_detail;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.crud.DataSupport;
import org.litepal.crud.callback.FindMultiCallback;

import java.util.Date;
import java.util.List;

import butterknife.Bind;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.Event;
import lilun.com.pensionlife.app.User;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.adapter.ChatAdapter;
import lilun.com.pensionlife.module.bean.OrganizationActivity;
import lilun.com.pensionlife.module.bean.OrganizationProduct;
import lilun.com.pensionlife.module.bean.PushMessage;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.module.utils.ToastHelper;
import lilun.com.pensionlife.module.utils.mqtt.MQTTManager;
import lilun.com.pensionlife.module.utils.mqtt.MQTTTopicUtils;
import lilun.com.pensionlife.ui.activity.activity_detail.ActivityDetailFragment;
import lilun.com.pensionlife.ui.activity.classify.ActivityClassifyFragment;
import lilun.com.pensionlife.widget.InputSendView;
import lilun.com.pensionlife.widget.NormalDialog;
import lilun.com.pensionlife.widget.NormalTitleBar;
import lilun.com.pensionlife.widget.chatView.ChatView;

/**
 * 互动式订单详情
 * Created by zp on 2017/5/3.
 */

public class OrderDetailChatFragment extends BaseFragment {
    public static String productId = ""; //记录当前聊天的产品id
    private OrganizationProduct productInfo;
    ChatAdapter chatAdapter;

    private String topic;
    private int ops = 2;

    @Bind(R.id.title_bar)
    NormalTitleBar titleBar;

    @Bind(R.id.cv_activity)
    ChatView cvActivity;

    private int unReadCount;


    public static OrderDetailChatFragment newInstance(OrganizationActivity activity) {
        OrderDetailChatFragment fragment = new OrderDetailChatFragment();
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

    /**
     * 获取产品id，订阅产品所在组织的聊天频道
     *
     * @param arguments
     */
    @Override
    protected void getTransferData(Bundle arguments) {
        super.getTransferData(arguments);
        productInfo = (OrganizationProduct) arguments.getSerializable("productInfo");
        productId = productInfo.getId();
        topic = MQTTTopicUtils.getActivityTopic(productInfo.getOrganizationId(), productInfo.getId());

        // 更新数据库 未读标识
        ContentValues values = new ContentValues();
        values.put("unread", false);
        DataSupport.updateAllAsync(PushMessage.class, values, "activityId = ? and unread = 1", productInfo.getId())
                .listen(rowsAffected -> {
                    unReadCount = rowsAffected;
                    Logger.d("生效个数: " + rowsAffected);
                });


    }

    @Override
    public void onPause() {
        super.onPause();
        _mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        productId = "";

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
        titleBar.setTitle("");
        titleBar.setOnBackClickListener(new NormalTitleBar.OnBackClickListener() {
            @Override
            public void onBackClick() {
                pop();
            }
        });

        DataSupport.where("activityId = ?", productInfo.getId()).findAsync(PushMessage.class)
                .listen(new FindMultiCallback() {
                    @Override
                    public <T> void onFinish(List<T> t) {
                        chatAdapter = new ChatAdapter((List<PushMessage>) t);
                        chatAdapter.setHeaderView(setProductView());
                        cvActivity.setChatAdapter(chatAdapter);
                        cvActivity.setOnSendListener(new InputSendView.OnSendListener() {
                            @Override
                            public void send(String sendStr) {
                                sendData(sendStr);
                            }
                        });
                        cvActivity.setUnreadCount(unReadCount);
                    }
                });


    }

    public View setProductView() {
        TextView tvOrderCreateTime, tvOrderStatus, tvOrderProductTitle, tvOrderPrice,
                tvOrderRegisterTime, tvProdcutNumber, tvOrderTotalPrice,
                tvContactName, tvContactMobile, tvContactAddress;
        ImageView ivOrderImg;

        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.view_product_info_layout, null, false);
        Button cancelOrder = (Button) inflate.findViewById(R.id.btn_cancel_order);
        LinearLayout llContactInfo = (LinearLayout) inflate.findViewById(R.id.ll_contact_info);
        RelativeLayout rlProductInfo = (RelativeLayout) inflate.findViewById(R.id.rl_product_info);
        tvOrderCreateTime = (TextView) inflate.findViewById(R.id.tv_order_create_time);
        tvOrderStatus = (TextView) inflate.findViewById(R.id.tv_order_status);
        tvOrderProductTitle = (TextView) inflate.findViewById(R.id.tv_order_product_title);
        tvOrderPrice = (TextView) inflate.findViewById(R.id.tv_order_price);
        tvOrderRegisterTime = (TextView) inflate.findViewById(R.id.tv_order_register_time);
        tvProdcutNumber = (TextView) inflate.findViewById(R.id.tv_prodcut_number);
        tvOrderTotalPrice = (TextView) inflate.findViewById(R.id.tv_order_total_price);
        tvContactName = (TextView) inflate.findViewById(R.id.tv_contact_name);
        tvContactMobile = (TextView) inflate.findViewById(R.id.tv_contact_mobile);
        tvContactAddress = (TextView) inflate.findViewById(R.id.tv_contact_address);
        ivOrderImg = (ImageView) inflate.findViewById(R.id.iv_order_img);
        tvOrderCreateTime.setText("预约时间:2018-01-24 20:14:27");
        tvOrderStatus.setText("待处理");
        tvOrderProductTitle.setText("英孚教育-四点半课堂1111111111111111");
        tvOrderPrice.setText("￥53.00");
        tvOrderRegisterTime.setText("服务时间:2018-01-24");
        tvProdcutNumber.setText("x 1");
        tvOrderTotalPrice.setText(Html.fromHtml("共 <font color=\"red\">1</font>件商品，合计: <font color=\"red\">￥53.00</font>"));
        tvContactName.setText("收货人：张三");
        tvContactMobile.setText("电话：185****8537");
        tvContactAddress.setText("地址：重庆 重庆市 南岸区长生桥镇茶园 新区城南家园六组团2栋三单元2-1");
        cancelOrder.setOnClickListener((v) -> {
            ToastHelper.get().showShort("canceled !");
        });
        llContactInfo.setOnClickListener((v) -> {
            ToastHelper.get().showShort("联系人!");
        });
        rlProductInfo.setOnClickListener((v) -> {
            ToastHelper.get().showShort("产品信息!");
        });
        return inflate;
    }

    /**
     * 收到一个新的消息添加到聊天界面
     *
     * @param chat
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void RefreshNewMessage(Event.RefreshChatAddOne chat) {
        if (chat.getActivityId().equals(productId))
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
