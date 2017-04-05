package lilun.com.pension.ui.agency.reservation;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import lilun.com.pension.R;
import lilun.com.pension.app.App;
import lilun.com.pension.app.User;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.adapter.ServiceUserInfoAdapter;
import lilun.com.pension.module.bean.ServiceUserInformation;
import lilun.com.pension.module.utils.RxUtils;
import lilun.com.pension.net.NetHelper;
import lilun.com.pension.net.RxSubscriber;
import lilun.com.pension.widget.NormalItemDecoration;
import lilun.com.pension.widget.NormalTitleBar;

/**
 * 自己预约信息列表V
 *
 * @author yk
 *         create at 2017/3/29 18:47
 *         email : yk_developer@163.com
 */
public class ServiceUserInfoFragment extends BaseFragment {

    @Bind(R.id.rv_info)
    RecyclerView rvInfo;

    @Bind(R.id.titleBar)
    NormalTitleBar titleBar;

    @OnClick({R.id.btn_add_info})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_info:
                start(AddServiceInfoFragment.newInstance());
                break;
        }
    }

    public static ServiceUserInfoFragment newInstance() {
        ServiceUserInfoFragment fragment = new ServiceUserInfoFragment();
        return fragment;
    }

    @Override
    protected void initPresenter() {
        //TODO 获取个人健康信息列表
        String filter = "{\"where\":{\"creatorId\":\"" + User.getUserId() + "\"}}";
        NetHelper.getApi().getServiceUserInfos(filter)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<ServiceUserInformation>>() {
                    @Override
                    public void _next(List<ServiceUserInformation> userInfos) {
                        showUserInfo(userInfos);
                    }
                });
    }

    private void showUserInfo(List<ServiceUserInformation> userInfos) {
        ServiceUserInfoAdapter adapter = new ServiceUserInfoAdapter(userInfos);
        adapter.setOnRecyclerViewItemClickListener((view, i) -> Logger.d("预约" + i));
        adapter.setOnItemClickListener(new ServiceUserInfoAdapter.OnItemClickListener() {
            @Override
            public void onDelete() {
                Logger.d("删除个人信息");
            }

            @Override
            public void onEdit() {
                Logger.d("编辑个人信息");
            }
        });
        rvInfo.setAdapter(adapter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_personal_info_list;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        titleBar.setOnBackClickListener(this::pop);

        rvInfo.setLayoutManager(new LinearLayoutManager(App.context, LinearLayoutManager.VERTICAL, false));
        rvInfo.addItemDecoration(new NormalItemDecoration(10));
    }


}
