package lilun.com.pensionlife.ui.lbs;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.app.Event;
import lilun.com.pensionlife.module.bean.Information;
import lilun.com.pensionlife.module.utils.CacheMsgClassify;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.module.utils.ScreenUtils;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.net.NetHelper;
import lilun.com.pensionlife.net.RxSubscriber;
import lilun.com.pensionlife.ui.push_info.CacheInfoListActivity;
import lilun.com.pensionlife.widget.ProgressWebView;

/**
 * 紧急消息弹窗
 *
 * @author yk
 *         create at 2017/4/27 9:27
 *         email : yk_developer@163.com
 */
public class AnnounceInfoActivity extends Activity {

    @Bind(R.id.iv_express_icon)
    ImageView ivExpressIcon;
    @Bind(R.id.tv_express_title)
    TextView tvExpressTitle;
    @Bind(R.id.tv_express_time)
    TextView tvExpressTime;
    @Bind(R.id.content)
    TextView tvContent;
    @Bind(R.id.wv_content)
    ProgressWebView progressWebView;
    @Bind(R.id.btn_all)
    Button btnAll;
    @Bind(R.id.tv_express_come)
    TextView tvExpressCome;
    private int infoCount = 0;
    private AnimationDrawable drawableAnim;
    private String mInformationid;

    @Subscribe(sticky = true)
    public void refreshUrgentCount(Event.RefreshPushInformation pushInformation) {
        Logger.d("开始刷新公告数据",pushInformation.infoId);
        infoCount++;
        getInfo(pushInformation.infoId);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_express_help);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

//        mInformationid = getIntent().getStringExtra("informationId");
//        Preconditions.checkNull(mInformationid);
//
//
//        getInfo(mInformationid);

        setAppearance();
    }

    private void getInfo(String infoId) {
        NetHelper.getApi().getInformation(infoId)
                .compose(RxUtils.applySchedule())
                .compose(RxUtils.handleResult())
                .subscribe(new RxSubscriber<Information>(this) {
                    @Override
                    public void _next(Information information) {
                        initView(information);
                    }
                });
    }

    private void setAppearance() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreenUtils.getScreenWith(App.context) / 5 * 4;
        getWindow().setAttributes(params);
    }


    protected void initView(Information information) {
        drawableAnim = (AnimationDrawable) getResources().getDrawable(R.drawable.anim_express);
        ivExpressIcon.setBackground(drawableAnim);
        start();

        tvExpressTitle.setText(information.getTitle());
        String createdAt = information.getCreatedAt();
        tvExpressTime.setText(StringUtils.IOS2ToUTC(createdAt, 0) + "  " + StringUtils.IOS2ToUTC(createdAt, 3));
        btnAll.setText("查看全部(" + infoCount + ")");

        tvExpressCome.setText("来源：" + information.getCreatorName() + "");

        int contextType = information.getContextType();
        String content = information.getContext();
        tvContent.setVisibility(contextType == 5 ? View.VISIBLE : View.GONE);
        progressWebView.setVisibility(contextType == 2 ||contextType == 3 ? View.VISIBLE : View.GONE);
        switch (contextType) {
            //html
            case 2:
                progressWebView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
//                progressWebView.loadData(content, "text/html; charset=UTF-8;", null);
                break;

            case 3:
                progressWebView.loadUrl(content);
                break;
            //json
            case 0:
                tvContent.setText(information.getContext());
                break;
        }
    }


    @OnClick({R.id.express_delete, R.id.btn_all})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.express_delete:
                stop();
                finish();
                break;

            case R.id.btn_all:
                //查看全部
                CacheMsgClassify msgClassify = new CacheMsgClassify();
                Intent intent = new Intent(this, CacheInfoListActivity.class);
                intent.putExtra("title", "社区公告");
                intent.putExtra("classify", msgClassify.announce);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void start() {
        if (drawableAnim != null && !drawableAnim.isRunning()) {
            drawableAnim.start();
        }
    }

    private void stop() {
        if (drawableAnim != null && drawableAnim.isRunning()) {
            drawableAnim.stop();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mMapView.onDestroy();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
        setResult(0);
    }

}
