package lilun.com.pensionlife.ui.push_info;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import org.greenrobot.eventbus.Subscribe;
import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.Bind;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.User;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.adapter.OrganizationInvitationAdapter;
import lilun.com.pensionlife.module.bean.Invitation;

/**
 * 个人
 *
 * @author yk
 *         create at 2017/4/27 19:15
 *         email : yk_developer@163.com
 */

public class PersonalInfoFragment extends BaseFragment {
    @Bind(R.id.rv)
    RecyclerView rv;
    private List<Invitation> invitations;
    private OrganizationInvitationAdapter adapter;

    @Override
    protected void initPresenter() {
        invitations = DataSupport.where("userId = ?", User.getUserId()).order("id desc").find(Invitation.class);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_personal_info;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        rv.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false));

        adapter = new OrganizationInvitationAdapter(invitations);
        adapter.setEmptyView();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                BaseFragment parentFragment = (BaseFragment) getParentFragment();
                parentFragment.start(InvitationDetailFragment.newInstance((Invitation) adapter.getItem(position)));
            }
        });
        rv.setAdapter(adapter);
    }

    @Subscribe
    public void refreshData(int num) {
        if (num == 10086) {
            invitations = DataSupport.where("userId = ?", User.getUserId()).order("id desc").find(Invitation.class);
            adapter.notifyDataChanged();
        }
    }

}
