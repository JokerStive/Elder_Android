package lilun.com.pensionlife.module.adapter;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.orhanobut.logger.Logger;

import java.util.List;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.module.bean.TakePhotoResult;

/**
 * 展示photo的adapter
 *
 * @author yk
 *         create at 2017/2/27 10:39
 *         email : yk_developer@163.com
 */
public class TakePhotoAdapter extends BaseMultiItemQuickAdapter<TakePhotoResult, BaseViewHolder> {
    private OnItemClickListener listener;

    public TakePhotoAdapter(List<TakePhotoResult> data) {
        super(data);
        addItemType(TakePhotoResult.TYPE_ADD, R.layout.item_add_take_photo);
        addItemType(TakePhotoResult.TYPE_PHOTO, R.layout.item_show_take_photo);
        setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.iv_add_take_photo:
                        if (listener != null) {
                            listener.onItemClick(TakePhotoResult.TYPE_ADD, null);
                        }
                        break;

                    case R.id.iv_photo:
                        if (listener != null) {
                            listener.onItemClick(TakePhotoResult.TYPE_PHOTO, getItem(position));
                        }
                        break;

                    case R.id.iv_remove_photo:
                        remove(position);
                        break;
                }
            }
        });

    }

    @Override
    protected void convert(BaseViewHolder helper, TakePhotoResult result) {
        switch (helper.getItemViewType()) {
            case TakePhotoResult.TYPE_ADD:
                helper.setImageResource(R.id.iv_add_take_photo, R.drawable.add_photo);
                helper.addOnClickListener(R.id.iv_add_take_photo);
                break;

            case TakePhotoResult.TYPE_PHOTO:
                ImageView ivPhoto = helper.getView(R.id.iv_photo);
                showImageFromResult(result, ivPhoto);
                helper.addOnClickListener(R.id.iv_photo);
                helper.addOnClickListener(R.id.iv_remove_photo);
                break;
        }

    }


    private void showImageFromResult(TakePhotoResult result, ImageView targetImageView) {
        String path = result.getOriginalPath();
        Logger.d("图片的位置" + path);
        Glide.with(App.context).load(path)
                .error(R.drawable.avatar)
                .centerCrop()
                .into(targetImageView);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int type, TakePhotoResult result);
    }


}
