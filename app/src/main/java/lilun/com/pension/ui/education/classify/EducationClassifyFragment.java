package lilun.com.pension.ui.education.classify;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.orhanobut.logger.Logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import lilun.com.pension.R;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.adapter.ElderModuleAdapter;
import lilun.com.pension.module.adapter.OrganizationEdusAdapter;
import lilun.com.pension.module.bean.ActivityCategory;
import lilun.com.pension.module.bean.EdusColleageCourse;
import lilun.com.pension.module.bean.ElderEdus;
import lilun.com.pension.module.bean.ElderModule;
import lilun.com.pension.module.bean.Information;
import lilun.com.pension.module.bean.OrganizationActivity;
import lilun.com.pension.module.callback.TitleBarClickCallBack;
import lilun.com.pension.ui.activity.activity_list.ActivityListFragment;
import lilun.com.pension.ui.announcement.AnnouncementFragment;
import lilun.com.pension.ui.education.course_details.CourseDetailFragment;
import lilun.com.pension.ui.education.colleage_list.EducationListFragment;
import lilun.com.pension.widget.ElderModuleClassifyDecoration;
import lilun.com.pension.widget.ElderModuleItemDecoration;
import lilun.com.pension.widget.PositionTitleBar;

/**
 * 老年教育V
 *
 * @author yk
 *         create at 2017/2/7 16:04
 *         email : yk_developer@163.com
 */
public class EducationClassifyFragment extends BaseFragment<EducationClassifyContract.Presenter>
        implements EducationClassifyContract.View {


    @Bind(R.id.title_bar)
    PositionTitleBar titleBar;

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.swipe_layout)
    SwipeRefreshLayout mSwipeLayout;


    private RecyclerView mClassifyRecycler;

    private List<ElderEdus> products = new ArrayList<>();
    private OrganizationEdusAdapter mAdapter;
    private ArrayList<Information> announcements;

    public static EducationClassifyFragment newInstance(List<Information> announcements) {
        EducationClassifyFragment fragment = new EducationClassifyFragment();
        Bundle args = new Bundle();
        args.putSerializable("announcements", (Serializable) announcements);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void getTransferData(Bundle arguments) {
        announcements = (ArrayList<Information>) arguments.getSerializable("announcements");
    }

    @Override
    protected void initPresenter() {
        mPresenter = new EducationClassifyPresenter();
        mPresenter.bindView(this);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_second_module;
    }


    @Override
    protected void initView(LayoutInflater inflater) {
        titleBar.setTitle(getString(R.string.pension_education));
        titleBar.setTitleBarClickListener(new TitleBarClickCallBack() {
            @Override
            public void onBackClick() {
                pop();
            }

            @Override
            public void onPositionClick() {

            }

            @Override
            public void onRightClick() {

            }
        });


        //初始化公告栏
        if (announcements == null || announcements.size() == 0) {
            Logger.d("公告数据为空");
        } else {
            replaceLoadRootFragment(R.id.fl_announcement_container, AnnouncementFragment.newInstance(announcements), false);
        }

        //类别
        mClassifyRecycler = new RecyclerView(_mActivity);
        mClassifyRecycler.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mClassifyRecycler.addItemDecoration(new ElderModuleClassifyDecoration());


        //求助列表
        mRecyclerView.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new ElderModuleItemDecoration());


        //刷新
        mSwipeLayout.setOnRefreshListener(() -> {
                    if (mPresenter != null) {
                        getServices(0);
                    }
                }
        );


        //设置数据
        setAdapter();
      //  getServices(0);
    }


    private void setAdapter() {
        mAdapter = new OrganizationEdusAdapter(this, products);
        mAdapter.addHeaderView(mClassifyRecycler);
        mAdapter.setOnItemClickListener((item)->{
            if(item.getType() == OrganizationActivity.TYPE){

            }else if(item.getType() == EdusColleageCourse.TYPE){
                start(CourseDetailFragment.newInstance(item.toEdusColleageCourse()));
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    protected void initEvent() {
        refreshData();
    }

    public void refreshData() {
        mSwipeLayout.setRefreshing(true);
        getClassifies();
    }

    private void getClassifies() {
        mPresenter.getClassifies();
    }

    private void getServices(int skip) {
        String filter = "{\"\":\"\"}";
        mPresenter.getAboutMe(filter, skip);
    }


    @Override
    public void showClassifies(List<ElderModule> elderModules) {
        completeRefresh();
        mClassifyRecycler.setLayoutManager(new GridLayoutManager(_mActivity, spanCountByData(elderModules)));
        ElderModuleAdapter adapter = new ElderModuleAdapter(this, elderModules);
        adapter.setOnItemClickListener((edusClassify -> {
            if (edusClassify.getService().equals("Activity")) {
                String filter = "{\"where\":{\"name\":\"" + edusClassify.getServiceConfig().getCategory() + "\"}}";
                mPresenter.getOrgActivityCategory(filter);
            } else
                start(EducationListFragment.newInstance(edusClassify));
        }));
        mClassifyRecycler.setAdapter(adapter);
        getServices(0);
    }


    @Override
    public void showAboutMe(List<ElderEdus> products, boolean isLoadMore) {
        completeRefresh();
        if (products != null) {
            if (isLoadMore) {
                mAdapter.addAll(products);
            } else {
                mAdapter.replaceAll(products);
            }
        }
    }

    @Override
    public void showOrgActivityCategory(List<ActivityCategory> activityCategories) {
        if (activityCategories != null && activityCategories.size() > 0)
            start(ActivityListFragment.newInstance(activityCategories.get(0)));
    }


    @Override
    public void completeRefresh() {
        if (mSwipeLayout != null && mSwipeLayout.isRefreshing()) {
            mSwipeLayout.setRefreshing(false);
        }
    }

    private int spanCountByData(List data) {
        int count;
        if (data.size() == 0) {
            count = 1;
        } else {
            count = data.size() >= 3 ? 3 : data.size();
        }
        return count;
    }


}
