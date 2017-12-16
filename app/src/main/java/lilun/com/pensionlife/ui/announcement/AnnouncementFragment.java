package lilun.com.pensionlife.ui.announcement;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;

import com.orhanobut.logger.Logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.adapter.ViewPagerFragmentAdapter;
import lilun.com.pensionlife.module.bean.Information;
import lilun.com.pensionlife.module.utils.Preconditions;
import me.relex.circleindicator.CircleIndicator;

/**
 * 公告栏
 *
 * @author yk
 *         create at 2017/2/13 16:40
 *         email : yk_developer@163.com
 */
public class AnnouncementFragment extends BaseFragment<AnnouncementContract.Presenter> implements AnnouncementContract.View {

    public ViewPager viewPager;
    private CircleIndicator indicator;


    private int currentPosition = 0;
    //    private List<Information> informationList;
    private String parentId;
    private List<Information> announces;

    public static AnnouncementFragment newInstance(List<Information> information) {
        AnnouncementFragment fragment = new AnnouncementFragment();
        Bundle args = new Bundle();
        args.putSerializable("information", (Serializable) information);
        fragment.setArguments(args);
        return fragment;
    }


    public static AnnouncementFragment newInstance(String parentId) {
        AnnouncementFragment fragment = new AnnouncementFragment();
        Bundle args = new Bundle();
        args.putString("parentId", parentId);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void getTransferData(Bundle arguments) {
        super.getTransferData(arguments);
        String parentId = arguments.getString("parentId");
        if (!TextUtils.isEmpty(parentId)) {
            this.parentId = parentId;
        }
        Logger.d("公告的parentId---" + parentId);
        Preconditions.checkNull(this.parentId);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new AnnouncementPresenter();
        mPresenter.bindView(this);

    }


    @Override
    protected void initData() {
//        informationList = (List<Information>) getArguments().getSerializable("information");
//        if (informationList == null) {
//            throw new NullPointerException();
//        }

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_advantage;
    }

    @Override
    protected void initView(LayoutInflater inflater) {

        viewPager = (ViewPager) mRootView.findViewById(R.id.vp_container);
        indicator = (CircleIndicator) mRootView.findViewById(R.id.indicator);

//        initIndicator();
//        initViewPager();
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        mPresenter.getAnnounce(parentId);

    }

//    private void initIndicator() {
////        CommonNavigator
//    }

//    private void initViewPager() {
//
//
//    }


    @Override
    public void setVpCurrentPosition() {
        if (currentPosition++ == announces.size()) {
            viewPager.setCurrentItem(0, false);
        } else {
            viewPager.setCurrentItem(currentPosition, true);
        }
    }

    @Override
    public void showAnnounce(List<Information> announces) {

        this.announces = announces;
        if (announces!=null && announces.size()>0){
            mPresenter.initTimer();
        }
        Logger.d(parentId + "的公告条数为" + announces.size());

        List<BaseFragment> listFragments = new ArrayList<>();
        for (Information announcement : announces) {
            AnnouncementItemFragment fragment = AnnouncementItemFragment.newInstance(announcement);
            listFragments.add(fragment);
        }
        viewPager.setAdapter(new ViewPagerFragmentAdapter(getActivity().getSupportFragmentManager(), listFragments));
        if (listFragments.size() > 1) {
            indicator.setViewPager(viewPager);
        }
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
            }
        });


//        mPresenter.initTimer();
    }


    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
//        mPresenter.unBindView();
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
//        mPresenter.bindView(this);

    }
}
