package lilun.com.pension.ui.help.help_detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import lilun.com.pension.R;
import lilun.com.pension.app.App;
import lilun.com.pension.app.IconUrl;
import lilun.com.pension.app.User;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.adapter.AidAskListAdapter;
import lilun.com.pension.module.bean.OrganizationAid;
import lilun.com.pension.module.bean.OrganizationReply;
import lilun.com.pension.module.utils.Preconditions;
import lilun.com.pension.module.utils.StringUtils;
import lilun.com.pension.module.utils.UIUtils;

/**
 * 互助详情V
 *
 * @author yk
 *         create at 2017/2/20 9:34
 *         email : yk_developer@163.com
 */
public class AskDetailFragment extends BaseFragment<HelpDetailContract.Presenter> implements HelpDetailContract.View, View.OnClickListener {


    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.swipe_layout)
    SwipeRefreshLayout swipeLayout;
    private OrganizationAid mAid;
    private ImageView ivIcon;
    private TextView tvChangeStatus;
    private TextView tvTitle;
    private TextView tvTime;
    private TextView tvCreator;
    private TextView tvJonerTitle;

    private int status_new = 0;
    private int status_answered = 1;
    private int status_solved = 2;
    private boolean mCreatorIsOwn;
    private int mStatus;
    private List<OrganizationReply> mDetailData = new ArrayList<>();
    private View mHeadView;
    private TextView tvPrice;
    private AidAskListAdapter mReplyAdapter;
    private ImageView ivAvatar;

    public static AskDetailFragment newInstance(OrganizationAid aid) {
        AskDetailFragment fragment = new AskDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("OrganizationAid", aid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void getTransferData(Bundle arguments) {
        mAid = (OrganizationAid) arguments.getSerializable("OrganizationAid");
        Preconditions.checkNull(mAid);
        mCreatorIsOwn = User.creatorIsOwn(mAid.getCreatorId());
        mStatus = mAid.getStatus();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new HelpDetailPresenter();
        mPresenter.bindView(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_recycler_view;
    }

    @Override
    protected void initView(LayoutInflater inflater) {

        swipeLayout.setEnabled(false);

        mHeadView = inflater.inflate(R.layout.head_help_detail, null);

        //不管是问还是帮都具有的
        ivIcon = (ImageView) mHeadView.findViewById(R.id.iv_aid_icon);
        ivAvatar= (ImageView) mHeadView.findViewById(R.id.iv_avatar);
        mHeadView.findViewById(R.id.iv_back).setOnClickListener(this);


        tvChangeStatus = (TextView) mHeadView.findViewById(R.id.tv_reservation);
        tvChangeStatus.setOnClickListener(this);

        //标题加粗
        tvTitle = (TextView) mHeadView.findViewById(R.id.tv_item_title);
        UIUtils.setBold(tvTitle);

        tvTime = (TextView) mHeadView.findViewById(R.id.tv_aid_time);
        tvPrice = (TextView) mHeadView.findViewById(R.id.tv_price);
        tvCreator = (TextView) mHeadView.findViewById(R.id.tv_aid_creator);

        //回答者列表
        tvJonerTitle = (TextView) mHeadView.findViewById(R.id.tv_joiner_title);
        tvJonerTitle.setText(getString(R.string.answer));
        UIUtils.setBold(tvJonerTitle);

        //  只有 "帮" 才具有的=============================
        LinearLayout llTypeControl = (LinearLayout) mHeadView.findViewById(R.id.ll_type_help);
        llTypeControl.setVisibility(View.GONE);


        //是自己创建的
        if (mCreatorIsOwn) {
            if (mStatus == status_new || mStatus == status_answered) {
                setChangeStatus(getString(R.string.cancel));
            }
        }
        //不是自己创建的
        else {
            if (mStatus == status_new || mStatus == status_answered) {
                setChangeStatus(getString(R.string.answer));
            }
        }

        Glide.with(this).load(IconUrl.account(User.getUserId(),null))
                .error(R.drawable.avatar)
                .into(ivAvatar);

        setJoinerAdapter();

    }


    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        mPresenter.getHelpDetail(_mActivity, mAid.getId());
    }

    /**
     * 设置参与者列表数据
     */
    private void setJoinerAdapter() {
        recyclerView.setLayoutManager(new LinearLayoutManager(App.context, LinearLayoutManager.VERTICAL, false));
        mReplyAdapter = new AidAskListAdapter(this, mDetailData,mCreatorIsOwn);
        mReplyAdapter.addHeaderView(mHeadView);
        mReplyAdapter.setOnAgreeClickListenerr(id -> {
            mReplyAdapter.setAnswerId(id);
            mReplyAdapter.notifyDataSetChanged();
        });
        recyclerView.setAdapter(mReplyAdapter);
    }

    /**
     * 根据状态，设置按钮的显示与否
     */
    private void setChangeStatus(String string) {
        if (tvChangeStatus.getVisibility() == View.GONE) {
            tvChangeStatus.setVisibility(View.VISIBLE);
        }
        tvChangeStatus.setText(string);
    }

    @Override
    public void showHelpDetail(OrganizationAid aid) {

        //标题、时间
        tvTitle.setText(aid.getTitle());
        tvTime.setText(StringUtils.timeFormat(aid.getCreatedAt()));


        //显示补贴
        int price = aid.getPrice();
        if (price == 0) {
            tvPrice.setVisibility(View.INVISIBLE);
        } else {
            tvPrice.setText(String.format(getString(R.string.format_price), aid.getPrice()));
        }

        //显示发起人
        tvCreator.setText(String.format(getString(R.string.format_creator), aid.getCreatorName()));


        //设置回答者列表
        List<OrganizationReply> replies = aid.getReplies();
        if (replies == null || replies.size() == 0) {
            tvJonerTitle.setVisibility(View.GONE);
        } else {
            mReplyAdapter.setAnswerId(aid.getAnswerId());
            mReplyAdapter.addAll(replies);
        }

    }

    @Override
    public void replySuccess() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                pop();
                break;
            case R.id.tv_reservation:
                doNext();
                break;
        }
    }


    private void doNext() {
        String status = tvChangeStatus.getText().toString();
        if (status.equals(getString(R.string.cancel))) {
            //TODO 取消这条求助信息
            Logger.d("delete this aid");

        } else if (status.equals(getString(R.string.answer))) {
            //TODO 弹出键盘回答
            Logger.d("answer this aid");
        }
    }
}
