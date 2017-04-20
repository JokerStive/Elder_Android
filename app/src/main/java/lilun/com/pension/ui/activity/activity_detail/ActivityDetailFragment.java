package lilun.com.pension.ui.activity.activity_detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import lilun.com.pension.R;
import lilun.com.pension.app.App;
import lilun.com.pension.app.Event;
import lilun.com.pension.app.IconUrl;
import lilun.com.pension.app.User;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.adapter.NestedReplyAdapter;
import lilun.com.pension.module.bean.ActivityDetail;
import lilun.com.pension.module.bean.IconModule;
import lilun.com.pension.module.bean.NestedReply;
import lilun.com.pension.module.bean.OrganizationActivity;
import lilun.com.pension.module.bean.OrganizationReply;
import lilun.com.pension.module.utils.StringUtils;
import lilun.com.pension.ui.activity.activity_question.ActivityQuestionListFragment;
import lilun.com.pension.widget.InputSendPopupWindow;
import lilun.com.pension.widget.NormalDialog;
import lilun.com.pension.widget.NormalItemDecoration;
import lilun.com.pension.widget.slider.BannerPager;

/**
 * Created by zp on 2017/3/6.
 */

public class ActivityDetailFragment extends BaseFragment<ActivityDetailContact.Presenter> implements ActivityDetailContact.View, View.OnClickListener {

    @Bind(R.id.srl_swipe_layout)
    SwipeRefreshLayout mSwipeLayout;
    @Bind(R.id.bp_activity_images)
    BannerPager bpActivityImages;
    @Bind(R.id.actv_activity_name)
    AppCompatTextView actvActName;
    @Bind(R.id.actv_activity_type)
    AppCompatTextView actvActType;
    @Bind(R.id.actv_activity_time)
    AppCompatTextView actvActTime;
    @Bind(R.id.ll_activity_start)
    LinearLayout llactvActStart;

    @Bind(R.id.actv_activity_address)
    AppCompatTextView actvActAddress;
    @Bind(R.id.actv_activity_repeat)
    AppCompatTextView actvActRepeat;
    @Bind(R.id.actv_activity_num_people)
    AppCompatTextView actvActNumPeople;
    @Bind(R.id.actv_activity_creator)
    AppCompatTextView actvActCreator;
    @Bind(R.id.actv_activity_content)
    AppCompatTextView actvActContent;
    @Bind(R.id.ll_evalute)
    LinearLayout llEevalute;
    @Bind(R.id.rv_activity_evalute)
    RecyclerView rvActEvalute;
    @Bind(R.id.ll_question_list)
    LinearLayout llQuestionList;
    @Bind(R.id.actv_question_list)
    AppCompatTextView actvActQuestionNumber;
    @Bind(R.id.rv_question_list)
    RecyclerView rvQuestionList;
    @Bind(R.id.tv_more_question)
    TextView tvMoreQuestion;

    @Bind(R.id.acbt_joined_number)
    AppCompatButton acbtJoinedNumber;
    @Bind(R.id.acbt_sign_up)
    AppCompatButton acbtSignUp;
    @Bind(R.id.acbt_sign_up_back)
    AppCompatButton acbtSignUpBack;
    @Bind(R.id.acbt_question)
    AppCompatButton acbtQuestion;
    @Bind(R.id.acbt_sign_up_list)
    AppCompatButton acbtSignUpList;


    boolean isMaster = false; //活动创建人
    boolean isSignUp = false;

    String mActivityId;

    private NestedReplyAdapter nestedReplyAdapter;
    OrganizationActivity activity;
    private InputSendPopupWindow inputSendPopupWindow;

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
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_activity_detail;
    }

    @Override
    protected void initView(LayoutInflater inflater) {

        showDetail(activity);

        mSwipeLayout.setOnRefreshListener(this::getActivityDetail);

        ArrayList<String> images = new ArrayList<>();
        if (activity.getIcon() != null)
            for (IconModule tmp : activity.getIcon())
                images.add(IconUrl.moduleIconUrlOfActivity(IconUrl.OrganizationActivities, activity.getId(), tmp.getFileName()));
        bpActivityImages.setData(images);

        rvQuestionList.setLayoutManager(new LinearLayoutManager(App.context, LinearLayoutManager.VERTICAL, false));
        rvQuestionList.addItemDecoration(new NormalItemDecoration(17));
        List<NestedReply> nestedReplies = new ArrayList<>();
        setReplyData(nestedReplies, false);

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
                    inputSendPopupWindow.setOnSendListener(new InputSendPopupWindow.OnSendListener() {
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
            rvQuestionList.setAdapter(nestedReplyAdapter);
        } else if (isLoadMore) {
            nestedReplyAdapter.addAll(nestedReplies);
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

    private void getActivityDetail() {
        mPresenter.getActivityDetail(mActivityId);
    }

    public void showDetail(OrganizationActivity activity) {
        actvActName.setText(activity.getTitle());
        String[] split = activity.getCategoryId().split("#activity-category.");
        if (split.length > 0)
            actvActType.setText(getString(R.string.activity_type_, split[1]));
        if (!TextUtils.isEmpty(activity.getRepeatedDesc())) {
            actvActTime.setText(getString(R.string.activity_time_, activity.getRepeatedDesc()));
            llactvActStart.setVisibility(View.GONE);
        } else {
            actvActTime.setText(getString(R.string.activity_time_, StringUtils.IOS2ToUTC(activity.getStartTime())));
            llactvActStart.setVisibility(View.VISIBLE);
        }
        actvActAddress.setText(getString(R.string.activity_address_, activity.getAddress()));
        String repeat = TextUtils.isEmpty(activity.getRepeatedDesc()) ?
                getString(R.string.single) : getString(R.string.cyclical);
        actvActRepeat.setText(getString(R.string.activity_repeat_, repeat));
        String numPeople = activity.getMaxPartner() == 0 ? "不限" : activity.getMaxPartner() + "人";
        actvActNumPeople.setText(getString(R.string.targart_partin_, numPeople));
        actvActCreator.setText(getString(R.string.activit_creator_, activity.getCreatorName()));
        actvActContent.setText(activity.getDescription());
        String signUpNumber;
        if (activity.getPartnerList() == null)
            signUpNumber = "0";
        else {
            signUpNumber = activity.getPartnerList().size() + "";
            isSignUp = activity.getPartnerList().contains(User.getUserId());
        }
        acbtJoinedNumber.setText(getString(R.string.has_sign_up, signUpNumber));

        isMaster = User.getUserId().equals(activity.getMasterId());
        llQuestionList.setVisibility(View.GONE);
        showButton(isMaster, isSignUp);
    }

    /**
     * 1.判断是否是创建者， 创建人只显示参与者列表；
     * 2.不是创建人，  判断是否报名
     * 3.未报名 - 显示 报名、提问
     * 4.已报名 - 显示 退出、参与者列表
     *
     * @param isCreator
     * @param isCreator
     */
    public void showButton(boolean isCreator, boolean isSignUp) {
        if (isCreator) {
            acbtSignUp.setVisibility(View.GONE);
            acbtSignUpBack.setVisibility(View.GONE);
            acbtQuestion.setVisibility(View.GONE);
            acbtSignUpList.setVisibility(View.VISIBLE);
            return;
        }
        if (!isSignUp) {
            acbtSignUp.setVisibility(View.VISIBLE);
            acbtQuestion.setVisibility(View.VISIBLE);
            acbtSignUpBack.setVisibility(View.GONE);
            acbtSignUpList.setVisibility(View.GONE);
            return;
        }

        acbtSignUp.setVisibility(View.GONE);
        acbtQuestion.setVisibility(View.GONE);
        acbtSignUpBack.setVisibility(View.VISIBLE);
        acbtSignUpList.setVisibility(View.VISIBLE);
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
        inputSendPopupWindow.clearInput();
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
        refreshActivityData();
        activity.getPartnerList().add(User.getUserId());
        showDetail(activity);
    }

    @Override
    public void sucQuitActivity() {
        refreshActivityData();
        activity.getPartnerList().remove(User.getUserId());
        showDetail(activity);
    }


    @OnClick({R.id.acbt_sign_up, R.id.acbt_sign_up_back, R.id.acbt_question, R.id.acbt_sign_up_list, R.id.tv_more_question})
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.acbt_sign_up:
                joinActivity();
                break;


            case R.id.acbt_sign_up_back:
                quitActivity();
                break;
            case R.id.tv_more_question:
            case R.id.acbt_question:
                addQuestion();
                break;
            case R.id.acbt_sign_up_list:

                break;
        }
    }


    private void cancelActivity() {
        new NormalDialog().createNormal(_mActivity, R.string.confirm_cancel_activity, () -> {
            mPresenter.cancelActivity(mActivityId);
        });
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

    private void addQuestion() {
        start(ActivityQuestionListFragment.newInstance(activity));
    }


}
