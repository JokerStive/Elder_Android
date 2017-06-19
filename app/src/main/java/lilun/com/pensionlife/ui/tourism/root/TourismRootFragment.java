package lilun.com.pensionlife.ui.tourism.root;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.adapter.PopularDestinationAdapter;
import lilun.com.pensionlife.module.adapter.TourismBigAdapter;
import lilun.com.pensionlife.module.bean.Tourism;
import lilun.com.pensionlife.ui.announcement.AnnouncementFragment;
import lilun.com.pensionlife.ui.tourism.detail.TourismDetailFragment;
import lilun.com.pensionlife.ui.tourism.list.TourismListFragment;
import lilun.com.pensionlife.widget.DividerGridItemDecoration;
import lilun.com.pensionlife.widget.NormalItemDecoration;
import lilun.com.pensionlife.widget.NormalTitleBar;

/**
 * 旅游首页V
 *
 * @author yk
 *         create at 2017/4/13 9:39
 *         email : yk_developer@163.com
 */
public class TourismRootFragment extends BaseFragment<TourismRootContract.Presenter> implements TourismRootContract.View {

    @Bind(R.id.titleBar)
    NormalTitleBar titleBar;

    @Bind(R.id.fl_announcement_container)
    FrameLayout flAnnouncementContainer;

    @Bind(R.id.rv_destination)
    RecyclerView rvDestination;

    @Bind(R.id.rv_popular_journey)
    RecyclerView rvPopularJourney;

    private String categoryId;
    private TourismBigAdapter adapter;

    public static TourismRootFragment newInstance(String categoryId) {
        TourismRootFragment fragment = new TourismRootFragment();
        Bundle bundle = new Bundle();
        bundle.putString("categoryId", categoryId);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected void getTransferData(Bundle arguments) {
        super.getTransferData(arguments);
        categoryId = arguments.getString("categoryId");
    }

    @Override
    protected void initPresenter() {
        mPresenter = new TourismRootPresenter();
        mPresenter.bindView(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tourism_root;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        titleBar.setOnBackClickListener(this::pop);


        rvDestination.setLayoutManager(new GridLayoutManager(getContext(), 4, LinearLayoutManager.VERTICAL, false));
        rvDestination.addItemDecoration(new DividerGridItemDecoration(getContext()));

        rvPopularJourney.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvPopularJourney.addItemDecoration(new NormalItemDecoration(10));
        rvPopularJourney.setNestedScrollingEnabled(false);
        rvPopularJourney.setHasFixedSize(false);


        replaceLoadRootFragment(R.id.fl_announcement_container, AnnouncementFragment.newInstance("旅游"), false);
    }


    @OnClick({R.id.hiking, R.id.by_car, R.id.honey_moon, R.id.parents, R.id.near, R.id.domestic, R.id.multinational, R.id.parenting})
    public void onClick(View view) {
        String tag = (String) view.getTag();
        start(TourismListFragment.newInstance(categoryId, tag, null));
    }


    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        mPresenter.getPopularDestinations();

        getJourneys(0);
    }


    @Override
    public void showPopularDestination(List<String> destinations) {
        PopularDestinationAdapter destinationAdapter = new PopularDestinationAdapter(destinations);
        destinationAdapter.setOnRecyclerViewItemClickListener((view, i) -> {
            String destination = destinationAdapter.getData().get(i);
            start(TourismListFragment.newInstance(categoryId, null, destination));
        });
        rvDestination.setAdapter(destinationAdapter);
    }

    @Override
    public void showPopularJourney(List<Tourism> journeys, boolean isLoadMore) {
        if (adapter == null) {
            adapter = new TourismBigAdapter(journeys);
            adapter.setOnRecyclerViewItemClickListener((view, i) -> {
                Tourism tourism = adapter.getData().get(i);
                start(TourismDetailFragment.newInstance(tourism));
            });
            adapter.setOnLoadMoreListener(() -> {
                Logger.d("旅游加载更多");
                getJourneys(adapter.getItemCount());
            });

            rvPopularJourney.setAdapter(adapter);
        } else if (isLoadMore) {
            adapter.addAll(journeys);
        } else {
            adapter.replaceAll(journeys);
        }
    }

    @Override
    public void completeRefresh() {

    }


    /**
     * 获取旅游
     */
    private void getJourneys(int skip) {
        String filter = "{\"where\":{\"extend.tag\":\"热门\",\"visible\":0,\"categoryId\":\"" + categoryId + "\"}}";
        mPresenter.getPopularJourneys(filter, skip);
    }


}
