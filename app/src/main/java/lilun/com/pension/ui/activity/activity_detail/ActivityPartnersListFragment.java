package lilun.com.pension.ui.activity.activity_detail;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import lilun.com.pension.R;
import lilun.com.pension.app.Event;
import lilun.com.pension.app.User;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.adapter.PartnersAdapter;
import lilun.com.pension.module.bean.Account;
import lilun.com.pension.module.bean.OrganizationActivity;
import lilun.com.pension.module.utils.Preconditions;
import lilun.com.pension.widget.ButtonPopupWindow;
import lilun.com.pension.widget.NormalDialog;
import lilun.com.pension.widget.NormalItemDecoration;
import lilun.com.pension.widget.NormalTitleBar;

/**
 * Created by zp on 2017/4/20.
 */

public class ActivityPartnersListFragment extends BaseFragment<ActivityDetailContact.PPartner>
        implements ActivityDetailContact.VPartner {
    ButtonPopupWindow buttonPopupWindow;
    OrganizationActivity activity;
    PartnersAdapter partnersAdapter;
    int skip = 0;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.swipe_layout)
    SwipeRefreshLayout mSwipeLayout;

    @Bind(R.id.titleBar)
    NormalTitleBar titleBar;


    @Bind(R.id.null_data)
    ImageView nullData;
    @Bind(R.id.ll_question)
    LinearLayout llCancelActivity;

    @OnClick(R.id.acbt_cancel_activity)
    public void onClick() {
        new NormalDialog().createNormal(_mActivity, getString(R.string.confirm_cancel_activity), new NormalDialog.OnPositiveListener() {
            @Override
            public void onPositiveClick() {
                mPresenter.cancelActivity(activity.getId());
            }
        });

    }

    public static ActivityPartnersListFragment newInstance(OrganizationActivity activity) {
        ActivityPartnersListFragment fragment = new ActivityPartnersListFragment();
        Bundle args = new Bundle();
        args.putSerializable("activity", activity);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void getTransferData(Bundle arguments) {
        super.getTransferData(arguments);
        activity = (OrganizationActivity) arguments.getSerializable("activity");
        Preconditions.checkNull(activity);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ActivityPartnersListPresenter();
        mPresenter.bindView(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_partners_list;
    }

    @Override
    protected void initData() {
        skip = 0;
        mPresenter.queryPartners(activity.getId(), "", skip);
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        titleBar.setTitle("参与者列表");
        titleBar.setOnBackClickListener(new NormalTitleBar.OnBackClickListener() {
            @Override
            public void onBackClick() {
                pop();
            }
        });
        if (User.getUserId().equals(activity.getMasterId()))
            llCancelActivity.setVisibility(View.VISIBLE);
        else
            llCancelActivity.setVisibility(View.GONE);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new NormalItemDecoration(1));
        //刷新
        mSwipeLayout.setOnRefreshListener(() -> {
                    if (mPresenter != null) {
                        skip = 0;
                        mPresenter.queryPartners(activity.getId(), "", skip);
                    }
                }
        );
    }


    @Override
    public void completeRefresh() {
        if (mSwipeLayout != null && mSwipeLayout.isRefreshing()) {
            mSwipeLayout.setRefreshing(false);
        }
    }


    @Override
    public void showPartners(List<Account> accounts) {
        boolean isLoadMore = skip != 0;

        skip += accounts.size();
        if (skip == 0) {
            nullData.setVisibility(View.VISIBLE);
        } else
            nullData.setVisibility(View.GONE);
        if (partnersAdapter == null) {
            partnersAdapter = new PartnersAdapter(accounts);
            mRecyclerView.setAdapter(partnersAdapter);
            partnersAdapter.setOnRecyclerViewItemLongClickListener(new BaseQuickAdapter.OnRecyclerViewItemLongClickListener() {
                @Override
                public boolean onItemLongClick(View view, int i) {
                    if (!User.getUserId().equals(activity.getMasterId())) return false;
                    final String userId = partnersAdapter.getData().get(i).getId();
                    Log.d("zp", "长按" + i);
                    if (buttonPopupWindow == null) {
                        synchronized (this) {
                            buttonPopupWindow = new ButtonPopupWindow(getContext(), new String[]{"踢人"});
                        }
                    }
                    buttonPopupWindow.setOnChildListener(new ButtonPopupWindow.OnChildListener() {
                        @Override
                        public void childClick(TextView view, int position) {
                            Log.d("zp", "长按--" + position + "  " + view.getText().toString());
                            mPresenter.deletePartners(activity.getId(), userId);
                        }
                    });
                    buttonPopupWindow.showAsDropDown(view, 0, -view.getHeight() - view.getHeight(), Gravity.CENTER);
                    return false;
                }
            });

        } else if (isLoadMore) {
            partnersAdapter.addAll(accounts);
        } else {
            partnersAdapter.replaceAll(accounts);
        }
    }

    @Override
    public void successDeletePartners() {
        if (buttonPopupWindow != null) buttonPopupWindow.dismiss();
        skip = 0;
        mPresenter.queryPartners(activity.getId(), "", skip);
        /**
         * {@link ActivityDetailFragment}
         */
        EventBus.getDefault().post(new Event.RefreshActivityDetail());
    }

    @Override
    public void successCancelActivity() {
        EventBus.getDefault().post(new Event.RefreshActivityDetail());
        pop();
        pop();
    }


}
