package lilun.com.pension.ui.activity.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import lilun.com.pension.R;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.adapter.OrganizationActivityAdapter;
import lilun.com.pension.module.bean.ActivityCategory;
import lilun.com.pension.module.bean.OrganizationActivity;
import lilun.com.pension.module.utils.Preconditions;
import lilun.com.pension.ui.activity.classify.ActivityClassifyContract;
import lilun.com.pension.ui.activity.classify.ActivityClassifyPresenter;
import lilun.com.pension.widget.ElderModuleItemDecoration;
import lilun.com.pension.widget.NormalTitleBar;

/**
 * 分类活动V
 *
 * @author yk
 *         create at 2017/2/7 16:04
 *         email : yk_developer@163.com
 */
public class ActivityListFragment extends BaseFragment<ActivityClassifyContract.Presenter> implements ActivityClassifyContract.View {


    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.swipe_layout)
    SwipeRefreshLayout mSwipeLayout;
    @Bind(R.id.titleBar)
    NormalTitleBar titleBar;

    private OrganizationActivityAdapter mActivityAdapter;
    private ActivityCategory mCategory;

    public static ActivityListFragment newInstance(ActivityCategory category) {
        ActivityListFragment fragment = new ActivityListFragment();
        Bundle args = new Bundle();
        args.putSerializable("OrganizationActivityCategory", category);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void getTransferData(Bundle arguments) {
        mCategory = (ActivityCategory) arguments.getSerializable("OrganizationActivityCategory");
        Preconditions.checkNull(mCategory);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ActivityClassifyPresenter();
        mPresenter.bindView(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_recycler;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        titleBar.setTitle(mCategory.getName());
        titleBar.setOnBackClickListener(this::pop);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new ElderModuleItemDecoration());
        //刷新
        mSwipeLayout.setOnRefreshListener(() -> {
                    if (mPresenter != null) {
                        getHelps(0);
                    }
                }
        );
    }


    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            mSwipeLayout.setRefreshing(true);
            getHelps(0);
        }
    }

    private void getHelps(int skip) {
        // TODO 关联organizationId
        String filter = "{\"where\":{\"categoryId\":\"" + mCategory.getId() + "\"}}";
        mPresenter.getAboutMe(filter, skip);
    }

    @Override
    public void showClassifies(List<ActivityCategory> activityCategories) {

    }

    @Override
    public void showAboutMe(List<OrganizationActivity> activities, boolean islOadMore) {
        completeRefresh();
        if (mActivityAdapter == null) {
            mActivityAdapter = new OrganizationActivityAdapter(this, activities);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
