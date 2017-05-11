package lilun.com.pension.ui.announcement;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;

import lilun.com.pension.R;
import lilun.com.pension.app.IconUrl;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.bean.IconModule;
import lilun.com.pension.module.bean.Information;
import lilun.com.pension.module.utils.Preconditions;
import lilun.com.pension.module.utils.ScreenUtils;
import lilun.com.pension.module.utils.UIUtils;
import lilun.com.pension.widget.image_loader.ImageLoaderUtil;

/**
 * Created by yk on 2017/1/5.
 * 广告栏item
 */
public class AnnouncementItemFragment extends BaseFragment {


    private ImageView ivIcon;
    private Information information;
    private String mFileName;

    public static AnnouncementItemFragment newInstance(Information information) {
        AnnouncementItemFragment fragment = new AnnouncementItemFragment();
        Bundle args = new Bundle();
        args.putSerializable("information", information);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void getTransferData(Bundle arguments) {
        information = (Information) arguments.getSerializable("information");
        Preconditions.checkNull(information);
        List<IconModule> picture = information.getImage();
        if (picture != null && picture.size() > 0) {
            mFileName = picture.get(0).getFileName();
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
        ivIcon = (ImageView) mRootView.findViewById(R.id.iv_advantage);

        int screenWith = ScreenUtils.getScreenWith(mContent);
        ivIcon.setLayoutParams(new LinearLayout.LayoutParams(screenWith, UIUtils.dp2px(mContent, 150)));

    }

    @Override
    protected void initEvent() {
        ivIcon.setOnClickListener(v -> {
            Fragment parentFragment = getParentFragment();
            if (parentFragment instanceof AnnouncementFragment) {
                ((AnnouncementFragment) parentFragment).startAnnounceDetail(information);
            }

        });
        loadImage();
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);

    }

    private void loadImage() {
        if (TextUtils.isEmpty(mFileName)) {
            //TODO 占位图
        } else {
            String url = IconUrl.moduleIconUrl(IconUrl.OrganizationInformations, this.information.getId(), mFileName);
            ImageLoaderUtil.instance().loadImage(url, R.drawable.icon_def, ivIcon);
        }
    }
}
