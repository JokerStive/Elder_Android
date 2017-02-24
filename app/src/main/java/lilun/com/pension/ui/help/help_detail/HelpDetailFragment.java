package lilun.com.pension.ui.help.help_detail;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import lilun.com.pension.R;
import lilun.com.pension.app.App;
import lilun.com.pension.app.Event;
import lilun.com.pension.app.User;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.adapter.AidHelperListAdapter;
import lilun.com.pension.module.bean.OrganizationAid;
import lilun.com.pension.module.bean.OrganizationReply;
import lilun.com.pension.module.utils.Preconditions;
import lilun.com.pension.module.utils.StringUtils;
import lilun.com.pension.module.utils.UIUtils;
import lilun.com.pension.widget.NormalItemDecoration;

/**
 * 互助详情V
 *
 * @author yk
 *         create at 2017/2/20 9:34
 *         email : yk_developer@163.com
 */
public class HelpDetailFragment extends BaseFragment<HelpDetailContract.Presenter> implements HelpDetailContract.View, View.OnClickListener {


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
    private TextView tvPhone;
    private TextView tvAddress;

    private int status_new = 0;
    private int status_answered = 1;
    private int status_solved = 2;
    private boolean mCreatorIsOwn;
    private int mStatus;
    private List<OrganizationReply> mDetailData = new ArrayList<>();
    private View mHeadView;
    private TextView tvPrice;
    private AidHelperListAdapter mReplyAdapter;
    private TextView tvJoinerTitle;

    public static HelpDetailFragment newInstance(OrganizationAid aid) {
        HelpDetailFragment fragment = new HelpDetailFragment();
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
        mHeadView.findViewById(R.id.iv_back).setOnClickListener(this);


        tvChangeStatus = (TextView) mHeadView.findViewById(R.id.tv_reservation);
        tvChangeStatus.setOnClickListener(this);

        //标题加粗
        tvTitle = (TextView) mHeadView.findViewById(R.id.tv_item_title);
        setBold(tvTitle);

        tvTime = (TextView) mHeadView.findViewById(R.id.tv_aid_time);
        tvPrice = (TextView) mHeadView.findViewById(R.id.tv_price);
        tvCreator = (TextView) mHeadView.findViewById(R.id.tv_aid_creator);

        //参与者或者回答者列表的title
        tvJoinerTitle = (TextView) mHeadView.findViewById(R.id.tv_joiner_title);
        tvJoinerTitle.setText(getString(R.string.joiners));
        UIUtils.setBold(tvJoinerTitle);

        //  只有 "帮" 才具有的=============================
        tvPhone = (TextView) mHeadView.findViewById(R.id.tv_aid_phone);
        tvAddress = (TextView) mHeadView.findViewById(R.id.tv_aid_address);
        TextView tvAddressTitle = (TextView) mHeadView.findViewById(R.id.tv_address_title);
        setBold(tvAddressTitle);


        //是自己创建的
        if (mCreatorIsOwn) {
            if (mStatus == status_new || mStatus == status_answered) {
                setChangeStatus(getString(R.string.cancel));
            }
        }
        //不是自己创建的
        else {
            if (mStatus == status_new || mStatus == status_answered) {
                if (mAid.isCancelable()){
                    setChangeStatus(getString(R.string.cancel));
                }else {
                    setChangeStatus(getString(R.string.help));
                }
            }
        }


        setJoinerAdapter();

    }

    private void setBold(TextView textView) {
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD_ITALIC);
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
        recyclerView.addItemDecoration(new NormalItemDecoration(17));
        mReplyAdapter = new AidHelperListAdapter(this, mDetailData);
        mReplyAdapter.setOnFunctionClickListener(new AidHelperListAdapter.OnFunctionClickListener() {
            @Override
            public void agree(String id) {
                accept(mReplyAdapter.getData(),id);
                mReplyAdapter.notifyDataSetChanged();
            }

            @Override
            public void evaluation() {

            }
        });
        mReplyAdapter.addHeaderView(mHeadView);
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


        //设置地址和电话
        tvAddress.setText(aid.getAddress());
        tvPhone.setText(String.format("联系电话：%1$s", "13206011223"));


        //设置回答者列表
        List<OrganizationReply> replies = aid.getReplies();
        if (replies == null || replies.size() == 0 || !mCreatorIsOwn) {
            tvJoinerTitle.setVisibility(View.GONE);
        } else {
            //如果已经接受某人的帮忙，就只显示那一条数据
            String answerId = aid.getAnswerId();
            accept(replies, answerId);
            mReplyAdapter.addAll(replies);
        }

    }

    /**
    *采纳某一条数据
    */
    private void accept(List<OrganizationReply> replies, String answerId) {
        if (!TextUtils.isEmpty(answerId)){
            for(int i=0;i<replies.size();i++){
                if (!replies.get(i).getId().equals(answerId)){
                    replies.remove(i);
                    i--;
                }
            }
        }
        mReplyAdapter.setAgree(false,answerId);
    }

    @Override
    public void replySuccess() {
        EventBus.getDefault().post(new Event.RefreshHelpData());
        pop();
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
            if (mCreatorIsOwn) {
                //TODO 改变这条help的状态为"已取消"
                Logger.d("delete this aid");
            } else {
                //TODO 不再帮助
                Logger.d("cancel help");
            }

        } else if (status.equals(getString(R.string.help))) {
            helpOther();
        }
    }

    private void helpOther() {
        new MaterialDialog.Builder(_mActivity)
                .content(R.string.confirm_help_other)
                .positiveText(R.string.confirm)
                .negativeText(R.string.cancel)
                .onPositive((dialog1, which) -> {
                    mPresenter.createHelpReply(mAid.getId(), null);
                })
                .onNegative((dialog1, which) -> {
                    dialog1.dismiss();
                })
                .show();
    }
}
