package lilun.com.pensionlife.ui.change_organization;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.Button;

import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.Config;
import lilun.com.pensionlife.app.Constants;
import lilun.com.pensionlife.app.Event;
import lilun.com.pensionlife.app.User;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.adapter.ChangeOrganizationAdapter;
import lilun.com.pensionlife.module.bean.Organization;
import lilun.com.pensionlife.module.utils.ACache;
import lilun.com.pensionlife.module.utils.ToastHelper;
import lilun.com.pensionlife.widget.BreadCrumbsView;
import lilun.com.pensionlife.widget.ElderModuleClassifyDecoration;

////

/**
 * 切换地球村社区
 * 修改: 2017/6/2  切换小区最高层级为重庆   必须选择到街道及以下区域
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
    private String currentId = Constants.organization_root_chongqi;
//    private int skip = 0;

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
//            skip = 0;
            getData(0, false);
        });
        btnConfirm.setOnClickListener(v -> changeCurrentOrganization());

        RecyclerView recyclerView = (RecyclerView) mRootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new ElderModuleClassifyDecoration(10));

        adapter = new ChangeOrganizationAdapter(new ArrayList<>());
        adapter.setOnLoadMoreListener(() -> getData(adapter.getItemCount(), false), recyclerView);
        adapter.setOnItemClickListener((baseQuickAdapter, view, position) -> {
            Organization organization = adapter.getData().get(position);
            currentId = organization.getId();
//            skip = 0;
            getData(0, true);
        });

        recyclerView.setAdapter(adapter);


        swipeLayout.setOnRefreshListener(() -> {
            getData(0, false);
        });

    }

    private void changeCurrentOrganization() {
        String belongsOrganizationId = User.getBelongsOrganizationId();
        boolean equals = belongsOrganizationId.equals(Constants.organization_root);
        //如果本来的所属组织不是地丢村，并且当前选择的组织是本身的默认组织
        if (!equals && TextUtils.equals(currentId, belongsOrganizationId)) {
            if (User.currentOrganizationHasChanged()) {
                mPresenter.changeDefBelongOrganization(User.getBelongOrganizationAccountId());
            } else {
                saveData();
            }
        } else {
            if (currentId.split("/").length < 7) {
                ToastHelper.get().showWareShort("您需要选择到街道以下区域");
                return;
            }
            saveData();
        }
    }

    @Override
    public void showOrganizations(List<Organization> organizations, boolean isLoadMore, boolean isAddCrumb) {
        completeRefresh();
        if (isLoadMore) {
            adapter.addAll(organizations, Config.defLoadDatCount);
        } else {
            adapter.replaceAll(organizations);
        }
        if (isAddCrumb) {
            Logger.d("获取组织成功，添加面包屑---" + currentId);
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
