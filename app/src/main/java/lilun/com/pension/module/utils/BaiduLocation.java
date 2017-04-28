package lilun.com.pension.module.utils;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;


public class BaiduLocation {
    /**
     * 经度
     */
    public static double mylongitude = -1.0;
    /**
     * 纬度
     */
    public static double mylatitude = -1.0;

    /**
     * 城市
     */
    public static String myCity = null;

    /**
     * 街道
     */
    public static String address = null;

    /**
     * 回调经纬度方法
     *
     * @param myLocationListener
     */
    public static void setMyLocationListener(currentLocationListener myLocationListener) {
        BaiduLocation.myLocationListener = myLocationListener;
    }

    public static currentLocationListener myLocationListener;

    /**
     * 回调经纬度的接口定义
     *
     * @author Administrator
     */
    public static interface currentLocationListener {
        public void currentLocation(double longitude, double latitude, String address);
    }

    public static void getLocation(Context context) {

        final LocationClient locationClient = new LocationClient(context);

        // 设置定位条件

        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 是否打开GPS
        option.setCoorType("bd09ll"); // 设置返回值的坐标类型。
        option.setPriority(LocationClientOption.NetWorkFirst); // 设置定位优先级
        option.setProdName("智慧养老");
        option.setScanSpan(5000); //
        option.setAddrType("all");
        locationClient.setLocOption(option);
        // 注册位置监听器
        locationClient.registerLocationListener(location -> {
            if (location == null) {
                return;
            }
            if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                String district = location.getAddress().district;
                address = location.getAddrStr();
                mylongitude = location.getLongitude();
                mylatitude = location.getLatitude();
                // 经纬度
                if (myLocationListener != null) {
                    myLocationListener.currentLocation(mylongitude, mylatitude, address);
                    locationClient.stop();
                }
            }
        });

        locationClient.start();
        locationClient.requestLocation();
    }
}