package lilun.com.pension.ui.education.colleage_list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import lilun.com.pension.R;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.adapter.EdusColleageAdapter;
import lilun.com.pension.module.bean.ElderEdusColleage;
import lilun.com.pension.module.bean.ElderModule;
import lilun.com.pension.module.utils.Preconditions;
import lilun.com.pension.ui.education.colleage_details.ColleageDetailFragment;
import lilun.com.pension.widget.ElderModuleItemDecoration;
import lilun.com.pension.widget.SearchTitleBar;
import lilun.com.pension.widget.filter_view.FilterView;

/**
 * 老年教育 各类型列表 V
 *
 * @author yk
 *         create at 2017/2/7 16:04
 *         email : yk_developer@163.com
 */
public class EducationListFragment extends BaseFragment<EducationListContract.Presenter>
        implements EducationListContract.View {
    private SearchTitleBar.LayoutType layoutType = SearchTitleBar.LayoutType.BIG;

    ElderModule mElderModule;
    List<ElderEdusColleage> dataList = new ArrayList<>();
    private String[] filterTitles = {"区域","价格","等级"};
    String searchStr = "";

    private EdusColleageAdapter mActivityAdapter;
    @Bind(R.id.searchBar)
    SearchTitleBar searchBar;
    @Bind(R.id.filter_view)
    FilterView filterView;
    @Bind(R.id.swipe_layout)
    SwipeRefreshLayout mSwipeLayout;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.null_data)
    ImageView nullData;
    int skip = 0;


    public static EducationListFragment newInstance(ElderModule elderModule) {
        EducationListFragment fragment = new EducationListFragment();
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
        mPresenter = new EducationListPresenter();
        mPresenter.bindView(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_education;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            // mAdapter.setRefreshing(true);

            skip = 0;
            getDataList(skip, searchStr);
        }
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        searchBar.setNoNullLayout();
        searchBar.setFragment(this);
        searchBar.setOnItemClickListener(new SearchTitleBar.OnItemClickListener() {
            @Override
            public void onBack() {
                pop();
            }

            @Override
            public void onSearch(String str) {
                searchStr = str;
                Log.d("zzpp", searchStr);
                getDataList(0, searchStr);
            }

            @Override
            public void onChangeLayout(SearchTitleBar.LayoutType type) {
                layoutType = type;
                if (dataList != null && dataList.size() != 0) {
                    setRecyclerAdapter(dataList);
                }
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        mRecyclerView.addItemDecoration(new ElderModuleItemDecoration());
        mSwipeLayout.setOnRefreshListener(() -> {
                    if (mPresenter != null) {
                        skip = 0;
                        getDataList(skip, searchStr);
                    }
                }
        );
        initFilter();
    }

    private void initFilter() {
//        List<ViewStep2> pops = new ArrayList<>();
//
//        //除了区域以外的条件弹窗
//        List<List<ConditionOption>> optionsList = mPresenter.getConditionOptionsList();
//        for (int i = 0; i < optionsList.size(); i++) {
//            RecyclerView recyclerView = new RecyclerView(App.context);
//            recyclerView.setLayoutManager(new LinearLayoutManager(App.context, LinearLayoutManager.VERTICAL, false));
//            NormalFilterAdapter adapter = new NormalFilterAdapter(optionsList.get(i));
//            final int finalI = i + 1;
//            adapter.setOnItemClickListener((position, option) -> {
//                filterView.setTabText(position == 0 ? Arrays.asList(filterTitles).get(finalI) : option.getVal(), position == 0);
//                //TODO 条件的map加入条件
//            });
//            recyclerView.setAdapter(adapter);
//            pops.add(recyclerView);
//        }
//
//
//        //TODO 区域
//
//        AreaFilter areaFilter = new AreaFilter(mContent);
//        pops.add(0, areaFilter);
//
//
//        filterView.setTitlesAndPops(Arrays.asList(filterTitles), pops, mSwipeLayout);
    }


    private void setRecyclerAdapter(List<ElderEdusColleage> data) {
        mActivityAdapter = getAdapterFromLayoutType(data);
        if (mActivityAdapter != null) {
            mActivityAdapter.setOnItemClickListener((item) -> {
                start(ColleageDetailFragment.newInstance(item));
            });
            mActivityAdapter.setEmptyView();
        }
        mRecyclerView.setAdapter(mActivityAdapter);
    }

    private EdusColleageAdapter getAdapterFromLayoutType(List<ElderEdusColleage> data) {
        EdusColleageAdapter adapter = null;
        int layoutId = 0;
        if (layoutType == SearchTitleBar.LayoutType.BIG) {
            layoutId = R.layout.item_colleage_big;
        } else if (layoutType == SearchTitleBar.LayoutType.SMALL) {
            layoutId = R.layout.item_colleage_small;
        }
        adapter = new EdusColleageAdapter(data, layoutId);
        return adapter;
    }

    private void getDataList(int skip, String searchStr) {
        String filter = "";
        if (mElderModule.getName().equals(getString(R.string.pension_university)))
            filter = "{\"include\":\"contact\",\"where\":{\"location\":{\"exists\": true},\"name\":{\"like\":\"" + searchStr + "\"}}, \"skip\":" + skip + "}";
        else if (mElderModule.getName().equals(getString(R.string.net_university)))
            filter = "{\"include\":\"contact\",\"where\":{\"location\":{\"exists\": false},\"name\":{\"like\":\"" + searchStr + "\"}}, \"skip\":" + skip + "}";
        mPresenter.getColleage(filter, skip);
    }


    @Override
    public void showEdusList(List<ElderEdusColleage> elderEdusList, boolean isLoadMore) {

        skip += elderEdusList.size();
        if (skip == 0) {
            nullData.setVisibility(View.VISIBLE);
        } else
            nullData.setVisibility(View.GONE);
        if (mActivityAdapter == null) {

//            mActivityAdapter = new EdusColleageAdapter(this, elderEdusList);
//            mRecyclerView.setAdapter(mActivityAdapter);
//            mActivityAdapter.setOnItemClickListener((item) -> {
//                start(ColleageDetailFragment.newInstance(item));
//            });
//
//            mActivityAdapter.setOnLoadMoreListener(() -> {
//                getDataList(skip);
//            });
            dataList.addAll(elderEdusList);
            setRecyclerAdapter(dataList);
        } else if (isLoadMore) {
            dataList.addAll(elderEdusList);
            // mActivityAdapter.addAll(dataList);
        } else {
            dataList.clear();
            dataList.addAll(elderEdusList);

            // mActivityAdapter.replaceAll(dataList);
        }
        mActivityAdapter.notifyDataSetChanged();
    }

    @Override
    public void completeRefresh() {
        if (mSwipeLayout != null && mSwipeLayout.isRefreshing()) {
            mSwipeLayout.setRefreshing(false);
        }
    }


}
