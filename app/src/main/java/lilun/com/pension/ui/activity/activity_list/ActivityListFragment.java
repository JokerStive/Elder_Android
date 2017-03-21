package lilun.com.pension.ui.activity.activity_list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.Bind;
import lilun.com.pension.R;
import lilun.com.pension.app.Event;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.adapter.OrganizationActivityAdapter;
import lilun.com.pension.module.bean.ActivityCategory;
import lilun.com.pension.module.bean.OrganizationActivity;
import lilun.com.pension.module.utils.Preconditions;
import lilun.com.pension.ui.activity.activity_detail.ActivityDetailFragment;
import lilun.com.pension.widget.ElderModuleItemDecoration;

/**
 * 分类活动V
 *
 * @author yk
 *         create at 2017/2/7 16:04
 *         email : yk_developer@163.com
 */
public class ActivityListFragment extends BaseFragment<ActivityListContract.Presenter>
        implements ActivityListContract.View {


    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.swipe_layout)
    SwipeRefreshLayout mSwipeLayout;
//    @Bind(R.id.titleBar)
//    NormalTitleBar titleBar;

    private OrganizationActivityAdapter mActivityAdapter;
    private ActivityCategory mCategory;

    public static ActivityListFragment newInstance(ActivityCategory category) {
        ActivityListFragment fragment = new ActivityListFragment();
        Bundle args = new Bundle();
        args.putSerializable("OrganizationActivityCategory", category);
        fragment.setArguments(args);
        return fragment;
    }

    @Subscribe
    public void refreshData(Event.RefreshActivityData event){
        getActivityList(0);
    }


    @Override
    protected void getTransferData(Bundle arguments) {
        mCategory = (ActivityCategory) arguments.getSerializable("OrganizationActivityCategory");
        Preconditions.checkNull(mCategory);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ActivtyListPresenter();
        mPresenter.bindView(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search_list;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
//        titleBar.setTitle(mCategory.getName());
//        titleBar.setOnBackClickListener(this::pop);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new ElderModuleItemDecoration());
        //刷新
        mSwipeLayout.setOnRefreshListener(() -> {
                    if (mPresenter != null) {
                        getActivityList(0);
                    }
                }
        );
    }


    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            mSwipeLayout.setRefreshing(true);
            getActivityList(0);
        }
    }

    private void getActivityList(int skip) {
        // TODO 关联organizationId
        String filter = "{\"where\":{\"categoryId\":\"" + mCategory.getId() + "\"}}";
        mPresenter.getOrganizationActivities(filter, skip);
    }


    @Override
    public void showActivityList(List<OrganizationActivity> activities, boolean islOadMore) {
        completeRefresh();
        if (mActivityAdapter == null) {
            mActivityAdapter = new OrganizationActivityAdapter(this, activities);
            mActivityAdapter.setEmptyView();
            mActivityAdapter.setOnItemClickListener((activityItem)->{
                start(ActivityDetailFragment.newInstance(activityItem.getId()));
            });
            mRecyclerView.setAdapter(mActivityAdapter);
        } else if (islOadMore) {
            mActivityAdapter.addAll(activities);
        } else {
            mActivityAdapter.replaceAll(activities);
        }
    }

    @Override
    public void completeRefresh() {
        if (mSwipeLayout != null && mSwipeLayout.isRefreshing()) {
            mSwipeLayout.setRefreshing(false);
        }
    }


}
