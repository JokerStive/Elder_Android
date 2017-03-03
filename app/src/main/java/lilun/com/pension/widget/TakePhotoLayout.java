package lilun.com.pension.widget;

import android.app.FragmentManager;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import lilun.com.pension.R;
import lilun.com.pension.app.App;
import lilun.com.pension.app.Config;
import lilun.com.pension.module.adapter.TakePhotoAdapter;
import lilun.com.pension.module.bean.TakePhotoResult;
import lilun.com.pension.module.callback.TakePhotoClickListener;
import lilun.com.pension.module.utils.ToastHelper;

/**
 * 通用添加展示图片
 *
 * @author yk
 *         create at 2017/2/27 10:24
 *         email : yk_developer@163.com
 */
public class TakePhotoLayout extends SwipeRefreshLayout implements TakePhotoClickListener {


    private RecyclerView rvPhotoContainer;
    private TakePhotoAdapter adapter;
    private int spanCount = 4;
    private FragmentManager fragmentManager;
    private TakePhotoDialogFragment fragment;
    private TakePhotoClickListener listener;

    public TakePhotoLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAdapter();
        init();
    }

    public TakePhotoLayout(Context context) {
        super(context);
        initAdapter();
        init();
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void setOnResultListener(TakePhotoClickListener listener) {
        this.listener = listener;
    }


    private void initAdapter() {
        List<TakePhotoResult> results = new ArrayList<>();
        TakePhotoResult result = new TakePhotoResult(null, null, TakePhotoResult.TYPE_ADD);
        results.add(result);
        adapter = new TakePhotoAdapter(results);
        adapter.setOnItemClickListener(result1 -> {
            if (result1.getItemType() == TakePhotoResult.TYPE_ADD) {
                if (fragmentManager != null) {
                    if (adapter.getItemCount() - 1 < Config.uploadPhotoCount) {
                        fragment = TakePhotoDialogFragment.newInstance();
                        fragment.setOnResultListener(TakePhotoLayout.this);
                        fragment.show(fragmentManager, null);
                    } else {
                        ToastHelper.get().showWareShort(String.format("最多只能上传%1$s张图片", Config.uploadPhotoCount));
                    }
                }
            } else {
                Logger.d("图片查看");
            }
        });

    }

    private void init() {
        View inflate = LayoutInflater.from(App.context).inflate(R.layout.layout_recycler_view, this);
        rvPhotoContainer = (RecyclerView) inflate.findViewById(R.id.recycler_view);
        rvPhotoContainer.setLayoutManager(new LinearLayoutManager(App.context, LinearLayoutManager.HORIZONTAL, false));
        rvPhotoContainer.addItemDecoration(new ElderModuleClassifyDecoration(10));

        if (adapter != null) {
            rvPhotoContainer.setAdapter(adapter);
        }

    }


    public void showPhotos(List<TakePhotoResult> results) {
        if (adapter != null) {
            adapter.addAll(results);
//            for(TakePhotoResult result:adapter.getData()){
//                if (result.getItemType()==TakePhotoResult.TYPE_ADD){
//                    adapter.remove(result);
//                }
//            }
        }
    }

    @Override
    public void onCameraClick() {
        listener.onCameraClick();
    }

    @Override
    public void onAlbumClick() {
        listener.onAlbumClick();
    }


    /**
     * 获取图片地址集合
     */
    public List<String> getData() {
        if (adapter != null) {
            List<TakePhotoResult> data = adapter.getData();
            if (data != null && data.size() > 1) {
                    List<String> iconPath = new ArrayList<>();
                    for (TakePhotoResult result : data) {
                        if (result.getItemType()==TakePhotoResult.TYPE_PHOTO && !TextUtils.isEmpty(result.getCompressPath())){
                            iconPath.add(result.getCompressPath());
                        }
                }
                return iconPath;
            }
        }
        return null;
    }

    public int getEnableDataCount() {
        return Config.uploadPhotoCount - adapter.getItemCount() + 1;
    }
}