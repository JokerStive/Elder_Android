package lilun.com.pension.ui.change_organization;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.Button;

import com.afollestad.materialdialogs.MaterialDialog;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import lilun.com.pension.R;
import lilun.com.pension.app.App;
import lilun.com.pension.app.Constants;
import lilun.com.pension.app.Event;
import lilun.com.pension.app.User;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.adapter.ChangeOrganizationAdapter;
import lilun.com.pension.module.bean.Organization;
import lilun.com.pension.module.utils.ACache;
import lilun.com.pension.widget.BreadCrumbsView;
import lilun.com.pension.widget.DividerGridItemDecoration;
import lilun.com.pension.widget.NormalDialog;

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
//    @Bind(R.id.recycler_view)
//    RecyclerView recyclerView;
    @Bind(R.id.swipe_layout)
    SwipeRefreshLayout swipeLayout;
    @Bind(R.id.btn_confirm)
    Button btnConfirm;

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

        crumbView.setonCrumbClickListener(id -> {
            currentId = id;
            getData(0, false);
        });
        btnConfirm.setOnClickListener(v -> changeCurrentOrganization());

        adapter = new ChangeOrganizationAdapter(new ArrayList<>());
        RecyclerView recyclerView = (RecyclerView) mRootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerGridItemDecoration(App.context));
        recyclerView.setAdapter(adapter);
        adapter.setOnRecyclerViewItemClickListener((view, i) -> {
            Organization organization = adapter.getData().get(i);
            currentId = organization.getId();
            getData(0, true);
        });
        adapter.setOnLoadMoreListener(() -> getData(adapter.getItemCount(),false));

        swipeLayout.setOnRefreshListener(() -> getData(0, false));

    }

    private void changeCurrentOrganization() {
        String belongsOrganizationId = User.getBelongsOrganizationId();
        boolean equals = belongsOrganizationId.equals(Constants.organization_root);
        //如果本来的所属组织不是地丢村，并且当前选择的组织是本身的默认组织
        if (!equals && TextUtils.equals(currentId, belongsOrganizationId)) {
            if (User.currentOrganizationHasChanged()) {
                mPresenter.changeDefBelongOrganization(User.getBelongOrganizationAccountId());
            } else {
                _mActivity.finish();
            }
        } else {
            saveData();
        }
    }

    @Override
    public void showOrganizations(List<Organization> organizations, boolean isLoadMore, boolean isAddCrumb) {
        completeRefresh();
        if (isLoadMore) {
            adapter.addAll(organizations);
        } else {
            adapter.replaceAll(organizations);
        }
        if (isAddCrumb) {
            Logger.d("获取组织成功，添加面包屑---"+currentId);
            crumbView.addBreadCrumb(currentId);
        }
    }

    /**
     * 切换了社区保存数据退出
     */
    private void saveData() {
        User.putCurrentOrganizationId(currentId);
        EventBus.getDefault().post(new Event.ChangedOrganization());
        User.putCurrentOrganizationHasChanged(true);
        _mActivity.finish();
        cacheIds();
    }

    private void cacheIds() {
        ACache.get().put("chooseIds", "");
        List<String> ids = crumbView.getIds();
        ACache.get().put("chooseIds", (Serializable) ids);
    }

    @Override
    public void changedRoot() {
        if (User.currentOrganizationHasChanged() && ACache.get().isExit("chooseIds")) {
            List<String> ids = (List<String>) ACache.get().getAsObject("chooseIds");
            currentId = ids.get(ids.size() - 1);
            Logger.d("当前组织id = " + currentId);
            for (String organizationId : ids) {
                Logger.d("有缓存的面包屑，添加...");
                crumbView.addBreadCrumb(organizationId);
            }
        } else {
            Logger.d("没有任何面包屑，添加root");
            crumbView.addBreadCrumb(currentId);
        }
        getData(0, false);
    }

    @Override
    public void changedBelong() {
//        Logger.d("切换会了自己的组织");/**/
        EventBus.getDefault().post(new Event.ChangedOrganization());
        User.putCurrentOrganizationHasChanged(false);
        ACache.get().put("chooseIds", "");
        _mActivity.finish();
    }


    private void getData(int skip, boolean isAddCrumb) {
        swipeLayout.setRefreshing(true);
        mPresenter.getOrganizations(currentId, null, skip, isAddCrumb);
    }

    @Override
    public void completeRefresh() {
        if (swipeLayout != null && swipeLayout.isRefreshing()) {
            swipeLayout.setRefreshing(false);
        }
    }


}
