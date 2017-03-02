package lilun.com.pension.ui.help;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.Bind;
import lilun.com.pension.R;
import lilun.com.pension.app.Event;
import lilun.com.pension.app.User;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.adapter.OrganizationAidAdapter;
import lilun.com.pension.module.bean.ElderModule;
import lilun.com.pension.module.bean.OrganizationAid;
import lilun.com.pension.module.utils.Preconditions;
import lilun.com.pension.ui.help.help_detail.AskDetailFragment;
import lilun.com.pension.ui.help.help_detail.HelpDetailFragment;
import lilun.com.pension.widget.NormalItemDecoration;
import lilun.com.pension.widget.NormalTitleBar;

/**
 * 分类求助（问？帮？）V
 *
 * @author yk
 *         create at 2017/2/7 16:04
 *         email : yk_developer@163.com
 */
public class HelpFragment extends BaseFragment<HelpContract.Presenter> implements HelpContract.View {


    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.swipe_layout)
    SwipeRefreshLayout mSwipeLayout;

    @Bind(R.id.titleBar)
    NormalTitleBar titleBar;

    private OrganizationAidAdapter mAidAdapter;
    private ElderModule elderModule;

    public static HelpFragment newInstance(ElderModule elderModule) {
        HelpFragment fragment = new HelpFragment();
        Bundle args = new Bundle();
        args.putSerializable("elderModule", elderModule);
        fragment.setArguments(args);
        return fragment;
    }

    @Subscribe
    public void refreshData(Event.RefreshHelpData event){
        getHelps(0);
    }

    @Override
    protected void getTransferData(Bundle arguments) {
        elderModule = (ElderModule) arguments.getSerializable("elderModule");
        Preconditions.checkNull(elderModule);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new HelpPresenter();
        mPresenter.bindView(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_recycler;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        titleBar.setTitle(elderModule.getName());
        titleBar.setOnBackClickListener(() -> {
            pop();
        });


        mRecyclerView.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new NormalItemDecoration(27));
        //刷新
        mSwipeLayout.setOnRefreshListener(() -> {
                    if (mPresenter != null) {
                        getHelps(0);
                    }
                }
        );


    }


    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            mSwipeLayout.setRefreshing(true);
            getHelps(0);
        }
    }

    private void getHelps(int skip) {
        String filter = "{\"where\":{\"kind\":\"" + elderModule.getServiceConfig().getKind() + "\"}}";
        mPresenter.getHelps(filter, skip);
    }

    @Override
    public void showClassifies(List<ElderModule> elderModules) {
    }

    @Override
    public void showAboutMe(List<OrganizationAid> helps, boolean isLoadMore) {
        completeRefresh();
        if (helps != null) {
            for (OrganizationAid aid : helps) {
                aid.setItemType(aid.getKind());
            }
            if (mAidAdapter == null) {
                mAidAdapter = new OrganizationAidAdapter(this, helps);
                mAidAdapter.setOnItemClickListener((aid) -> {
                    start(aid.getKind() == 0 ? AskDetailFragment.newInstance(aid.getId(), User.creatorIsOwn(aid.getCreatorId())) : HelpDetailFragment.newInstance(aid.getId()));
                });
                mRecyclerView.setAdapter(mAidAdapter);
            } else if (isLoadMore) {
                mAidAdapter.addAll(helps);
            } else {
                mAidAdapter.replaceAll(helps);
            }
        }
    }

    @Override
    public void completeRefresh() {
        if (mSwipeLayout != null && mSwipeLayout.isRefreshing()) {
            mSwipeLayout.setRefreshing(false);
        }
    }


}
