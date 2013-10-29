package m.z.util;

import android.content.Context;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.LocationData;
import m.z.imagelocus.entity.Lbs;
import m.z.imagelocus.entity.convert.LbsConvert;

/**
 * 百度定位API使用类，启动定位，当返回定位结果是停止定位
 * @author Winnid
 */
public class LocationUtil {

    private static Context mContext;
    private static LocationUtil locationUtil = null;

    // 定位相关
    LocationClient locClient;
    LocationListener locListener = new LocationListener();
    LocationData locationData = null;    //定位数据
    BDLocation locationBD = null;     //定位数据
    Lbs locationLbs = null;    //定位数据

    public static LocationUtil getInstance(Context context) {
        mContext = context;
        if (locationUtil == null) {
            locationUtil = new LocationUtil(context);
        }
        return locationUtil;
    }

    private LocationUtil(Context context) {
        locClient = new LocationClient(context);
        locClient.registerLocationListener(locListener);
        locClient.start();
    }

    /**
     * 开始定位请求，结果在回调中
     */
    public void startLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setAddrType("all");// 返回的定位结果包含地址信息
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
        option.disableCache(true);// 禁止启用缓存定位
        locClient.setLocOption(option);
        locClient.requestLocation();
    }


    /**
     * 监听函数，有新位置的时候，格式化成字符串，输出到屏幕中
     */
    public class LocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null)
                return;
            locationBD = location;
            locationLbs = LbsConvert.BDLocation2Lbs(location);
            locationData = LbsConvert.Lbs2LocationData(locationLbs);
            locClient.stop();
        }

        public void onReceivePoi(BDLocation poiLocation) {

        }
    }
}
