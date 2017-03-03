package lilun.com.pension.ui.education.course_list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import java.util.List;

import butterknife.Bind;
import lilun.com.pension.R;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.adapter.EduCourseAdapter;
import lilun.com.pension.module.bean.EdusColleageCourse;
import lilun.com.pension.module.bean.ElderEdus;
import lilun.com.pension.module.utils.Preconditions;
import lilun.com.pension.ui.education.course_details.CourseDetailFragment;
import lilun.com.pension.widget.ElderModuleItemDecoration;
import lilun.com.pension.widget.NormalTitleBar;

/**
 * 老年教育 课程列表 V
 *
 * @author yk
 *         create at 2017/2/7 16:04
 *         email : yk_developer@163.com
 */
public class CourseListFragment extends BaseFragment<CourseListContract.Presenter>
        implements CourseListContract.View {

    ElderEdus mColleage;
    private EduCourseAdapter mEduCourseAdapter;
    @Bind(R.id.titleBar)
    NormalTitleBar titleBar;
    @Bind(R.id.swipe_layout)
    SwipeRefreshLayout mSwipeLayout;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    int skip = 0;


    public static CourseListFragment newInstance(ElderEdus colleage) {
        CourseListFragment fragment = new CourseListFragment();
        Bundle args = new Bundle();
        args.putSerializable("Colleage", colleage);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void getTransferData(Bundle arguments) {
        mColleage = (ElderEdus) arguments.getSerializable("Colleage");
        Preconditions.checkNull(mColleage);
    }


    @Override
    protected void initPresenter() {
        mPresenter = new CourseListPresenter();
        mPresenter.bindView(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_recycler;
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
        titleBar.setTitle(mColleage.getTitle());
        titleBar.setOnBackClickListener(new NormalTitleBar.OnBackClickListener() {
            @Override
            public void onBackClick() {
                pop();
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
    }

    private void getDataList(String schoolId, int skip) {

        mPresenter.getCollgCourseList(schoolId, "", skip);

    }


    @Override
    public void showCollgCourseList(List<EdusColleageCourse> courses, boolean isLoadMore) {
        skip += courses.size();
        if (mEduCourseAdapter == null) {
            mEduCourseAdapter = new EduCourseAdapter(this, courses);

            mRecyclerView.setAdapter(mEduCourseAdapter);
            mEduCourseAdapter.setOnItemClickListener((item) -> {
                   start(CourseDetailFragment.newInstance(mColleage,item));
            });

            mEduCourseAdapter.setOnLoadMoreListener(() -> {
                getDataList(mColleage.getId(), skip);
            });
        } else if (isLoadMore) {
            mEduCourseAdapter.addAll(courses);
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
