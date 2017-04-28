package lilun.com.pension.ui.lbs;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.LogoPosition;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import lilun.com.pension.R;

/**
 * 加载地图
 *
 * @author yk
 *         create at  13:29
 *         email : yk_developer@163.com
 */
public class LoadMapFragment extends Fragment {

    private MapView mMapView;
    private View mRootView;
    private double longitude;
    private double latitude;


    public static LoadMapFragment newInstance(double longitude, double latitude) {
        LoadMapFragment fragment = new LoadMapFragment();
        Bundle args = new Bundle();
        args.putDouble("longitude", longitude);
        args.putDouble("latitude", latitude);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            longitude = arguments.getDouble("longitude");
            latitude = arguments.getDouble("latitude");

        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_load_map, null);
        mMapView = (MapView) mRootView.findViewById(R.id.map_view);

        return mRootView;
    }




    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        setMap(longitude, latitude);
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    public void setMap(double longitude, double latitude) {
        //logo的位置
        mMapView.setLogoPosition(LogoPosition.logoPostionRightTop);
        mMapView.setBackgroundColor(Color.WHITE);


        BaiduMap map = mMapView.getMap();
        map.setMapType(BaiduMap.MAP_TYPE_NORMAL);

        //设置缩放的级数，最大21,最小3,
        map.setMaxAndMinZoomLevel(21, 18);


        //允许定位图层
        map.setMyLocationEnabled(true);
        //设置定位信息，经纬度，精度。。
        map.setMyLocationData(new MyLocationData.Builder()
                .accuracy(10)
                .direction(10)
                .longitude(longitude)
                .latitude(latitude).build());


        //这个定位配置，这个必须设置，定位图标。。。
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.location);
        MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.COMPASS, false, bitmap);
        map.setMyLocationConfiguration(config);


        LatLng point = new LatLng(longitude, latitude);

        OverlayOptions option = new MarkerOptions()
                .position(point)
                .title("求助人的位置")
                .icon(bitmap);
        map.addOverlay(option);
    }
}
