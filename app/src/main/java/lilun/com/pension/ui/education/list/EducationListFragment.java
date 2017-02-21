package lilun.com.pension.ui.education.list;

import android.os.Bundle;
import android.view.LayoutInflater;

import java.io.Serializable;
import java.util.List;

import butterknife.Bind;
import lilun.com.pension.R;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.bean.Announcement;
import lilun.com.pension.module.callback.TitleBarClickCallBack;
import lilun.com.pension.widget.PositionTitleBar;

/**
 * 老年教育V
 *
 * @author yk
 *         create at 2017/2/7 16:04
 *         email : yk_developer@163.com
 */
public class EducationListFragment extends BaseFragment {

    @Bind(R.id.title_bar)
    PositionTitleBar titleBar;


    public static EducationListFragment newInstance(List<Announcement> announcements) {
        EducationListFragment fragment = new EducationListFragment();
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
        return R.layout.fragment_pension_education;
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
//            public void onItemClick() {
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
