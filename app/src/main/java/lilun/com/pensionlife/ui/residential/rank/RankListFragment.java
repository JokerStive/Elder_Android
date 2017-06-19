package lilun.com.pensionlife.ui.residential.rank;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.adapter.RankAdapter;
import lilun.com.pensionlife.module.bean.Rank;
import lilun.com.pensionlife.module.utils.Preconditions;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.widget.NormalItemDecoration;
import lilun.com.pensionlife.widget.NormalTitleBar;

/**
 * 评价界面
 *
 * @author yk
 *         create at 2017/2/28 8:52
 *         email : yk_developer@163.com
 */
public class RankListFragment extends BaseFragment<RankListContract.Presenter> implements RankListContract.View {


    @Bind(R.id.titleBar)
    NormalTitleBar titleBar;

    @Bind(R.id.tv_topic)
    TextView tvTitle;

    @Bind(R.id.rv_reply)
    RecyclerView rvRank;

    @Bind(R.id.swipe_layout)
    SwipeRefreshLayout swipeLayout;



    private RankAdapter mRankAdapter;
    private String whatModule;
    private String whatId;
    private String title;

    public static RankListFragment newInstance(String whatModule, String whatId, String title) {
        RankListFragment fragment = new RankListFragment();
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
    }


    @Override
    protected void initPresenter() {
        mPresenter = new RankListPresenter();
        mPresenter.bindView(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_rank_list;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        titleBar.setOnBackClickListener(this::pop);

        tvTitle.setText(StringUtils.filterNull(title));

        rvRank.setLayoutManager(new LinearLayoutManager(App.context, LinearLayoutManager.VERTICAL, false));
        rvRank.addItemDecoration(new NormalItemDecoration(17));


        //刷新
        swipeLayout.setOnRefreshListener(() -> {
                    if (mPresenter != null) {
                        getReplies(0);
                    }
                }
        );




    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        getReplies(0);
    }


    private void getReplies(int skip) {
        swipeLayout.setRefreshing(true);
        mPresenter.getRanks(whatModule, whatId, skip);
    }


    @Override
    public void showRanks(List<Rank> ranks, boolean isLoadMore) {
        completeRefresh();
        if (mRankAdapter == null) {
            if (ranks == null) {
                ranks = new ArrayList<>();
            }
            mRankAdapter = new RankAdapter(ranks);
            rvRank.setAdapter(mRankAdapter);
            mRankAdapter.setOnLoadMoreListener(() -> {
                //TODO load_more
            });
        } else if (isLoadMore) {
            mRankAdapter.addAll(ranks);
        } else {
            mRankAdapter.replaceAll(ranks);
        }
    }




    @Override
    public void completeRefresh() {
        if (swipeLayout != null && swipeLayout.isRefreshing()) {
            swipeLayout.setRefreshing(false);
        }
    }


}
