package lilun.com.pension.ui.health.list;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import lilun.com.pension.R;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.adapter.PushInfoAdapter;
import lilun.com.pension.module.bean.Announcement;
import lilun.com.pension.module.callback.TitleBarClickCallBack;
import lilun.com.pension.widget.PositionTitleBar;

/**
 * 健康服务V
 *
 * @author yk
 *         create at 2017/2/7 16:04
 *         email : yk_developer@163.com
 */
public class HealthListFragment extends BaseFragment {

    @Bind(R.id.title_bar)
    PositionTitleBar titleBar;

    private RecyclerView rvPushInfo;
    private PushInfoAdapter pushInfoAdapter;
    private ArrayList<String> data;

    public static HealthListFragment newInstance(List<Announcement> announcements) {
        HealthListFragment fragment = new HealthListFragment();
        Bundle args = new Bundle();
        args.putSerializable("announcements", (Serializable) announcements);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initPresenter() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_health_service;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        titleBar.setTitleBarClickListener(new TitleBarClickCallBack() {
            @Override
            public void onBackClick() {
                pop();
            }
        });
    }


    private void initPushBar() {
//        rvPushInfo = (RecyclerView) mRootView.findViewById(R.id.rv_push_info);
//        pushInfoAdapter = new PushInfoAdapter(_mActivity, data, R.layout.item_push_info);
//        pushInfoAdapter.setOnPushClickListener(new PushInfoAdapter.onPushClickListener() {
//            @Override
//            public void onDeleteClick(PushInfoAdapter.MyViewHolder item) {
//                pushInfoAdapter.remove(item);
//            }
//
//            @Override
//            public void agree() {
//            }
//
//            @Override
//            public void onExpandClick() {
//                rvPushInfo.setLayoutManager(new LinearLayoutManager(App.context, LinearLayoutManager.VERTICAL, false));
//            }
//        });
//
//        rvPushInfo.setLayoutManager(new OverLayCardLayoutManager());
//        rvPushInfo.setAdapter(pushInfoAdapter);
//        CardConfig.initConfig(_mActivity);
//        CardConfig.MAX_SHOW_COUNT = 3;
//        ItemTouchHelper.Callback callback = new MyCallBack(rvPushInfo, pushInfoAdapter, data);
//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
//        itemTouchHelper.attachToRecyclerView(rvPushInfo);
    }


}
