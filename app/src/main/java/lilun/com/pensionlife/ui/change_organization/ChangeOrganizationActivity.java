package lilun.com.pensionlife.ui.change_organization;

import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.Constants;
import lilun.com.pensionlife.app.User;
import lilun.com.pensionlife.base.BaseActivity;
import lilun.com.pensionlife.module.bean.Organization;

/**
 * 切换当前位置
 *
 * @author yk
 *         create at 2017/4/5 13:13
 *         email : yk_developer@163.com
 */

public class ChangeOrganizationActivity extends BaseActivity<ChangeOrganizationContract.Presenter> implements ChangeOrganizationContract.View {


    @Bind(R.id.tv_near)
    TextView tvNear;

    @Bind(R.id.tv_root)
    TextView tvRoot;

    @Bind(R.id.fl_near)
    FrameLayout flNear;

    @Bind(R.id.fl_root)
    FrameLayout flRoot;

//    @Bind(R.id.fragment_root)
//    RootOrganizationFragment fragmentRoot;
//    @Bind(R.id.fragment_near)
//    NearOrganizationFragment fragmentNear;

    private List<TextView> textViews;
    private List<FrameLayout> frameLayouts;

    @Override
    protected void initPresenter() {
        mPresenter = new ChangeOrganizationPresenter();
        mPresenter.bindView(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_change_organization;
    }

    @Override
    protected void initView() {
        tvNear.setSelected(true);

        textViews = new ArrayList<>();
        textViews.add(tvNear);
        textViews.add(tvRoot);

        frameLayouts = new ArrayList<>();
        frameLayouts.add(flNear);
        frameLayouts.add(flRoot);

        boolean equals = User.getBelongsOrganizationId().equals(Constants.organization_root);
        if (!equals && !User.currentOrganizationHasChanged()) {
            mPresenter.changeDefBelongOrganization(User.getRootOrganizationAccountId());
        } else {
            changedRoot();
        }
    }


    @OnClick({R.id.iv_back, R.id.tv_near, R.id.tv_root})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                if (!User.currentOrganizationHasChanged()) {
                    changeToBelong();
                } else {
                    finish();
                }
                break;

            case R.id.tv_near:
                showWhichView(tvNear);
                break;


            case R.id.tv_root:
                showWhichView(tvRoot);
                break;

        }
    }

    private void showWhichView(TextView v) {
        if (!v.isSelected()) {
            for (TextView textView : textViews) {
                boolean selected = textView.isSelected();
                textView.setSelected(!selected);
            }

            for (FrameLayout frameLayout : frameLayouts) {
                int visibility = frameLayout.getVisibility();
                frameLayout.setVisibility(visibility == View.VISIBLE ? View.INVISIBLE : View.VISIBLE);
            }
        }
    }


    /**
     * 切回自己原来属于的社区
     */
    private void changeToBelong() {
        boolean equals = User.getBelongsOrganizationId().equals(Constants.organization_root);
        if (!equals) {
            mPresenter.changeDefBelongOrganization(User.getBelongOrganizationAccountId());
        } else {
            changedBelong();
        }
    }


    @Override
    public void showOrganizations(List<Organization> organizations, boolean isLoadMore, boolean i) {

    }

    @Override
    public void changedRoot() {
        RootOrganizationFragment fragment = findFragment(RootOrganizationFragment.class);
        fragment.changedRoot();
//        fragmentRoot.changedRoot();
    }


    @Override
    public void changedBelong() {
        RootOrganizationFragment fragment = findFragment(RootOrganizationFragment.class);
        fragment.changedBelong();
//        finish();
    }

    @Override
    public void completeRefresh() {

    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && !User.currentOrganizationHasChanged()) {
            changeToBelong();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }




}
