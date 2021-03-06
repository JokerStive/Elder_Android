package lilun.com.pensionlife.base;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.ButterKnife;
import lilun.com.pensionlife.app.Event;
import lilun.com.pensionlife.widget.NormalDialog;
import lilun.com.pensionlife.widget.progress.RxProgressDialog;
import me.yokeyword.fragmentation.SupportFragment;
import me.yokeyword.fragmentation.anim.DefaultNoAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;
import pub.devrel.easypermissions.EasyPermissions;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by yk on 2017/1/5.
 * fragment基类
 */
public abstract class BaseFragment<T extends IPresenter> extends SupportFragment implements EasyPermissions.PermissionCallbacks {
    private final String TAG = "fragment";
    protected Context mContent;
    protected View mRootView;
    protected T mPresenter;
    protected Subscription subscribe;
    private RxProgressDialog dialog;
    protected CompositeSubscription subscription = new CompositeSubscription();

    /**
     * 用于监听弹出软键盘的Enter事件；
     */
    public View.OnKeyListener editOnKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                editViewEnterButton();
            }

            return false;
        }
    };
    private PermissionListener callbacks;
    private LayoutInflater inflater;


    /**
     * 软键盘的Enter事件响应
     */
    public void editViewEnterButton() {

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContent = context != null ? context : getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

//        Logger.d(TAG, getClass().getFrom() + "------onCreate");
        Bundle arguments = getArguments();
        if (arguments != null) {
            getTransferData(arguments);
        }
        initPresenter();
        initData();

    }


    @Subscribe
    public void doNothing(Event.TokenFailure event) {
    }


    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultNoAnimator();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(getLayoutId(), container, false);
            ButterKnife.bind(this, mRootView);
            initView(inflater);
            initEvent();
        }

        return mRootView;
    }


    /**
     * ======================子类需要实现的方法=============================
     *
     * @param arguments
     */

    protected void getTransferData(Bundle arguments) {
    }


    protected void initData() {

    }

    protected abstract void initPresenter();

    protected abstract int getLayoutId();

    protected abstract void initView(LayoutInflater inflater);

    protected void initEvent() {

    }

//    protected abstract void lazyLoadData();

    /**
     * ======================子类需要实现的方法=============================
     */


    public interface PermissionListener {
        void onPermissionGranted();

        void onPermissionDenied();
    }

    protected void requestPermission(String permission, PermissionListener callbacks) {
        this.callbacks = callbacks;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            callbacks.onPermissionGranted();
            return;
        }

        if (EasyPermissions.hasPermissions(getActivity(), permission)) {
            callbacks.onPermissionGranted();
        } else {
            EasyPermissions.requestPermissions(this, "系统需要确认这个权限", 100, permission);
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (callbacks != null) {
            callbacks.onPermissionDenied();
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (callbacks != null) {
            callbacks.onPermissionGranted();
        }
    }

    protected boolean hasPermission(String permission) {
        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt < 23) {
            return true;
        }
        int result = getActivity().checkSelfPermission(permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    protected void requestPermission(String permission, int requestCode) {
        requestPermissions(new String[]{permission}, requestCode);
    }

    public boolean shouldShowRequestPermissionRationale(String permission) {
        boolean shouldShowRational = ActivityCompat.shouldShowRequestPermissionRationale(_mActivity, permission);
        return shouldShowRational;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //    Logger.d(TAG, getClass().getFrom() + "------onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        //    Logger.d(TAG, getClass().getFrom() + "------onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        //     Logger.d(TAG, getClass().getFrom() + "------onResume");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //     Logger.d(TAG, getClass().getFrom() + "------onViewCreated");

    }


    @Override
    public void onPause() {
        super.onPause();
        //    Logger.d(TAG, getClass().getFrom() + "------onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        //    Logger.d(TAG, getClass().getFrom() + "------onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //   Logger.d(TAG, getClass().getFrom() + "------onDestroyView");
        ButterKnife.unbind(mRootView);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.unBindView();
            mPresenter = null;
        }

        if (subscribe != null && subscribe.isUnsubscribed()) {
            subscribe.unsubscribe();
        }

        if (subscription.isUnsubscribed()) {
            subscription.unsubscribe();
            subscription.clear();
        }

        EventBus.getDefault().unregister(this);
        //  Logger.d(TAG, getClass().getFrom() + "------onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //  Logger.d(TAG, getClass().getFrom() + "------onDetach");
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        // Logger.d(TAG, getClass().getFrom() + "------setUserVisibleHint" + "===" + isVisibleToUser);
    }

    public void showDialog(String str) {
        new NormalDialog().createShowMessage(_mActivity, str, new NormalDialog.OnPositiveListener() {
            @Override
            public void onPositiveClick() {
                return;
            }
        });
    }

    @Override
    public void pop() {
        hideSoftInput();
        super.pop();
    }


    public void showDialog() {
        if (dialog == null) {
            dialog = new RxProgressDialog(_mActivity);
        }

        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    public void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
