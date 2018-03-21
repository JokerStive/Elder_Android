package lilun.com.pensionlife.ui.activity.classify;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.Subscribe;
import org.litepal.crud.DataSupport;
import org.litepal.crud.async.CountExecutor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.Config;
import lilun.com.pensionlife.app.Event;
import lilun.com.pensionlife.app.User;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.adapter.ActivityCategoryAdapter;
import lilun.com.pensionlife.module.adapter.OrganizationActivityAdapter;
import lilun.com.pensionlife.module.bean.ActivityCategory;
import lilun.com.pensionlife.module.bean.OrganizationActivity;
import lilun.com.pensionlife.module.bean.ds_bean.OrganizationActivityDS;
import lilun.com.pensionlife.module.callback.TitleBarClickCallBack;
import lilun.com.pensionlife.module.utils.DBHelper;
import lilun.com.pensionlife.module.utils.Preconditions;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.module.utils.ToastHelper;
import lilun.com.pensionlife.ui.activity.activity_add.AddActivityFragment;
import lilun.com.pensionlife.ui.activity.activity_detail.ActivityChatFragment;
import lilun.com.pensionlife.ui.activity.activity_list.ActivityListFragment;
import lilun.com.pensionlife.ui.announcement.AnnouncementFragment;
import lilun.com.pensionlife.widget.ElderModuleClassifyDecoration;
import lilun.com.pensionlife.widget.NormalItemDecoration;
import lilun.com.pensionlife.widget.PositionTitleBar;
import lilun.com.pensionlife.widget.filter_view.FilterLayoutView;

/**
 * 社区活动宿主activity
 *
 * @author yk
 *         create at 2017/2/13 9:34
 *         email : yk_developer@163.com
 *         二级页面由宿主activity+fragment构成
 *         1、activity控制标题栏和公告栏（titleBar+AnnouncementFragment+fragmentContainer）
 *         2、初始化显示 ： 活动分类+我参加的活动（recyclerView+headView），MyActivityFragment替换fragmentContainer
 *         3、点击分类  ： 修改activity标题栏、XX分类的fragment替换fragmentContainer、传递的参数有活动分类名称（name）、icon地址
 *         4、回退 ：层级为宿主activity时宿主activity出栈，否则fragment出栈
 */
public class ActivityClassifyFragment extends BaseFragment<ActivityClassifyContract.Presenter> implements ActivityClassifyContract.View {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.title_bar)
    PositionTitleBar titleBar;

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.rg_classify)
    RecyclerView mClassifyRecycler;

    @Bind(R.id.swipe_layout)
    SwipeRefreshLayout mSwipeLayout;

    //    private ArrayList<Information> announcements;
    private ArrayList<ActivityCategory> activityCategories;

    private OrganizationActivityAdapter mContentAdapter;
    private List<OrganizationActivity> organizationActivities = new ArrayList<>();
    private String parentId;

    private int skip = 0;
    private ActivityCategoryAdapter categoryAdapter;

    public static ActivityClassifyFragment newInstance(String parentId) {
        ActivityClassifyFragment fragment = new ActivityClassifyFragment();
        Bundle args = new Bundle();
        args.putString("parentId", parentId);
        fragment.setArguments(args);
        return fragment;
    }


    //有新消息到来，需要更新
    @Subscribe
    public void activityNewMessage(Event.ActivityNew activityNew) {
        if (activityNew == null || activityNew.getActCatMsg() == null) return;
        OrganizationActivityDS data = activityNew.getActCatMsg().getData();
        if (data == null) return;
        CalcCatUnRead(data.getCategoryId());
    }

    @Subscribe
    public void activityCancel(Event.ActivityCancel activityCancel) {
        if (activityCancel == null || activityCancel.getActCatMsg() == null) return;
        OrganizationActivityDS data = activityCancel.getActCatMsg().getData();
        if (data == null) return;
        //删除对应数据，更新显示
        DataSupport.deleteAllAsync(OrganizationActivityDS.class, "actId = ?", data.getActId())
                .listen(rowsAffected -> {
                    if (rowsAffected != 0) {
                        CalcCatUnRead(data.getCategoryId());
                    }
                });

    }

    /**
     * 查询 categoryId类别 未读条数据
     *
     * @param categoryId 传入""查询所有类别
     */
    public void CalcCatUnRead(String categoryId) {
        if (categoryAdapter == null) return;
        List<ActivityCategory> list = categoryAdapter.getData();
        if (list == null) return;

        String[] categorySplit = categoryId.split("/");
        String categoryType = categorySplit[categorySplit.length - 1];

        for (int i = 0; i < list.size(); i++) {
            int index = i;
            ActivityCategory category = list.get(i);
            String id = category.getId();
            if (categoryType == "") {
                int uc = 0;
                unReadCategoryCount(list.get(i).getId()).listen(count -> {
                    list.get(index).setUnRead(count);
                    categoryAdapter.notifyItemChanged(index);
                });

            } else if (id.contains(categoryType)) {
                String tmpCate = categoryId.replace("/" + categoryType, "");
                String idCate = id.replace("/" + categoryType, "");
                if (idCate.contains(tmpCate)) {
                    unReadCategoryCount(list.get(i).getId()).listen(count -> {
                        list.get(index).setUnRead(count);
                        categoryAdapter.notifyItemChanged(index);
                    });
                    break;
                }
            }
        }

    }

    /**
     * 从数据库中获取到当前类别 向上至市 的所有 未读消息条数
     *
     * @param id
     * @return CountExecutor 数据库执行器
     */
    public CountExecutor unReadCategoryCount(String id) {
        int pos = id.indexOf("/#");
        String categotyType = id.substring(pos);
        String orgId = id.replace(categotyType, "");
        ArrayList<String> orgs = User.levelIds(orgId);

        String categoryIdStr = ""; //sql条件语句
        for (int i = 0; i < orgs.size(); i++) {
            orgs.set(i, orgs.get(i) + categotyType);
            if (i == 0) categoryIdStr = "categoryId = ?";
            else categoryIdStr += " or categoryId = ?";
        }
        orgs.add(0, categoryIdStr);
        String[] orgsArray = new String[orgs.size()];
        orgs.toArray(orgsArray);
        return DataSupport.where(orgsArray).countAsync(OrganizationActivityDS.class);
    }

    @Subscribe
    public void newChatMsg(Event.NewChatMsg chatMsg) {
        if (chatMsg == null || chatMsg.getPushMessage() == null
                || chatMsg.getPushMessage().getActivityId() == null)
            return;
        if (mContentAdapter == null || mContentAdapter.getData() == null) return;
        int count = mContentAdapter.getData().size();
        List<OrganizationActivity> list = mContentAdapter.getData();
        for (int i = 0; i < count; i++) {
            if (list.get(i).getId().equals(chatMsg.getPushMessage().getActivityId())) {
                mContentAdapter.notityUnRead(i);
                break;
            }
        }
    }

    @Subscribe
    public void refreshData(Event.RefreshActivityData event) {
        refreshData(false);
    }

    @Override
    protected void getTransferData(Bundle arguments) {
        parentId = arguments.getString("parentId");
        Preconditions.checkNull(parentId);
//        announcements = (ArrayList<Information>) arguments.getSerializable("announcements");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_activity_classify;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ActivityClassifyPresenter();
        mPresenter.bindView(this);
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        _mActivity.setSupportActionBar(toolbar);
        titleBar.setTitle(getString(R.string.community_activity));

        titleBar.setTvRightText(getString(R.string.add_activity));
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
                if (!StringUtils.isResisterTopCommunity(User.getCurrentOrganizationId(), User.getBelongsOrganizationId())) {
                    ToastHelper.get().showWareShort("您在当前区域不能创建活动!");
                    return;
                }
                if (activityCategories != null) {
                    start(AddActivityFragment.newInstance(activityCategories));

                }
            }
        });

        replaceLoadRootFragment(R.id.fl_announcement_container, AnnouncementFragment.newInstance(parentId), false);


        mClassifyRecycler.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mClassifyRecycler.addItemDecoration(new ElderModuleClassifyDecoration());

        //我参与的活动列表
        mRecyclerView.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new NormalItemDecoration(10));

        //刷新
        mSwipeLayout.setOnRefreshListener(() -> {
                    if (mPresenter != null) {
                        refreshData(activityCategories == null);
                    }
                }
        );


        //设置数据
        setAdapter();
    }

    private void setAdapter() {
        mContentAdapter = new OrganizationActivityAdapter(organizationActivities, R.layout.item_activity_small, FilterLayoutView.LayoutType.SMALL, true);
        mContentAdapter.setOnItemClickListener((baseQuickAdapter, view, activityItem) -> {
            start(ActivityChatFragment.newInstance(mContentAdapter.getItem(activityItem)));
            mContentAdapter.getData().get(activityItem).setUnRead(0);
            mContentAdapter.notifyItemChanged(activityItem);

        });
        mContentAdapter.setEmptyView();

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (mContentAdapter != null) {
                    if (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                        mContentAdapter.setmScrollIdle(true);
                        mContentAdapter.notifyDataChanged();
                    } else mContentAdapter.setmScrollIdle(false);
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        mRecyclerView.setAdapter(mContentAdapter);

        mContentAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
        mContentAdapter.setOnLoadMoreListener(() -> {
            Logger.d("加载更多");
            getAboutMe(skip);
        }, mRecyclerView);
    }


    @Override
    protected void initEvent() {
        super.initData();
        refreshData(true);
    }


    private void refreshData(boolean needRefreshClassify) {
        mSwipeLayout.setRefreshing(true);
        if (needRefreshClassify) {
            getClassifies();
        }
        skip = 0;
        if (mContentAdapter != null) {
//            mContentAdapter.openLoadMore(Config.defLoadDatCount, true);
            mContentAdapter.removeAllFooterView();
        }
        getAboutMe(skip);
    }

    private void getClassifies() {
        mPresenter.getClassifies(User.getCurrentOrganizationId());
    }

    private void getAboutMe(int skip) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String localtime = format.format(new Date());
        //未开始    现在时间<开始时间
        String activity_status = "\"endTime\":{\"gt\":\"" + localtime + "\"}";
        //TODO 获取关于我的活动 1.是活动类型  2.创建人是自己  3.参加的活动
        String filter = "{\"where\":{" +
                "\"or\":[{\"status\":0},{\"status\":1}]" +
                ",\"or\":[{\"masterId\":\"" + User.getUserId() + "\"}" +
                ",{\"partnerList\":\"" + User.getUserId() + "\"}]}" +
                ",\"order\":\"createdAt DESC\"}";
        mPresenter.getAboutMe(filter, skip);
    }

    @Override
    public void showClassifies(List<ActivityCategory> activityCategories) {
        completeRefresh();
        this.activityCategories = (ArrayList<ActivityCategory>) activityCategories;
        mClassifyRecycler.setLayoutManager(new GridLayoutManager(_mActivity, StringUtils.spanCountByData(activityCategories)));
        categoryAdapter = new ActivityCategoryAdapter(this, activityCategories);
        categoryAdapter.setOnItemClickListener((activityCategory -> {
            DBHelper.getDefault().deleterCategory(activityCategory.getId());
            CalcCatUnRead(activityCategory.getId());
            start(ActivityListFragment.newInstance(activityCategory));
        }));

        mClassifyRecycler.setAdapter(categoryAdapter);
        CalcCatUnRead("");
    }

    @Override
    public void showAboutMe(List<OrganizationActivity> activities, boolean isFirstLoad) {
        completeRefresh();
        skip += activities.size();

        if (isFirstLoad) {
            mContentAdapter.replaceAll(activities);
        } else {
            mContentAdapter.addAll(activities, Config.defLoadDatCount);
        }
        mContentAdapter.notityUnReadAll();
        if (activities.size() < Config.defLoadDatCount) {
//            mContentAdapter.setEnableLoadMore(false);
//            TextView nodata = new TextView(getContext());
//            nodata.setText("-没有更多数据-");
//            nodata.setGravity(Gravity.CENTER);
//            if (App.widthDP > 820)
//                nodata.setTextSize(getResources().getDimension(R.dimen.sp_14));
//            mContentAdapter.addFooterView(nodata);
        }

    }

    @Override
    public void completeRefresh() {
        if (mSwipeLayout != null && mSwipeLayout.isRefreshing()) {
            mSwipeLayout.setRefreshing(false);
        }
    }


}
