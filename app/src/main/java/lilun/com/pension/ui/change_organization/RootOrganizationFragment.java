package lilun.com.pension.ui.change_organization;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.Bind;
import lilun.com.pension.R;
import lilun.com.pension.app.Config;
import lilun.com.pension.app.Constants;
import lilun.com.pension.app.Event;
import lilun.com.pension.app.User;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.adapter.ChangeOrganizationAdapter;
import lilun.com.pension.module.bean.Organization;
import lilun.com.pension.module.utils.PreUtils;
import lilun.com.pension.widget.BreadCrumbsView;
import lilun.com.pension.widget.NormalItemDecoration;

/**
 * 切换地球村社区
 *
 * @author yk
 *         create at 2017/4/21 10:17
 *         email : yk_developer@163.com
 */
public class RootOrganizationFragment extends BaseFragment<ChangeOrganizationContract.Presenter> implements ChangeOrganizationContract.View {
    @Bind(R.id.crumb_view)
    BreadCrumbsView crumbView;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.swipe_layout)
    SwipeRefreshLayout swipeLayout;
    private ChangeOrganizationAdapter adapter;
    private String currentId = Constants.organization_root;

    @Override
    protected void initPresenter() {
        mPresenter = new ChangeOrganizationPresenter();
        mPresenter.bindView(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_change_organization_root;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        crumbView.addBreadCrumb("地球村", Constants.organization_root);
        crumbView.setonCrumbClickListener(id -> {
            currentId = id;
            getData(0);
        });

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new NormalItemDecoration(Config.list_decoration));

//        swipeLayout.setRefreshing(true);
    }

    @Override
    public void showOrganizations(List<Organization> organizations, boolean isLoadMore) {
        completeRefresh();
        if (adapter == null) {
            adapter = new ChangeOrganizationAdapter(organizations);
            adapter.setOnRecyclerViewItemClickListener((view, i) -> {
                Organization organization = adapter.getData().get(i);
                crumbView.addBreadCrumb(organization.getName(), organization.getId());
                currentId = organization.getId();
                getData(0);

//                saveData(organization);
            });
            recyclerView.setAdapter(adapter);
        } else if (isLoadMore) {
            adapter.addAll(organizations);
        } else {
            adapter.replaceAll(organizations);
        }
    }

    /**
     * 切换了社区保存数据退出
     */
    private void saveData(Organization organization) {
        User.puttCurrentOrganizationId(organization.getId());
        EventBus.getDefault().post(new Event.ChangedOrganization());
        PreUtils.putBoolean("currentOrganizationHadChanged", true);
        _mActivity.finish();
    }

    @Override
    public void changedRoot() {
        Logger.d("加载地球村数据");
        getData(0);
    }

    @Override
    public void changedBelong() {
        Logger.d("切换会了自己的组织");
    }


    private void getData(int skip) {
        swipeLayout.setRefreshing(true);

//        String belongsOrganizationId =User.getBelongsOrganizationId();
//        Logger.d(belongsOrganizationId);
//        String substring = belongsOrganizationId.substring(0, belongsOrganizationId.lastIndexOf("/"));
        mPresenter.getOrganizations(currentId, null, skip);
    }

    @Override
    public void completeRefresh() {
        if (swipeLayout != null && swipeLayout.isRefreshing()) {
            swipeLayout.setRefreshing(false);
        }
    }
}
