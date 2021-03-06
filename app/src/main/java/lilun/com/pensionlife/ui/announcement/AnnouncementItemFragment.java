package lilun.com.pensionlife.ui.announcement;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.ImageView;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.User;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.bean.Information;
import lilun.com.pensionlife.module.utils.ACache;
import lilun.com.pensionlife.module.utils.Preconditions;
import lilun.com.pensionlife.ui.WebActivity;
import lilun.com.pensionlife.widget.image_loader.ImageLoaderUtil;

/**
 * Created by yk on 2017/1/5.
 * 广告栏item
 */
public class AnnouncementItemFragment extends BaseFragment {


    private ImageView ivIcon;
    private Information information;
    private int contextType=0;

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
            contextType = information.getContextType();
            if(contextType==0){
                return;
            }
            String context = information.getContext();
            if (contextType == 2 && !TextUtils.isEmpty(context)) {
                ACache.get().put(information.getId() + "h5", context);
                Intent intent = new Intent(_mActivity, AnnounceDetailActivity.class);
                intent.putExtra("infoId", information.getId());
                getActivity().startActivity(intent);
            } else if (information.getContextType() == 3 && !TextUtils.isEmpty(context)) {

                Intent intent = new Intent(_mActivity, WebActivity.class);
                intent.putExtra("url", information.getContext() + "?sojumpparm=" + User.getUserId());
                intent.putExtra("title", information.getTitle());
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
//        String url = StringUtils.getFirstIcon(information.getImage());
        ImageLoaderUtil.instance().loadImage(information.getCover(), ivIcon);
    }
}
