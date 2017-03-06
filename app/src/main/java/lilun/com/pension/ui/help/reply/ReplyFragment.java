package lilun.com.pension.ui.help.reply;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import lilun.com.pension.R;
import lilun.com.pension.app.App;
import lilun.com.pension.app.Event;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.adapter.AidAskListAdapter;
import lilun.com.pension.module.bean.OrganizationAid;
import lilun.com.pension.module.bean.OrganizationReply;
import lilun.com.pension.module.utils.Preconditions;
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

    private AidAskListAdapter mReplyAdapter;
    private String whatModule;
    private String whatId;
    private String title;

    public static ReplyFragment newInstance(String whatModule,String whatId,String title) {
        ReplyFragment fragment = new ReplyFragment();
        Bundle args = new Bundle();
        args.putString("whatModule", whatModule);
        args.putString("whatId", whatId);
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void getTransferData(Bundle arguments) {
        whatModule = arguments.getString("whatModule");
        whatId = arguments.getString("whatId");
        title = arguments.getString("title");
        Preconditions.checkNull(whatModule);
        Preconditions.checkNull(whatId);
        Preconditions.checkNull(title);
    }


    @Override
    protected void initPresenter() {
        mPresenter = new ReplyPresenter();
        mPresenter.bindView(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_help_reply;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        titleBar.setOnBackClickListener(this::pop);

        tvTopic.setText(mAid.getTitle());

        rvReply.setLayoutManager(new LinearLayoutManager(App.context, LinearLayoutManager.VERTICAL, false));
        rvReply.addItemDecoration(new NormalItemDecoration(17));

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
            reply.setWhatModel("OrganizationAid");
            reply.setWhatId(mAid.getId());
            reply.setContent(replyContent);
            mPresenter.newReply(reply);
        }
    }

    private void getReplies(int skip) {
        swipeLayout.setRefreshing(true);
        mPresenter.getReplies(mAid.getId(), skip);
    }


    @Override
    public void showReplies(List<OrganizationReply> replies, boolean isLoadMore) {
        completeRefresh();
        if (mReplyAdapter == null) {
            if (replies == null) {
                replies = new ArrayList<>();
            }
            mReplyAdapter = new AidAskListAdapter(this, replies, false);
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
}
