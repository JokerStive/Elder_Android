package lilun.com.pensionlife.ui.push_info;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.utils.CacheMsgClassify;

/**
 * 系统消息
 *
 * @author yk
 *         create at 2017/4/27 19:15
 *         email : yk_developer@163.com
 */

public class SystemInfoFragment extends BaseFragment {
    @Bind(R.id.tv_announce)
    TextView tvAnnounce;
    @Bind(R.id.tv_urgent)
    TextView tvUrgent;
    @Bind(R.id.tv_activity)
    TextView tvActivity;
    @Bind(R.id.tv_neighbor)
    TextView tvNeighbor;
    @Bind(R.id.tv_agency)
    TextView tvAgency;
    @Bind(R.id.tv_education)
    TextView tvEducation;
    @Bind(R.id.tv_policy)
    TextView tvPolicy;
    @Bind(R.id.tv_health)
    TextView tvHealth;
    @Bind(R.id.tv_family)
    TextView tvFamily;

    @Override
    protected void initPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_system_info_classify;
    }

    @Override
    protected void initView(LayoutInflater inflater) {

    }

    @OnClick({R.id.tv_urgent, R.id.tv_announce})
    public void onClick(View view) {
        CacheMsgClassify msgClassify = new CacheMsgClassify();
        switch (view.getId()) {
            case R.id.tv_urgent:
                startCacheInfoList(tvUrgent, msgClassify.urgent_help);
                break;

            case R.id.tv_announce:
                startCacheInfoList(tvAnnounce, msgClassify.announce);
                break;
        }
    }

    private void startCacheInfoList(TextView textView, int classify) {
        Intent intent = new Intent(_mActivity, CacheInfoListActivity.class);
        intent.putExtra("title", textView.getText().toString());
        intent.putExtra("classify", classify);
        startActivity(intent);
    }

}
