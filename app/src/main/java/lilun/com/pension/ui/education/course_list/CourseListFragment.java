package lilun.com.pension.ui.education.course_list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import lilun.com.pension.R;
import lilun.com.pension.app.App;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.adapter.EduCourseAdapter;
import lilun.com.pension.module.adapter.NormalFilterAdapter;
import lilun.com.pension.module.bean.ConditionOption;
import lilun.com.pension.module.bean.EdusColleageCourse;
import lilun.com.pension.module.bean.ElderEdusColleage;
import lilun.com.pension.module.utils.Preconditions;
import lilun.com.pension.ui.education.course_details.CourseDetailFragment;
import lilun.com.pension.widget.ElderModuleItemDecoration;
import lilun.com.pension.widget.SearchTitleBar;
import lilun.com.pension.widget.filter_view.AreaFilter;
import lilun.com.pension.widget.filter_view.FilterView;

/**
 * 老年教育 课程列表 V
 *
 * @author yk
 *         create at 2017/2/7 16:04
 *         email : yk_developer@163.com
 */
public class CourseListFragment extends BaseFragment<CourseListContract.Presenter>
        implements CourseListContract.View {
    private SearchTitleBar.LayoutType layoutType = SearchTitleBar.LayoutType.BIG;
    List<EdusColleageCourse> dataList = new ArrayList<>();
    ElderEdusColleage mColleage;
    private EduCourseAdapter mEduCourseAdapter;
    private String[] filterTitles = {"区域", "价格", "等级"};
    String searchStr = "";

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


    public static CourseListFragment newInstance(ElderEdusColleage colleage) {
        CourseListFragment fragment = new CourseListFragment();
        Bundle args = new Bundle();
        args.putSerializable("Colleage", colleage);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void getTransferData(Bundle arguments) {
        mColleage = (ElderEdusColleage) arguments.getSerializable("Colleage");
        Preconditions.checkNull(mColleage);
    }


    @Override
    protected void initPresenter() {
        mPresenter = new CourseListPresenter();
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
            getDataList(mColleage.getId(), skip);
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

                getDataList(mColleage.getId(), 0);
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
                        getDataList(mColleage.getId(), skip);
                    }
                }
        );
        initFilter();
    }


    private void setRecyclerAdapter(List<EdusColleageCourse> data) {
        mEduCourseAdapter = getAdapterFromLayoutType(data);
        if (mEduCourseAdapter != null) {
            mEduCourseAdapter.setOnItemClickListener((item) -> {
                start(CourseDetailFragment.newInstance(item));
            });
            mEduCourseAdapter.setEmptyView();
        }
        mRecyclerView.setAdapter(mEduCourseAdapter);
    }

    private EduCourseAdapter getAdapterFromLayoutType(List<EdusColleageCourse> data) {
        EduCourseAdapter adapter = null;
        int layoutId = 0;
        if (layoutType == SearchTitleBar.LayoutType.BIG) {
            layoutId = R.layout.item_colleage_big;
        } else if (layoutType == SearchTitleBar.LayoutType.SMALL) {
            layoutId = R.layout.item_colleage_small;
        }
        adapter = new EduCourseAdapter(data, layoutId);
        return adapter;
    }

    private void initFilter() {
        List<View> pops = new ArrayList<>();

        //除了区域以外的条件弹窗
        List<List<ConditionOption>> optionsList = mPresenter.getConditionOptionsList();
        for (int i = 0; i < optionsList.size(); i++) {
            RecyclerView recyclerView = new RecyclerView(App.context);
            recyclerView.setLayoutManager(new LinearLayoutManager(App.context, LinearLayoutManager.VERTICAL, false));
            NormalFilterAdapter adapter = new NormalFilterAdapter(optionsList.get(i));
            final int finalI = i + 1;
            adapter.setOnItemClickListener((position, option) -> {
                filterView.setTabText(position == 0 ? Arrays.asList(filterTitles).get(finalI) : option.getConditionValue(), position == 0);
                //TODO 条件的map加入条件
            });
            recyclerView.setAdapter(adapter);
            pops.add(recyclerView);
        }


        //TODO 区域

        AreaFilter areaFilter = new AreaFilter(mContent);
        pops.add(0, areaFilter);


        filterView.setTitlesAndPops(Arrays.asList(filterTitles), pops, mSwipeLayout);
    }

    private void getDataList(String schoolId, int skip) {
        String filter = "{\"where\":{\"name\":{\"like\":\"" + searchStr + "\"}}}";
        mPresenter.getCollgCourseList(schoolId, filter, skip);

    }


    @Override
    public void showCollgCourseList(List<EdusColleageCourse> courses, boolean isLoadMore) {
        skip += courses.size();
        if (skip == 0) {
            nullData.setVisibility(View.VISIBLE);
        } else
            nullData.setVisibility(View.GONE);

        if (mEduCourseAdapter == null) {
//            mEduCourseAdapter = new EduCourseAdapter(this, courses);
//
//            mRecyclerView.setAdapter(mEduCourseAdapter);
//            mEduCourseAdapter.setOnItemClickListener((item) -> {
//                   start(CourseDetailFragment.newInstance(item));
//            });
//
//            mEduCourseAdapter.setOnLoadMoreListener(() -> {
//                getDataList(mColleage.getId(), skip);
//            });
            dataList.addAll(courses);
            setRecyclerAdapter(dataList);
        } else if (isLoadMore) {
            dataList.addAll(courses);
        } else {
            dataList.clear();
            dataList.addAll(courses);
        }
        mEduCourseAdapter.notifyDataSetChanged();
    }


    @Override
    public void completeRefresh() {
        if (mSwipeLayout != null && mSwipeLayout.isRefreshing()) {
            mSwipeLayout.setRefreshing(false);
        }
    }


}
