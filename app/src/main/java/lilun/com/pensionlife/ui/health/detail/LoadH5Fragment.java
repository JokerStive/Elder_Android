package lilun.com.pensionlife.ui.health.detail;

import android.os.Bundle;
import android.view.LayoutInflater;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.utils.Preconditions;
import lilun.com.pensionlife.widget.NormalTitleBar;
import lilun.com.pensionlife.widget.ProgressWebView;

/**
 * 加载h5界面
 *
 * @author yk
 *         create at 2017/5/11 13:17
 *         email : yk_developer@163.com
 */
public class LoadH5Fragment extends BaseFragment {
    private ProgressWebView wvH5;
    private String url;
    private String title;
    private NormalTitleBar titleBar;

    public static LoadH5Fragment newInstance(String title, String url) {
        LoadH5Fragment fragment = new LoadH5Fragment();
        Bundle args = new Bundle();
        args.putString("url", url);
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void getTransferData(Bundle arguments) {
        super.getTransferData(arguments);
        url = arguments.getString("url");
        title = arguments.getString("title");
        Preconditions.checkNull(url);
        Preconditions.checkNull(title);
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_load_h5;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        wvH5 = (ProgressWebView) mRootView.findViewById(R.id.wv_h5);
        titleBar = (NormalTitleBar) mRootView.findViewById(R.id.titleBar);
        titleBar.setOnBackClickListener(this::pop);
        titleBar.setTitle(title);

//        BaseActivity baseActivity = (BaseActivity) _mActivity;
//        WebViewClient mWebViewClient = new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                return true;
//            }
//
//        };
//
//        WebChromeClient webChromeClient = new WebChromeClient() {
//            @Override
//            public void onProgressChanged(WebView view, int newProgress) {
//                if (newProgress == 0) {
//                    baseActivity.showDialog();
//                }
//
//                if (newProgress == 100) {
//                    baseActivity.dismissDialog();
//                }
//            }
//        };
//
//        wvH5.setWebViewClient(mWebViewClient);
//        wvH5.setWebChromeClient(webChromeClient);
        wvH5.loadUrl(url);
    }

    @Override
    public boolean onBackPressedSupport() {
        if (wvH5.canGoBack()) {
            wvH5.goBack();
            return true;
        }

        return super.onBackPressedSupport();
    }
}
