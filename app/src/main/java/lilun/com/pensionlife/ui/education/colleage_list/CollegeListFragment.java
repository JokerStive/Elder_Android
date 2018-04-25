package lilun.com.pensionlife.ui.education.colleage_list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.adapter.CollageAdapter;
import lilun.com.pensionlife.module.bean.ElderModule;
import lilun.com.pensionlife.module.bean.Organization;
import lilun.com.pensionlife.module.utils.Preconditions;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.net.NetHelper;
import lilun.com.pensionlife.net.RxSubscriber;
import lilun.com.pensionlife.ui.education.course_list.CourseListFragment;
import lilun.com.pensionlife.widget.ElderModuleItemDecoration;
import lilun.com.pensionlife.widget.NormalTitleBar;
import rx.Observable;

/**
 * 老年教育 各类型列表 V
 *
 * @author yk
 *         create at 2017/2/7 16:04
 *         email : yk_developer@163.com
 */
public class CollegeListFragment extends BaseFragment {

    ElderModule mElderModule;
    @Bind(R.id.titleBar)
    NormalTitleBar titleBar;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.swipe_layout)
    SwipeRefreshLayout mSwipeLayout;

    private CollageAdapter mCollageAdapter;


    public static CollegeListFragment newInstance(ElderModule elderModule) {
        CollegeListFragment fragment = new CollegeListFragment();
        Bundle args = new Bundle();
        args.putSerializable("mElderModule", elderModule);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void getTransferData(Bundle arguments) {
        mElderModule = (ElderModule) arguments.getSerializable("mElderModule");
        Preconditions.checkNull(mElderModule);
    }


    @Override
    protected void initPresenter() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_education;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            getDataList(0);
        }
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        titleBar.setTitle("老年大学");
        titleBar.setOnBackClickListener(this::pop);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        mRecyclerView.addItemDecoration(new ElderModuleItemDecoration());

        mCollageAdapter = new CollageAdapter(new ArrayList<>());
        mCollageAdapter.setEmptyView();
        mCollageAdapter.setOnItemClickListener((adapter, view, position) -> {
            Organization collage = mCollageAdapter.getItem(position);
            assert collage != null;
            start(CourseListFragment.newInstance(collage.getId()));
        });
        mCollageAdapter.setOnLoadMoreListener(() -> {
            getDataList(mCollageAdapter.getItemCount());
        }, mRecyclerView);


        mRecyclerView.setAdapter(mCollageAdapter);

        mSwipeLayout.setOnRefreshListener(() -> {
                    getDataList(0);
                }
        );
    }


    private void getDataList(int skip) {
        mSwipeLayout.setRefreshing(skip == 0);
        aggregateObservable()
                .flatMap(this::getColleges)
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<Organization>>() {
                    @Override
                    public void _next(List<Organization> organizations) {
                        completeRefresh();
                        showCollages(organizations, skip != 0);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        completeRefresh();
                    }
                });
    }


    Observable<List<Organization>> aggregateObservable() {
        aggregateFilter collegeFilter = new aggregateFilter();
        Gson gson = new Gson();
        String filter = gson.toJson(collegeFilter);
        return NetHelper.getApi()
                .aggregate(filter)
                .compose(RxUtils.handleResult());
    }

    Observable<List<Organization>> getColleges(List<Organization> collegeIds) {
        ArrayList<String> ids = new ArrayList<>();
        for (Organization organization : collegeIds) {
            String idSuffix = organization.getId();
            String id = StringUtils.removeSpecialSuffix(idSuffix);
            ids.add(id);
        }
        CollegeFilter collegeFilter = new CollegeFilter(ids);

        Gson gson = new Gson();
        String filter = gson.toJson(collegeFilter);
        return NetHelper.getApi()
                .getOrganizationList(filter)
                .compose(RxUtils.handleResult());
    }

    public void showCollages(List<Organization> collages, boolean isLoadMore) {
        if (isLoadMore) {
            mCollageAdapter.addAll(collages, true);
        } else {
            mCollageAdapter.replaceAll(collages);
        }
    }

    public void completeRefresh() {
        if (mSwipeLayout != null && mSwipeLayout.isRefreshing()) {
            mSwipeLayout.setRefreshing(false);
        }
    }


}
