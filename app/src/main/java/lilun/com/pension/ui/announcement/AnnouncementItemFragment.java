package lilun.com.pension.ui.announcement;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import lilun.com.pension.R;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.bean.Announcement;
import lilun.com.pension.module.utils.ToastHelper;

/**
 * Created by yk on 2017/1/5.
 * 广告栏item
 */
public class AnnouncementItemFragment extends BaseFragment {


    private ImageView ivAdvantage;
    private Announcement announcement;

    public static AnnouncementItemFragment newInstance(Announcement announcement) {
        AnnouncementItemFragment fragment = new AnnouncementItemFragment();
        Bundle args = new Bundle();
        args.putSerializable("announcement", announcement);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void getTransferData(Bundle arguments) {
        if (arguments.getSerializable("announcement") == null) {
            throw new NullPointerException();
        } else {
            announcement = (Announcement) arguments.getSerializable("announcement");
//            Logger.d(announcement.getImageUrl());
        }
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_advantage_item;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        ivAdvantage = (ImageView) mRootView.findViewById(R.id.iv_advantage);

    }

    @Override
    protected void initEvent() {
        ivAdvantage.setOnClickListener(view -> ToastHelper.get().showShort(announcement.getImageUrl()));
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        loadImage();
    }

    private void loadImage() {
        if (TextUtils.isEmpty(announcement.getImageUrl())) {
            //TODO 占位图
        } else {
            Glide.with(this).load(announcement.getImageUrl()).into(ivAdvantage);
        }
    }
}
