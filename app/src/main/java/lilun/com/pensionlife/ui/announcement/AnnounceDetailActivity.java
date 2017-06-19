package lilun.com.pensionlife.ui.announcement;

import android.text.TextUtils;
import android.webkit.WebView;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.base.BaseActivity;
import lilun.com.pensionlife.module.utils.ACache;
import lilun.com.pensionlife.widget.NormalTitleBar;
import lilun.com.pensionlife.widget.ProgressWebView;

/**
 * 公告详情
 *
 * @author yk
 *         create at 2017/5/9 9:59
 *         email : yk_developer@163.com
 */
public class AnnounceDetailActivity extends BaseActivity {


    //    private Information information;
//    private String content;
    private WebView wvH5;
    private String infoId;

//    public static AnnouncementItemFragment newInstance(Information information) {
//        AnnouncementItemFragment fragment = new AnnouncementItemFragment();
//        Bundle args = new Bundle();
//        args.putSerializable("information", information);
//        fragment.setArguments(args);
//        return fragment;
//    }

//    @Subscribe
//    public void showH5(Event.AnnounceH5 event) {
//        this.information = event.information;
//        showH5();
//    }

    @Override
    protected void getTransferData() {
        infoId = getIntent().getStringExtra("infoId");
//        Preconditions.checkNull(information);
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_announce_detail;
    }

    @Override
    protected void initView() {
        NormalTitleBar titleBar = (NormalTitleBar) findViewById(R.id.titleBar);
        titleBar.setOnBackClickListener(() -> {
            removeCache();
            finish();
        });

        wvH5 = (ProgressWebView) findViewById(R.id.wv_h5);
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        showH5();
    }

    private void showH5() {
        String content = ACache.get().getAsString(infoId + "h5");
        if (!TextUtils.isEmpty(content)) {
            wvH5.loadData(content, "text/html; charset=UTF-8;", null);
        }
    }


    @Override
    public void onBackPressedSupport() {
        super.onBackPressedSupport();
        removeCache();
    }

    private void removeCache() {
        ACache.get().remove(infoId + "h5");

    }
}
