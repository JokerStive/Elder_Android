package lilun.com.pension.ui.tourism.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.Bind;
import lilun.com.pension.R;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.adapter.TourismSmallAdapter;
import lilun.com.pension.module.bean.Tourism;
import lilun.com.pension.module.utils.Preconditions;
import lilun.com.pension.ui.tourism.detail.TourismDetailFragment;
import lilun.com.pension.widget.NormalItemDecoration;
import lilun.com.pension.widget.SearchTitleBar;
import lilun.com.pension.widget.filter_view.FilterView;

/**
 * 旅游列表V
 *
 * @author yk
 *         create at 2017/4/13 11:06
 *         email : yk_developer@163.com
 */
public class TourismListFragment extends BaseFragment<TourismListContract.Presenter> implements TourismListContract.View {


    @Bind(R.id.searchBar)
    SearchTitleBar searchBar;
    @Bind(R.id.filter_view)
    FilterView filterView;
    @Bind(R.id.rv_journey_tags)
    RecyclerView rvJourneyTags;
    @Bind(R.id.rv_journey)
    RecyclerView rvJourney;
    @Bind(R.id.swipe_layout)
    SwipeRefreshLayout swipeLayout;
    private String mTag;
    private String categoryId;
    private TourismSmallAdapter adapter;

    public static TourismListFragment newInstance(String categoryId, String tag) {
        TourismListFragment fragment = new TourismListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("tag", tag);
        bundle.putString("categoryId", categoryId);
        fragment.setArguments(bundle);
        return fragment;
    }

    SearchTitleBar.OnItemClickListener listener = new SearchTitleBar.OnItemClickListener() {
        @Override
        public void onBack() {
            pop();
        }

        @Override
        public void onSearch(String searchStr) {

        }

        @Override
        public void onChangeLayout(SearchTitleBar.LayoutType layoutType) {

        }
    };


    @Override
    protected void getTransferData(Bundle arguments) {
        super.getTransferData(arguments);
        mTag = arguments.getString("tag");
        categoryId = arguments.getString("categoryId");
        Preconditions.checkNull(mTag);
        Preconditions.checkNull(categoryId);
        Logger.d("tag = " + mTag);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new TourismListPresenter();
        mPresenter.bindView(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragmeng_tourism_list;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        searchBar.setFragment(this);
        searchBar.isChangeLayout(false);
        searchBar.setOnItemClickListener(listener);


        rvJourneyTags.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvJourneyTags.addItemDecoration(new NormalItemDecoration(10));

        rvJourney.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvJourney.addItemDecoration(new NormalItemDecoration(10));

        swipeLayout.setOnRefreshListener(() -> {
            getJourneys(0);
        });
    }


    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        getJourneys(0);
    }



    /**
     * 获取旅游
     */
    private void getJourneys(int skip) {
        swipeLayout.setRefreshing(true);
        String filter = "{\"where\":{\"categoryId\":\"" + categoryId + "\"}}";
        mPresenter.getJourneys(filter, skip);
    }

    @Override
    public void showJourneyTags(List<String> tags) {

    }

    @Override
    public void showJourney(List<Tourism> journeys, boolean isLoadMore) {
        completeRefresh();
        if (adapter == null) {
            adapter = new TourismSmallAdapter(journeys);
            adapter.setOnRecyclerViewItemClickListener((view, i) -> {
                Tourism tourism = adapter.getData().get(i);
                start(TourismDetailFragment.newInstance(tourism));
            });
            adapter.setOnLoadMoreListener(() -> {
                Logger.d("旅游加载更多");
                getJourneys(adapter.getItemCount());
            });

            rvJourney.setAdapter(adapter);
        } else if (isLoadMore) {
            adapter.addAll(journeys);
        } else {
            adapter.replaceAll(journeys);
        }
    }

    @Override
    public void completeRefresh() {
        if (swipeLayout != null && swipeLayout.isRefreshing()) {
            swipeLayout.setRefreshing(false);
        }
    }
}
