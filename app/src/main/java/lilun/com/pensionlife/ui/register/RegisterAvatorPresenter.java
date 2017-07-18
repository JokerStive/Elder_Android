package lilun.com.pensionlife.ui.register;

import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;

import lilun.com.pensionlife.app.User;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.base.RxPresenter;
import lilun.com.pensionlife.module.utils.BitmapUtils;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.net.NetHelper;
import lilun.com.pensionlife.net.RxSubscriber;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by zp on 2017/4/14.
 */

public class RegisterAvatorPresenter extends RxPresenter<RegisterContract.ViewAvator>
        implements RegisterContract.PresenterAvator {
    @Override
    public void updateImage(String id, String imageName, String path) {
        Log.d("zp", User.getToken() + "   \n" + id + "     \n" + imageName + "   \n" + path);
        if (TextUtils.isEmpty(imageName))
            imageName = "{imageName}";
        ArrayList<String> paths = new ArrayList<>();
        paths.add(path);
        byte[] pathString = BitmapUtils.bitmapToBytes(path);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), pathString);
        addSubscribe(NetHelper.getApi()
                .updateImage(id, imageName, requestBody)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<Object>(((BaseFragment) view).getActivity()) {
                    @Override
                    public void _next(Object o) {
                        Log.d("zp", "上传图片返回");
                        dissDialog();
                        view.successOfUpdateImage();
                    }
                }));
    }
}
