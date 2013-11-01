package m.z.imagelocus.activity.map;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.map.MyLocationOverlay.LocationMode;
import com.baidu.mapapi.search.*;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.google.gson.Gson;
import com.googlecode.androidannotations.annotations.*;
import m.z.common.CommonView;
import m.z.imagelocus.R;
import m.z.imagelocus.application.MapInitApplication;
import m.z.imagelocus.config.SystemAdapter;
import m.z.imagelocus.entity.Lbs;
import m.z.imagelocus.entity.convert.LbsConvert;
import m.z.imagelocus.entity.convert.LbsYunConvert;
import m.z.imagelocus.entity.yun.LbsDoPoiYun;
import m.z.imagelocus.entity.yun.PoiList;
import m.z.imagelocus.service.http.LbsYunService;
import m.z.imagelocus.service.http.SearchPoiService;
import m.z.imagelocus.view.map.LocationMapView;
import m.z.imagelocus.view.map.LocationOverlay;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoTitle
@EActivity(R.layout.activity_map_periphery)
public class MapPeripheryActivity extends Activity{

    public static MapPeripheryActivity instance = null;

    @ViewById(R.id.tv_middle)
    TextView tv_middle;
    //右上角第一个按钮
    @ViewById(R.id.btn_right)
    Button btn_right;

    @ViewById(R.id.btn_poi)
    Button btn_poi;
    @ViewById(R.id.ll_poi_category)
    LinearLayout ll_poi_category;

    @ViewById(R.id.ll_category_food)
    LinearLayout ll_category_food;
    @ViewById(R.id.ll_category_hostel)
    LinearLayout ll_category_hostel;
    @ViewById(R.id.ll_category_ktv)
    LinearLayout ll_category_ktv;
    @ViewById(R.id.ll_category_leisure)
    LinearLayout ll_category_leisure;


    @ViewById(R.id.et_poi_search)
    EditText et_poi_search;
    @ViewById(R.id.btn_poi_search)
    Button btn_poi_search;

    @ViewById(R.id.sb_poi_distance)
    SeekBar sb_poi_distance;
    @ViewById(R.id.tv_category_range)
    TextView tv_category_range;

    //存储获得的分类信息
    Map<Integer, List<Lbs>> peripheryLbsMap  = new HashMap<Integer, List<Lbs>>();;

    //地图相关，使用继承MapView的LocationMapView目的是重写touch事件实现泡泡处理
    //如果不处理touch事件，则无需继承，直接使用MapView即可
    @ViewById(R.id.bmap_view)
    LocationMapView mMapView;	    // 地图View
    MapController mMapController = null;

    // 定位相关
    LocationClient locClient;
    LocationListener locListener = new LocationListener();
    LocationOverlay locOverlay = null;      // 定位图层
    Lbs lbsDataNow = null;    //定位数据

    //搜索服务
    public MKSearch mMKSearch = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
    }

    @AfterViews
    void init() {

        tv_middle.setText("周边生活");

        //地图初始化
        mMapController = mMapView.getController();
        mMapView.getController().setZoom(14);
        mMapView.getController().enableClick(true);
        mMapView.setBuiltInZoomControls(true);

        //定位初始化
        initLocation();

        //搜索初始化
        mMKSearch = new MKSearch();
        mMKSearch.init(MapInitApplication.getInstance().mBMapManager, new MapSearchListener());//注意，MKSearchListener只支持一个，以最后一次设置为准

        //seekbar初始化
        sb_poi_distance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv_category_range.setText("当前范围：" + ((progress+1)*100) + "米");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    /**
     *初始化位置
     */
    private void initLocation() {

        locClient = new LocationClient( this );
        locClient.registerLocationListener( locListener );
        LocationClientOption option = new LocationClientOption();
        option.setAddrType("all");//返回的定位结果包含地址信息
        option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(999);//设置发起定位请求的间隔时间为10000ms
        option.disableCache(false);//禁止启用缓存定位

        //网络查询类型
        option.setOpenGps(true);//打开gps
        option.setPriority(LocationClientOption.GpsFirst);

        //poi参数设置
        option.setPoiNumber(1000);    //最多返回POI个数
        option.setPoiDistance(500); //poi查询距离
        option.setPoiExtraInfo(true); //是否需要POI的电话和地址等详细信息

        locClient.setLocOption(option);
        locClient.start();

        //定位图层初始化
        locOverlay = new LocationOverlay(mMapView, mMapView.pop);
        //添加定位图层
        mMapView.getOverlays().add(locOverlay);
        //修改定位数据后刷新图层生效
        mMapView.refresh();
    }




    /**
     * 手动触发一次定位请求
     */
    public void requestLocClick(){
        locClient.requestLocation();
        CommonView.displayLong(instance, "正在定位……");
    }

    /**
     * 手动触发一次Poi请求
     */
    public void requestPoiClick(){
        locClient.requestPoi();
        CommonView.displayLong(instance, "正在搜寻……");
    }

    @Click(R.id.btn_right)
    void btn_right_onClick() {
    }

    @Click(R.id.btn_locate)
    void btn_locate_onClick() {
        requestLocClick();
    }

    boolean poiOpenState = true;
    @Click(R.id.btn_poi)
    void btn_poi_onClick() {
        if(!poiOpenState) {
            poiOpenState = true;
            btn_poi.setBackgroundResource(R.drawable.poi_smallest);
            ll_poi_category.setVisibility(View.VISIBLE);
        } else {
            smallestPoi();
        }
    }

    void smallestPoi() {
        poiOpenState = false;
        btn_poi.setBackgroundResource(R.drawable.poi_near);
        ll_poi_category.setVisibility(View.GONE);
    }

    @Click(R.id.ll_category_food)
    void btn_category_food_onClick() {
        search("餐厅", (sb_poi_distance.getProgress() + 1) * 100);
    }

    @Click(R.id.ll_category_hostel)
    void btn_category_hostel_onClick() {
        search("酒店", (sb_poi_distance.getProgress() + 1) * 100);
    }

    @Click(R.id.ll_category_ktv)
    void btn_category_ktv_onClick() {
        search("KTV", (sb_poi_distance.getProgress() + 1) * 100);
    }

    @Click(R.id.ll_category_leisure)
     void btn_category_leisure_onClick() {
        search("电影院", (sb_poi_distance.getProgress() + 1) * 100);
    }

    @Click(R.id.btn_poi_search)
    void btn_poi_search_onClick() {
        if(et_poi_search.getText() != null && !et_poi_search.getText().toString().equals("")) {
            search(et_poi_search.getText().toString(), (sb_poi_distance.getProgress() + 1) * 100);
        } else {
            CommonView.displayGravity(instance, "写点什么在搜吧~");
        }
    }


    @UiThread
    void search(String keyword, Integer distance) {
        if(lbsDataNow != null) {
            mMKSearch.poiSearchNearBy(keyword, lbsDataNow.getGeoPoint(), distance);//1000米       范围

            sendSearchInfo(lbsDataNow, keyword, distance);

        } else {
            locClient.requestLocation();
            CommonView.displayLong(instance, "正在定位……");
        }
    }

    void sendSearchInfo(Lbs lbs, String keyword, Integer distance) {

        LbsDoPoiYun lbsDoPoiYun = LbsYunConvert.Lbs2LbsDoPoiYun(lbsDataNow);
        lbsDoPoiYun.setKeyword(keyword);
        lbsDoPoiYun.setDistance(distance);

        new SearchPoiService(instance, SearchPoiService.FunctionName.sendBaiduServer, lbsDoPoiYun) {
            @Override
            public void doResult(Map<String, Object> resultMap) {
                CommonView.displayShort(instance, (String) resultMap.get("msg"));
            }
        };
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
            sb.append("\nerror code (LocType) : ");
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
                PoiList poiList = SystemAdapter.gson.fromJson(poiLocation.getPoi(), PoiList.class);
                sb.append("\nPoi:");
                sb.append(poiList.getP().length);
            } else {
                sb.append("noPoi information");
            }

            CommonView.displayLong(instance, poiLocation.getPoi());
        }
    }
    public class MapSearchListener implements MKSearchListener {
        @Override
        public void onGetAddrResult(MKAddrInfo result, int iError) {
            //返回地址信息搜索结果
        }
        @Override
        public void onGetDrivingRouteResult(MKDrivingRouteResult result, int iError) {
            //返回驾乘路线搜索结果
        }
        @Override
        public void onGetPoiResult(MKPoiResult result, int type, int iError) {
            //返回poi搜索结果
            // 错误号可参考MKEvent中的定义
            if ( iError == MKEvent.ERROR_RESULT_NOT_FOUND){
                CommonView.displayLong(instance, "抱歉，未找到结果");
                return ;
            }
            else if (iError != 0 || result == null) {
                CommonView.displayLong(instance, "搜索出错了..");
                return;
            }

            //缩小搜索框
            smallestPoi();

            mMapView.removeAllMarker();
            mMapView.addLocationMarker(lbsDataNow);

            // 将poi结果显示到地图上
            PoiOverlay poiOverlay = new PoiOverlay(instance, mMapView);
            poiOverlay.setData(result.getAllPoi());
            mMapView.getOverlays().add(poiOverlay);
            mMapView.refresh();
            //当ePoiType为2（公交线路）或4（地铁线路）时， poi坐标为空
            for(MKPoiInfo info : result.getAllPoi() ){
                if ( info.pt != null ){
                    mMapView.getController().animateTo(info.pt);
                    break;
                }
            }
            if (result.getAllPoi().get(0).hasCaterDetails == true){
                mMKSearch.poiDetailSearch(result.getAllPoi().get(0).uid);
                // 其中，poi 对象可由常规poi检索返回
            }
        }
        @Override
        public void onGetTransitRouteResult(MKTransitRouteResult result, int iError) {
            //返回公交搜索结果
        }
        @Override
        public void onGetWalkingRouteResult(MKWalkingRouteResult result, int iError) {
            //返回步行路线搜索结果
        }
        @Override
        public void onGetBusDetailResult(MKBusLineResult result, int iError) {
            //返回公交车详情信息搜索结果
        }
        @Override
        public void onGetSuggestionResult(MKSuggestionResult result, int iError) {
            //返回联想词信息搜索结果
        }

        @Override
        public void onGetPoiDetailSearchResult(int type, int iError) {
            //返回poi详细信息搜索的结果
        }

        @Override
        public void onGetShareUrlResult(MKShareUrlResult result , int type, int error) {
            //在此处理短串请求返回结果.
        }
    }
    /********************************Listener.end*************************************/
}




