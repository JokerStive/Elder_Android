package lilun.com.pensionlife.ui.education.classify;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.app.Config;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.adapter.EducationClassifyAdapter;
import lilun.com.pensionlife.module.adapter.OrganizationEdusAdapter;
import lilun.com.pensionlife.module.bean.ActivityCategory;
import lilun.com.pensionlife.module.bean.ElderEdus;
import lilun.com.pensionlife.module.bean.ElderModule;
import lilun.com.pensionlife.module.callback.TitleBarClickCallBack;
import lilun.com.pensionlife.module.utils.ToastHelper;
import lilun.com.pensionlife.ui.activity.activity_list.ActivityListFragment;
import lilun.com.pensionlife.ui.announcement.AnnouncementFragment;
import lilun.com.pensionlife.widget.DividerDecoration;
import lilun.com.pensionlife.widget.ElderModuleClassifyDecoration;
import lilun.com.pensionlife.widget.PositionTitleBar;

/**
 * 老年教育V
 *
 * @author yk
 *         create at 2017/2/7 16:04
 *         email : yk_developer@163.com
 */
public class EducationClassifyFragment extends BaseFragment<EducationClassifyContract.Presenter>
        implements EducationClassifyContract.View {


    @Bind(R.id.title_bar)
    PositionTitleBar titleBar;

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.swipe_layout)
    SwipeRefreshLayout mSwipeLayout;


    private RecyclerView mClassifyRecycler;

    private List<ElderEdus> products = new ArrayList<>();
    private OrganizationEdusAdapter mAdapter;
    //    private ArrayList<Information> announcements;
    private String parentId;

//    public static EducationClassifyFragment newInstance(List<Information> announcements) {
//        EducationClassifyFragment fragment = new EducationClassifyFragment();
//        Bundle args = new Bundle();
//        args.putSerializable("announcements", (Serializable) announcements);
//        fragment.setArguments(args);
//        return fragment;
//    }


    public static EducationClassifyFragment newInstance(String parentId) {
        EducationClassifyFragment fragment = new EducationClassifyFragment();
        Bundle args = new Bundle();
        args.putString("parentId", parentId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void getTransferData(Bundle arguments) {
        parentId = arguments.getString("parentId");
//        announcements = (ArrayList<Information>) arguments.getSerializable("announcements");
    }


    @Override
    protected void initPresenter() {
        mPresenter = new EducationClassifyPresenter();
        mPresenter.bindView(this);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_education_classify;
    }


    @Override
    protected void initView(LayoutInflater inflater) {
        titleBar.setTitle(getString(R.string.pension_education));
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
        mRecyclerView.addItemDecoration(new DividerDecoration(App.context, LinearLayoutManager.VERTICAL, (int) App.context.getResources().getDimension(R.dimen.dp_1), Color.parseColor("#000000")));


        //刷新
        mSwipeLayout.setOnRefreshListener(() -> {
                    if (mPresenter != null) {
                        getServices(0);
                    }
                }
        );


        //设置数据
        setAdapter();
        //  getServices(0);
    }


    private void setAdapter() {
        mAdapter = new OrganizationEdusAdapter(this, products);
        mAdapter.addHeaderView(mClassifyRecycler);
        mAdapter.setOnItemClickListener((item) -> {
        });
        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    protected void initEvent() {
        refreshData();
    }

    public void refreshData() {
        mSwipeLayout.setRefreshing(true);
        getClassifies();
    }

    private void getClassifies() {
        mPresenter.getClassifies();
    }

    private void getServices(int skip) {
        String filter = "{\"\":\"\"}";
        //   mPresenter.getAboutMe(filter, skip);
        completeRefresh();
    }


    @Override
    public void showClassifies(List<ElderModule> elderModules) {
        completeRefresh();
        mClassifyRecycler.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        EducationClassifyAdapter adapter = new EducationClassifyAdapter(elderModules);
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            ToastHelper.get().showShort(getString(R.string.building));
//            ElderModule elderModule = (ElderModule) adapter1.getData().get(position);
//            if (elderModule.getName().equals("老年大学")) {
//                start(EducationListFragment.newInstance(elderModule));
//            } else {
//                ToastHelper.get().showShort(getString(R.string.building));
//            }
        });
        mClassifyRecycler.setAdapter(adapter);
        getServices(0);
    }


    @Override
    public void showAboutMe(List<ElderEdus> products, boolean isLoadMore) {
        completeRefresh();
        if (products != null) {
            if (isLoadMore) {
                mAdapter.addAll(products, Config.defLoadDatCount);
            } else {
                mAdapter.replaceAll(products);
            }
        }
    }

    @Override
    public void showOrgActivityCategory(List<ActivityCategory> activityCategories) {
        if (activityCategories != null && activityCategories.size() > 0)
            start(ActivityListFragment.newInstance(activityCategories.get(0)));
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
