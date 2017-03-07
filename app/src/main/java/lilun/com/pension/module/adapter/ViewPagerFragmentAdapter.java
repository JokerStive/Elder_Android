package lilun.com.pension.module.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import lilun.com.pension.base.BaseFragment;

/**
 * Created by yk on 2017/1/6.
 * 广告栏adapter
 */
public class ViewPagerFragmentAdapter extends FragmentStatePagerAdapter {

    private List<BaseFragment> fragments;

    public ViewPagerFragmentAdapter(FragmentManager fm, List<BaseFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
