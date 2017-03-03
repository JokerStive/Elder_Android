package lilun.com.pension.ui.education.list;

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
import lilun.com.pension.module.adapter.EdusColleageAdapter;
import lilun.com.pension.module.adapter.OrganizationEdusAdapter;
import lilun.com.pension.module.bean.ElderEdus;
import lilun.com.pension.module.bean.ElderEdusColleage;
import lilun.com.pension.module.bean.ElderModule;
import lilun.com.pension.module.utils.Preconditions;
import lilun.com.pension.ui.education.edu_details.ColleageDetailFragment;
import lilun.com.pension.widget.ElderModuleItemDecoration;
import lilun.com.pension.widget.NormalTitleBar;

/**
 * 老年教育 各类型列表 V
 *
 * @author yk
 *         create at 2017/2/7 16:04
 *         email : yk_developer@163.com
 */
public class EducationListFragment extends BaseFragment<EducationListContract.Presenter>
        implements EducationListContract.View {

    ElderModule mElderModule;
    private EdusColleageAdapter mActivityAdapter;
    @Bind(R.id.titleBar)
    NormalTitleBar titleBar;
    @Bind(R.id.swipe_layout)
    SwipeRefreshLayout mSwipeLayout;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
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
        return R.layout.layout_recycler;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            // mAdapter.setRefreshing(true);

            skip = 0;
            getDataList(skip);
        }
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        titleBar.setTitle(mElderModule.getName());
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
                        getDataList(skip);
                    }
                }
        );
    }

    private void getDataList(int skip) {
        String filter = "";
        if (mElderModule.getName().equals(getString(R.string.pension_university)))
            filter = "{\"include\":\"contact\",\"where\":{\"location\":{\"exists\": true}}, \"skip\":" + skip + "}";
        else if (mElderModule.getName().equals(getString(R.string.net_university)))
            filter = "{\"include\":\"contact\",\"where\":{\"location\":{\"exists\": false}}, \"skip\":" + skip + "}";
        mPresenter.getColleage(filter, skip);

    }


    @Override
    public void showEdusList(List<ElderEdusColleage> elderEdusList, boolean isLoadMore) {
        skip += elderEdusList.size();
        if (mActivityAdapter == null) {

            mActivityAdapter = new EdusColleageAdapter(this, elderEdusList);

            mRecyclerView.setAdapter(mActivityAdapter);
            mActivityAdapter.setOnItemClickListener((item) -> {
                start(ColleageDetailFragment.newInstance(item));
            });

            mActivityAdapter.setOnLoadMoreListener(() -> {
                getDataList(skip);
            });
        } else if (isLoadMore) {
            mActivityAdapter.addAll(elderEdusList);
        } else {
            mActivityAdapter.replaceAll(elderEdusList);
        }
    }

    @Override
    public void completeRefresh() {
        if (mSwipeLayout != null && mSwipeLayout.isRefreshing()) {
            mSwipeLayout.setRefreshing(false);
        }
    }


}
