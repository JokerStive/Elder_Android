package lilun.com.pensionlife.ui.help.help_detail;

import android.graphics.Typeface;
import android.os.Bundle;
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
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.app.Event;
import lilun.com.pensionlife.app.IconUrl;
import lilun.com.pensionlife.app.User;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.adapter.AidJoinerAdapter;
import lilun.com.pensionlife.module.bean.AidDetail;
import lilun.com.pensionlife.module.bean.OrganizationAid;
import lilun.com.pensionlife.module.bean.OrganizationReply;
import lilun.com.pensionlife.module.utils.Preconditions;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.module.utils.UIUtils;
import lilun.com.pensionlife.widget.NormalDialog;
import lilun.com.pensionlife.widget.NormalItemDecoration;
import lilun.com.pensionlife.widget.image_loader.ImageLoaderUtil;
import lilun.com.pensionlife.widget.slider.BannerPager;

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
    private TextView tvHelp;
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
    private LinearLayout llContent;
    private TextView tvContent;

    public static HelpDetailFragment newInstance(String aidId) {
        HelpDetailFragment fragment = new HelpDetailFragment();
        Bundle args = new Bundle();
        args.putString("adiId", aidId);
        fragment.setArguments(args);
        return fragment;
    }


    @Subscribe
    public void RefreshHelpDetail(Event.AfterRank event) {
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


        tvHelp = (TextView) mHeadView.findViewById(R.id.tv_reservation);
        tvHelp.setOnClickListener(this);

        //标题加粗
        tvTitle = (TextView) mHeadView.findViewById(R.id.tv_aid_title);
        setBold(tvTitle);


        //各种信息
        tvTime = (TextView) mHeadView.findViewById(R.id.tv_aid_time);
        tvPrice = (TextView) mHeadView.findViewById(R.id.tv_aid_price);
        tvCreator = (TextView) mHeadView.findViewById(R.id.tv_aid_creator);


        //内容
        llContent = (LinearLayout) mHeadView.findViewById(R.id.ll_content);
        TextView tvContentTitle = (TextView) mHeadView.findViewById(R.id.tv_content_title);
        UIUtils.setBold(tvContentTitle);
        tvContent = (TextView) mHeadView.findViewById(R.id.tv_content);

        //参与者或者回答者列表的title
        tvJoinerTitle = (TextView) mHeadView.findViewById(R.id.tv_joiner_title);
        tvJoinerTitle.setText(getString(R.string.joiners));
        UIUtils.setBold(tvJoinerTitle);

        //  只有 "帮" 才具有的=============================
        tvPhone = (TextView) mHeadView.findViewById(R.id.tv_aid_phone);
        tvAddress = (TextView) mHeadView.findViewById(R.id.tv_aid_address);
        TextView tvAddressTitle = (TextView) mHeadView.findViewById(R.id.tv_environment_title);
        setBold(tvAddressTitle);

        setJoinerAdapter();


        //如果说商家，屏蔽忙帮
        if (!User.isCustomer()) {
//            setChangeStatus(false,null);
        }

    }

    private void setBold(TextView textView) {
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD_ITALIC);
    }


    @Override
    protected void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
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
                mPresenter.acceptOneReply(mAidId, id);
            }

            @Override
            public void evaluation(String replyId) {
//                start(RankFragment.newInstance(Constants.organizationAid, mAidId));
            }
        });
        mReplyAdapter.addHeaderView(mHeadView);
        recyclerView.setAdapter(mReplyAdapter);
    }

    /**
     * 根据状态，设置按钮的显示与否
     */
    private void setChangeStatus(boolean isShow, String string) {
        tvHelp.setVisibility(isShow ? View.VISIBLE : View.GONE);
        if (!TextUtils.isEmpty(string)) {
            tvHelp.setText(string);
        }
    }

    @Override
    public void showHelpDetail(AidDetail detail) {
        mAid = detail.getAid();
        mCreatorIsOwn = User.creatorIsOwn(mAid.getCreatorId());

        //显示图片
        List<String> urls = new ArrayList<>();
        if (mAid.getImage() != null) {
            for (String url : mAid.getImage()) {
                urls.add(url);
            }
        } else {
            String url = IconUrl.moduleIconUrl(IconUrl.OrganizationAids, mAid.getId(), null);
            urls.add(url);
        }
        banner.setData(urls);


        //标题、时间
        tvTitle.setText(mAid.getTitle());
        tvTime.setText(StringUtils.timeFormat(mAid.getCreatedAt()));


        //显示补贴
        double price = mAid.getPrice();
        if (price == 0) {
            tvPrice.setVisibility(View.INVISIBLE);
        } else {
            tvPrice.setText(String.format("（" + price + "元补贴）"));
        }

        //显示发起人
        tvCreator.setText(String.format(getString(R.string.format_creator), mAid.getCreatorName()));
        ImageLoaderUtil.instance().loadAvatar(detail.getAid().getCreatorId(), ivAvatar);


        //设置地址和电话
        tvAddress.setText(mAid.getAddress());
        tvPhone.setText(String.format("联系电话：%1$s", mAid.getMobile()));


        //显示内容
        String memo = mAid.getMemo();
        if (TextUtils.isEmpty(memo)) {
            llContent.setVisibility(View.GONE);
        } else {
            tvContent.setText(memo);
        }

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
    public void refreshData(int operate) {
        //0 创建回复  1 删除回复  2.删除aid
        EventBus.getDefault().post(new Event.RefreshHelpData());
        switch (operate) {
            case 0:
                getDetail();
                setChangeStatus(true, getString(R.string.cancel));
                break;
            case 1:
                getDetail();
                setChangeStatus(true, getString(R.string.help));
                break;
            case 2:
                pop();
                break;
        }
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
        String status = tvHelp.getText().toString();
        if (status.equals(getString(R.string.cancel))) {
            if (mCreatorIsOwn) {
                new NormalDialog().createNormal(_mActivity, R.string.confirm_delete_help, () -> {
                    mPresenter.deleteAid(mAid.getId());
                });
            } else {
                new NormalDialog().createNormal(_mActivity, R.string.confirm_cancel_help, () -> {
                    mPresenter.deleteAidAnswer(mAidId, mReplyId);
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
