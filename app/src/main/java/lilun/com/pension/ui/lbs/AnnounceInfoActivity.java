package lilun.com.pension.ui.lbs;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lilun.com.pension.R;
import lilun.com.pension.app.Constants;
import lilun.com.pension.app.Event;
import lilun.com.pension.module.adapter.UrgentInfoAdapter;
import lilun.com.pension.module.bean.Information;
import lilun.com.pension.module.utils.Preconditions;
import lilun.com.pension.module.utils.StringUtils;
import lilun.com.pension.ui.push_info.CacheInfoListActivity;

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
    TextView content;
    @Bind(R.id.btn_all)
    Button btnAll;
    private int infoCount = 1;
    private AnimationDrawable drawableAnim;
    private UrgentInfoAdapter adapter;
    private Information information;

    @Subscribe
    public void refreshUrgentCount(Event.RefreshUrgentInfo event) {
        infoCount++;
        btnAll.setText("查看全部(" + infoCount + ")");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_express_help);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        information = (Information) getIntent().getSerializableExtra("organizationInfo");
        Preconditions.checkNull(information);
        initView();
    }


    protected void initView() {
        drawableAnim = (AnimationDrawable) getResources().getDrawable(R.drawable.anim_express);
        ivExpressIcon.setBackground(drawableAnim);
        start();

        tvExpressTitle.setText(information.getTitle());
        String createdAt = information.getCreatedAt();
        tvExpressTime.setText(StringUtils.IOS2ToUTC(createdAt, 0) + "  " + StringUtils.IOS2ToUTC(createdAt, 3));
        content.setText(information.getContext());
        btnAll.setText("查看全部(" + infoCount + ")");
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
                Intent intent = new Intent(this, CacheInfoListActivity.class);
                intent.putExtra("title", "社区公告");
                intent.putExtra("model", Constants.organizationInfo);
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
