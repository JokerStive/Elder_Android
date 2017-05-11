package lilun.com.pension.ui.health.classify;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import lilun.com.pension.R;
import lilun.com.pension.app.User;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.adapter.ElderModuleAdapter;
import lilun.com.pension.module.adapter.HealthServiceAdapter;
import lilun.com.pension.module.bean.ElderModule;
import lilun.com.pension.module.bean.Information;
import lilun.com.pension.module.callback.TitleBarClickCallBack;
import lilun.com.pension.module.utils.Preconditions;
import lilun.com.pension.ui.agency.list.AgencyServiceListFragment;
import lilun.com.pension.ui.announcement.AnnouncementFragment;
import lilun.com.pension.ui.health.detail.LoadH5Fragment;
import lilun.com.pension.ui.health.list.HealthListFragment;
import lilun.com.pension.widget.ElderModuleClassifyDecoration;
import lilun.com.pension.widget.ElderModuleItemDecoration;
import lilun.com.pension.widget.PositionTitleBar;

/**
 * 健康服务V
 *
 * @author yk
 *         create at 2017/2/7 16:04
 *         email : yk_developer@163.com
 */
public class HealthClassifyFragment extends BaseFragment<HealthClassifyContract.Presenter> implements HealthClassifyContract.View {


    @Bind(R.id.title_bar)
    PositionTitleBar titleBar;


    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.swipe_layout)
    SwipeRefreshLayout mSwipeLayout;


    private RecyclerView mClassifyRecycler;

    private List<Information> products = new ArrayList<>();
    private HealthServiceAdapter mAdapter;
    private String parentId;

    public static HealthClassifyFragment newInstance(String parentId) {
        HealthClassifyFragment fragment = new HealthClassifyFragment();
        Bundle args = new Bundle();
        args.putString("parentId", parentId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void getTransferData(Bundle arguments) {
        parentId = arguments.getString("parentId");
        Preconditions.checkNull(parentId);
//        announcements = (ArrayList<Information>) arguments.getSerializable("announcements");
    }

    @Override
    protected void initPresenter() {
        mPresenter = new HealthClassifyPresenter();
        mPresenter.bindView(this);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_second_module;
    }


    @Override
    protected void initView(LayoutInflater inflater) {
        titleBar.setTitle(parentId);
        titleBar.setTitleBarClickListener(new TitleBarClickCallBack() {
            @Override
            public void onBackClick() {
                pop();
            }

            @Override
            public void onPositionChanged() {

            }

            @Override
            public void onRightClick() {

            }
        });


        replaceLoadRootFragment(R.id.fl_announcement_container, AnnouncementFragment.newInstance(parentId), false);


        //类别
        mClassifyRecycler = new RecyclerView(_mActivity);
        mClassifyRecycler.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mClassifyRecycler.addItemDecoration(new ElderModuleClassifyDecoration());


        //求助列表
        mRecyclerView.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new ElderModuleItemDecoration());


        //刷新
        mSwipeLayout.setEnabled(false);
//        mSwipeLayout.setOnRefreshListener(() -> {
//                    if (mPresenter != null) {
////                        getServices(0);
//                    }
//                }
//        );


        //设置数据
        setAdapter();

    }


    private void setAdapter() {
        mAdapter = new HealthServiceAdapter(this, products);
        mAdapter.addHeaderView(mClassifyRecycler);
        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    protected void initEvent() {
        refreshData();
    }

    private void refreshData() {
        mSwipeLayout.setRefreshing(true);
        getClassifies();
        //  getServices(0);
    }

    private void getClassifies() {
        mPresenter.getClassifies(parentId);
    }

    private void getServices(int skip) {
        String filter = "{\"where\":{\"categoryId\":\"" + User.getUserId() + "\"}}";
        mPresenter.getAboutMe(filter, skip);
    }


    @Override
    public void showClassifies(List<ElderModule> elderModules) {
        completeRefresh();
        mClassifyRecycler.setLayoutManager(new GridLayoutManager(_mActivity, spanCountByData(elderModules)));
        ElderModuleAdapter adapter = new ElderModuleAdapter(this, elderModules);
        adapter.setOnItemClickListener((elderModule -> {
            String name = elderModule.getName();
            if (name.equals("陪诊服务")) {
                //跳转到居家服务
                start(AgencyServiceListFragment.newInstance("陪诊服务", "/服务/居家服务/陪诊服务", 0));
            } else if (name.equals("健康刊物")) {
                //加载一个url
                start(LoadH5Fragment.newInstance(name, "http://www.yydaobao.com/"));
            } else {
                //列表展示信息
                start(HealthListFragment.newInstance(elderModule));
            }

        }));
        mClassifyRecycler.setAdapter(adapter);
        getServices(0);
    }

    @Override
    public void showAboutMe(List<Information> products, boolean isLoadMore) {
        completeRefresh();
//        if (products != null) {
//            if (isLoadMore) {
//                mAdapter.addAll(products);
//            } else {
//                mAdapter.replaceAll(products);
//            }
//        }
    }

    @Override
    public void completeRefresh() {
        if (mSwipeLayout != null && mSwipeLayout.isRefreshing()) {
            mSwipeLayout.setRefreshing(false);
        }
    }

    private int spanCountByData(List data) {
        int count;
        if (data.size() == 0) {
            count = 1;
        } else {
            count = data.size() >= 3 ? 3 : data.size();
        }
        return count;
    }


}
