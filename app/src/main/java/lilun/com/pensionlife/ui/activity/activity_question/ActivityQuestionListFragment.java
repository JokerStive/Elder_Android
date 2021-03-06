package lilun.com.pensionlife.ui.activity.activity_question;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.chad.library.adapter.base.BaseQuickAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.Config;
import lilun.com.pensionlife.app.Event;
import lilun.com.pensionlife.app.User;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.adapter.NestedReplyAdapter;
import lilun.com.pensionlife.module.bean.NestedReply;
import lilun.com.pensionlife.module.bean.OrganizationActivity;
import lilun.com.pensionlife.module.bean.OrganizationReply;
import lilun.com.pensionlife.module.utils.Preconditions;
import lilun.com.pensionlife.module.utils.ToastHelper;
import lilun.com.pensionlife.widget.InputSendPopupWindow;
import lilun.com.pensionlife.widget.InputSendView;
import lilun.com.pensionlife.widget.NormalItemDecoration;
import lilun.com.pensionlife.widget.NormalTitleBar;

/**
 * Created by zp on 2017/4/19.
 */

public class ActivityQuestionListFragment extends BaseFragment<ActivityQuestionListContract.Presenter>
        implements ActivityQuestionListContract.View {

    private OrganizationActivity mActivity;
    private NestedReplyAdapter nestedReplyAdapter;

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.swipe_layout)
    SwipeRefreshLayout mSwipeLayout;

    @Bind(R.id.titleBar)
    NormalTitleBar titleBar;


    @Bind(R.id.null_data)
    ImageView nullData;

    @Bind(R.id.ll_question)
    LinearLayout llQuestion;
    @Bind(R.id.acet_question)
    AppCompatEditText acetQuestion;
    @Bind(R.id.acbt_send)
    AppCompatButton acbtSend;
    InputSendPopupWindow inputSendPopupWindow;

    private boolean isMaster;

    int skip = 0;

    @OnClick(R.id.acbt_send)
    public void onClickQuestionSend() {
        String sendQuestion = acetQuestion.getText().toString().trim();
        if (sendQuestion.length() == 0) {
            ToastHelper.get(getContext()).showWareShort(getString(R.string.question_hint));
            return;
        }
        mPresenter.addQuestion(mActivity.getId(), sendQuestion);
    }


    public static ActivityQuestionListFragment newInstance(OrganizationActivity activity) {
        ActivityQuestionListFragment fragment = new ActivityQuestionListFragment();
        Bundle args = new Bundle();
        args.putSerializable("activity", activity);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void getTransferData(Bundle arguments) {
        super.getTransferData(arguments);
        mActivity = (OrganizationActivity) arguments.getSerializable("activity");
        Preconditions.checkNull(mActivity);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ActivityQuestionListPresenter();
        mPresenter.bindView(this);
    }

    @Override
    protected void initData() {
        super.initData();
        skip = 0;
        getReplyList(skip);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_question_list;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        isMaster = User.getUserId().equals(mActivity.getMasterId());
        if (isMaster) llQuestion.setVisibility(View.GONE);

        titleBar.setTitle("提问列表");
        titleBar.setOnBackClickListener(new NormalTitleBar.OnBackClickListener() {
            @Override
            public void onBackClick() {
                pop();
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new NormalItemDecoration(5));


        nestedReplyAdapter = new NestedReplyAdapter(this, new ArrayList<>(), isMaster, mActivity.getCreatorName(), new NestedReplyAdapter.AnswerListener() {
            @Override
            public void OnClickAnswer(NestedReply nestedReply, int position) {
                Log.d("zp", position + "");
                inputSendPopupWindow = new InputSendPopupWindow(getContext());
                inputSendPopupWindow.setOnSendListener(new InputSendView.OnSendListener() {
                    @Override
                    public void send(String sendStr) {
                        Log.d("zp", sendStr);
                        mPresenter.addAnswer(mActivity.getId(), nestedReply.getQuestion().getId(), sendStr, position);
                    }
                });
                //设置弹出窗体需要软键盘，
                inputSendPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
                //再设置模式，和Activity的一样，覆盖。
                inputSendPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                inputSendPopupWindow.showAtLocation(mRecyclerView, Gravity.BOTTOM, 0, 0);
            }
        });
        nestedReplyAdapter.setOnLoadMoreListener(() -> {
            getReplyList(skip);
        },mRecyclerView);

        nestedReplyAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);




        mRecyclerView.setAdapter(nestedReplyAdapter);

        //刷新
        mSwipeLayout.setOnRefreshListener(() -> {
                    if (mPresenter != null) {
                        skip = 0;
                        getReplyList(skip);
                    }
                }
        );
        acetQuestion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() == 0) {
                    acbtSend.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.shape_rect_write_corner));
                }
                acbtSend.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.shape_rect_red_corner_5));
            }
        });
    }

    public void getReplyList(int skip) {
        String filter = "";
        mPresenter.replyList(mActivity.getId(), filter, skip);
    }

    @Override
    public void completeRefresh() {
        if (mSwipeLayout != null && mSwipeLayout.isRefreshing()) {
            mSwipeLayout.setRefreshing(false);
        }
    }


    @Override
    public void showAnswer(OrganizationReply answer, int position) {
        Log.d("zp", "加入Answer数据");
        inputSendPopupWindow.dismiss();
        nestedReplyAdapter.getItem(position).setAnswer(answer);
        nestedReplyAdapter.notifyDataSetChanged();
        EventBus.getDefault().post(new Event.RefreshActivityReply());
    }

    /**
     * 通知详情页面刷新提问数据
     * {@link lilun.com.pensionlife.ui.activity.activity_detail.ActivityDetailFragment}
     *
     * @param question
     */
    @Override
    public void showQuestion(OrganizationReply question) {
        if (nestedReplyAdapter != null) {
            //  nestedReplyAdapter.add();
            nestedReplyAdapter.add(0, new NestedReply(question, null));
            mRecyclerView.scrollToPosition(0);
        }
        acetQuestion.setText("");
        nullData.setVisibility(View.GONE);
        EventBus.getDefault().post(new Event.RefreshActivityReply());
    }

    @Override
    public void showReplyList(List<NestedReply> nestedReplies) {
        boolean isLoadMore = skip != 0;

        skip += nestedReplies.size();


        if (nestedReplyAdapter == null) {

        } else if (isLoadMore) {
            nestedReplyAdapter.addAll(nestedReplies,Config.defLoadDatCount);
        } else {
            nestedReplyAdapter.replaceAll(nestedReplies);
        }
        if (skip == 0) {
            nullData.setVisibility(View.VISIBLE);
        } else {
            nullData.setVisibility(View.GONE);
        }
    }
}
