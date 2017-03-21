package lilun.com.pension.module.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jph.takephoto.model.TImage;
import com.orhanobut.logger.Logger;

import java.util.List;

import lilun.com.pension.R;
import lilun.com.pension.app.App;
import lilun.com.pension.base.QuickAdapter;
import lilun.com.pension.module.bean.TakePhotoResult;

/**
 * 展示photo的adapter
 *
 * @author yk
 *         create at 2017/2/27 10:39
 *         email : yk_developer@163.com
 */
public class TakePhotoAdapter extends QuickAdapter<TakePhotoResult> {
    private OnItemClickListener listener;


//    private  BaseFragment fragment;

    public TakePhotoAdapter(List<TakePhotoResult> data) {
        super(R.layout.item_take_photo, data);
//        this.fragment =fragment;
    }

    @Override
    protected void convert(BaseViewHolder helper, TakePhotoResult result) {
        ImageView ivPhoto = helper.getView(R.id.iv_photo);

        if (result.getItemType() == TakePhotoResult.TYPE_ADD) {
            ivPhoto.setBackgroundResource(R.drawable.add_photo);
        } else if (result.getItemType() == TakePhotoResult.TYPE_PHOTO) {
            // TODO 根据图片来源怎么加载
            String path;
            if (result.getFrom()== TImage.FromType.CAMERA){
                path= result.getOriginalPath();
            }else {
                path = result.getOriginalPath();
            }
            Logger.d("图片的位置"+path);
            Glide.with(App.context).load(path)
                    .error(R.drawable.avatar)
                    .centerCrop()
                    .into(ivPhoto);

        }

        ivPhoto.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(result);
            }
        });

    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(TakePhotoResult result);
    }
}
