package lilun.com.pension.ui.health.list;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.widget.ImageView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import lilun.com.pension.R;
import lilun.com.pension.app.OrganizationChildrenConfig;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.adapter.HealthServiceAdapter;
import lilun.com.pension.module.bean.ElderModule;
import lilun.com.pension.module.bean.HealtheaProduct;
import lilun.com.pension.module.bean.Information;
import lilun.com.pension.module.utils.Preconditions;
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

    private List<HealtheaProduct> products = new ArrayList<>();

    private RecyclerView rvPushInfo;
    private HealthServiceAdapter mAdapter;
    private ArrayList<String> data;
    private ElderModule mClassify;
    private int skip = 0;

    public static HealthListFragment newInstance(ElderModule classify) {
        HealthListFragment fragment = new HealthListFragment();
        Bundle args = new Bundle();
        args.putSerializable("HealtheaProduct", (Serializable) classify);
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
        titleBar.setOnBackClickListener(() -> {
            pop();
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new ElderModuleItemDecoration());

        mSwipeLayout.setOnRefreshListener(() -> {
            refreshData();
        });
        refreshData();
    }

    private void refreshData() {
        skip = 0;
        String filter = "{\"where\":{\"organizationId\":\"" + OrganizationChildrenConfig.information() + "\"," +
                "\"isCat\":\"false\"," +
                "\"parentId\":{\"like\":\"/#information/" + mClassify.getServiceConfig().getCategory() + "\"}}}";
        mPresenter.getDataList(filter, skip);
    }


    @Override
    public void showDataList(List<Information> list, boolean isLoadMore) {
        skip += list.size();
//        if (skip == 0)
//            nullData.setVisibility(View.VISIBLE);
//        else
//            nullData.setVisibility(View.GONE);

        if (mAdapter == null) {
            mAdapter = new HealthServiceAdapter(this, list);
            mAdapter.setEmptyView();
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


    private void initPushBar() {
//        rvPushInfo = (RecyclerView) mRootView.findViewById(R.id.rv_push_info);
//        pushInfoAdapter = new PushInfoAdapter(_mActivity, data, R.layout.item_push_info);
//        pushInfoAdapter.setOnPushClickListener(new PushInfoAdapter.onPushClickListener() {
//            @Override
//            public void onDeleteClick(PushInfoAdapter.MyViewHolder item) {
//                pushInfoAdapter.remove(item);
//            }
//
//            @Override
//            public void agree() {
//            }
//
//            @Override
//            public void onExpandClick() {
//                rvPushInfo.setLayoutManager(new LinearLayoutManager(App.context, LinearLayoutManager.VERTICAL, false));
//            }
//        });
//
//        rvPushInfo.setLayoutManager(new OverLayCardLayoutManager());
//        rvPushInfo.setAdapter(pushInfoAdapter);
//        CardConfig.initConfig(_mActivity);
//        CardConfig.MAX_SHOW_COUNT = 3;
//        ItemTouchHelper.Callback callback = new MyCallBack(rvPushInfo, pushInfoAdapter, data);
//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
//        itemTouchHelper.attachToRecyclerView(rvPushInfo);
    }

    public List<HealtheaProduct> getData() {
        List<HealtheaProduct> data = new ArrayList<>();
        HealtheaProduct product1 = new HealtheaProduct();
        product1.setName("有高血压怎么办");
        product1.setAddress(" 患了高血压病,一要在全球高血压联盟推荐的6类降压药中选择,且主张联合应用；二要尽可能选用长效降压药,一日1次用药便可维持血压24小时稳定,减少波动；三要与生活方式改善及饮食调理紧密结合,特别注意低盐,低脂,控制体重和体育锻炼. 全球高血压联盟推荐的6类降压药是利尿剂,血管紧张素转化酶抑制剂（ACEI）,血管紧张素Ⅱ受体拮抗剂,－受体阻滞剂,钙拮抗剂和抗胆碱药.缬沙坦是不错的,可以考虑.");
        data.add(product1);
        HealtheaProduct product2 = new HealtheaProduct();
        product2.setName("佳之佳药房");
        product2.setAddress("重庆市南岸区长生桥镇长生路62号");
        data.add(product2);
        HealtheaProduct product3 = new HealtheaProduct();
        product3.setName("昌野药房");
        product3.setAddress("重庆市南岸区通江大道98号");
        data.add(product3);

        return data;
    }
}
