package lilun.com.pension.ui.change_organization;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
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
import lilun.com.pension.module.utils.ACache;
import lilun.com.pension.module.utils.StringUtils;
import lilun.com.pension.widget.NormalDialog;
import lilun.com.pension.widget.NormalItemDecoration;

/**
 * 切换附近社区
 *
 * @author yk
 *         create at 2017/4/21 10:17
 *         email : yk_developer@163.com
 */
public class NearOrganizationFragment extends BaseFragment<ChangeOrganizationContract.Presenter> implements ChangeOrganizationContract.View {
    @Bind(R.id.tv_current_organization)
    TextView tvCurrentOrganization;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.swipe_layout)
    SwipeRefreshLayout swipeLayout;
    @Bind(R.id.tv_locat_address)
    TextView tvLocatAddress;
    private ChangeOrganizationAdapter adapter;

    @Override
    protected void initPresenter() {

        mPresenter = new ChangeOrganizationPresenter();
        mPresenter.bindView(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_change_organization_near;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        String name = User.getCurrentOrganizationName();
        tvCurrentOrganization.setText("当前社区:" + name);
        tvLocatAddress.setText(StringUtils.getOrganizationNameFromId(User.getBelongToDistrict()));
        tvLocatAddress.setOnClickListener(v->{
            new NormalDialog().createNormal(_mActivity, "确定要切换回居住小区么？", new NormalDialog.OnPositiveListener() {
                @Override
                public void onPositiveClick() {
                    changeCurrentOrganization();
                }
            });
        });
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new NormalItemDecoration(Config.list_decoration));

//        swipeLayout.setRefreshing(true);
    }
    private void changeCurrentOrganization() {
        mPresenter.changeDefBelongOrganization(User.getBelongToDistrict());
    }


    @Override
    public void showOrganizations(List<Organization> organizations, boolean isLoadMore,boolean isAddCrumb) {
        completeRefresh();
        if (adapter == null) {
            adapter = new ChangeOrganizationAdapter(organizations);
            adapter.setOnRecyclerViewItemClickListener((view, i) -> {
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
        Logger.d("加载地球村数据");
    }

    @Override
    public void changedBelong() {
        Logger.d("切换会了自己的组织");
        EventBus.getDefault().post(new Event.ChangedOrganization());
        User.putCurrentOrganizationHasChanged(false);
        ACache.get().put("chooseIds", "");
        _mActivity.finish();
    }

    @Override
    public void completeRefresh() {
        if (swipeLayout != null && swipeLayout.isRefreshing()) {
            swipeLayout.setRefreshing(false);
        }
    }
}
