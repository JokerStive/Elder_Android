package lilun.com.pensionlife.ui.home;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.app.User;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.adapter.CollageAdapter;
import lilun.com.pensionlife.module.bean.Account;
import lilun.com.pensionlife.module.bean.Organization;
import lilun.com.pensionlife.module.bean.OrganizationAccount;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.module.utils.ToastHelper;
import lilun.com.pensionlife.net.NetHelper;
import lilun.com.pensionlife.net.RxSubscriber;
import lilun.com.pensionlife.ui.education.colleage_list.CollegeFilter;
import lilun.com.pensionlife.ui.education.course_list.CourseListFragment;
import lilun.com.pensionlife.ui.welcome.LoginModule;
import lilun.com.pensionlife.widget.ElderModuleItemDecoration;
import lilun.com.pensionlife.widget.NormalTitleBar;
import rx.Observable;

/**
 * 我加入的组织
 */
public class MyJoinedOrganizationsFragment extends BaseFragment {

    private NormalTitleBar titleBar;
    private RecyclerView rvJoinedOrganizations;
    public boolean isChangeBelongOrganization = false;
    private CollageAdapter mCollageAdapter;
    private LoginModule module;

    @Override
    protected void initPresenter() {
        module = new LoginModule();
        getBelongOrganizations();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_education;
    }


    @Override
    protected void initView(LayoutInflater inflater) {
        titleBar = (NormalTitleBar) mRootView.findViewById(R.id.titleBar);
        SwipeRefreshLayout swl = (SwipeRefreshLayout) mRootView.findViewById(R.id.swipe_layout);
        swl.setRefreshing(false);

        rvJoinedOrganizations = (RecyclerView) mRootView.findViewById(R.id.recycler_view);


        titleBar.setOnBackClickListener(new NormalTitleBar.OnBackClickListener() {
            @Override
            public void onBackClick() {
                back();
            }
        });


        rvJoinedOrganizations.setLayoutManager(new LinearLayoutManager(App.context));
        rvJoinedOrganizations.addItemDecoration(new ElderModuleItemDecoration());
        mCollageAdapter = new CollageAdapter(new ArrayList<>());
        mCollageAdapter.setEmptyView();
        mCollageAdapter.setEmptyView();
        mCollageAdapter.setOnItemClickListener((adapter, view, position) -> {
            Organization collage = mCollageAdapter.getItem(position);
            assert collage != null;
            String id = collage.getId();
            changeBelongOrganization(id, new CallBack() {
                @Override
                public void changeSuccess(String id) {
                    start(CourseListFragment.newInstance(collage.getId(), 30));
                }
            });
        });

        rvJoinedOrganizations.setAdapter(mCollageAdapter);
    }

    private void back() {
        if (isChangeBelongOrganization) {
            changeBelongOrganization(User.getBelongsOrganizationId(), new CallBack() {
                @Override
                public void changeSuccess(String id) {
                    _mActivity.finish();
                }
            });
        } else {
            _mActivity.finish();
        }
    }


    public void performBack() {
        back();
    }

    private void getBelongOrganizations() {
        organizationAccountObs()
                .flatMap(this::organizationsObs)
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<Organization>>() {
                    @Override
                    public void _next(List<Organization> organizations) {
                        showCollages(organizations);
                    }
                });
    }


    public void showCollages(List<Organization> collages) {
        mCollageAdapter.replaceAll(filterOrganization(collages));
    }


    Observable<List<Organization>> organizationsObs(List<OrganizationAccount> ass) {
        module.putBelongOrganizations(ass);
        ArrayList<String> ids = new ArrayList<>();
        for (OrganizationAccount as : ass) {
            String idSuffix = as.getOrganizationId();
            String id = StringUtils.removeSpecialSuffix(idSuffix);
            ids.add(id);
        }
        CollegeFilter collegeFilter = new CollegeFilter(ids);
        collegeFilter.getWhere().setAreaIds(null);
        Gson gson = new Gson();
        String filter = gson.toJson(collegeFilter);
        return NetHelper.getApi()
                .getOrganizationList(filter)
                .compose(RxUtils.handleResult());
    }

    private Observable<List<OrganizationAccount>> organizationAccountObs() {
        String filter = "{\"where\":{\"status\":1}}";
        return NetHelper.getApi().getUserOrganizationAccounts(User.getUserId(), filter)
                .compose(RxUtils.handleResult());
    }


    public interface CallBack {
        void changeSuccess(String id);
    }

    public void changeBelongOrganization(String organizationId, CallBack callBack) {
        String locationOrganizationAccountId = module.getOrganizationIdMappingOrganizationAccountId(organizationId);
        if (TextUtils.isEmpty(locationOrganizationAccountId)) {
            ToastHelper.get().showLong("location对应的organizationAccount没有找到");
            return;
        }
        Account account = new Account();
        account.setDefaultOrganizationId(locationOrganizationAccountId);
        NetHelper.getApi()
                .putAccount(User.getUserId(), account)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<Object>(_mActivity) {
                    @Override
                    public void _next(Object o) {
                        User.putCurrentOrganizationId(organizationId);
                        isChangeBelongOrganization = true;
                        callBack.changeSuccess(organizationId);
                    }
                });
    }


    private ArrayList<Organization> filterOrganization(List<Organization> organizations) {
        ArrayList<Organization> result = null;
        if (organizations != null) {
            result = new ArrayList<>();
            for (int i = 0; i < organizations.size(); i++) {
                Organization organization = organizations.get(i);
                String organizationId = organization.getId();
                if (organizationId.startsWith("/社会组织")) {
                    String[] split = organizationId.split("/");
                    if (split.length >= 3) {
                        result.add(organization);
                    }
                }

            }

        }
        return result;
    }

}
