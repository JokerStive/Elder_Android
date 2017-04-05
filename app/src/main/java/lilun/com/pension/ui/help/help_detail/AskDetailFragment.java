package lilun.com.pension.ui.help.help_detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import lilun.com.pension.R;
import lilun.com.pension.app.App;
import lilun.com.pension.app.Event;
import lilun.com.pension.app.IconUrl;
import lilun.com.pension.app.User;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.adapter.AidReplyAdapter;
import lilun.com.pension.module.bean.AidDetail;
import lilun.com.pension.module.bean.IconModule;
import lilun.com.pension.module.bean.OrganizationAid;
import lilun.com.pension.module.bean.OrganizationReply;
import lilun.com.pension.module.utils.Preconditions;
import lilun.com.pension.module.utils.StringUtils;
import lilun.com.pension.module.utils.UIUtils;
import lilun.com.pension.ui.help.reply.ReplyFragment;
import lilun.com.pension.widget.NormalDialog;
import lilun.com.pension.widget.NormalItemDecoration;
import lilun.com.pension.widget.image_loader.ImageLoaderUtil;
import lilun.com.pension.widget.slider.BannerPager;

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
    private TextView tvChangeStatus;
    private TextView tvTitle;
    private TextView tvTime;
    private TextView tvCreator;
    private TextView tvJonerTitle;

    private boolean mCreatorIsOwn;
    private List<OrganizationReply> mDetailData = new ArrayList<>();
    private View mHeadView;
    private TextView tvPrice;
    private AidReplyAdapter mReplyAdapter;
    private ImageView ivAvatar;
    private BannerPager banner;
    private String mAidId;

    public static AskDetailFragment newInstance(String aidId,boolean creatorIsOwn) {
        AskDetailFragment fragment = new AskDetailFragment();
        Bundle args = new Bundle();
        args.putString("adiId", aidId);
        args.putBoolean("creatorIsOwn", creatorIsOwn);
        fragment.setArguments(args);
        return fragment;
    }

    @Subscribe
    public void refreshReplies(Event.RefreshHelpReply event) {
        if (mReplyAdapter != null && event != null) {
            mReplyAdapter.add(event.reply);
            if (tvJonerTitle.getVisibility() == View.GONE) {
                tvJonerTitle.setVisibility(View.VISIBLE);
            }

        }
    }

    @Override
    protected void getTransferData(Bundle arguments) {
        mAidId = arguments.getString("adiId");
        mCreatorIsOwn = arguments.getBoolean("creatorIsOwn",false);
        Preconditions.checkNull(mAidId);
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
        banner = (BannerPager) mHeadView.findViewById(R.id.iv_icon);
        ivAvatar = (ImageView) mHeadView.findViewById(R.id.iv_avatar);
        mHeadView.findViewById(R.id.iv_back).setOnClickListener(this);


        tvChangeStatus = (TextView) mHeadView.findViewById(R.id.tv_reservation);
        tvChangeStatus.setOnClickListener(this);

        //标题加粗
        tvTitle = (TextView) mHeadView.findViewById(R.id.tv_item_title);
        UIUtils.setBold(tvTitle);

        tvTime = (TextView) mHeadView.findViewById(R.id.tv_aid_time);
        tvPrice = (TextView) mHeadView.findViewById(R.id.tv_mobile);
        tvCreator = (TextView) mHeadView.findViewById(R.id.tv_aid_creator);

        //回答者列表
        tvJonerTitle = (TextView) mHeadView.findViewById(R.id.tv_joiner_title);
        tvJonerTitle.setText(getString(R.string.answer));
        UIUtils.setBold(tvJonerTitle);

        //  只有 "帮" 才具有的=============================
        LinearLayout llTypeControl = (LinearLayout) mHeadView.findViewById(R.id.ll_type_help);
        llTypeControl.setVisibility(View.GONE);


        ImageLoaderUtil.instance().loadImage(IconUrl.moduleIconUrl(IconUrl.Accounts,User.getUserId(), null), R.drawable.icon_def, ivAvatar);

        setJoinerAdapter();

    }


    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        mPresenter.getHelpDetail(_mActivity, mAidId);

    }

    /**
     * 设置参与者列表数据
     */
    private void setJoinerAdapter() {
        recyclerView.setLayoutManager(new LinearLayoutManager(App.context, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new NormalItemDecoration(17));
        mReplyAdapter = new AidReplyAdapter(this, mDetailData, mCreatorIsOwn);
        mReplyAdapter.addHeaderView(mHeadView);
        mReplyAdapter.setOnAgreeClickListenerr(id -> {
            mPresenter.acceptOneReply(mAid.getId(), id, mAid.getKind());
        });
        recyclerView.setAdapter(mReplyAdapter);
    }

    /**
     * 根据状态，设置按钮的显示与否
     */
    private void setChangeStatus(boolean isShow, String string) {
        tvChangeStatus.setVisibility(isShow ? View.VISIBLE : View.GONE);
        if (!TextUtils.isEmpty(string)) {
            tvChangeStatus.setText(string);
        }
    }

    @Override
    public void showHelpDetail(AidDetail detail) {
        mAid = detail.getAid();
//        mCreatorIsOwn = User.creatorIsOwn(mAid.getCreatorId());

        //显示aid图片
        List<String> urls = new ArrayList<>();
        if (mAid.getImage() != null) {
            for (IconModule iconModule : mAid.getImage()) {
                String url = IconUrl.moduleIconUrl(IconUrl.OrganizationAids,mAid.getId(), iconModule.getFileName());
                urls.add(url);
            }
        }

        else {
            String url = IconUrl.moduleIconUrl(IconUrl.OrganizationAids,mAid.getId(), null);
            urls.add(url);
        }
        banner.setData(urls);

        //标题、时间
        tvTitle.setText(mAid.getTitle());
        tvTime.setText(StringUtils.timeFormat(mAid.getCreatedAt()));


        //显示补贴
        int price = mAid.getPrice();
        if (price == 0) {
            tvPrice.setVisibility(View.INVISIBLE);
        } else {
            tvPrice.setText(String.format(getString(R.string.format_subsidy), mAid.getPrice()));
        }

        //显示发起人
        tvCreator.setText(String.format(getString(R.string.format_creator), mAid.getCreatorName()));


        //是自己创建的
        if (mCreatorIsOwn) {
            if (detail.isCancelable()) {
                setChangeStatus(true, getString(R.string.cancel));
            }
        }
        //不是自己创建的
        else {
            if (TextUtils.isEmpty(mAid.getAnswerId())) {
                setChangeStatus(true, getString(R.string.answer));
            }
        }

        //设置回答者列表
        List<OrganizationReply> replies = detail.getReplyList();
        if (replies == null || replies.size() == 0) {
            tvJonerTitle.setVisibility(View.GONE);
        } else {
            mReplyAdapter.setAnswerId(mAid.getAnswerId());
            mReplyAdapter.addAll(replies);
        }

    }

    @Override
    public void acceptSuccess(String replyId) {
        mReplyAdapter.setAnswerId(replyId);
        mReplyAdapter.notifyDataSetChanged();
        setChangeStatus(false, null);
    }

    @Override
    public void refreshData() {
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
            new NormalDialog().createNormal(_mActivity, R.string.confirm_delete_help, () -> {
                mPresenter.deleteAid(mAid.getId());
            });


        } else if (status.equals(getString(R.string.answer))) {
            start(ReplyFragment.newInstance("OrganizationAid",mAid.getId(),mAid.getTitle(),true));
        }
    }
}
