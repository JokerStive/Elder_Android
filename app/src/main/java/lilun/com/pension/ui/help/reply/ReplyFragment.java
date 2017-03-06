package lilun.com.pension.ui.help.reply;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import lilun.com.pension.R;
import lilun.com.pension.app.App;
import lilun.com.pension.app.Event;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.adapter.ReplyAdapter;
import lilun.com.pension.module.bean.OrganizationReply;
import lilun.com.pension.module.utils.Preconditions;
import lilun.com.pension.module.utils.StringUtils;
import lilun.com.pension.widget.NormalItemDecoration;
import lilun.com.pension.widget.NormalTitleBar;

/**
 * 互助评价界面
 *
 * @author yk
 *         create at 2017/2/28 8:52
 *         email : yk_developer@163.com
 */
public class ReplyFragment extends BaseFragment<ReplyContract.Presenter> implements ReplyContract.View {


    @Bind(R.id.titleBar)
    NormalTitleBar titleBar;

    @Bind(R.id.tv_topic)
    TextView tvTopic;

    @Bind(R.id.rv_reply)
    RecyclerView rvReply;

    @Bind(R.id.swipe_layout)
    SwipeRefreshLayout swipeLayout;

    @Bind(R.id.et_reply)
    EditText etReply;

    @Bind(R.id.tv_confirm)
    TextView tvConfirm;
    @Bind(R.id.ll_reply_container)
    LinearLayout llReplyContainer;

    private ReplyAdapter mReplyAdapter;
    private String whatModule;
    private String whatId;
    private String title;
    private boolean isShowReply;

    public static ReplyFragment newInstance(String whatModule, String whatId, String title, boolean isShowReply) {
        ReplyFragment fragment = new ReplyFragment();
        Bundle args = new Bundle();
        args.putString("whatModule", whatModule);
        args.putString("whatId", whatId);
        args.putString("title", title);
        args.putBoolean("isShowReply", isShowReply);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void getTransferData(Bundle arguments) {
        whatModule = arguments.getString("whatModule");
        whatId = arguments.getString("whatId");
        title = arguments.getString("title");
        isShowReply = arguments.getBoolean("isShowReply", true);
        Preconditions.checkNull(whatModule);
        Preconditions.checkNull(whatId);
    }


    @Override
    protected void initPresenter() {
        mPresenter = new ReplyPresenter();
        mPresenter.bindView(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_reply;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        titleBar.setOnBackClickListener(this::pop);

        tvTopic.setText(StringUtils.filterNull(title));

        rvReply.setLayoutManager(new LinearLayoutManager(App.context, LinearLayoutManager.VERTICAL, false));
        rvReply.addItemDecoration(new NormalItemDecoration(17));

        llReplyContainer.setVisibility(isShowReply?View.VISIBLE:View.GONE);

        //刷新
        swipeLayout.setOnRefreshListener(() -> {
                    if (mPresenter != null) {
                        getReplies(0);
                    }
                }
        );


        //提交回答
        tvConfirm.setOnClickListener(v -> createReply());


    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        getReplies(0);
    }

    private void createReply() {
        if (etReply.getText() != null) {
            String replyContent = etReply.getText().toString();
            OrganizationReply reply = new OrganizationReply();
            reply.setWhatModel(whatModule);
            reply.setWhatId(whatId);
            reply.setContent(replyContent);
            mPresenter.newReply(reply);
        }
    }

    private void getReplies(int skip) {
        swipeLayout.setRefreshing(true);
        mPresenter.getReplies(whatModule, whatId, skip);
    }


    @Override
    public void showReplies(List<OrganizationReply> replies, boolean isLoadMore) {
        completeRefresh();
        if (mReplyAdapter == null) {
            if (replies == null) {
                replies = new ArrayList<>();
            }
            mReplyAdapter = new ReplyAdapter(this, replies, false);
            rvReply.setAdapter(mReplyAdapter);
            mReplyAdapter.setOnLoadMoreListener(() -> {
                //TODO load_more
            });
        } else if (isLoadMore) {
            mReplyAdapter.addAll(replies);
        } else {
            mReplyAdapter.replaceAll(replies);
        }
    }

    @Override
    public void newReplySuccess(OrganizationReply reply) {
        Logger.d("reply success");
        if (mReplyAdapter != null) {
            mReplyAdapter.add(reply);
            EventBus.getDefault().post(new Event.RefreshHelpReply(reply));
            EventBus.getDefault().post(new Event.RefreshHelpData());
        } else {

        }
    }


    @Override
    public void completeRefresh() {
        if (swipeLayout != null && swipeLayout.isRefreshing()) {
            swipeLayout.setRefreshing(false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
