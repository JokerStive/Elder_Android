package lilun.com.pensionlife.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.base.BaseActivity;
import lilun.com.pensionlife.ui.contact.ContactListActivity;
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
        wvH5.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }
        });
        wvH5.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                try {
                    if(url.startsWith("weixin://") //微信
                            || url.startsWith("alipays://") //支付宝
                            || url.startsWith("mailto://") //邮件
                            || url.startsWith("tel://")//电话
                            || url.startsWith("dianping://")//大众点评
                            || url.startsWith("wbmain://")//大众点评
                        //其他自定义的scheme
                            ) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, request.getUrl());
                        startActivity(intent);
                        return true;
                    }
                } catch (Exception e) { //防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                    return true;//没有安装该app时，返回true，表示拦截自定义链接，但不跳转，避免弹出上面的错误页面
                }


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    view.loadUrl(request.getUrl().toString());
                } else {
                    view.loadUrl(request.toString());
                }
                return false;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
                return super.shouldOverrideKeyEvent(view, event);
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return super.shouldInterceptRequest(view, request);
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                return super.shouldInterceptRequest(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
        wvH5.getSettings().setJavaScriptEnabled(true);
        wvH5.addJavascriptInterface(new JavaScriptinterface(this), "android");
        wvH5.getSettings().setUseWideViewPort(true);
        wvH5.getSettings().setSupportZoom(true);
        wvH5.getSettings().setBuiltInZoomControls(true);
        wvH5.getSettings().setDisplayZoomControls(false);
        wvH5.getSettings().setLoadWithOverviewMode(true);

        wvH5.getSettings().setAppCacheEnabled(true);
        wvH5.getSettings().setDomStorageEnabled(true);
        wvH5.getSettings().setAppCacheMaxSize(1024*1024*8);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        wvH5.getSettings().setAppCachePath(appCachePath);
        wvH5.getSettings().setAllowFileAccess(true);
        wvH5.loadUrl(url);
    }

    public class JavaScriptinterface {
        Context context;

        public JavaScriptinterface(Context c) {
            context = c;
        }

        /**
         * 与js交互时用到的方法，在js里直接调用的
         */
        @JavascriptInterface
        public void callAndroid() {
            Intent intent = new Intent(getApplicationContext(), ContactListActivity.class);
            startActivity(intent);
            //   Toast.makeText(context, "显示地址", Toast.LENGTH_LONG).show();
        }
    }

}
