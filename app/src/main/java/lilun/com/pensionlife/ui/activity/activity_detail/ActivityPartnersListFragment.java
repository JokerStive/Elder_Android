package lilun.com.pensionlife.ui.activity.activity_detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.Config;
import lilun.com.pensionlife.app.Event;
import lilun.com.pensionlife.app.IconUrl;
import lilun.com.pensionlife.app.User;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.adapter.PartnersAdapter;
import lilun.com.pensionlife.module.bean.Account;
import lilun.com.pensionlife.module.bean.OrganizationActivity;
import lilun.com.pensionlife.module.bean.PushMessage;
import lilun.com.pensionlife.module.utils.Preconditions;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.module.utils.mqtt.MQTTManager;
import lilun.com.pensionlife.module.utils.mqtt.MQTTTopicUtils;
import lilun.com.pensionlife.widget.BottonPopupWindow;
import lilun.com.pensionlife.widget.CircleImageView;
import lilun.com.pensionlife.widget.DividerDecoration;
import lilun.com.pensionlife.widget.NormalDialog;
import lilun.com.pensionlife.widget.NormalTitleBar;
import lilun.com.pensionlife.widget.image_loader.ImageLoaderUtil;

/**
 * Created by zp on 2017/4/20.
 * 2017/6/30  踢人时可选择是否加入黑名单选项
 * 2017/8/2  布局文件更新，下拉自动加载更多，软键盘弹出显示“搜索”并实现其功能；
 */

public class ActivityPartnersListFragment extends BaseFragment<ActivityDetailContact.PPartner>
        implements ActivityDetailContact.VPartner {
    BottonPopupWindow bottonPopupWindow;
    OrganizationActivity activity;
    PartnersAdapter partnersAdapter;
    String searchKey = "";


    int skip = 0;
    private View.OnKeyListener backListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK
                    && event.getAction() == KeyEvent.ACTION_DOWN) {
                // ToDo
                return true;
            }
            return false;
        }
    };

    private String topic;
    private int qos = 2;

    @Bind(R.id.et_search_name)
    EditText etSearchName;
    @Bind(R.id.tv_search_name)
    TextView tvSearchName;

    @Bind(R.id.iv_avatar)
    CircleImageView masterIcon;
    @Bind(R.id.tv_name)
    TextView masterName;

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.swipe_layout)
    SwipeRefreshLayout mSwipeLayout;

    @Bind(R.id.titleBar)
    NormalTitleBar titleBar;

    @Bind(R.id.tv_parents_number)
    TextView tvParentsNum;
    @Bind(R.id.null_data)
    ImageView nullData;

    /**
     * 获取过滤条件
     *
     * @return
     */
    public String getFilterIdName() {
        //只查询名字和id即可;
        //{"fields":{"id":true,"name":true},"where":{"name":{"like":""}},"limit":"10","skip":"0"}
        return "{\"fields\":{\"id\":true,\"name\":true},\"where\":{\"name\":{\"like\":\"" + searchKey + "\"}}}";
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
        topic = MQTTTopicUtils.getActivityTopic(activity.getOrganizationId(), activity.getId());
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
    public void editViewEnterButton() {
        searchKey = etSearchName.getText().toString().trim();
        skip = 0;
        mPresenter.queryPartners(activity.getId(), getFilterIdName(), skip);
        if (TextUtils.isEmpty(searchKey)) tvSearchName.setVisibility(View.GONE);
        hideSoftInput();
    }

    @Override
    protected void initData() {
        skip = 0;
        mPresenter.queryPartners(activity.getId(), getFilterIdName(), skip);
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        etSearchName.setOnKeyListener(editOnKeyListener);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerDecoration(getContext(), LinearLayoutManager.VERTICAL, 1, ContextCompat.getColor(getContext(), R.color.help)));


        partnersAdapter = new PartnersAdapter(new ArrayList<>());
        partnersAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
        partnersAdapter.setOnLoadMoreListener(() -> {
            mPresenter.queryPartners(activity.getId(), getFilterIdName(), skip);
        }, mRecyclerView);
        partnersAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int i) {
                if (mRecyclerView.isSelected()) {
                    partnersAdapter.dealSelectedStatus(i);
                    partnersAdapter.notifyItemChanged(i);
                    if (partnersAdapter.getSelectedList().size() > 0) {
                        titleBar.setRightWitchShow(NormalTitleBar.TEXT);
                    } else
                        titleBar.setRightWitchShow(NormalTitleBar.NONE);
                }
            }
        });
        mRecyclerView.setAdapter(partnersAdapter);
        bottonPopupWindow = new BottonPopupWindow(_mActivity);
        bottonPopupWindow.setOnDeleteListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2017/5/8 进入多选状态
                titleBar.setRightWitchShow(NormalTitleBar.NONE);
                if (partnersAdapter != null) {
                    partnersAdapter.setShowSelectedStatus(true);
                    partnersAdapter.notifyDataSetChanged();
                }
                mRecyclerView.setSelected(true);
                bottonPopupWindow.dismiss();
            }
        });
        bottonPopupWindow.setOnCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottonPopupWindow.dismiss();
            }
        });

        titleBar.setTitle("参与者列表");
        titleBar.setOnBackClickListener(new NormalTitleBar.OnBackClickListener() {
            @Override
            public void onBackClick() {
                if (dealOnBack()) return;
                pop();
            }
        });
        titleBar.setOnRightClickListener(new NormalTitleBar.OnRightClickListener() {
            @Override
            public void onRightClick() {
                if (titleBar.getRightWitchShow() == NormalTitleBar.ICON) {

                    bottonPopupWindow.isShowDelete(User.getUserId().equals(activity.getMasterId()));
                    bottonPopupWindow.showAtLocation(titleBar, Gravity.BOTTOM, 0, 0);

                } else if (titleBar.getRightWitchShow() == NormalTitleBar.TEXT) {
                    // TODO: 2017/5/8 处理删除的数据 ,响应成功后切换回ICON状态,并退出可选状态
                    new NormalDialog().createCheckDialog(_mActivity, "确定删除选择中员", true,
                            new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    mRecyclerView.setSelected(false);
                                    titleBar.setRightWitchShow(NormalTitleBar.ICON);
                                    if (partnersAdapter != null && partnersAdapter.getData().size() > 0 &&
                                            partnersAdapter.getSelectedList() != null && partnersAdapter.getSelectedList().size() > 0) {
                                        ArrayList<Integer> selectedList = partnersAdapter.getSelectedList();

                                        String userId = "[";
                                        String userName = "";
                                        for (int i = 0; i < selectedList.size(); i++) {
                                            userId += "\"" + partnersAdapter.getItem(selectedList.get(i)).getId() + "\",";
                                            userName += partnersAdapter.getItem(selectedList.get(i)).getName() + ",";
                                        }
                                        userId = userId.length() > 1 ? userId.substring(0, userId.length() - 1) + "]" : "]";
                                        userName = userName.length() > 1 ? userName.substring(0, userName.length() - 1) : "";

                                        if (dialog.isPromptCheckBoxChecked()) {
                                            Logger.d(userId);
                                            mPresenter.addBlockUser(activity.getId(), userId, userName);
                                        } else
                                            mPresenter.deletePartners(activity.getId(), userId, userName);
                                    }
                                }
                            });

                }
            }
        });

        masterName.setText(activity.getCreatorName());
        etSearchName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) tvSearchName.setVisibility(View.VISIBLE);
                else {
                    searchKey = "";
                }
            }
        });
        tvSearchName.setVisibility(View.GONE);
        tvSearchName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchKey = etSearchName.getText().toString().trim();
                skip = 0;
                mPresenter.queryPartners(activity.getId(), getFilterIdName(), skip);
                if (TextUtils.isEmpty(searchKey)) tvSearchName.setVisibility(View.GONE);
            }
        });
        ImageLoaderUtil.instance().loadImage(
                IconUrl.moduleIconUrl(IconUrl.Accounts, activity.getMasterId(), null),
                R.drawable.icon_def, masterIcon);

        //刷新
        mSwipeLayout.setOnRefreshListener(() -> {
                    if (mPresenter != null) {
                        skip = 0;
                        mPresenter.queryPartners(activity.getId(), getFilterIdName(), skip);
                    }
                }
        );

    }

    public boolean dealOnBack() {
        if (mRecyclerView.isSelected()) {
            mRecyclerView.setSelected(false);
            if (partnersAdapter.getSelectedList() != null)
                partnersAdapter.getSelectedList().clear();
            partnersAdapter.setShowSelectedStatus(false);
            partnersAdapter.notifyDataSetChanged();
            titleBar.setRightWitchShow(NormalTitleBar.ICON);
            return true;
        }
        return false;
    }


    @Override
    public void completeRefresh() {
        if (mSwipeLayout != null && mSwipeLayout.isRefreshing()) {
            mSwipeLayout.setRefreshing(false);
        }
    }


    @Override
    public void showPartners(List<Account> accounts) {
        boolean isFirstLoad = skip == 0;

        skip += accounts.size();

        if (isFirstLoad) {
            partnersAdapter.replaceAll(accounts);
        } else {
            partnersAdapter.addAll(accounts, Config.defLoadDatCount);
        }
        if (skip == 0) {
            nullData.setVisibility(View.VISIBLE);
        } else {
            nullData.setVisibility(View.GONE);
            String tmp = getString(R.string.partner_list) + "(" + (partnersAdapter.getData() == null ? 0 : partnersAdapter.getData().size()) + ")";
            tvParentsNum.setText(tmp);
        }
    }

    @Override
    public void successDeletePartners(String userId, String userName) {
        if (bottonPopupWindow != null) bottonPopupWindow.dismiss();
        skip = 0;
        partnersAdapter.setShowSelectedStatus(false);
        mPresenter.queryPartners(activity.getId(), getFilterIdName(), skip);
        if (partnersAdapter.getSelectedList() != null) partnersAdapter.getSelectedList().clear();
        /**
         * {@link ActivityDetailFragment}
         */
        EventBus.getDefault().post(new Event.RefreshActivityDetail());
        PushMessage pushMessage = new PushMessage();
        userId = userId.replace("[", "").replace("]", "");
        String[] userIds = userId.split(",");
        String[] userNames = userName.split(",");
        String to = "[";
        for (int i = 0; i < userIds.length; i++) {
            to += "{\"id\":" + userIds[i] + ",\"name\":\"" + userNames[i] + "\"},";
        }
        to = to.substring(0, to.length() - 1);
        to += "]";
        pushMessage.setVerb(PushMessage.VERB_KICK)
                .setTo(to)
                .setMessage(userName + "被主持人请出了本活动")
                .setTime(StringUtils.date2String(new Date()));
        MQTTManager.getInstance().publish(topic, qos, pushMessage.getJsonStr());
    }

}
