package lilun.com.pensionlife.ui.education.course_list;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.adapter.CourseCategoryExpandAdapter;
import lilun.com.pensionlife.module.adapter.EduCourseAdapter;
import lilun.com.pensionlife.module.adapter.NormalFilterAdapter;
import lilun.com.pensionlife.module.adapter.SemesterAdapter;
import lilun.com.pensionlife.module.bean.ConditionOption;
import lilun.com.pensionlife.module.bean.Option;
import lilun.com.pensionlife.module.bean.OrganizationProduct;
import lilun.com.pensionlife.module.bean.OrganizationProductCategory;
import lilun.com.pensionlife.module.bean.Semester;
import lilun.com.pensionlife.module.utils.Preconditions;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.ui.education.colleage_details.CollegeDetailFragment;
import lilun.com.pensionlife.ui.education.course_details.CourseDetailFragment;
import lilun.com.pensionlife.widget.DividerDecoration;
import lilun.com.pensionlife.widget.NormalTitleBar;
import lilun.com.pensionlife.widget.filter_view.FilterView;

//import lilun.com.pensionlife.module.adapter.CourseCategoryExpandAdapter;

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

    @Bind(R.id.iv_college_introduction)
    ImageView ivCollegeIntroduction;
    private EduCourseAdapter mEduCourseAdapter;
    //    private String[] filterTitles = {"区域", "价格", "等级"};
    private String mOrganizationId;
    private CourseListFilter mFilter;
    private CourseCategoryExpandAdapter categoryExpandAdapter;
    private OrganizationProductCategory mCurrentClickCategory;
    private int mCurrentClickPosition;
    private List<Integer> expandedPositions = new ArrayList<>();
    private SemesterAdapter semesterAdapter;


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
            getCategories();
            getSemesters();
        }
    }


    @Override
    protected void initView(LayoutInflater inflater) {
        titleBar.setTitle("课程分类");
        titleBar.setOnBackClickListener(this::pop);

        ivCollegeIntroduction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //大学简介
                start(CollegeDetailFragment.newInstance(mOrganizationId));
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerDecoration(App.context, LinearLayoutManager.VERTICAL, (int) App.context.getResources().getDimension(R.dimen.dp_1), Color.parseColor("#f5f5f9")));

        mEduCourseAdapter = new EduCourseAdapter(new ArrayList<>());
        mEduCourseAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                OrganizationProduct course = mEduCourseAdapter.getItem(position);
                //TODO 课程详情 预约
                assert course != null;
                start(CourseDetailFragment.newInstance(course.getId()));
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


        filterTitles.add("智能筛选");
        RecyclerView categoryRecyclerView = new RecyclerView(App.context);
        ArrayList<OrganizationProductCategory> init = new ArrayList<>();
        OrganizationProductCategory initCategory = new OrganizationProductCategory();
        initCategory.setTitle("智能筛选");
        init.add(initCategory);
//        categoryRecyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1000));
//        categoryRecyclerView.setBackgroundColor(Color.parseColor("#ffffff"));
        categoryExpandAdapter = new CourseCategoryExpandAdapter(init);
        categoryExpandAdapter.setOnItemClickListener((adapter, view, position) -> {
            mCurrentClickCategory = categoryExpandAdapter.getItem(position);
            assert mCurrentClickCategory != null;

            //如果是第一个条目就去掉分类筛选
            if (position == 0) {
                filterView.setTabText(mCurrentClickCategory.getTitle(), true);
                mFilter.getWhere().setOrgCategoryId(null);
                getDataList(0);
                //TODO 折叠所有的
                List<OrganizationProductCategory> data = categoryExpandAdapter.getData();
                for (int i = 0; i < data.size(); i++) {
                    OrganizationProductCategory category = data.get(i);
                    if (category.getLevel() > 0) {
                        data.remove(category);
                        i--;
                    }
                }
                categoryExpandAdapter.notifyDataSetChanged();
                return;
            }


            //如果是课程就直接刷新数据
//            String kind = mCurrentClickCategory.getTag().get("kind");
//            if (kind.equals("course")) {
//                filterView.setTabText(mCurrentClickCategory.getTitle(), false);
//                mFilter.getWhere().setOrgCategoryId(mCurrentClickCategory.getId());
//                getDataList(0);
//                return;
//            }


            mCurrentClickPosition = position;
            assert mCurrentClickCategory != null;

            //是打开的就关闭
            if (mCurrentClickCategory.isExpanded()) {
                List<OrganizationProductCategory> subZhuantes = mCurrentClickCategory.getSubItems();
                //TODO 隐藏当前和所有儿子中展开的条目
                for (int i = 0; i < subZhuantes.size(); i++) {
                    OrganizationProductCategory subZhuanye = subZhuantes.get(i);
                    if (subZhuanye.isExpanded()) {
                        List<OrganizationProductCategory> courses = subZhuanye.getSubItems();
                        for (int j = 0; i < courses.size(); j++) {
                            OrganizationProductCategory course = courses.get(j);
                            subZhuanye.removeSubItem(course);
                            categoryExpandAdapter.getData().remove(course);
                            j--;
                        }
                    }
                    mCurrentClickCategory.removeSubItem(subZhuanye);
                    categoryExpandAdapter.getData().remove(subZhuanye);
                    i--;
                }
                categoryExpandAdapter.notifyDataSetChanged();
                mCurrentClickCategory.setExpanded(false);
                return;
            }


            if (!mCurrentClickCategory.hasSubItem()) {
                String categoryId = mCurrentClickCategory.getId();
                String filter = "{\"where\":{\"parentId\":\"" + categoryId + "\",\"organizationId\":\"" + mOrganizationId + "/#category" + "\"}}";
                mPresenter.getCourseCategories(filter, 0);
            } else {
                expandSome();
            }


        });
        categoryRecyclerView.setAdapter(categoryExpandAdapter);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(App.context, LinearLayoutManager.VERTICAL, false));
        categoryExpandAdapter.expandAll();
        pops.add(categoryRecyclerView);

        //学期
        RecyclerView semesterView = new RecyclerView(App.context);
        filterTitles.add("学期筛选");
        ArrayList<Semester> semesters = new ArrayList<>();
        Semester semester = new Semester();
        semester.setName("不指定学期");
        semesters.add(semester);
        semesterView.setLayoutManager(new LinearLayoutManager(App.context, LinearLayoutManager.VERTICAL, false));
        semesterAdapter = new SemesterAdapter(semesters);
        semesterAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Semester semester = (Semester) adapter.getData().get(position);
                filterView.setTabText(semester.getName(), position == 0);
                mFilter.getWhere().setTermId(position == 0 ? null : semester.getId());
                getDataList(0);
            }
        });

        semesterView.setAdapter(semesterAdapter);
        pops.add(semesterView);


        //时间
        List<Option> kindOptions = new ArrayList<>();
        Option optionDef = new Option("createdAt DESC", "时间排序");
        Option optionDESC = new Option("createdAt DESC", "降序排序");
        Option optionHelp = new Option("createdAt ASC", "升序排序");
        kindOptions.add(optionDef);
        kindOptions.add(optionDESC);
        kindOptions.add(optionHelp);
        ConditionOption conditionOptionOrder = new ConditionOption("order", "时间排序", kindOptions);
        filterTitles.add(conditionOptionOrder.getCondition());
        RecyclerView timeView = new RecyclerView(App.context);
        timeView.setLayoutManager(new LinearLayoutManager(App.context, LinearLayoutManager.VERTICAL, false));
        NormalFilterAdapter adapter = new NormalFilterAdapter(conditionOptionOrder);
        adapter.setOnItemClickListener((position, title, whereKey, whereValue) -> {
            filterView.setTabText(title, position == 0);
            mFilter.setOrder(whereValue);
            getDataList(0);
        });
        timeView.setAdapter(adapter);
        pops.add(timeView);

        filterView.setTitlesAndPops(filterTitles, pops, mSwipeLayout);
    }

    private void expandSome() {
        categoryExpandAdapter.expand(mCurrentClickPosition);
        mCurrentClickCategory.setExpanded(true);
        expandedPositions.add(mCurrentClickPosition);
    }

    /**
     * 获取班级（产品）列表
     */
    private void getDataList(int skip) {
        String gtmTime = StringUtils.currentTimeToGTM();
        mFilter.setTime(gtmTime);
        Gson gson = new Gson();
        String filter = gson.toJson(mFilter);
        Logger.d("班级 filter ----- " + filter);
        mSwipeLayout.setRefreshing(skip == 0);
        mPresenter.getCourses(filter, skip);
    }

    /**
     * 获取分类列表
     */
    private void getCategories() {
        String gtmTime = StringUtils.currentTimeToGTM();
        String initParentId = mOrganizationId + "/教育服务/其他教育服务/老年教育服务";
        String categoryFilter = "{\"where\":{\"parentId\":\"" + initParentId + "\",\"organizationId\":\"" + mOrganizationId + "/#category" + "\",\"tag.kind\":\"major\",\"and\":[{\"or\":[{\"startTime\":{\"lte\":\"" + gtmTime + "\"}},{\"startTime\":{\"$exists\":false}}]},{\"or\":[{\"endTime\":{\"gte\":\"" + gtmTime + "\"}},{\"endTime\":{\"$exists\":false}}]}]}}";
        Logger.d("获取班级分类 filter ----- " + categoryFilter);
        mPresenter.getCourseCategories(categoryFilter, 0);
    }


    /**
     * 获取学期列表
     */
    private void getSemesters() {
        String gtmTime = StringUtils.currentTimeToGTM();
        String organizationId = mOrganizationId + "/#semester";
        String filter = "{\"where\":{\"organizationId\":\"" + organizationId + "\",\"and\":[{\"or\":[{\"startTime\":{\"lte\":\"" + gtmTime + "\"}},{\"startTime\":{\"$exists\":false}}]},{\"or\":[{\"endTime\":{\"gte\":\"" + gtmTime + "\"}},{\"endTime\":{\"$exists\":false}}]}]}}";
        Logger.d("获取学期 filter ----- " + filter);
        mPresenter.getSemesters(filter);
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

    @Override
    public void getCategorySuccess(List<OrganizationProductCategory> categories) {
        if (mCurrentClickCategory != null) {
            if (categories.size() > 0) {
                //把展开的position存起来
                expandSome();
                for (OrganizationProductCategory category : categories) {
                    category.setLevel(mCurrentClickCategory.getLevel() + 1);
                    mCurrentClickCategory.addSubItem(category);
                    categoryExpandAdapter.addData(mCurrentClickPosition + 1, category);
                }
            } else {
                //如果没有孩子类别了，直接刷新数据
                filterView.setTabText(mCurrentClickCategory.getTitle(), false);
                mFilter.getWhere().setOrgCategoryId(mCurrentClickCategory.getId());
                getDataList(0);
            }
        } else {
            categoryExpandAdapter.addData(categories);
        }

    }

    @Override
    public void getSemesterSuccess(List<Semester> semesters) {
        semesterAdapter.addAll(semesters);
    }


}
