package lilun.com.pensionlife.ui.activity.activity_detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.app.Config;
import lilun.com.pensionlife.app.Event;
import lilun.com.pensionlife.app.IconUrl;
import lilun.com.pensionlife.app.User;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.adapter.NestedReplyAdapter;
import lilun.com.pensionlife.module.adapter.PartnersIconAdapter;
import lilun.com.pensionlife.module.bean.ActivityDetail;
import lilun.com.pensionlife.module.bean.ActivityEvaluate;
import lilun.com.pensionlife.module.bean.NestedReply;
import lilun.com.pensionlife.module.bean.OrganizationActivity;
import lilun.com.pensionlife.module.bean.OrganizationReply;
import lilun.com.pensionlife.module.bean.PushMessage;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.module.utils.ToastHelper;
import lilun.com.pensionlife.module.utils.UIUtils;
import lilun.com.pensionlife.module.utils.mqtt.MQTTManager;
import lilun.com.pensionlife.module.utils.mqtt.MQTTTopicUtils;
import lilun.com.pensionlife.ui.activity.activity_question.ActivityQuestionListFragment;
import lilun.com.pensionlife.ui.activity.classify.ActivityClassifyFragment;
import lilun.com.pensionlife.widget.CircleImageView;
import lilun.com.pensionlife.widget.CountDownView;
import lilun.com.pensionlife.widget.InputSendPopupWindow;
import lilun.com.pensionlife.widget.InputSendView;
import lilun.com.pensionlife.widget.NormalDialog;
import lilun.com.pensionlife.widget.NormalItemDecoration;
import lilun.com.pensionlife.widget.NormalTitleBar;
import lilun.com.pensionlife.widget.image_loader.ImageLoaderUtil;
import lilun.com.pensionlife.widget.slider.BannerPager;

/**
 * Created by zp on 2017/3/6.
 * 2017/6/30  活动添加黑名单字段，加入活动前判断是否在黑名单内
 */

public class ActivityDetailFragment extends BaseFragment<ActivityDetailContact.Presenter>
        implements ActivityDetailContact.View, View.OnClickListener, PopupMenu.OnMenuItemClickListener {
    @Bind(R.id.title_bar)
    NormalTitleBar titleBar;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.iv_menu)
    ImageView ivMenu;
    @Bind(R.id.tv_title_name)
    TextView tvTitleName;
    @Bind(R.id.srl_swipe_layout)
    SwipeRefreshLayout mSwipeLayout;
    @Bind(R.id.nsv_scrollview)
    NestedScrollView nsvScrollView;
    @Bind(R.id.bp_activity_images)
    BannerPager bpActivityImages;
    @Bind(R.id.actv_activity_name)
    AppCompatTextView actvActName;
    @Bind(R.id.actv_activity_type)
    AppCompatTextView actvActType;
    @Bind(R.id.actv_activity_time)
    AppCompatTextView actvActTime;
    @Bind(R.id.cdv_time)
    CountDownView cdvTime;
    @Bind(R.id.ll_activity_start)
    LinearLayout llactvActStart;
    @Bind(R.id.actv_start)
    AppCompatTextView actvStart;


    @Bind(R.id.actv_activity_address)
    AppCompatTextView actvActAddress;
    @Bind(R.id.actv_activity_repeat)
    AppCompatTextView actvActRepeat;
    @Bind(R.id.actv_activity_num_people)
    AppCompatTextView actvActNumPeople;
    @Bind(R.id.actv_activity_creator)
    AppCompatTextView actvActCreator;

    @Bind(R.id.ll_partners_list)
    LinearLayout llPartnerList;
    @Bind(R.id.rv_partner_list)
    RecyclerView rvPartnerList;
    @Bind(R.id.actv_people_number)
    AppCompatTextView actvJoinedNumber;

    @Bind(R.id.actv_activity_content)
    AppCompatTextView actvActContent;
    @Bind(R.id.ll_avg_evaluate)
    LinearLayout llAvgEvaluate;
    @Bind(R.id.rb_avg_evaluate)
    RatingBar rbAvgEvaluate;
    @Bind(R.id.ll_evalute)
    LinearLayout llEevalute;
    @Bind(R.id.civ_account_avatar)
    CircleImageView civAccountAvatar;
    @Bind(R.id.actv_my_evaluate)
    AppCompatTextView actvMyEvaluate;
    @Bind(R.id.rb_bar)
    RatingBar rbEvaluate;
    @Bind(R.id.ll_question_list)
    LinearLayout llQuestionList;
    @Bind(R.id.actv_question_list)
    AppCompatTextView actvActQuestionNumber;
    @Bind(R.id.rv_question_list)
    RecyclerView rvQuestionList;
    @Bind(R.id.tv_more_question)
    TextView tvMoreQuestion;


    @Bind(R.id.acbt_sign_up)
    AppCompatButton acbtSignUp;

    @Bind(R.id.acbt_question_and_chat)
    AppCompatButton acbtQuestionChat;


    boolean isMaster = false; //活动创建人
    boolean isSignUp = false;
    int hasStart = OrganizationActivity.UNSTARTED;  // 0 - 未开始   1-已开始   2-已结束
    boolean hasfull = false;  //已满
    String mActivityId;
    PartnersIconAdapter partnersIconAdapter;
    private NestedReplyAdapter nestedReplyAdapter;
    OrganizationActivity activity;
    private InputSendPopupWindow inputSendPopupWindow;
    private String topic;
    private int qos = 2;
    private PopupMenu menu;

    public static ActivityDetailFragment newInstance(OrganizationActivity activity) {
        ActivityDetailFragment fragment = new ActivityDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("activity", activity);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void initPresenter() {
        mPresenter = new ActivityDetailPresenter();
        mPresenter.bindView(this);
    }

    @Override
    protected void getTransferData(Bundle arguments) {
        super.getTransferData(arguments);
        activity = (OrganizationActivity) arguments.getSerializable("activity");
        mActivityId = activity.getId();
        topic = MQTTTopicUtils.getActivityTopic(activity.getOrganizationId(), activity.getId());
        isMaster = User.getUserId().equals(activity.getMasterId());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_activity_detail;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        titleBar.setAlpha(0);
        ivBack.setAlpha(1);
        tvTitleName.setAlpha(1);
        titleBar.setOnBackClickListener(new NormalTitleBar.OnBackClickListener() {
            @Override
            public void onBackClick() {
                pop();
            }
        });
        initMenu();
        llQuestionList.setVisibility(View.GONE);
        mSwipeLayout.setOnRefreshListener(this::getActivityDetail);
        nsvScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                float value = UIUtils.dp2px(_mActivity, 80);
                int delY = scrollY - oldScrollY;
                //   Log.d("zp ", scrollY + "  " + oldScrollY + "  " + value);
                if (scrollY > value) scrollY = (int) value;
                if (delY < 0) {  //下滑
                    titleBar.setAlpha(1 - (value - scrollY) / value);
                } else {
                    titleBar.setAlpha(scrollY / value);
                }
                if (scrollY == 0) {
                    ivBack.setAlpha(1);
                    tvTitleName.setAlpha(1);
                } else {
                    ivBack.setAlpha(0);
                    tvTitleName.setAlpha(0);
                }

            }
        });

        if (activity.getIcon() != null)
            bpActivityImages.setData(activity.getIcon());

        rvQuestionList.setLayoutManager(new LinearLayoutManager(App.context, LinearLayoutManager.VERTICAL, false));
        rvQuestionList.addItemDecoration(new NormalItemDecoration(17));
        List<NestedReply> nestedReplies = new ArrayList<>();
        setReplyData(nestedReplies, false);
        partnersIconAdapter = new PartnersIconAdapter(activity.getPartnerList());
        rvPartnerList.setLayoutManager(new LinearLayoutManager(App.context, LinearLayoutManager.HORIZONTAL, false));
        rvPartnerList.addItemDecoration(new NormalItemDecoration(4));
        rvPartnerList.setAdapter(partnersIconAdapter);
        partnersIconAdapter.notifyDataSetChanged();

        rbEvaluate.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (fromUser) {
                    new NormalDialog().createNormal(_mActivity, getString(R.string.commit_evalue, rating + ""), new NormalDialog.OnPositiveListener() {
                        @Override
                        public void onPositiveClick() {
                            mPresenter.postActivityRank(activity.getId(), (int) rating);
                        }
                    });
                }
            }
        });
        showDetail(activity);
    }


    /**
     * 显示提问列表
     */
    private void setReplyData(List<NestedReply> nestedReplies, boolean isLoadMore) {
        if (nestedReplyAdapter == null) {
            nestedReplyAdapter = new NestedReplyAdapter(this, nestedReplies, isMaster, activity.getCreatorName(), new NestedReplyAdapter.AnswerListener() {
                @Override
                public void OnClickAnswer(NestedReply nestedReply, int position) {
                    Log.d("zp", position + "");
                    inputSendPopupWindow = new InputSendPopupWindow(getContext());
                    inputSendPopupWindow.setOnSendListener(new InputSendView.OnSendListener() {
                        @Override
                        public void send(String sendStr) {
                            Log.d("zp", sendStr);
                            mPresenter.addAnswer(activity.getId(), nestedReply.getQuestion().getId(), sendStr, position);
                        }
                    });
                    //设置弹出窗体需要软键盘，
                    inputSendPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
                    //再设置模式，和Activity的一样，覆盖。
                    inputSendPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    inputSendPopupWindow.showAtLocation(rvQuestionList, Gravity.BOTTOM, 0, 0);
                }
            });
            nestedReplyAdapter.setOnLoadMoreListener(() -> mPresenter.replyList(mActivityId, "", nestedReplyAdapter.getItemCount()), rvPartnerList);
            rvQuestionList.setAdapter(nestedReplyAdapter);
        } else if (isLoadMore) {
            nestedReplyAdapter.addAll(nestedReplies, Config.defLoadDatCount);
        } else {
            if (nestedReplies.size() >= 3) {
                nestedReplyAdapter.replaceAll(nestedReplies.subList(0, 3));
                tvMoreQuestion.setVisibility(View.VISIBLE);
            } else {
                nestedReplyAdapter.replaceAll(nestedReplies);
                tvMoreQuestion.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        getActivityDetail();
        mPresenter.getActivityRank(activity.getId());
        mPresenter.replyList(mActivityId, "", 0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cdvTime.stop();
    }

    /**
     * 接收来 Event.RefreshActivityReply 事件
     * 由 {@link ActivityQuestionListFragment}产生
     *
     * @param event
     */
    @Subscribe
    public void refreshReplyData(Event.RefreshActivityReply event) {
        mPresenter.replyList(mActivityId, "", 0);
    }

    @Subscribe
    public void refreshDetail(Event.RefreshActivityDetail event) {
        mPresenter.getActivityDetail(mActivityId);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void forcedquitChat(Event.ForcedQuitChat forcedQuitChat) {
        new NormalDialog().createShowMessage(_mActivity, forcedQuitChat.getShowMessage(), new NormalDialog.OnPositiveListener() {
            @Override
            public void onPositiveClick() {
                ActivityClassifyFragment fragment = findFragment(ActivityClassifyFragment.class);
                if (fragment != null)
                    popTo(ActivityClassifyFragment.class, false);
            }
        }, false);
    }

    private void getActivityDetail() {
        mPresenter.getActivityDetail(mActivityId);
    }

    public void showDetail(OrganizationActivity activity) {

        int signUpNumber;

        if (activity.getPartnerList() == null)
            signUpNumber = 0;
        else {
            signUpNumber = activity.getPartnerList().size();
            isSignUp = activity.getPartnerList().contains(User.getUserId());
        }

        actvActName.setText(activity.getTitle());
        String[] split = activity.getCategoryId().split("#activity-category.");
        if (split.length > 0) actvActType.setText(getString(R.string.activity_type_, split[1]));

        if (isSignUp) {
            llAvgEvaluate.setVisibility(View.VISIBLE);
            llEevalute.setVisibility(View.VISIBLE);
        } else llEevalute.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(activity.getRepeatedDesc())) {
            actvActTime.setText(getString(R.string.activity_time_, activity.getRepeatedDesc()));
            llactvActStart.setVisibility(View.GONE);
            cdvTime.setVisibility(View.GONE);
        } else {
            actvActTime.setText(getString(R.string.activity_time_, StringUtils.IOS2ToUTC(activity.getStartTime())));


            llactvActStart.setVisibility(View.VISIBLE);
            cdvTime.setVisibility(View.VISIBLE);
            if (activity.getEndTime() != null && new Date().after(StringUtils.IOS2ToUTCDate(activity.getEndTime()))) {
                //活动结束
                //   actvStart.setText(getString(R.string.activity_start_, getString(R.string.activity_has_finished)));
                cdvTime.setText(getString(R.string.activity_has_started));

                ImageLoaderUtil.instance().loadImage(IconUrl.moduleIconUrl(IconUrl.Accounts, User.getUserId(), null), R.drawable.icon_def, civAccountAvatar);

                hasStart = OrganizationActivity.FINISHED;
            } else if (activity.getStartTime() != null && new Date().after(StringUtils.IOS2ToUTCDate(activity.getStartTime()))) {
                //   actvStart.setText(getString(R.string.activity_start_, getString(R.string.activity_has_started)));
                cdvTime.setText(getString(R.string.activity_has_started));
                hasStart = OrganizationActivity.STARTED;
            } else {
                //   actvStart.setText(getString(R.string.activity_start_, ""));

                if (activity.getStartTime() != null)
                    if (cdvTime.getTime() == null)
                        cdvTime.setTime(StringUtils.IOS2ToUTCDate(activity.getStartTime())).start();
                    else
                        cdvTime.start();
            }
        }

        actvActAddress.setText(getString(R.string.activity_address_, activity.getAddress()));
        String repeat = TextUtils.isEmpty(activity.getRepeatedDesc()) ?
                getString(R.string.single) : getString(R.string.cyclical);
        actvActRepeat.setText(getString(R.string.activity_repeat_, repeat));
        String numPeople = activity.getMaxPartner() == 0 ? "不限" : activity.getMaxPartner() + "人";
        actvActNumPeople.setText(getString(R.string.targart_partin_, numPeople));
        actvActCreator.setText(getString(R.string.activit_creator_, activity.getCreatorName()));
        actvActContent.setText(activity.getDescription());

        if (signUpNumber == activity.getMaxPartner() && signUpNumber != 0) {
            hasfull = true;
        }
        actvJoinedNumber.setText(getString(R.string.people_number_, signUpNumber + ""));


        showButton(isMaster, isSignUp);
        if (activity.getPartnerList() != null && activity.getPartnerList().size() > 0)
            llPartnerList.setVisibility(View.VISIBLE);
        else
            llPartnerList.setVisibility(View.GONE);
        if (partnersIconAdapter != null)
            partnersIconAdapter.replaceAll(activity.getPartnerList());
    }

    /**
     * 1.判断是否是创建者， 创建人只显示解散活动 聊天室；
     * 2.不是创建人，  判断是否报名
     * 3.未报名 - 显示 报名、提问
     * 4.已报名 - 显示 退出、参与者列表
     *
     * @param isCreator
     * @param isSignUp
     */
    public void showButton(boolean isCreator, boolean isSignUp) {

        if (isCreator) {
            acbtSignUp.setText(getString(R.string.close_activity));
            acbtQuestionChat.setText(R.string.chat);
        } else {
            //未报名
            if (!isSignUp) {
                acbtSignUp.setText(getString(R.string.sign_up));
                acbtQuestionChat.setText(R.string.question);
                return;
            }
            acbtSignUp.setText(getString(R.string.sign_up_back));
            acbtQuestionChat.setText(R.string.chat);
        }

    }


    /**
     * 显示回复信息
     *
     * @param replyList
     */
    @Override
    public void showReplyList(List<NestedReply> replyList) {
        if (replyList == null) return;
        if (replyList.size() == 0) return;

        llQuestionList.setVisibility(View.VISIBLE);
        actvActQuestionNumber.setText(getString(R.string.activity_question_list_, replyList.size() + ""));
        setReplyData(replyList, false);
    }

    @Override
    public void showAnswer(OrganizationReply answer, int position) {
        inputSendPopupWindow.dismiss();
        nestedReplyAdapter.getItem(position).setAnswer(answer);
        nestedReplyAdapter.notifyDataSetChanged();
    }


    @Override
    public void showActivityDetail(ActivityDetail activityDetail) {
        completeRefresh();
        activity = activityDetail.getActivity();
        if (activity == null) {
            return;
        }
        showDetail(activity);
        showReplyList(activityDetail.getReplyList());


    }

    @Override
    public void completeRefresh() {
        if (mSwipeLayout != null && mSwipeLayout.isRefreshing()) {
            mSwipeLayout.setRefreshing(false);
        }
    }

    @Override
    public void refreshActivityData() {
        EventBus.getDefault().post(new Event.RefreshActivityData());

    }

    @Override
    public void sucJoinActivity() {
        if (MQTTManager.getInstance().isConnected()) {
            Logger.i("活动订阅中:" + topic);
            MQTTManager.getInstance().subscribe(topic, qos, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    showDialog("活动订阅成功");

                    Date date = new Date();
                    //     date.setTime(new Date().getTime() - 8 * 60 * 60 * 1000);
                    String from = "{\"id\":\"" + User.getUserId() + "\",\"name\":\"" + User.getName() + "\"}";
                    PushMessage pushMessage = new PushMessage().setVerb(PushMessage.VERB_JOIN)
                            .setFrom(from)
                            .setMessage("欢迎 " + User.getName() + " 加入本活动")
                            .setTime(StringUtils.date2String(date));
                    MQTTManager.getInstance().publish(topic, 2, pushMessage.getJsonStr());
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Logger.i("活动订阅失败");
                }


            });
        }
        refreshActivityData();
        activity.getPartnerList().add(User.getUserId());
        showDetail(activity);
    }

    @Override
    public void sucQuitActivity() {
        refreshActivityData();
        activity.getPartnerList().remove(User.getUserId());
        showDetail(activity);
        Date date = new Date();
        //  date.setTime(new Date().getTime() - 8 * 60 * 60 * 1000);
        String from = "{\"id\":\"" + User.getUserId() + "\",\"name\":\"" + User.getName() + "\"}";
        PushMessage pushMessage = new PushMessage().setVerb(PushMessage.VERB_QUIT)
                .setFrom(from)
                .setMessage(User.getName() + "已退出本活动")
                .setTime(StringUtils.date2String(date));
        MQTTManager.getInstance().publish(topic, qos, pushMessage.getJsonStr());
        MQTTManager.getInstance().unSubscribe(topic, null, new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                ToastHelper.get().showShort("取消订阅成功");
                popTo(ActivityClassifyFragment.class, false);
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                showDialog("取消订阅失败");
            }
        });
    }

    @Override
    public void showActivityRank(ActivityEvaluate evaluate) {

        if (evaluate.getRank() == -1) {
            rbEvaluate.setIsIndicator(false);
            actvMyEvaluate.setText(getString(R.string.want_evaluate));
        } else {
            rbEvaluate.setIsIndicator(true);
            actvMyEvaluate.setText(getString(R.string.my_evaluate));
        }
        rbEvaluate.setRating(evaluate.getRank());
        rbAvgEvaluate.setRating(evaluate.getAvgRank());
    }


    @Override
    public void successCancelActivity() {
        refreshActivityData();
        PushMessage pushMessage = new PushMessage();
        pushMessage.setVerb(PushMessage.VERB_QUIT)
                .setMessage("主持人已解散活动")
                .setTime(StringUtils.date2String(new Date()));
        MQTTManager.getInstance().publish(topic, qos, pushMessage.getJsonStr());
        popTo(ActivityClassifyFragment.class, false);
    }

    @OnClick({R.id.iv_menu, R.id.acbt_sign_up, R.id.acbt_question_and_chat, R.id.tv_more_question, R.id.actv_people_number})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_menu:
                openMenu();
                break;
            case R.id.acbt_sign_up:
                dealSignUp();
                break;
            case R.id.acbt_question_and_chat:
                dealQuestionChat();
                break;
            case R.id.tv_more_question:
                addQuestion();
                break;
            case R.id.actv_people_number:
                goPartnersList();
                break;
        }
    }


    /**
     * 初始化menu
     */
    private void initMenu() {
        menu = new PopupMenu(_mActivity, ivMenu);  //ivMenu是触发弹出menu的View
        menu.getMenuInflater().inflate(R.menu.menu_activity, menu.getMenu());  //设置关联布局文件
        ((MenuBuilder) menu.getMenu()).setOptionalIconsVisible(true);     //设置其icon显示，默认是不显示的
        menu.setOnMenuItemClickListener(this);    //设置监听器
    }

    /**
     * 打开menu
     */
    private void openMenu() {
        menu.show();
    }

    /**
     * 处理选中的Item
     * 删除本活动的聊天数据
     *
     * @param item
     * @return
     */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.item_delete_message) {
            if (DataSupport.deleteAll(PushMessage.class, "activityId = ?", activity.getId()) != 0) {
                ToastHelper.get().showShort("清理成功");
                EventBus.getDefault().post(new Event.ClearChat());
            }
        }
        return false;
    }

    /**
     * 处理提问 及 参与 者列表
     */
    private void dealQuestionChat() {
        if ("cancle".equals(activity.getStatus())) {
            showDialog(getString(R.string.activity_has_cancel));
            return;
        }


        if (getString(R.string.question).equals(acbtQuestionChat.getText().toString().trim())) {
            if (hasStart == OrganizationActivity.FINISHED) {
                showDialog(getString(R.string.activity_has_started));
                return;
            }
            if (activity.getStartTime() != null && new Date().after(StringUtils.IOS2ToUTCDate(activity.getStartTime()))) {
                showDialog(getString(R.string.activity_has_started));
                return;
            }
            addQuestion();
        } else if (getString(R.string.chat).equals(acbtQuestionChat.getText().toString().trim())) {
            goChat();
        }
    }

    /**
     * 处理参与按钮及其提示语
     */
    private void dealSignUp() {

        if (getString(R.string.sign_up).equals(acbtSignUp.getText().toString().trim())) {
            if (hasStart == OrganizationActivity.FINISHED) {
                showDialog(getString(R.string.activity_has_finished));
                return;
            }
            if (activity.getStartTime() != null && new Date().after(StringUtils.IOS2ToUTCDate(activity.getStartTime()))) {
                showDialog(getString(R.string.activity_has_started));
                return;
            }
            if (hasfull) {
                showDialog(getString(R.string.activity_has_people_full));
                return;
            }
            if (activity.getBlackList().contains(User.getUserId())) {
                showDialog(getString(R.string.you_not_allow_join));
                return;
            }
            joinActivity();
        } else if (getString(R.string.sign_up_back).equals(acbtSignUp.getText().toString().trim())) {
            quitActivity();
        } else if (getString(R.string.close_activity).equals(acbtSignUp.getText().toString().trim())) {
            cancelActivity();
        }
    }


    private void goChat() {
        if (findFragment(ActivityChatFragment.class) == null)
            start(ActivityChatFragment.newInstance(activity));
        else
            popTo(ActivityChatFragment.class, false);
    }

    private void goPartnersList() {
        start(ActivityPartnersListFragment.newInstance(activity));
    }


    private void joinActivity() {
        new NormalDialog().createNormal(_mActivity, R.string.confirm_join_activity, () -> {
            mPresenter.joinActivity(mActivityId);
        });
    }

    private void quitActivity() {
        new NormalDialog().createNormal(_mActivity, R.string.confirm_quite_activity, () -> {
            mPresenter.quitActivity(mActivityId);
        });
    }

    private void cancelActivity() {
        new NormalDialog().createNormal(_mActivity, getString(R.string.confirm_cancel_activity), new NormalDialog.OnPositiveListener() {
            @Override
            public void onPositiveClick() {
                mPresenter.cancelActivity(activity.getId());
            }
        });
    }

    private void addQuestion() {
        start(ActivityQuestionListFragment.newInstance(activity));
    }


}
