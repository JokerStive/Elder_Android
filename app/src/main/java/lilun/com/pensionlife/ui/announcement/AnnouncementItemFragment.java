package lilun.com.pensionlife.ui.announcement;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.ImageView;

import java.util.List;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.IconUrl;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.bean.IconModule;
import lilun.com.pensionlife.module.bean.Information;
import lilun.com.pensionlife.module.utils.ACache;
import lilun.com.pensionlife.module.utils.Preconditions;
import lilun.com.pensionlife.widget.image_loader.ImageLoaderUtil;

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

        ivIcon.setOnClickListener(v -> {
            String context = information.getContext();
            if (information.getContextType() == 2 && !TextUtils.isEmpty(context)) {
                ACache.get().put(information.getId() + "h5", context);
                Intent intent = new Intent(_mActivity, AnnounceDetailActivity.class);
                intent.putExtra("infoId", information.getId());
                getActivity().startActivity(intent);
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
//            if(information.getParentId().contains("阳光政务")){
//                Logger.i(url);
//            }
            ImageLoaderUtil.instance().loadImage(url, R.drawable.icon_def, ivIcon);
        }
    }
}
