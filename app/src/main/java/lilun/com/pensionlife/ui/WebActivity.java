package lilun.com.pensionlife.ui;

import android.os.Build;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.base.BaseActivity;
import lilun.com.pensionlife.widget.NormalTitleBar;

/**
 * 网页详情
 */
public class WebActivity extends BaseActivity {


    private NormalTitleBar titleBar;
    private WebView wvH5;
    private String url;
    private String title;


    @Override
    protected void getTransferData() {
        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_web_detail;
    }

    @Override
    protected void initView() {
        titleBar = (NormalTitleBar) findViewById(R.id.titleBar);
        titleBar.setOnBackClickListener(this::finish);
        titleBar.setTitle(title);
        wvH5 = (WebView) findViewById(R.id.wv_h5);
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        showH5();
    }

    private void showH5() {
        wvH5.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    view.loadUrl(request.getUrl().toString());
                } else {
                    view.loadUrl(request.toString());
                }
                return true;
            }
        });
        wvH5.getSettings().setJavaScriptEnabled(true);
        wvH5.getSettings().setUseWideViewPort(true);
        wvH5.getSettings().setSupportZoom(true);
        wvH5.getSettings().setBuiltInZoomControls(true);
        wvH5.getSettings().setDisplayZoomControls(false);
        wvH5.getSettings().setLoadWithOverviewMode(true);
        wvH5.loadUrl(url);
    }

}
