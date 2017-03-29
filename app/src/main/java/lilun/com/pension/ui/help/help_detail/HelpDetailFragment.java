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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import lilun.com.pension.R;
import lilun.com.pension.app.App;
import lilun.com.pension.app.Constants;
import lilun.com.pension.app.Event;
import lilun.com.pension.app.IconUrl;
import lilun.com.pension.app.User;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.adapter.AidJoinerAdapter;
import lilun.com.pension.module.bean.AidDetail;
import lilun.com.pension.module.bean.IconModule;
import lilun.com.pension.module.bean.OrganizationAid;
import lilun.com.pension.module.bean.OrganizationReply;
import lilun.com.pension.module.utils.Preconditions;
import lilun.com.pension.module.utils.StringUtils;
import lilun.com.pension.module.utils.UIUtils;
import lilun.com.pension.ui.help.RankFragment;
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
public class HelpDetailFragment extends BaseFragment<HelpDetailContract.Presenter> implements HelpDetailContract.View, View.OnClickListener {


    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.swipe_layout)
    SwipeRefreshLayout swipeLayout;
    private OrganizationAid mAid;
    private TextView tvChangeStatus;
    private TextView tvTitle;
    private TextView tvTime;
    private TextView tvCreator;
    private TextView tvPhone;
    private TextView tvAddress;

    private boolean mCreatorIsOwn;
    private List<OrganizationReply> mDetailData = new ArrayList<>();
    private View mHeadView;
    private TextView tvPrice;
    private AidJoinerAdapter mReplyAdapter;
    private TextView tvJoinerTitle;
    private ImageView ivAvatar;
    private BannerPager banner;
    private String mReplyId;
    private String mAidId;

    public static HelpDetailFragment newInstance(String aidId) {
        HelpDetailFragment fragment = new HelpDetailFragment();
        Bundle args = new Bundle();
        args.putString("adiId", aidId);
        fragment.setArguments(args);
        return fragment;
    }


    @Subscribe
    public void RefreshHelpDetail(Event.RefreshHelpDetail event) {
        getDetail();
    }

    @Override
    protected void getTransferData(Bundle arguments) {
        mAidId = arguments.getString("adiId");
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
        ivAvatar = (ImageView) mHeadView.findViewById(R.id.iv_avatar);
        mHeadView.findViewById(R.id.iv_back).setOnClickListener(this);

        //banner
        banner = (BannerPager) mHeadView.findViewById(R.id.iv_icon);


        tvChangeStatus = (TextView) mHeadView.findViewById(R.id.tv_reservation);
        tvChangeStatus.setOnClickListener(this);

        //标题加粗
        tvTitle = (TextView) mHeadView.findViewById(R.id.tv_item_title);
        setBold(tvTitle);


        //各种信息
        tvTime = (TextView) mHeadView.findViewById(R.id.tv_aid_time);
        tvPrice = (TextView) mHeadView.findViewById(R.id.tv_mobile);
        tvCreator = (TextView) mHeadView.findViewById(R.id.tv_aid_creator);

        //参与者或者回答者列表的title
        tvJoinerTitle = (TextView) mHeadView.findViewById(R.id.tv_joiner_title);
        tvJoinerTitle.setText(getString(R.string.joiners));
        UIUtils.setBold(tvJoinerTitle);

        //  只有 "帮" 才具有的=============================
        tvPhone = (TextView) mHeadView.findViewById(R.id.tv_aid_phone);
        tvAddress = (TextView) mHeadView.findViewById(R.id.tv_aid_address);
        TextView tvAddressTitle = (TextView) mHeadView.findViewById(R.id.tv_environment_title);
        setBold(tvAddressTitle);


        ImageLoaderUtil.instance().loadImage(IconUrl.account(User.getUserId(), null), R.drawable.avatar, ivAvatar);

        setJoinerAdapter();

    }

    private void setBold(TextView textView) {
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD_ITALIC);
    }


    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        getDetail();
    }


    private void getDetail() {
        mPresenter.getHelpDetail(_mActivity, mAidId);
    }

    /**
     * 设置参与者列表数据
     */
    private void setJoinerAdapter() {
        recyclerView.setLayoutManager(new LinearLayoutManager(App.context, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new NormalItemDecoration(17));
        mReplyAdapter = new AidJoinerAdapter(this, mDetailData);
        mReplyAdapter.setOnFunctionClickListener(new AidJoinerAdapter.OnFunctionClickListener() {
            @Override
            public void agree(String id) {
                mPresenter.acceptOneReply(mAidId, id, mAid.getKind());
            }

            @Override
            public void evaluation(String replyId) {
                start(RankFragment.newInstance(Constants.organizationAid, mAidId));
            }
        });
        mReplyAdapter.addHeaderView(mHeadView);
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
        mCreatorIsOwn = User.creatorIsOwn(mAid.getCreatorId());

        //显示图片
        List<String> urls = new ArrayList<>();
        if (mAid.getPicture() != null) {
            for (IconModule iconModule : mAid.getPicture()) {
                String url = IconUrl.organizationAid(mAid.getId(), iconModule.getFileName());
                urls.add(url);
            }
        } else {
            String url = IconUrl.organizationAid(mAid.getId(), null);
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


        //设置地址和电话
        tvAddress.setText(mAid.getAddress());
        tvPhone.setText(String.format("联系电话：%1$s", "13206011223"));

        //是自己创建的
        if (mCreatorIsOwn) {
            if (detail.isCancelable()) {
                setChangeStatus(true, getString(R.string.cancel));
            }
        }
        //不是自己创建的
        else {
            if (TextUtils.isEmpty(mAid.getAnswerId())) {
                boolean isCancelReply = false;
                if (detail.getReplyList() != null) {
                    for (OrganizationReply reply : detail.getReplyList()) {
                        if (reply.getCreatorId().equals(User.getUserId())) {
                            mReplyId = reply.getId();
                            isCancelReply = true;
                            setChangeStatus(true, getString(R.string.cancel));
                        }
                    }
                }
                setChangeStatus(true, isCancelReply ? getString(R.string.cancel) : getString(R.string.help));
            }
        }


        //设置回答者列表
        List<OrganizationReply> replies = detail.getReplyList();
        if (replies == null || replies.size() == 0 || !mCreatorIsOwn) {
            tvJoinerTitle.setVisibility(View.GONE);
        } else {
            //如果已经接受某人的帮忙，就只显示那一条数据
            String answerId = mAid.getAnswerId();
            mReplyAdapter.replaceAll(acceptOneAnswer(replies, answerId));
        }

    }

    @Override
    public void acceptSuccess(String replyId) {
        acceptOneAnswer(mReplyAdapter.getData(), replyId);
        mReplyAdapter.notifyDataSetChanged();
        setChangeStatus(false, null);
    }

    /**
     * 采纳某一条数据
     */
    private List<OrganizationReply> acceptOneAnswer(List<OrganizationReply> replies, String answerId) {
        if (!TextUtils.isEmpty(answerId)) {
            for (int i = 0; i < replies.size(); i++) {
                if (!replies.get(i).getId().equals(answerId)) {
                    replies.remove(i);
                    i--;
                }
            }
        }
        mReplyAdapter.setAnswerId(answerId, mAid.getRankId());
        return replies;
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
            if (mCreatorIsOwn) {
                new NormalDialog().createNormal(_mActivity, R.string.confirm_delete_help, () -> {
                    mPresenter.deleteAid(mAid.getId());
                });
            } else {
                new NormalDialog().createNormal(_mActivity, R.string.confirm_cancel_help, () -> {
                    mPresenter.cancelReply(mReplyId);
                });
            }

        } else if (status.equals(getString(R.string.help))) {
            helpOther();
        }
    }

    private void helpOther() {
        new NormalDialog().createNormal(_mActivity, R.string.confirm_help_other, () -> {
            mPresenter.createHelpReply(mAid.getId(), null);
        });
    }
}
