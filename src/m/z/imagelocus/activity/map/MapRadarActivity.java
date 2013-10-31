package m.z.imagelocus.activity.map;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.TextView;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MyLocationOverlay.LocationMode;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.googlecode.androidannotations.annotations.*;
import m.z.common.CommonView;
import m.z.imagelocus.R;
import m.z.imagelocus.config.SystemAdapter;
import m.z.imagelocus.entity.Lbs;
import m.z.imagelocus.entity.LbsDo;
import m.z.imagelocus.entity.convert.LbsConvert;
import m.z.imagelocus.entity.yun.LbsDoPoiYun;
import m.z.imagelocus.service.http.LbsYunService;
import m.z.imagelocus.service.http.SearchPoiService;
import m.z.imagelocus.view.map.LocationMapView;
import m.z.imagelocus.view.map.LocationOverlay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoTitle
@EActivity(R.layout.activity_map_friend)
public class MapRadarActivity extends Activity{

    public static MapRadarActivity instance = null;
    private LayoutInflater inflater;

    @ViewById(R.id.tv_middle)
    TextView tv_middle;
    //右上角第一个按钮
    @ViewById(R.id.btn_right)
    Button btn_right;

    List<LbsDoPoiYun> radarLbsDoPoiList = null;    //位置数据
    Map<String, LbsDoPoiYun> radarLbsDoPoiMap = new HashMap<String, LbsDoPoiYun>();   //位置数据

    //地图相关，使用继承MapView的LocationMapView目的是重写touch事件实现泡泡处理
    //如果不处理touch事件，则无需继承，直接使用MapView即可
    @ViewById(R.id.bmap_view)
    LocationMapView mMapView;	    // 地图View
    MapController mMapController = null;

    // 定位相关
    LocationClient locClient;
    LocationListener locListener = new LocationListener();
    LocationOverlay locOverlay = null;      // 定位图层
    LocationData locDataNow = null;    //定位数据
    Lbs lbsDataNow = null;    //定位数据

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        inflater = LayoutInflater.from(this);
    }

    @AfterViews
    void init() {

        tv_middle.setText("信息雷达");

        //地图初始化
        mMapController = mMapView.getController();
        mMapView.getController().setZoom(14);
        mMapView.getController().enableClick(true);
        mMapView.setBuiltInZoomControls(true);

        //创建位置数据
        readLbsData();

        //定位初始化
        initLocation();

    }

    /**
     *初始化位置
     */
    private void initLocation() {

        locClient = new LocationClient( this );
        locClient.registerLocationListener( locListener );
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);//打开gps
        option.setAddrType("all");//返回的定位结果包含地址信息
        option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(999);//设置发起定位请求的间隔时间为10000ms
        option.disableCache(false);//禁止启用缓存定位
        option.setPriority(LocationClientOption.GpsFirst);
        option.setPoiNumber(2);    //最多返回POI个数
        option.setPoiDistance(500); //poi查询距离
        option.setPoiExtraInfo(true); //是否需要POI的电话和地址等详细信息
        locClient.setLocOption(option);
        locClient.start();

        //定位图层初始化
        locOverlay = new LocationOverlay(mMapView, mMapView.pop, true);
        //设置定位数据
        locOverlay.setData(locDataNow);
        //添加定位图层
        //mMapView.getOverlays().add(locOverlay);
        //修改定位数据后刷新图层生效
        mMapView.refresh();
    }

    /**
     * 创建位置数据
     * */
    private void readLbsData() {

        //lbsDataHistory = Service.lbsService.findByUser_id(user_id);

        //String app_user_id = SystemAdapter.currentUser.getApp_user_id();
        //new LbsYunService(instance, LbsYunService.FunctionName.findLbsByApp_User_id, app_user_id) {

        //清除之前的位置数据
        mMapView.getOverlays().clear();
        new SearchPoiService(instance, SearchPoiService.FunctionName.findAllLbs) {
            @Override
            public void doResult(Map<String, Object> resultMap) {
                CommonView.displayShort(instance, (String) resultMap.get("msg"));
                radarLbsDoPoiList = (List<LbsDoPoiYun>) resultMap.get("lbsDoPoiYunList");
                setLocationOverlay();
            }
        };
    }

    /**
     * 创建位置图层
     */
    private void setLocationOverlay() {
        if(radarLbsDoPoiList != null && radarLbsDoPoiList.size() != 0) {
            List<LocationOverlay> locationOverlays = new ArrayList<LocationOverlay>();
            for (LbsDoPoiYun lbsDoPoiYun : radarLbsDoPoiList ) {
                radarLbsDoPoiMap.put(lbsDoPoiYun.getKeyword(), lbsDoPoiYun);
                LocationOverlay _locOverlay = new LocationOverlay(mMapView, mMapView.pop, true, 1);
                _locOverlay.setData(lbsDoPoiYun);
                locationOverlays.add(_locOverlay);
            }
            mMapView.getOverlays().addAll(locationOverlays);
            mMapView.refresh();
        }
    }


    /**
     * 手动触发一次定位请求
     */
    public void requestLocClick(){
        locClient.requestLocation();
        CommonView.displayLong(instance, "正在定位……");
    }

    @Click(R.id.btn_right)
    void btn_right_onClick() {
        readLbsData();
    }

    @Click(R.id.btn_locate)
    void btn_locate_onClick() {
        requestLocClick();
    }


    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mMapView.destroy();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mMapView.onRestoreInstanceState(savedInstanceState);
    }


    /********************************Listener*************************************/
    /**
     * 定位SDK监听函数
     */
    public class LocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation == null)
                return ;

            //转换定位数据
            Lbs lbs =  LbsConvert.createLbs(SystemAdapter.currentUser.getApp_user_id(), bdLocation);
            lbsDataNow = lbs;

            //更新定位数据
            locOverlay.setData(lbsDataNow);
            //更新图层数据执行刷新后生效
            mMapView.refresh();
            //是手动触发请求或首次定位时，移动到定位点
            //移动地图到定位点
            Log.d("LocationOverlay", "receive location, animate to it");
            mMapController.animateTo(new GeoPoint((int)(lbsDataNow.getLatitude()* 1e6), (int)(lbsDataNow.getLongitude() *  1e6)));
            locOverlay.setLocationMode(LocationMode.NORMAL);
        }

        public void onReceivePoi(BDLocation poiLocation) {
            if (poiLocation == null){
                return ;
            }
            StringBuffer sb = new StringBuffer(256);
            sb.append("Poi time : ");
            sb.append(poiLocation.getTime());
            sb.append("\nerror code : ");
            sb.append(poiLocation.getLocType());
            sb.append("\nlatitude : ");
            sb.append(poiLocation.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(poiLocation.getLongitude());
            sb.append("\nradius : ");
            sb.append(poiLocation.getRadius());
            if (poiLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
                sb.append("\naddr : ");
                sb.append(poiLocation.getAddrStr());
            }
            if (poiLocation.hasPoi()) {
                sb.append("\nPoi:");
                sb.append(poiLocation.getPoi());
            } else {
                sb.append("noPoi information");
            }
            CommonView.displayLong(instance, sb.toString());
        }
    }/********************************Listener.end*************************************/
}




