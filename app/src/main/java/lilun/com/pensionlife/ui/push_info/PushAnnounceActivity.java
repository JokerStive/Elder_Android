package lilun.com.pensionlife.ui.push_info;

import android.content.Intent;
import android.webkit.WebView;

import butterknife.Bind;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.base.BaseActivity;
import lilun.com.pensionlife.module.bean.CacheInfo;
import lilun.com.pensionlife.module.utils.Preconditions;
import lilun.com.pensionlife.widget.NormalTitleBar;
import lilun.com.pensionlife.widget.ProgressWebView;

/**
 * Created by zp on 2017/7/8.
 */

public class PushAnnounceActivity extends BaseActivity {
    private static final String ITEM = "item";
    private CacheInfo item;

    @Bind(R.id.title_bar)
    NormalTitleBar titleBar;
    @Bind(R.id.wv_announce_data)
    ProgressWebView pwvAnnounceData;


    public static void start(BaseActivity activity, CacheInfo item) {
        Intent intent = new Intent(activity, PushAnnounceActivity.class);
        intent.putExtra(ITEM, item);
        activity.startActivity(intent);
    }

    @Override
    protected void getTransferData() {
        item = (CacheInfo) getIntent().getSerializableExtra(ITEM);
        Preconditions.checkNull(item);
    }

    @Override
    protected void initPresenter() {

    }


    @Override
    protected void initView() {
        titleBar.setOnBackClickListener(() -> {
            finish();
        });
        titleBar.setTitle(item.getFirst() + "详情");
     //   tvAnnounceData.setText(Html.fromHtml());
        pwvAnnounceData.loadData(item.getFourth(), "text/html; charset=UTF-8;", null);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_push_announce_layout;
    }


}
