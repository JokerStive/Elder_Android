package lilun.com.pensionlife.ui.help.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;

import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.Config;
import lilun.com.pensionlife.app.Event;
import lilun.com.pensionlife.app.User;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.adapter.OrganizationAidAdapter;
import lilun.com.pensionlife.module.bean.ConditionOption;
import lilun.com.pensionlife.module.bean.OrganizationAid;
import lilun.com.pensionlife.ui.help.help_detail.AskDetailFragment;
import lilun.com.pensionlife.ui.help.help_detail.HelpDetailFragment;
import lilun.com.pensionlife.widget.NormalItemDecoration;
import lilun.com.pensionlife.widget.SearchTitleBar;
import lilun.com.pensionlife.widget.filter_view.FilterView;

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

    @Bind(R.id.searchBar)
    SearchTitleBar searchBar;
    @Bind(R.id.filter_view)
    FilterView filterView;


    private String filter_kind = "kind";
    private String filter_priority = "priority";
    private String filter_status = "status";
    private boolean isMain;

    private SearchTitleBar.LayoutType layoutType = SearchTitleBar.LayoutType.SMALL;


    private HelpFilter helpFilter = new HelpFilter();
    private OrganizationAidAdapter mAidAdapter;
    private List<OrganizationAid> helps;
//    private Integer mkind;

    public static HelpFragment newInstance(boolean isMain) {
        HelpFragment fragment = new HelpFragment();
        Bundle args = new Bundle();
        args.putBoolean("isMain", isMain);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void getTransferData(Bundle arguments) {
        super.getTransferData(arguments);
        isMain = arguments.getBoolean("isMain", false);
    }

    @Subscribe
    public void refreshData(Event.RefreshHelpData event) {
        refreshHelpWithFilter();
    }


    @Override
    protected void initPresenter() {
        mPresenter = new HelpPresenter();
        mPresenter.bindView(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_help_list;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        initConditionOption();
        searchBar.isChangeLayout(true);
        searchBar.setLayoutTypeIcon(layoutType);
        searchBar.setFragment(this);
        searchBar.setOnItemClickListener(new SearchTitleBar.OnItemClickListener() {
            @Override
            public void onBack() {
                pop();
            }

            @Override
            public void onChangeLayout(SearchTitleBar.LayoutType type) {
                layoutType = type;
                if (helps != null && helps.size() != 0) {
                    setRecyclerAdapter(helps);
                }
            }

            @Override
            public void onSearch(String searchStr) {
                helpFilter.setTitle(searchStr);
                refreshHelpWithFilter();
            }


        });


        mRecyclerView.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new NormalItemDecoration(Config.list_decoration));
        //刷新
        mSwipeLayout.setOnRefreshListener(() -> {
                    if (mPresenter != null) {
                        getHelps(0);
                    }
                }
        );


    }


    private void refreshHelpWithFilter() {
        getHelps(0);
    }

    private void initConditionOption() {
        List<String> conditionTitles = new ArrayList<>();
        List<ConditionOption> conditionOptionsList = mPresenter.getConditionOptionsList(filter_kind, filter_status, filter_priority);
        if (conditionOptionsList != null) {
            for (ConditionOption conditionOption : conditionOptionsList) {
                conditionTitles.add(conditionOption.getCondition());
            }
            filterView.setTitlesAndDatas(conditionTitles, conditionOptionsList, mSwipeLayout);
            filterView.setOnOptionClickListener((whereKey, whereValue) -> {
                if (TextUtils.equals(whereKey, filter_kind)) {
//                    mkind=Integer.parseInt(whereKey);
                    helpFilter.setKind(whereValue);
                } else if (TextUtils.equals(whereKey, filter_priority)) {
                    helpFilter.setPriority(whereValue);
                } else if (TextUtils.equals(whereKey, filter_status)) {
                    helpFilter.setStatus(whereValue);
                }
                refreshHelpWithFilter();
            });
        }
    }


    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            getHelps(0);
        }
    }

    private void getHelps(int skip) {
        mSwipeLayout.setRefreshing(skip == 0);
        helpFilter.setOrganizationId(isMain ? "" : User.getCurrentOrganizationId());

        String filter = helpFilter.toString();
        Logger.d("求助列表 filter--" + filter);
        if (isMain) {
            mPresenter.getAboutMe(filter, skip);
        } else {
            mPresenter.getHelps(filter, skip);
        }
    }


    @Override
    public void showAboutMe(List<OrganizationAid> helps, boolean isLoadMore) {
        this.helps = helps;
        completeRefresh();
        if (helps == null) {
            helps = new ArrayList<>();
        }
        if (mAidAdapter == null) {
            setRecyclerAdapter(helps);
        } else if (isLoadMore) {
            mAidAdapter.addAll(helps, Config.defLoadDatCount);
        } else {
            mAidAdapter.replaceAll(helps);
        }

    }


    private void setRecyclerAdapter(List<OrganizationAid> helps) {
        mAidAdapter = getAdapterFromLayoutType(helps);
        if (mAidAdapter != null) {
            mAidAdapter.setOnItemClickListener((aid) -> {
                start(aid.getKind() == 0 ? AskDetailFragment.newInstance(aid.getId(), User.creatorIsOwn(aid.getCreatorId())) : HelpDetailFragment.newInstance(aid.getId()));
            });
            mAidAdapter.setOnLoadMoreListener(() -> getHelps(mAidAdapter.getItemCount()), mRecyclerView);
            mAidAdapter.setEmptyView();
            mRecyclerView.setAdapter(mAidAdapter);
        }


    }

    private OrganizationAidAdapter getAdapterFromLayoutType(List<OrganizationAid> helps) {
        OrganizationAidAdapter adapter;
        int layoutId;
        if (layoutType == SearchTitleBar.LayoutType.BIG) {
            layoutId = R.layout.item_aid_big;
        } else if (layoutType == SearchTitleBar.LayoutType.SMALL) {
            layoutId = R.layout.item_aid_small;
        } else {
            layoutId = R.layout.item_aid_null;
        }
        adapter = new OrganizationAidAdapter(helps, layoutId, layoutType);
//        adapter.setOnLoadMoreListener(() -> getHelps(adapter.getItemCount()));
        return adapter;
    }

    @Override
    public void completeRefresh() {
        if (mSwipeLayout != null && mSwipeLayout.isRefreshing()) {
            mSwipeLayout.setRefreshing(false);
        }
    }


}
