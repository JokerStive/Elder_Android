package lilun.com.pensionlife.ui.health.list;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.widget.ImageView;

import java.util.List;

import butterknife.Bind;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.app.Config;
import lilun.com.pensionlife.app.User;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.adapter.HealthServiceAdapter;
import lilun.com.pensionlife.module.bean.ElderModule;
import lilun.com.pensionlife.module.bean.Information;
import lilun.com.pensionlife.module.utils.Preconditions;
import lilun.com.pensionlife.ui.health.detail.InfoDetailFragment;
import lilun.com.pensionlife.widget.DividerDecoration;
import lilun.com.pensionlife.widget.NormalTitleBar;

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
     //   mRecyclerView.addItemDecoration(new DividerDecoration(App.context, LinearLayoutManager.VERTICAL, (int) App.context.getResources().getDimension(R.dimen.dp_1), Color.parseColor("#f5f5f9")));
        mSwipeLayout.setOnRefreshListener(() -> getData(0));
        getData(0);
    }

    private void getData(int skip) {
        mSwipeLayout.setRefreshing(skip == 0);
        String filter;
        String parent = mClassify.getParent();
        String name = mClassify.getName();
        String parentIdFilter = spliceParentId(parent + "/" + name);
        filter = "{\"order\":\"createdAt DESC\",\"where\":{\"visible\":0,\"isCat\":false,\"parentId\":{\"inq\":" + parentIdFilter + "}}}";
//        filter = "{\"where\":{\"order\":\"createdAt DESC\",\"visible\":0,\"isCat\":false,\"parentId\":{\"inq\":" + parentIdFilter + "}}}";
        mPresenter.getDataList(filter, skip);
    }


    private String spliceParentId(String name) {
        int startIndex = 3;
        String result = "[";
        String currentOrganizationId = User.getCurrentOrganizationId();
        String[] split = currentOrganizationId.split("/");
        if (split.length < startIndex) return "";
        for (int i = startIndex; i < split.length; i++) {
            String parentId = "";
            for (int j = 1; j <= i; j++) {
                parentId += "/" + split[j];
            }
            if (i == startIndex) {
                result += "\"" + parentId + "/#information/" + name + "\"";
            } else {
                result += "," + "\"" + parentId + "/#information/" + name + "\"";
            }
        }
        result += "]";
        return result;
    }

    @Override
    public void showDataList(List<Information> list, boolean isLoadMore) {
        if (mAdapter == null) {
            mAdapter = new HealthServiceAdapter(this, list);
            mAdapter.setOnItemClickListener((adapter, view, i) -> {
                Information information = mAdapter.getData().get(i);
                start(InfoDetailFragment.newInstance(information));
            });
            mAdapter.setOnLoadMoreListener(() -> getData(mAdapter.getItemCount()), mRecyclerView);
            mRecyclerView.setAdapter(mAdapter);

        } else if (isLoadMore) {
            mAdapter.addAll(list, Config.defLoadDatCount);
        } else
            mAdapter.replaceAll(list);
    }


    public void completeRefresh() {
        if (mSwipeLayout != null && mSwipeLayout.isRefreshing()) {
            mSwipeLayout.setRefreshing(false);
        }
    }


}
