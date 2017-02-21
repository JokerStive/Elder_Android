package lilun.com.pension.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportFragment;
import me.yokeyword.fragmentation.anim.DefaultNoAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * Created by yk on 2017/1/5.
 * fragment基类
 */
public abstract class BaseFragment<T extends IPresenter> extends SupportFragment{
    private final String TAG = getClass().getSimpleName();
    protected Context mContent;
    protected View mRootView;
    protected T mPresenter;
//    private boolean hasViewCreated;
//    private boolean hasLoadedData;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContent = context != null ? context : getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EventBus.getDefault().register(this);

        Log.d(TAG, getClass().getName() + "------onCreate");
        Bundle arguments = getArguments();
        if (arguments != null) {
            getTransferData(arguments);
        }
        initPresenter();
        initData();

    }


    @Override
    protected FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultNoAnimator();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(getLayoutId(), container, false);
            ButterKnife.bind(this,mRootView);
            initView(inflater);
            initEvent();
        }
        return mRootView;
    }

    /**
     * ======================子类需要实现的方法=============================
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, getClass().getName() + "------onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, getClass().getName() + "------onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, getClass().getName() + "------onResume");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, getClass().getName() + "------onViewCreated");

//        hasViewCreated = true;

//        prepareLazyLoadData();
    }

//    private void prepareLazyLoadData() {
//        if (getUserVisibleHint() && hasViewCreated && !hasLoadedData) {
//            hasLoadedData = true;
//            lazyLoadData();
//        }
//    }


    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, getClass().getName() + "------onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, getClass().getName() + "------onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, getClass().getName() + "------onDestroyView");
//        hasViewCreated=false;
//        hasLoadedData=false;
        ButterKnife.unbind(mRootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.unBindView();
            mPresenter = null;
        }

//        EventBus.getDefault().unregister(this);
        Log.d(TAG, getClass().getName() + "------onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, getClass().getName() + "------onDetach");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d(TAG, getClass().getName() + "------setUserVisibleHint" + "===" + isVisibleToUser);
    }


}
