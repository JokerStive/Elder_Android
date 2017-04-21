package lilun.com.pension.ui.tourism.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import lilun.com.pension.R;
import lilun.com.pension.app.Config;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.adapter.TourismSmallAdapter;
import lilun.com.pension.module.bean.Tourism;
import lilun.com.pension.module.utils.Preconditions;
import lilun.com.pension.ui.tourism.detail.TourismDetailFragment;
import lilun.com.pension.widget.FilterInputRangeView;
import lilun.com.pension.widget.FilterInputView;
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
    TourismFilter tourismFilter = new TourismFilter();
    private String destination;

    public static TourismListFragment newInstance(String categoryId, String tag, String destination) {
        TourismListFragment fragment = new TourismListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("tag", tag);
        bundle.putString("destination", destination);
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
            TourismFilter.WhereBean.TitleBean title = tourismFilter.where.getTitle();
            if (title == null) {
                title = new TourismFilter.WhereBean.TitleBean();
            }
            title.setLike(searchStr);
            tourismFilter.where.setTitle(title);
            refreshTourismWithFilter();
        }

        @Override
        public void onChangeLayout(SearchTitleBar.LayoutType layoutType) {

        }
    };


    @Override
    protected void getTransferData(Bundle arguments) {
        super.getTransferData(arguments);
        mTag = arguments.getString("tag");
        destination = arguments.getString("destination");
        categoryId = arguments.getString("categoryId");
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
        initFilter();


        searchBar.setFragment(this);
        searchBar.isChangeLayout(false);
        searchBar.setOnItemClickListener(listener);


        rvJourneyTags.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvJourneyTags.addItemDecoration(new NormalItemDecoration(Config.list_decoration));

        rvJourney.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvJourney.addItemDecoration(new NormalItemDecoration(Config.list_decoration));

        swipeLayout.setOnRefreshListener(() -> {
            getJourneys(0);
        });
    }

    private void initFilter() {
        //产品类别过滤
        tourismFilter.where.setCategoryId(categoryId);

        //目的地过滤
        if (!TextUtils.isEmpty(destination)) {
            tourismFilter.where.setDestination(destination);
        }

        //tag过滤
        if (!TextUtils.isEmpty(mTag)) {
            tourismFilter.where.setTag(mTag);
        }

        List<String> titles = new ArrayList<>();
        titles.add("价格");
        titles.add("出发地");
        if (TextUtils.isEmpty(destination)) {
            titles.add("目的地");
        }

        List<View> views = new ArrayList<>();

        //价格
        FilterInputRangeView rangePriceView = new FilterInputRangeView(getContext(), "价格");
        rangePriceView.setUnit("元");
        rangePriceView.setOnConfirmListener((range, show, isDef) -> {
            tourismFilter.where.setPrice(null);
            TourismFilter.WhereBean.PriceBean price = tourismFilter.where.getPrice();
            if (price == null) {
                price = new TourismFilter.WhereBean.PriceBean();
            }
            if (!isDef) {
                price.setBetween(range);
                tourismFilter.where.setPrice(price);
            }
            filterView.setTabText(show, isDef);
            refreshTourismWithFilter();
        });
        views.add(rangePriceView);

        //出发地
        FilterInputView departureView = new FilterInputView(getContext(), "出发地");
        departureView.setHint("请输入出发地");
        departureView.setOnConfirmListener((departure, isDef) -> {
            tourismFilter.where.setDeparture(null);
            if (!isDef) {
                tourismFilter.where.setDeparture(departure);
            }
            filterView.setTabText(departure, isDef);
            refreshTourismWithFilter();
        });
        views.add(departureView);

        //目的地
        if (TextUtils.isEmpty(destination)) {
            FilterInputView destinationView = new FilterInputView(getContext(), "目的地");
            destinationView.setHint("请输入目的地");
            destinationView.setOnConfirmListener((destination, isDef) -> {
                tourismFilter.where.setDestination(null);
                if (!isDef) {
                    tourismFilter.where.setDestination(destination);
                }
                filterView.setTabText(destination, isDef);
                refreshTourismWithFilter();
            });
            views.add(destinationView);
        }

        filterView.setTitlesAndPops(titles, views, swipeLayout);
    }


    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        getJourneys(0);
    }


    private void refreshTourismWithFilter() {
        getJourneys(0);
    }

    /**
     * 获取旅游
     */
    private void getJourneys(int skip) {
        swipeLayout.setRefreshing(true);
        Gson gson = new Gson();
        String filter = gson.toJson(tourismFilter);
        Logger.d("filter = " + filter);
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
