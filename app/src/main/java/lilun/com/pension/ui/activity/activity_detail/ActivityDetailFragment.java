package lilun.com.pension.ui.activity.activity_detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import lilun.com.pension.R;
import lilun.com.pension.app.IconUrl;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.bean.IconModule;
import lilun.com.pension.module.bean.OrganizationActivity;
import lilun.com.pension.module.utils.Preconditions;
import lilun.com.pension.module.utils.StringUtils;
import lilun.com.pension.widget.slider.BannerPager;

/**
 * Created by zp on 2017/3/6.
 */

public class ActivityDetailFragment extends BaseFragment<ActivityDetailContact.Presenter>
        implements ActivityDetailContact.View {
    OrganizationActivity mActivity;

    @Bind(R.id.bp_actvity_icon)
    BannerPager bgActivityIcon;
    @Bind(R.id.tv_activity_name)
    TextView tvActivityName;
    @Bind(R.id.tv_activity_time)
    TextView tvActivityTime;
    @Bind(R.id.tv_participation_request)
    TextView tvParticipationRequest;
    @Bind(R.id.tv_activity_address)
    TextView tvActivityAddress;

    public static ActivityDetailFragment newInstance(OrganizationActivity activity) {
        ActivityDetailFragment fragment = new ActivityDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("activity", activity);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void getTransferData(Bundle arguments) {
        mActivity = (OrganizationActivity) arguments.getSerializable("activity");
        Preconditions.checkNull(mActivity);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ActivityDetailPresenter();
        mPresenter.bindView(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_activity_detail;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        mPresenter.getActivityDetail(mActivity.getId());
    }

    @Override
    public void showActivityDetail(OrganizationActivity activity) {
        //显示图片
        List<String> urls = new ArrayList<>();
        if (mActivity.getIcon() != null) {
            for (IconModule iconModule : mActivity.getIcon()) {
                String url = IconUrl.organizationActivies(mActivity.getId(), iconModule.getFileName());
                urls.add(url);
            }
        } else {
            String url = IconUrl.organizationActivies(mActivity.getId(), null);
            urls.add(url);
        }
        bgActivityIcon.setData(urls);
        tvActivityName.setText(mActivity.getTitle() + getRepeatedType(mActivity.getRepeatedType()));
        tvActivityTime.setText(getString(R.string.activity_time_, StringUtils.IOS2ToUTC(mActivity.getStartTime())));
        tvParticipationRequest.setText("无");
        tvActivityAddress.setText(mActivity.getAddress());
    }

    @Override
    public void completeRefresh() {

    }

    String getRepeatedType(String type) {
        String ret = "";
        if ("daily".equals(type)) {
            ret = "(每天)";
        } else if ("weekly".equals(type)) {
            ret = "(每周)";
        } else if ("monthly".equals(type)) {
            ret = "(每月)";
        } else if ("yearly".equals(type)) {
            ret = "(每年)";
        }
        return ret;
    }
}
