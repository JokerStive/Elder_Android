package lilun.com.pension.ui.announcement;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lilun.com.pension.R;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.adapter.AdItemFragmentAdapter;
import lilun.com.pension.module.adapter.PushInfoAdapter;
import lilun.com.pension.module.bean.Announcement;
import lilun.com.pension.module.callback.MyCallBack;
import lilun.com.pension.widget.CardConfig;
import lilun.com.pension.widget.OverLayCardLayoutManager;
import me.relex.circleindicator.CircleIndicator;

/**
*公告栏
*@author yk
*create at 2017/2/13 16:40
*email : yk_developer@163.com
*/
public class AnnouncementFragment extends BaseFragment<AnnouncementContract.Presenter> implements AnnouncementContract.View {

    public ViewPager viewPager;
    private CircleIndicator indicator;

//    private HomeActivity mActivity;

    private int currentPosition = 0;
    private List<Announcement> announcements;
    private RecyclerView rvPushInfo;
    private PushInfoAdapter pushInfoAdapter;
    private List<String> data;

    public static AnnouncementFragment newInstance(List<Announcement> announcements) {
        AnnouncementFragment fragment = new AnnouncementFragment();
        Bundle args = new Bundle();
        args.putSerializable("announcements", (Serializable) announcements);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new AnnouncementPresenter();
        mPresenter.bindView(this);
    }

    @Override
    protected void initData() {
        announcements = (List<Announcement>) getArguments().getSerializable("announcements");
        if (announcements == null) {
            throw new NullPointerException();
        }

        //========================test
//        data = new ArrayList<>();
//        for (int i = 0; i <5; i++) {
//            data.add(i + "");
//        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_advantage;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        initPushBar();

        viewPager = (ViewPager) mRootView.findViewById(R.id.vp_container);
        indicator = (CircleIndicator) mRootView.findViewById(R.id.indicator);

        initIndicator();
        initViewPager();
    }

    private void initPushBar() {
        if (data==null || data.size()==0){
            return;
        }

        rvPushInfo = (RecyclerView) mRootView.findViewById(R.id.rv_push_info);
        pushInfoAdapter = new PushInfoAdapter(rvPushInfo,data,R.layout.item_push_info);


        rvPushInfo.setLayoutManager(new OverLayCardLayoutManager());
//        rvPushInfo.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL,false));
        rvPushInfo.setAdapter(pushInfoAdapter);
        CardConfig.initConfig(_mActivity);
        CardConfig.MAX_SHOW_COUNT = 5;


        ItemTouchHelper.Callback callback = new MyCallBack(rvPushInfo, pushInfoAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(rvPushInfo);


        pushInfoAdapter.setOnPushClickListener(new PushInfoAdapter.onPushClickListener() {
            @Override
            public void onDeleteClick(String item) {
//                pushInfoAdapter.re

            }

            @Override
            public void onItemClick() {

            }

            @Override
            public void onExpandClick() {

            }
        });
    }


    private void initIndicator() {
//        CommonNavigator
    }

    private void initViewPager() {
        List<BaseFragment> listFragments = new ArrayList<>();
        for (Announcement announcement : announcements) {
            AnnouncementItemFragment fragment = AnnouncementItemFragment.newInstance(announcement);
            listFragments.add(fragment);
        }
        viewPager.setAdapter(new AdItemFragmentAdapter(_mActivity.getSupportFragmentManager(), listFragments));
        indicator.setViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
            }
        });

    }


    @Override
    protected void initEvent() {
//        mPresenter.init.Timer();
    }


    @Override
    public void setVpCurrentPosition() {
        if (currentPosition++ == announcements.size()) {
            viewPager.setCurrentItem(0, false);
        } else {
            viewPager.setCurrentItem(currentPosition, true);
        }
    }


}
