package lilun.com.pension.ui.push_info;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import butterknife.OnClick;
import lilun.com.pension.R;
import lilun.com.pension.app.Constants;
import lilun.com.pension.base.BaseFragment;

/**
*系统消息
*@author yk
*create at 2017/4/27 19:15
*email : yk_developer@163.com
*/

public class SystemInfoFragment extends BaseFragment {
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

    @OnClick({R.id.tv_urgent})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_urgent:
                Intent intent = new Intent(_mActivity, CacheInfoListActivity.class);
                intent.putExtra("title","紧急求助");
                intent.putExtra("model", Constants.organizationAid);
                startActivity(intent);
                break;
        }
    }
}
