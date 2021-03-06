package lilun.com.pensionlife.ui.lbs;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.Event;
import lilun.com.pensionlife.module.adapter.UrgentInfoAdapter;
import lilun.com.pensionlife.module.bean.OrganizationAid;
import lilun.com.pensionlife.module.utils.CacheMsgClassify;
import lilun.com.pensionlife.module.utils.Preconditions;
import lilun.com.pensionlife.ui.push_info.CacheInfoListActivity;

/**
 * 紧急消息弹窗
 *
 * @author yk
 *         create at 2017/4/27 9:27
 *         email : yk_developer@163.com
 */
public class UrgentAidInfoActivity extends Activity {
    @Bind(R.id.iv_express_icon)
    ImageView ivExpressIcon;

    @Bind(R.id.tv_express_desc)
    TextView tvExpressDesc;


    @Bind(R.id.rv_express_info)
    RecyclerView rvExpressInfo;
    //
//    @Bind(R.id.map_view)
//    MapView mMapView;


    @Bind(R.id.btn_all)
    Button btnAll;

    private int infoCount = 1;
    private AnimationDrawable drawableAnim;
    private UrgentInfoAdapter adapter;
    private OrganizationAid aid;

    @Subscribe
    public void refreshUrgentCount(Event.RefreshUrgentInfo event) {
        infoCount++;
        btnAll.setText("查看全部(" + infoCount + ")");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_aid);
        ButterKnife.bind(this);
//        SDKInitializer.initialize(getApplicationContext());
        EventBus.getDefault().register(this);

        aid = (OrganizationAid) getIntent().getSerializableExtra("aid");
        infoCount  =getIntent().getIntExtra("count",1);
        Preconditions.checkNull(aid);
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    protected void initView() {
        drawableAnim = (AnimationDrawable) getResources().getDrawable(R.drawable.anim_express);
        ivExpressIcon.setBackground(drawableAnim);
        start();

        btnAll.setText("查看全部(" + infoCount + ")");


        rvExpressInfo.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        List<OrganizationAid> pushMessages = new ArrayList<>();
        pushMessages.add(aid);
        adapter = new UrgentInfoAdapter(pushMessages);
        rvExpressInfo.setAdapter(adapter);


    }

    private void setMap() {
        String memo = aid.getMemo();
        if (!TextUtils.isEmpty(memo)) {

            String longitudeString = "";
            String latitudeString = "";
            try {
                JSONObject jsonObject = new JSONObject(memo);
                latitudeString = jsonObject.getString("lat");
                longitudeString = jsonObject.getString("lng");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (!TextUtils.isEmpty(latitudeString) && !TextUtils.isEmpty(longitudeString)) {

                double longitude = Double.parseDouble(longitudeString);
                double latitude = Double.parseDouble(latitudeString);

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                LoadMapFragment loadMapFragment = LoadMapFragment.newInstance(longitude, latitude);
                transaction.replace(R.id.fl_container, loadMapFragment);

                transaction.commit();
            }
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
                intent.putExtra("title", "紧急求助");
                intent.putExtra("classify", msgClassify.urgent_help);
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

    //
    @Override
    protected void onResume() {
        super.onResume();
//        mMapView.onResume();

        //设置地图
        setMap();


    }

    @Override
    protected void onPause() {
        super.onPause();
//        mMapView.onPause();
    }
}
