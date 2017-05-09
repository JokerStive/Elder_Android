package lilun.com.pension.ui.lbs;

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

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lilun.com.pension.R;
import lilun.com.pension.app.Constants;
import lilun.com.pension.app.Event;
import lilun.com.pension.module.adapter.UrgentInfoAdapter;
import lilun.com.pension.module.bean.OrganizationAid;
import lilun.com.pension.module.utils.Preconditions;
import lilun.com.pension.ui.push_info.CacheInfoListActivity;

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
        Preconditions.checkNull(aid);
        initView();
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
        if (!TextUtils.isEmpty(memo) && memo.contains("/")) {
            String[] split = memo.split("/");
            if (split.length == 2) {
                String longitudeString = split[0];
                String latitudeString = split[1];
                double longitude = Double.parseDouble(longitudeString);
                double latitude = Double.parseDouble(latitudeString);

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                LoadMapFragment loadMapFragment = LoadMapFragment.newInstance(longitude, latitude);
                transaction.replace(R.id.fl_container,loadMapFragment);

                transaction.commit();
//
//                //logo的位置
//                mMapView.setLogoPosition(LogoPosition.logoPostionRightTop);
//                mMapView.setBackgroundColor(Color.WHITE);
//
//
//                BaiduMap map = mMapView.getMap();
//                map.setMapType(BaiduMap.MAP_TYPE_NORMAL);
//
//                //设置缩放的级数，最大21,最小3,
//                map.setMaxAndMinZoomLevel(21,18);
//
//
//                //允许定位图层
//                map.setMyLocationEnabled(true);
//                //设置定位信息，经纬度，精度。。
//                map.setMyLocationData(new MyLocationData.Builder()
//                        .accuracy(10)
//                        .direction(10)
//                        .longitude(longitude)
//                        .latitude(latitude).build());
//
//
//                //这个定位配置，这个必须设置，定位图标。。。
//                BitmapDescriptor bitmap = BitmapDescriptorFactory
//                        .fromResource(R.drawable.location);
//                MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.COMPASS, false, bitmap);
//                map.setMyLocationConfiguration(config);
//
//
//                LatLng point = new LatLng(longitude, latitude);
//
//                OverlayOptions option = new MarkerOptions()
//                        .position(point)
//                        .title("求助人的位置")
//                        .icon(bitmap);
//                map.addOverlay(option);
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
                Intent intent = new Intent(this, CacheInfoListActivity.class);
                intent.putExtra("title", "紧急求助");
                intent.putExtra("model", Constants.organizationAid);
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
