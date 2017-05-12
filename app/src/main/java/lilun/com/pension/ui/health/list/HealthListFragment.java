package lilun.com.pension.ui.health.list;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import butterknife.Bind;
import lilun.com.pension.R;
import lilun.com.pension.app.User;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.adapter.HealthServiceAdapter;
import lilun.com.pension.module.bean.ElderModule;
import lilun.com.pension.module.bean.Information;
import lilun.com.pension.module.utils.Preconditions;
import lilun.com.pension.ui.health.detail.InfoDetailFragment;
import lilun.com.pension.widget.ElderModuleItemDecoration;
import lilun.com.pension.widget.NormalTitleBar;

/**
 * 健康服务V
 *
 * @author yk
 *         create at 2017/2/7 16:04
 *         email : yk_developer@163.com
 */
public class HealthListFragment extends BaseFragment<HealthListContact.Presenter>
        implements HealthListContact.View {


    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.swipe_layout)
    SwipeRefreshLayout mSwipeLayout;
    @Bind(R.id.titleBar)
    NormalTitleBar titleBar;
    @Bind(R.id.null_data)
    ImageView nullData;


    private HealthServiceAdapter mAdapter;
    private ElderModule mClassify;
    private int skip = 0;

    public static HealthListFragment newInstance(ElderModule classify) {
        HealthListFragment fragment = new HealthListFragment();
        Bundle args = new Bundle();
        args.putSerializable("HealtheaProduct", classify);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void getTransferData(Bundle arguments) {
        super.getTransferData(arguments);
        mClassify = (ElderModule) arguments.getSerializable("HealtheaProduct");
        Preconditions.checkNull(mClassify);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new HealthListPresenter();
        mPresenter.bindView(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_recycler;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        titleBar.setTitle(mClassify.getName());
        titleBar.setOnBackClickListener(() -> pop());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new ElderModuleItemDecoration());

        mSwipeLayout.setOnRefreshListener(() -> refreshData());
        refreshData();
    }

    private void refreshData() {
        mSwipeLayout.setRefreshing(true);
        skip = 0;
        String filter;
        String parent = mClassify.getParent();
        String name = mClassify.getName();
        if (TextUtils.equals(parent, "健康服务")) {
            filter = "{\"where\":{\"visible\":0,\"isCat\":false,\"organizationId\":\"" + User.getCurrentOrganizationId() + "/#information" + "\",\"parentId\":\"" + User.getCurrentOrganizationId() + "/#information" + parent + "/" + name + "\"}}";
        } else {
            filter = "{\"where\":{\"visible\":0,\"isCat\":false,\"organizationId\":\"/地球村/中国/重庆/#information\",\"parentId\":\"/地球村/中国/重庆/#information/" + parent + "/" + name + "\"}}";
        }
        mPresenter.getDataList(filter, skip);
    }


    @Override
    public void showDataList(List<Information> list, boolean isLoadMore) {
        skip += list.size();

        if (mAdapter == null) {
            mAdapter = new HealthServiceAdapter(this, list);
            mAdapter.setEmptyView();
            mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, int i) {
                    Information information = mAdapter.getData().get(i);
                    start(InfoDetailFragment.newInstance(information));
                }
            });
            mRecyclerView.setAdapter(mAdapter);

        } else if (isLoadMore) {
            mAdapter.addAll(list);
        } else
            mAdapter.replaceAll(list);
    }


    public void completeRefresh() {
        if (mSwipeLayout != null && mSwipeLayout.isRefreshing()) {
            mSwipeLayout.setRefreshing(false);
        }
    }


}
