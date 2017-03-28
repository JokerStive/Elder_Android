package lilun.com.pension.ui.help.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import lilun.com.pension.R;
import lilun.com.pension.app.App;
import lilun.com.pension.app.Event;
import lilun.com.pension.app.User;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.adapter.NormalFilterAdapter;
import lilun.com.pension.module.adapter.OrganizationAidAdapter;
import lilun.com.pension.module.bean.ConditionOption;
import lilun.com.pension.module.bean.OrganizationAid;
import lilun.com.pension.ui.help.help_detail.AskDetailFragment;
import lilun.com.pension.ui.help.help_detail.HelpDetailFragment;
import lilun.com.pension.widget.NormalItemDecoration;
import lilun.com.pension.widget.SearchTitleBar;
import lilun.com.pension.widget.filter_view.FilterView;

/**
 * 分类求助（问？帮？）V
 *
 * @author yk
 *         create at 2017/2/7 16:04
 *         email : yk_developer@163.com
 */
public class HelpFragment extends BaseFragment<HelpContract.Presenter> implements HelpContract.View {


    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.swipe_layout)
    SwipeRefreshLayout mSwipeLayout;

    @Bind(R.id.searchBar)
    SearchTitleBar searchBar;
    @Bind(R.id.filter_view)
    FilterView filterView;


    private String condition_title = "title";
    private String condition_kind = "kind";
    private String condition_priority = "priority";
    private String condition_near = "near";
    private boolean isMain;

    private Map<String, String> conditionMap;
    private String[] conditionKind;
    private String[] conditionPriority;
    private String[] hedaers = new String[]{"类别","优先级"};
    private SearchTitleBar.LayoutType layoutType = SearchTitleBar.LayoutType.BIG;


    private OrganizationAidAdapter mAidAdapter;
    private List<OrganizationAid> helps;

    public static HelpFragment newInstance(boolean isMain) {
        HelpFragment fragment = new HelpFragment();
        Bundle args = new Bundle();
        args.putBoolean("isMain", isMain);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void getTransferData(Bundle arguments) {
        super.getTransferData(arguments);
        isMain = arguments.getBoolean("isMain", false);
    }

    @Subscribe
    public void refreshData(Event.RefreshHelpData event) {
        getHelps(0);
    }


    @Override
    protected void initPresenter() {
        mPresenter = new HelpPresenter();
        mPresenter.bindView(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_help_list;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        initConditionModules();
        searchBar.setOnItemClickListener(new SearchTitleBar.OnItemClickListener() {
            @Override
            public void onBack() {
                pop();
            }

            @Override
            public void onChangeLayout(SearchTitleBar.LayoutType type) {
                layoutType = type;
                if (helps != null && helps.size() != 0) {
                    setRecyclerAdapter(helps);
                }
            }
        });


        mRecyclerView.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new NormalItemDecoration(6));
        //刷新
        mSwipeLayout.setOnRefreshListener(() -> {
                    if (mPresenter != null) {
                        getHelps(0);
                    }
                }
        );

        initConditionMap();

    }

    private void initConditionModules() {
        List<ConditionOption> kindOptions = new ArrayList<>();
        ConditionOption kindOptionNull = new ConditionOption("kind","","不限");
        ConditionOption kindOptionAsk = new ConditionOption("kind","0","邻居问");
        ConditionOption kindOptionHelp = new ConditionOption("kind","1","帮邻居");
        kindOptions.add(kindOptionNull);
        kindOptions.add(kindOptionAsk);
        kindOptions.add(kindOptionHelp);

        List<ConditionOption> priorityOptions = new ArrayList<>();
        ConditionOption priorityOptionNull = new ConditionOption("priority","","不限");
        ConditionOption priorityOption0 = new ConditionOption("priority","0","一般");
        ConditionOption priorityOption1 = new ConditionOption("priority","1","加急");
        ConditionOption priorityOption2 = new ConditionOption("priority","2","紧急");
        ConditionOption priorityOption10 = new ConditionOption("priority","10","危急");
        priorityOptions.add(priorityOptionNull);
        priorityOptions.add(priorityOption0);
        priorityOptions.add(priorityOption1);
        priorityOptions.add(priorityOption2);
        priorityOptions.add(priorityOption10);


        List<View>  popViews = new ArrayList<>();
        RecyclerView kindOptionView = new RecyclerView(App.context);
        kindOptionView.setLayoutManager(new LinearLayoutManager(App.context,LinearLayoutManager.VERTICAL,false));
        NormalFilterAdapter kindOptionAdapter = new NormalFilterAdapter(kindOptions);
        kindOptionAdapter.setOnItemClickListener((position, option) -> {
            filterView.setTabText(position==0?hedaers[0]:option.getConditionValue(),position==0);
            conditionMap.put(option.getKey(),option.getConditionKey());
            getHelps(0);
        });
        kindOptionView.setAdapter(kindOptionAdapter);

        RecyclerView priorityOptionView = new RecyclerView(App.context);
        priorityOptionView.setLayoutManager(new LinearLayoutManager(App.context,LinearLayoutManager.VERTICAL,false));
        NormalFilterAdapter priorityOptionAdapter = new NormalFilterAdapter(priorityOptions);
        priorityOptionAdapter.setOnItemClickListener((position, option) -> {
            filterView.setTabText(position==0?hedaers[1]:option.getConditionValue(),position==0);
            conditionMap.put(option.getKey(),option.getConditionKey());
            getHelps(0);
        });
        priorityOptionView.setAdapter(priorityOptionAdapter);

        popViews.add(kindOptionView);
        popViews.add(priorityOptionView);

        filterView.setTitlesAndPops(Arrays.asList(hedaers),popViews,mSwipeLayout);

//        ArrayList<ConditionModule> conditionModules = new ArrayList<>();
//
//        ArrayList<ConditionModule.ConditionBean> kindConditionBeans = new ArrayList<>();
//        ConditionModule kindConditionModule = new ConditionModule();
//        kindConditionModule.setTitle("所有分类");
//        kindConditionModule.setKey(condition_kind);
//        ConditionModule.ConditionBean kindConditionBean0 = new ConditionModule.ConditionBean("所有分类", "");
//        ConditionModule.ConditionBean kindConditionBean1 = new ConditionModule.ConditionBean("问邻居", "0");
//        ConditionModule.ConditionBean kindConditionBean2 = new ConditionModule.ConditionBean("帮邻居", "1");
//        kindConditionBeans.add(kindConditionBean0);
//        kindConditionBeans.add(kindConditionBean1);
//        kindConditionBeans.add(kindConditionBean2);
//        kindConditionModule.setConditions(kindConditionBeans);
//        conditionModules.add(kindConditionModule);
//
//        if (!isMain) {
//            ConditionModule module1 = new ConditionModule();
//            module1.setTitle("附近");
//            module1.setKey(condition_near);
//            conditionModules.add(module1);
//        }
//
//
//        ArrayList<ConditionModule.ConditionBean> conditionBeens2 = new ArrayList<>();
//        ConditionModule module2 = new ConditionModule();
//        module2.setTitle("默认优先级");
//        module2.setKey(condition_priority);
//        ConditionModule.ConditionBean bean0 = new ConditionModule.ConditionBean("默认优先级", "");
//        ConditionModule.ConditionBean bean1 = new ConditionModule.ConditionBean("一般", "0");
//        ConditionModule.ConditionBean bean2 = new ConditionModule.ConditionBean("加急", "1");
//        ConditionModule.ConditionBean bean3 = new ConditionModule.ConditionBean("紧急", "2");
//        ConditionModule.ConditionBean bean4 = new ConditionModule.ConditionBean("危急", "10");
//        conditionBeens2.add(bean0);
//        conditionBeens2.add(bean1);
//        conditionBeens2.add(bean2);
//        conditionBeens2.add(bean3);
//        conditionBeens2.add(bean4);
//
//        module2.setConditions(conditionBeens2);
//        conditionModules.add(module2);

//        searchBar.setConditionModules(conditionModules);

    }

    private void initConditionMap() {
        conditionKind = getResources().getStringArray(R.array.help_kind);
        conditionPriority = new String[]{"默认优先级", "一般", "加急", "紧急", "危急"};

        conditionMap = new HashMap<>();
    }


    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            mSwipeLayout.setRefreshing(true);
            getHelps(0);
        }
    }

    private void getHelps(int skip) {
        String filter = getFilterWithCondition();
        if (isMain) {
            mPresenter.getAboutMe(filter, skip);
        } else {
            mPresenter.getHelps(filter, skip);
        }
    }

    private String getFilterWithCondition() {
        String filter = "{\"where\":{";
//        String filter = "{\"where\":{\"kind\":\""+conditionMap.get(condition_kind)+"\",\"priority\":\""+conditionMap.get(condition_priority)+"\",\"title\":{\"like\":\"\"}}}";
        int index = 0;
        for (String key : conditionMap.keySet()) {
            String value = conditionMap.get(key);
            if (!TextUtils.isEmpty(value)) {
                if (index == 0) {
                    if (TextUtils.equals(key, condition_title)) {
                        filter = filter + "\"" + key + "\"" + ":" + "{\"like\":\"" + value + "\"}";
                    } else {
                        filter = filter + "\"" + key + "\"" + ":" + "\"" + value + "\"";
                    }
                } else {
                    if (TextUtils.equals(key, condition_title)) {
                        filter = filter + "," + "\"" + key + "\"" + ":" + "{\"like\":\"" + value + "\"}";
                    } else {


                        filter = filter + "," + "\"" + key + "\"" + ":" + "\"" + value + "\"";
                    }
                }
                index++;
            }
        }
        filter = filter + "}}";
        Logger.d("filter = " + filter);
        return filter;
    }


    @Override
    public void showAboutMe(List<OrganizationAid> helps, boolean isLoadMore) {
        this.helps = helps;
        completeRefresh();
        if (helps != null) {
            for (OrganizationAid aid : helps) {
                aid.setItemType(aid.getKind());
            }
            if (mAidAdapter == null) {
                setRecyclerAdapter(helps);
            } else if (isLoadMore) {
                mAidAdapter.addAll(helps);
            } else {
                mAidAdapter.replaceAll(helps);
            }
        }
    }

    private void setRecyclerAdapter(List<OrganizationAid> helps) {
        mAidAdapter = getAdapterFromLayoutType(helps);
        if (mAidAdapter != null) {
            mAidAdapter.setOnItemClickListener((aid) -> {
                start(aid.getKind() == 0 ? AskDetailFragment.newInstance(aid.getId(), User.creatorIsOwn(aid.getCreatorId())) : HelpDetailFragment.newInstance(aid.getId()));
            });
            mAidAdapter.setEmptyView();
        }
        mRecyclerView.setAdapter(mAidAdapter);
    }

    private OrganizationAidAdapter getAdapterFromLayoutType(List<OrganizationAid> helps) {
        OrganizationAidAdapter adapter;
        int layoutId;
        if (layoutType == SearchTitleBar.LayoutType.BIG) {
            layoutId = R.layout.item_aid_big;
        } else if (layoutType == SearchTitleBar.LayoutType.SMALL) {
            layoutId = R.layout.item_aid_small;
        } else {
            layoutId = R.layout.item_aid_null;
        }
        adapter = new OrganizationAidAdapter(helps, layoutId, layoutType);
        return adapter;
    }

    @Override
    public void completeRefresh() {
        if (mSwipeLayout != null && mSwipeLayout.isRefreshing()) {
            mSwipeLayout.setRefreshing(false);
        }
    }



}
