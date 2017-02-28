package lilun.com.pension.widget;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import lilun.com.pension.R;
import lilun.com.pension.module.callback.TakePhotoClickListener;

/**
 * 底部弹出拍照或相册选择图片
 *
 * @author yk
 *         create at 2017/2/27 9:15
 *         email : yk_developer@163.com
 */
public class TakePhotoDialogFragment extends DialogFragment implements View.OnClickListener {


    private TakePhotoClickListener listener;

    public static TakePhotoDialogFragment newInstance() {
        TakePhotoDialogFragment fragment = new TakePhotoDialogFragment();
        return fragment;
    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getDialog().getWindow();
        window.setGravity(Gravity.BOTTOM);


        setStyle(R.style.ActionSheetDialogStyle, 0);
        View view = inflater.inflate(R.layout.fragment_take_photo, null);
        view.findViewById(R.id.tv_camera).setOnClickListener(this);
        view.findViewById(R.id.tv_album).setOnClickListener(this);
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow().setLayout((dm.widthPixels), ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_camera:
                takePhoto();
                break;
            case R.id.tv_album:
                choosePhoto();
                break;

        }
    }

    private void choosePhoto() {
        if (listener!=null){
            listener.onAlbumClick();
        }
        dismiss();
    }

    private void takePhoto() {
        if (listener!=null){
            listener.onCameraClick();
        }
        dismiss();
    }


    public void setOnResultListener(TakePhotoClickListener listener) {
        this.listener = listener;
    }


}
