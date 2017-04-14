package lilun.com.pension.ui.change_organization;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.Bind;
import lilun.com.pension.R;
import lilun.com.pension.app.Event;
import lilun.com.pension.app.User;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.adapter.ChangeOrganizationAdapter;
import lilun.com.pension.module.bean.Organization;
import lilun.com.pension.widget.NormalItemDecoration;
import lilun.com.pension.widget.NormalTitleBar;

/**
 * 切换当前位置
 *
 * @author yk
 *         create at 2017/4/5 13:13
 *         email : yk_developer@163.com
 */

public class ChangeOrganizationFragment extends BaseFragment<ChangeOrganizationContract.Presenter> implements ChangeOrganizationContract.View {

    @Bind(R.id.tv_current_organization)
    TextView tvCurrentOrganization;

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    @Bind(R.id.swipe_layout)
    SwipeRefreshLayout swipeLayout;

    @Bind(R.id.titleBar)
    NormalTitleBar titleBar;
    private ChangeOrganizationAdapter adapter;

    public static ChangeOrganizationFragment newInstance() {
        ChangeOrganizationFragment fragment = new ChangeOrganizationFragment();
        return fragment;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ChangeOrganizationPresenter();
        mPresenter.bindView(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_change_organization;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        String name = User.getCurrentOrganizationName();
        tvCurrentOrganization.setText("当前社区:" + name);


        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new NormalItemDecoration(10));


        titleBar.setOnBackClickListener(() -> changeToBelong());
    }


    /**
     * 切回自己原来属于的社区
     */
    private void changeToBelong() {
        mPresenter.changeDefBelongOrganization(User.getBelongOrganizationAccountId());
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);

        swipeLayout.setRefreshing(true);
        mPresenter.changeDefBelongOrganization(User.getRootOrganizationAccountId());
    }


    @Override
    public void showOrganizations(List<Organization> organizations, boolean isLoadMore) {
        completeRefresh();
        if (adapter == null) {
            adapter = new ChangeOrganizationAdapter(organizations);
            adapter.setOnRecyclerViewItemClickListener((view, i) -> {
                Organization organization = adapter.getData().get(i);
                User.puttCurrentOrganizationId(organization.getId());
                EventBus.getDefault().post(new Event.ChangedOrganization());
                pop();
            });
            recyclerView.setAdapter(adapter);
        } else if (isLoadMore) {
            adapter.addAll(organizations);
        } else {
            adapter.replaceAll(organizations);
        }

    }

    @Override
    public void changedRoot() {
        getData(0);
    }

    private void getData(int skip) {
        String belongsOrganizationId = User.getBelongsOrganizationId();
        String substring = belongsOrganizationId.substring(0, belongsOrganizationId.lastIndexOf("/"));
        mPresenter.getOrganizations(substring, null, skip);
    }

    @Override
    public void changedBelong() {
        pop();
    }

    @Override
    public void completeRefresh() {
        if (swipeLayout != null && swipeLayout.isRefreshing()) {
            swipeLayout.setRefreshing(false);
        }
    }


    @Override
    public boolean onBackPressedSupport() {
        changeToBelong();
        return true;
    }
}
