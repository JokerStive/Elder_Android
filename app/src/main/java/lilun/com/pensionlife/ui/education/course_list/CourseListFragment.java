package lilun.com.pensionlife.ui.education.course_list;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.adapter.EduCourseAdapter;
import lilun.com.pensionlife.module.adapter.NormalFilterAdapter;
import lilun.com.pensionlife.module.bean.ConditionOption;
import lilun.com.pensionlife.module.bean.Option;
import lilun.com.pensionlife.module.bean.OrganizationProduct;
import lilun.com.pensionlife.module.utils.Preconditions;
import lilun.com.pensionlife.widget.DividerDecoration;
import lilun.com.pensionlife.widget.NormalTitleBar;
import lilun.com.pensionlife.widget.filter_view.FilterView;

/**
 * 老年教育 课程列表 V
 *
 * @author yk
 *         create at 2017/2/7 16:04
 *         email : yk_developer@163.com
 */
public class CourseListFragment extends BaseFragment<CourseListContract.Presenter> implements CourseListContract.View {
    @Bind(R.id.titleBar)
    NormalTitleBar titleBar;
    @Bind(R.id.filter_view)
    FilterView filterView;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.swipe_layout)
    SwipeRefreshLayout mSwipeLayout;
    private EduCourseAdapter mEduCourseAdapter;
    //    private String[] filterTitles = {"区域", "价格", "等级"};
    private String mOrganizationId;
    private CourseListFilter mFilter;


    public static CourseListFragment newInstance(String organizationId) {
        CourseListFragment fragment = new CourseListFragment();
        Bundle args = new Bundle();
        args.putSerializable("organizationId", organizationId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void getTransferData(Bundle arguments) {
        mOrganizationId = arguments.getString("organizationId");
        Preconditions.checkNull(mOrganizationId);
        mFilter = new CourseListFilter(mOrganizationId);
    }


    @Override
    protected void initPresenter() {
        mPresenter = new CourseListPresenter();
        mPresenter.bindView(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_course_list;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            getDataList(0);
        }
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        titleBar.setTitle("课程分类");
        titleBar.setOnBackClickListener(this::pop);


        mRecyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        mRecyclerView.addItemDecoration(new DividerDecoration(App.context, LinearLayoutManager.VERTICAL, (int) App.context.getResources().getDimension(R.dimen.dp_1), Color.parseColor("#f5f5f9")));

        mEduCourseAdapter = new EduCourseAdapter(new ArrayList<>());
        mEduCourseAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                OrganizationProduct course = mEduCourseAdapter.getItem(position);
                //TODO 课程详情 预约
            }
        });
        mEduCourseAdapter.setOnLoadMoreListener(() -> getDataList(mEduCourseAdapter.getItemCount()), mRecyclerView);

        mRecyclerView.setAdapter(mEduCourseAdapter);

        mSwipeLayout.setOnRefreshListener(() -> {
                    if (mPresenter != null) {
                        getDataList(0);
                    }
                }
        );
        initFilter();
    }


    private void initFilter() {
        List<View> pops = new ArrayList<>();
        List<String> filterTitles = new ArrayList<>();


        //时间
        List<Option> kindOptions = new ArrayList<>();
        Option optionDESC = new Option("createAt DESC", "降序排序");
        Option optionHelp = new Option("createAt ASC", "升序排序");
        kindOptions.add(optionDESC);
        kindOptions.add(optionHelp);
        ConditionOption conditionOptionOrder = new ConditionOption("order", "开课时间", kindOptions);
        filterTitles.add(conditionOptionOrder.getCondition());
        RecyclerView recyclerView = new RecyclerView(App.context);
        recyclerView.setLayoutManager(new LinearLayoutManager(App.context, LinearLayoutManager.VERTICAL, false));
        NormalFilterAdapter adapter = new NormalFilterAdapter(conditionOptionOrder);
        adapter.setOnItemClickListener((position, title, whereKey, whereValue) -> {
            filterView.setTabText(title, position == 0);
            mFilter.setOrder(whereValue);
            getDataList(0);
        });
        recyclerView.setAdapter(adapter);
        pops.add(recyclerView);


        filterView.setTitlesAndPops(filterTitles, pops, mSwipeLayout);
    }

    private void getDataList(int skip) {
        Gson gson = new Gson();
        String filter = gson.toJson(mFilter);
        Logger.d("courseFilter ----- " + filter);
        mPresenter.getCourses(filter, skip);

    }


    @Override
    public void showCollageCourseList(List<OrganizationProduct> courses, boolean isLoadMore) {
        if (isLoadMore) {
            mEduCourseAdapter.addAll(courses, true);
        } else {
            mEduCourseAdapter.replaceAll(courses);
        }

    }


    @Override
    public void completeRefresh() {
        if (mSwipeLayout != null && mSwipeLayout.isRefreshing()) {
            mSwipeLayout.setRefreshing(false);
        }
    }


}
