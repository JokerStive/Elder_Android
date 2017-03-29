package lilun.com.pension.ui.activity.activity_detail;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import lilun.com.pension.R;
import lilun.com.pension.app.App;
import lilun.com.pension.app.Constants;
import lilun.com.pension.app.Event;
import lilun.com.pension.app.IconUrl;
import lilun.com.pension.app.User;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.adapter.NestedReplyAdapter;
import lilun.com.pension.module.bean.ActivityDetail;
import lilun.com.pension.module.bean.IconModule;
import lilun.com.pension.module.bean.NestedReply;
import lilun.com.pension.module.bean.OrganizationActivity;
import lilun.com.pension.module.utils.Preconditions;
import lilun.com.pension.module.utils.StringUtils;
import lilun.com.pension.module.utils.UIUtils;
import lilun.com.pension.ui.help.reply.ReplyFragment;
import lilun.com.pension.widget.CircleImageView;
import lilun.com.pension.widget.NormalDialog;
import lilun.com.pension.widget.NormalItemDecoration;
import lilun.com.pension.widget.image_loader.ImageLoaderUtil;
import lilun.com.pension.widget.slider.BannerPager;

/**
 * Created by zp on 2017/3/6.
 */

public class ActivityDetailFragment extends BaseFragment<ActivityDetailContact.Presenter> implements ActivityDetailContact.View, View.OnClickListener {


    private SwipeRefreshLayout mSwipeLayout;
    private TextView tvQuestTitle;
    private LinearLayout llContent;

    private void findView() {
        iconBanner = (BannerPager) mHeadView.findViewById(R.id.bp_actvity_icon);
        ivBack = (ImageView) mHeadView.findViewById(R.id.iv_back);
        tvActivityName = (TextView) mHeadView.findViewById(R.id.tv_activity_name);
        tvActivityTime = (TextView) mHeadView.findViewById(R.id.tv_activity_time);
        tvRequireTitle = (TextView) mHeadView.findViewById(R.id.tv_require_title);
        tvOriginatorPerson = (TextView) mHeadView.findViewById(R.id.tv_originator_person);
        tvParticipationRequest = (TextView) mHeadView.findViewById(R.id.tv_participation_request);
        tvAddressTitle = (TextView) mHeadView.findViewById(R.id.tv_environment_title);
        tvActivityAddress = (TextView) mHeadView.findViewById(R.id.tv_activity_address);
        tvQuestTitle = (TextView) mHeadView.findViewById(R.id.tv_activity_question_title);
        cigConnectIcon = (CircleImageView) mHeadView.findViewById(R.id.cig_connect_icon);

        question = (Button) mHeadView.findViewById(R.id.question);
        joinIn = (Button) mHeadView.findViewById(R.id.join_in);
        cancel = (Button) mHeadView.findViewById(R.id.cancel);

        llContent = (LinearLayout) mHeadView.findViewById(R.id.ll_content);

    }


    String mActivityId;

    String mActivityTitle;

    BannerPager iconBanner;

    ImageView ivBack;

    Button question;

    Button joinIn;

    Button cancel;

    TextView tvActivityName;

    TextView tvActivityTime;

    TextView tvOriginatorPerson;

    CircleImageView cigConnectIcon;

    TextView tvRequireTitle;

    TextView tvParticipationRequest;

    TextView tvAddressTitle;

    TextView tvActivityAddress;

    private RecyclerView rvReply;
    private View mHeadView;
    private NestedReplyAdapter nestedReplyAdapter;

    public static ActivityDetailFragment newInstance(String activityId) {
        ActivityDetailFragment fragment = new ActivityDetailFragment();
        Bundle args = new Bundle();
        args.putString("activityId", activityId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void getTransferData(Bundle arguments) {
        mActivityId = arguments.getString("activityId");
        Preconditions.checkNull(mActivityId);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ActivityDetailPresenter();
        mPresenter.bindView(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_recycler_view;
    }

    @Override
    protected void initView(LayoutInflater inflater) {

        rvReply = (RecyclerView) mRootView.findViewById(R.id.recycler_view);
        mSwipeLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.swipe_layout);
        mHeadView = inflater.inflate(R.layout.fragment_activity_detail, null);
        mSwipeLayout.setOnRefreshListener(this::getActivityDetail);
        findView();

        UIUtils.setBold(tvRequireTitle);
        UIUtils.setBold(tvAddressTitle);
        UIUtils.setBold(tvActivityName);
        UIUtils.setBold(tvQuestTitle);


        ivBack.setOnClickListener(this);
        joinIn.setOnClickListener(this);
        question.setOnClickListener(this);
        cancel.setOnClickListener(this);


        rvReply.setLayoutManager(new LinearLayoutManager(App.context, LinearLayoutManager.VERTICAL, false));
        rvReply.addItemDecoration(new NormalItemDecoration(17));
        List<NestedReply> nestedReplies = new ArrayList<>();
        setReplyData(nestedReplies, false);

    }


    /**
     * 显示提问列表
     */
    private void setReplyData(List<NestedReply> nestedReplies, boolean isLoadMore) {
        if (nestedReplyAdapter == null) {
            nestedReplyAdapter = new NestedReplyAdapter(this, nestedReplies);
            nestedReplyAdapter.addHeaderView(mHeadView);
            rvReply.setAdapter(nestedReplyAdapter);
        } else if (isLoadMore) {
            nestedReplyAdapter.addAll(nestedReplies);
        } else {
            nestedReplyAdapter.replaceAll(nestedReplies);
        }
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        getActivityDetail();
    }

    private void getActivityDetail() {
        mPresenter.getActivityDetail(mActivityId);
    }

    @Override
    public void showActivityDetail(ActivityDetail activityDetail) {
        completeRefresh();
        OrganizationActivity activity = activityDetail.getActivity();
        if (activity == null) {
            return;
        }

        //如果是自己创建的不能提问不能报名
        if (User.creatorIsOwn(activity.getCreatorId())) {
            cancel.setVisibility(View.VISIBLE);
            joinIn.setVisibility(View.GONE);
            question.setVisibility(View.GONE);
        } else {
            cancel.setVisibility(View.GONE);
            joinIn.setVisibility(View.VISIBLE);
            question.setVisibility(View.VISIBLE);
        }


        //显示活动图片
        List<String> urls = new ArrayList<>();
        if (activity.getIcon() != null) {
            for (IconModule iconModule : activity.getIcon()) {
                String url = IconUrl.organizationActivies(activity.getId(), iconModule.getFileName());
                urls.add(url);
            }
        } else {
            String url = IconUrl.organizationActivies(activity.getId(), null);
            urls.add(url);
        }
        iconBanner.setData(urls);

        ///显示活动创建头像
        ImageLoaderUtil.instance().loadImage(IconUrl.account(activity.getCreatorId(), null), R.drawable.avatar, cigConnectIcon);

        mActivityTitle = activity.getTitle();
        tvActivityName.setText(activity.getTitle() + getRepeatedType(activity.getRepeatedType()));
        tvActivityTime.setText(getString(R.string.activity_time_, StringUtils.IOS2ToUTC(activity.getStartTime())));
        tvOriginatorPerson.setText(getString(R.string.originator_person_, StringUtils.filterNull(activity.getCreatorName())));
        tvParticipationRequest.setText(StringUtils.filterNull(activity.getDescription()));
        tvActivityAddress.setText(StringUtils.filterNull(activity.getAddress()));

        //是否还能参加
        if (activityDetail.isIsRegisterActivity()) {
            joinIn.setText(getString(R.string.quit));
            joinIn.setTextColor(Color.WHITE);
            joinIn.setBackgroundResource(R.drawable.shape_circle_red);
        }

        List<NestedReply> replyList = activityDetail.getReplyList();
        if (replyList != null && replyList.size() != 0) {
            tvQuestTitle.setVisibility(View.VISIBLE);
            setReplyData(replyList, false);
        }

        llContent.setVisibility(View.VISIBLE);

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
        pop();
    }

    String getRepeatedType(String type) {
        String ret = "";
        if ("daily".equals(type)) {
            ret = "(每天)";
        } else if ("weekly".equals(type)) {
            ret = "(每周)";
        } else if ("monthly".equals(type)) {
            ret = "(每月)";
        } else if ("yearly".equals(type)) {
            ret = "(每年)";
        }
        return ret;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                pop();
                break;

            case R.id.question:
                start(ReplyFragment.newInstance(Constants.organizationActivity, mActivityId, mActivityTitle, true));
                break;


            case R.id.join_in:
                joinActivity();
                break;

            case R.id.cancel:
                cancelActivity();
                break;
        }
    }

    private void cancelActivity() {
        new NormalDialog().createNormal(_mActivity, R.string.confirm_cancel_activity, () -> {
            mPresenter.cancelActivity(mActivityId);
        });
    }

    private void joinActivity() {
        if (TextUtils.equals(getString(R.string.join), joinIn.getText().toString())) {
            new NormalDialog().createNormal(_mActivity, R.string.confirm_join_activity, () -> {
                mPresenter.joinActivity(mActivityId);
            });
        } else if (TextUtils.equals(getString(R.string.quit), joinIn.getText().toString())) {
            new NormalDialog().createNormal(_mActivity, R.string.confirm_quite_activity, () -> {
                mPresenter.quitActivity(mActivityId);
            });
        }
    }


}
