package lilun.com.pension.ui.help.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;

import com.afollestad.materialdialogs.MaterialDialog;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import lilun.com.pension.R;
import lilun.com.pension.app.Event;
import lilun.com.pension.app.User;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.adapter.OrganizationAidAdapter;
import lilun.com.pension.module.bean.OrganizationAid;
import lilun.com.pension.module.utils.ToastHelper;
import lilun.com.pension.ui.help.help_detail.AskDetailFragment;
import lilun.com.pension.ui.help.help_detail.HelpDetailFragment;
import lilun.com.pension.widget.NormalItemDecoration;
import lilun.com.pension.widget.SearchTitleBar;

/**
 * 分类求助（问？帮？）V
 *
 * @author yk
 *         create at 2017/2/7 16:04
 *         email : yk_developer@163.com
 */
public class HelpFragment extends BaseFragment<HelpContract.Presenter> implements HelpContract.View, View.OnClickListener {


    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.swipe_layout)
    SwipeRefreshLayout mSwipeLayout;

    @Bind(R.id.searchBar)
    SearchTitleBar searchBar;

    @Bind(R.id.rb_kind)
    RadioButton rbKind;

    @Bind(R.id.rb_near)
    RadioButton rbNear;

    @Bind(R.id.rb_property)
    RadioButton rbProperty;

    private Map<String, String> conditionMap;
    private String[] conditionKind;
    private String[] conditionPriority;
    private SearchTitleBar.LayoutType layoutType = SearchTitleBar.LayoutType.BIG;


    private OrganizationAidAdapter mAidAdapter;
    private List<OrganizationAid> helps;

    public static HelpFragment newInstance() {
        return new HelpFragment();
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
        searchBar.setOnItemClickListener(new SearchTitleBar.OnItemClickListener() {
            @Override
            public void onBack() {
                pop();
            }

            @Override
            public void onSearch(String searchStr) {
                getHelps(0);
            }

            @Override
            public void onChangeLayout(SearchTitleBar.LayoutType type) {
                layoutType = type;
                if (helps!=null && helps.size()!=0){
                    setRecyclerAdapter(helps);
                }
            }
        });

        rbKind.setOnClickListener(this);
        rbNear.setOnClickListener(this);
        rbProperty.setOnClickListener(this);

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
        mPresenter.getHelps(filter, skip);
    }

    private String getFilterWithCondition() {
        String filter = "{\"where\":{";
        int index = 0;
        for (String key : conditionMap.keySet()) {
            String value = conditionMap.get(key);
            if (!TextUtils.isEmpty(value)) {
                if (index == 0) {
                    filter = filter + "\"" + key + "\"" + ":" + "\"" + value + "\"";
                } else {
                    filter = filter + "," + "\"" + key + "\"" + ":" + "\"" + value + "\"";
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
//        mRecyclerView.setAdapter(null);
        mAidAdapter = getAdapterFromLayoutType(helps);
        if (mAidAdapter!=null){
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
        if (layoutType==SearchTitleBar.LayoutType.BIG){
            layoutId = R.layout.item_aid_big;
        }else if(layoutType==SearchTitleBar.LayoutType.SMALL){
            layoutId = R.layout.item_aid_small;
        }else {
            layoutId = R.layout.item_aid_null;
        }
        adapter = new OrganizationAidAdapter(helps,layoutId,layoutType);
        return adapter;
    }

    @Override
    public void completeRefresh() {
        if (mSwipeLayout != null && mSwipeLayout.isRefreshing()) {
            mSwipeLayout.setRefreshing(false);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rb_kind:
                //TODO 弹出类型框
                new MaterialDialog.Builder(_mActivity)
                        .items(conditionKind)
                        .itemsCallbackSingleChoice(0, (dialog, view, which, text) -> {
                            rbKind.setText("");
                            rbKind.setText(text);
                            conditionMap.put("kind", getKindByString(text.toString()));
                            getHelps(0);
                            return true;
                        })
                        .positiveText(R.string.choose)
                        .show();
                break;
            case R.id.rb_near:
                //TODO 附近
                ToastHelper.get().showWareShort("附近");
                break;
            case R.id.rb_property:
                //TODO 弹出优先级框
                new MaterialDialog.Builder(_mActivity)
                        .items(conditionPriority)
                        .itemsCallbackSingleChoice(0, (dialog, view, which, text) -> {
                            rbProperty.setText(text);
                            conditionMap.put("priority", getPriorityByString(text.toString()));
                            getHelps(0);
                            return true;
                        })
                        .positiveText(R.string.choose)
                        .show();
                break;
        }
    }

    private String getPriorityByString(CharSequence text) {
        String priority = "";
        if (TextUtils.equals(text, conditionPriority[1])) {
            priority = "0";
        } else if (TextUtils.equals(text, conditionPriority[2])) {
            priority = "1";
        } else if (TextUtils.equals(text, conditionPriority[3])) {
            priority = "2";
        } else if (TextUtils.equals(text, conditionPriority[4])) {
            priority = "3";
        }
        return priority;
    }


    private String getKindByString(CharSequence text) {
        String kind = "";
        if (TextUtils.equals(text, conditionKind[1])) {
            kind = "0";
        } else if (TextUtils.equals(text, conditionKind[2])) {
            kind = "1";
        }

        return kind;
    }


}
