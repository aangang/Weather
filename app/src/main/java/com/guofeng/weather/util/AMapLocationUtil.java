package com.guofeng.weather.util;

import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.guofeng.weather.base.BaseApplication;

/**
 * 高德地图定位 工具类
 * Created by GUOFENG on 2016/10/18.
 */

public class AMapLocationUtil {

    public AMapLocationUtil() {
        init();
    }

    public static AMapLocationUtil getDefault() {
        return AMapHolder.sInstance;
    }


    private static class AMapHolder {
        private static final AMapLocationUtil sInstance = new AMapLocationUtil();
    }


    //声明AMapLocationClient类对象
    private AMapLocationClient mLocationClient = null;
    //AMapLocationClientOption对象用来设置发起定位的模式和相关参数。
    //声明AMapLocationClientOption对象
    private AMapLocationClientOption mLocationOption = null;

    //声明定位回调监听器
    private AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    String mCity= Util.replaceCity(aMapLocation.getCity());
                    ToastUtil.showShortToast("您所在位置：" + mCity);
                    SharedPreferenceUtil.getInstance().setCityName(mCity);
                    stopAMap();
                } else {
                    //定位失败时，可通过ErrCode错误码来确定失败的原因，errInfo是错误信息。
                    Log.e("高德定位错误", "location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                    ToastUtil.showShortToast(aMapLocation.getErrorInfo());
                }
            }
        }
    };


    private void init() {
        //初始化定位
        mLocationClient = new AMapLocationClient(BaseApplication.getMyAppContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);

        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();


        /**
         * 定位模式 SDK默认选择使用高精度定位模式。
         */
        //1.高精度模式 会同时使用网络定位和GPS定位，优先返回最高精度的定位结果，以及对应的地址描述信息
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        // mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);

        //2.低功耗模式 不会使用GPS和其他传感器，只会使用网络定位（Wi-Fi和基站定位）；
        //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);

        //3.仅用设备模式：不需要连接网络，只使用GPS进行定位，这种模式下不支持室内环境的定位，且不会返回地址描述信息。
        //mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors);


        //设置是否只定位一次,该方法默认为false。
        mLocationOption.setOnceLocation(false);

        //获取最近3s内精度最高的一次定位结果：
        //设置下面接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。
        //如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(false);

        //SDK默认采用连续定位模式
        //自定义设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(1000);

        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);

        //设置是否强制刷新WIFI，默认为true,强制刷新。
        //每次定位主动刷新WIFI模块会提升WIFI定位精度，但相应的会多付出一些电量消耗。
        mLocationOption.setWifiActiveScan(false);

        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);


        //设置采用http/https方式发起网络定位请求，默认发起http请求。
        //注意：https方式请求定位对定位速度和性能有一定的损耗，定位流量会增大，但安全性更高。
        // AMapLocationClientOption.setLocationProtocol是静态方法。
        // AMapLocationClientOption.AMapLocationProtocol提供2种枚举:
        // HTTP代表使用http请求，HTTPS代表使用https请求。
        // 单个定位客户端生命周期内（调用AMapLocationClient.onDestroy()方法结束生命周期）设置一次即可。
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);

    }

    public void startAMap() {
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    public void stopAMap() {
        //停止定位,本地定位服务并不会被销毁
        mLocationClient.stopLocation();
        //销毁定位客户端，同时销毁本地定位服务,若要重新开启定位请重新New一个AMapLocationClient对象
        mLocationClient.onDestroy();
    }

}
