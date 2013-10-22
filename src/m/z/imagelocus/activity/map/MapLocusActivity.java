package m.z.imagelocus.activity.map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.baidu.android.pushservice.PushManager;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.search.*;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.google.gson.Gson;
import com.googlecode.androidannotations.annotations.*;
import m.z.common.CommonView;
import m.z.imagelocus.R;
import m.z.imagelocus.config.SystemAdapter;
import m.z.imagelocus.application.MapInitApplication;
import m.z.imagelocus.config.SystemConfig;
import m.z.imagelocus.config.SystemStore;
import m.z.imagelocus.entity.convert.LbsConvert;
import m.z.imagelocus.entity.Lbs;
import m.z.imagelocus.entity.User;
import m.z.imagelocus.service.LbsService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoTitle
@EActivity(R.layout.activity_maplocus)
public class MapLocusActivity extends Activity implements MKMapViewListener, BDLocationListener {


    MapController mMapController = null;
    BMapManager mBMapMan = null;
    Handler handler = new Handler();
    LbsService lbsServive;
    Context mContext;



    @ViewById(R.id.tv_middle)
    TextView tv_middle;
    //右上角第一个按钮
    @ViewById(R.id.btn_right)
    Button btn_right;

    @ViewById(R.id.bmapsView)
    MapView mMapView;//MapView 是地图主控件
    @ViewById(R.id.textView)
    TextView textView;

    //搜索服务
    public MKSearch mMKSearch = null;
    //定位服务
    public LocationClient mLocationClient = null;
    //绘制服务
    public GraphicsOverlay mGraphicsOverlay = null;

    //LBS存储
    List<Lbs> lbsList = new ArrayList<Lbs>();
    //LocationData存储
    List<LocationData> localdataList = new ArrayList<LocationData>();

    //菜单定义
    public static final int MAP_记录我的位置 = 1;
    public static final int MAP_离线记录我的位置 = 2;
    public static final int MAP_绘制我的历史位置 = 3;
    public static final int MAP_查看我的历史位置 = 4;
    public static final int MAP_发送我现在的位置 = 5;
    public static final int MAP_显示密友现在的位置 = 6;
    public static final int MAP_获取密友现在的位置 = 7;
    public static final int MAP_STOP = 10;



    //推送相关
    @ViewById(R.id.spn_push_userlist)
    Spinner spn_push_userlist;
    public static String sendUserId;
    public static List<User> userList = new ArrayList<User>();
    public static ArrayAdapter<User> userAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        MapInitApplication  app = (MapInitApplication)this.getApplication();
        if (app.mBMapManager == null) {
            app.mBMapManager = new BMapManager(this);
            //如果BMapManager没有初始化则初始化BMapManager
            app.mBMapManager.init(MapInitApplication.strKey,new MapInitApplication.BaiduMapGeneralListener());
        }
        mBMapMan = app.mBMapManager;
    }

    @AfterViews
    void init() {
        lbsServive = new LbsService(this);

        //地图基本服务
        mMapView.setBuiltInZoomControls(true);
        //设置启用内置的缩放控件
        MapController mMapController = mMapView.getController();
        //设置地图是否响应点击事件
        mMapController.enableClick(true);
        // 得到mMapView的控制权,可以用它控制和驱动平移和缩放
        GeoPoint point = new GeoPoint((int) (43.761466 * 1E6), (int) (87.618973 * 1E6));
        //用给定的经纬度构造一个GeoPoint，单位是微度 (度 * 1E6)
        mMapController.setCenter(point);//设置地图中心点
        mMapController.setZoom(17);//设置地图zoom级别
        mMapView.regMapViewListener(MapInitApplication.getInstance().mBMapManager, this);

        //定位服务
        mLocationClient = new LocationClient(getApplicationContext()); //声明LocationClient类
        mLocationClient.registerLocationListener(this); //注册监听函数

        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setAddrType("all");//返回的定位结果包含地址信息
        option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(999);//设置发起定位请求的间隔时间为10000ms
        option.disableCache(true);//禁止启用缓存定位
        option.setProdName("ImageLocus");
        option.setPriority(LocationClientOption.GpsFirst);
        option.setPoiNumber(2);    //最多返回POI个数
        option.setPoiDistance(500); //poi查询距离
        option.setPoiExtraInfo(true); //是否需要POI的电话和地址等详细信息
        mLocationClient.setLocOption(option);

        //绘制服务
        mGraphicsOverlay = new GraphicsOverlay(mMapView);
        mMapView.getOverlays().add(mGraphicsOverlay);


        //初始化用户列表
        userList = SystemStore.userData;
        userAdapter = new ArrayAdapter<User>(this, android.R.layout.simple_spinner_item, userList);
        userAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_push_userlist.setAdapter(userAdapter);
        spn_push_userlist.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView arg0, View arg1, int arg2, long arg3) {
                sendUserId = userAdapter.getItem(arg2).getApp_user_id();
                CommonView.displayLong(MapLocusActivity.this, "将要发送给" + userAdapter.getItem(arg2).getUsername());
            }
            public void onNothingSelected(AdapterView arg0) {
                arg0.setVisibility(View.VISIBLE);
            }
        });

        CommonView.displayLong(this, "收到位置了");
        //初始化消息接收后绘制位置
        String localInfo = getIntent().getStringExtra("localInfo");
        if(localInfo != null && !localInfo.trim().equals("")) {
            showReceiveLocalInfo(localInfo);
        }
        //初始化消息接收后绘制位置
        String find = getIntent().getStringExtra("find");
        if(find != null && !find.trim().equals("")) {
            sendMyLocationInfo();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // 如果要统计Push引起的用户使用应用情况，请实现本方法，且加上这一个语句
        setIntent(intent);

        CommonView.displayLong(this, "收到位置了2");
        //初始化消息接收后绘制位置
        String localInfo = intent.getStringExtra("localInfo");
        if(localInfo != null && !localInfo.trim().equals("")) {
            showReceiveLocalInfo(localInfo);
        }
    }

    @Override
    // 建议在APP整体退出之前调用MapApi的destroy()函数，不要在每个activity的OnDestroy中调用，
    // 避免MapApi重复创建初始化，提高效率
    protected void onDestroy() {
        mMapView.destroy();
        MapInitApplication app = (MapInitApplication)this.getApplication();
        if (app.mBMapManager != null) {
            app.mBMapManager.destroy();
            app.mBMapManager = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        MapInitApplication app = (MapInitApplication)this.getApplication();
        if (app.mBMapManager != null) {
            app.mBMapManager = null;
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        MapInitApplication app = (MapInitApplication)this.getApplication();
        if (!app.m_bKeyRight) {
            new AlertDialog.Builder(this)
                    .setTitle("错误")
                    .setMessage("授权Key失效!")
                    .setPositiveButton("确定", null)
                    .show();
        }
        super.onResume();
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {//创建系统功能菜单
        menu.add(0, MAP_记录我的位置, 1, "记录我的位置").setIcon(R.drawable.menu_findkey);
        menu.add(0, MAP_离线记录我的位置, 2, "离线记录我的位置").setIcon(R.drawable.menu_findkey);
        menu.add(0, MAP_绘制我的历史位置, 3, "绘制我的历史位置").setIcon(R.drawable.menu_findkey);
        menu.add(0, MAP_查看我的历史位置, 4, "查看我的历史位置").setIcon(R.drawable.menu_findkey);
        menu.add(0, MAP_发送我现在的位置, 5, "发送我现在的位置给密友").setIcon(R.drawable.menu_findkey);
        menu.add(0, MAP_显示密友现在的位置, 6, "显示密友现在的位置").setIcon(R.drawable.menu_findkey);
        menu.add(0, MAP_获取密友现在的位置, 7, "获取密友现在的位置").setIcon(R.drawable.menu_findkey);
        menu.add(0, MAP_STOP, 10, "停止定位").setIcon(R.drawable.menu_setting);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case MAP_记录我的位置:
                mLocationClient.start();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!mLocationClient.isStarted()) {
                            mLocationClient.start();
                        }
                        if (mLocationClient != null && mLocationClient.isStarted()) {
                            mLocationClient.requestLocation();
                        } else {
                            CommonView.displayLong(MapLocusActivity.this, "locClient is null or not started");
                        }

                        MyLocationOverlay myLocationOverlay = new MyLocationOverlay(mMapView);
                        //手动将位置源置为天安门，在实际应用中，请使用百度定位SDK获取位置信息，要在SDK中显示一个位置，需要使用百度经纬度坐标（bd09ll）
                        if(lbsList.size() == 0) return;
                        LocationData locData = LbsConvert.Lbs2LocationData(lbsList.get(lbsList.size() - 1));
                        myLocationOverlay.setData(locData);
                        mMapView.getOverlays().add(myLocationOverlay);
                        mMapView.refresh();
                        mMapView.getController().animateTo(new GeoPoint((int) (locData.latitude * 1e6),
                                (int) (locData.longitude * 1e6)));
                        mLocationClient.stop();

                    }
                },1000);
                break;
            case MAP_离线记录我的位置:
                mLocationClient.start();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!mLocationClient.isStarted()) {
                            mLocationClient.start();
                        }
                        if (mLocationClient != null && mLocationClient.isStarted()) {
                            mLocationClient.requestOfflineLocation();
                        } else {
                            CommonView.displayLong(MapLocusActivity.this, "locClient is null or not started");
                        }

                        MyLocationOverlay myLocationOverlay = new MyLocationOverlay(mMapView);
                        //手动将位置源置为天安门，在实际应用中，请使用百度定位SDK获取位置信息，要在SDK中显示一个位置，需要使用百度经纬度坐标（bd09ll）
                        if (lbsList.size() == 0) return;
                        LocationData locData = LbsConvert.Lbs2LocationData(lbsList.get(lbsList.size() - 1));
                        myLocationOverlay.setData(locData);
                        mMapView.getOverlays().add(myLocationOverlay);
                        mMapView.refresh();
                        mMapView.getController().animateTo(new GeoPoint((int) (locData.latitude * 1e6),
                                (int) (locData.longitude * 1e6)));
                        mLocationClient.stop();

                    }
                }, 1000);
                break;
            case MAP_绘制我的历史位置:
                List<Lbs> lbsHistoryList = lbsServive.findByUser_id(SystemAdapter.currentUser.getId());
                if(lbsHistoryList != null && lbsHistoryList.size() != 0) {

                    //标记点，连接点
                    for (Lbs lbs : lbsHistoryList) {
                        MyLocationOverlay myLocationOverlay = new MyLocationOverlay(mMapView);
                        LocationData locData = LbsConvert.Lbs2LocationData(lbs);
                        myLocationOverlay.setData(locData);
                        mMapView.getOverlays().add(myLocationOverlay);
                        mMapView.getController().animateTo(new GeoPoint((int) (locData.latitude * 1e6),
                                (int) (locData.longitude * 1e6)));
                    }
                    //添加线
                    GraphicsOverlay graphicsOverlay = new GraphicsOverlay(mMapView);
                    mMapView.getOverlays().add(graphicsOverlay);
                    graphicsOverlay.setData(drawLine(lbsHistoryList));
                    mMapView.refresh();
                } else {
                    CommonView.displayShort(this, "暂无历史记录");
                }
                break;
            case MAP_查看我的历史位置:
                Intent intentLbsHistory = new Intent(MapLocusActivity.this, LbsHistoryActivity_.class);
                startActivity(intentLbsHistory);
                break;
            case MAP_发送我现在的位置:
                if(lbsList.size() == 0) {
                    CommonView.displayShort(this, "暂无历史记录,请先定位");
                } else {
                    LocationData locData = LbsConvert.Lbs2LocationData(lbsList.get(lbsList.size() - 1));
                    sendMyLocationInfo(locData);
                }
                break;
            case MAP_显示密友现在的位置:
                //标记点
                MyLocationOverlay myLocationOverlay = new MyLocationOverlay(mMapView);
                LocationData locData = localdataList.get(0);
                myLocationOverlay.setData(locData);
                mMapView.getOverlays().add(myLocationOverlay);
                mMapView.getController().animateTo(new GeoPoint((int) (locData.latitude * 1e6),
                        (int) (locData.longitude * 1e6)));
                mMapView.refresh();
                break;
            case MAP_获取密友现在的位置:
                findFriendLocationInfo();
                break;
            case MAP_STOP:
                mLocationClient.stop();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 定位接口    start
     * ***/
    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        if (bdLocation == null)
            return;
       // CommonView.displayLong(this,  Lbs.getString(bdLocation));
//        Lbs lbs = new Lbs(SystemAdapter.currentUser.getId(), bdLocation);
//        lbsServive.save(lbs);
//        lbsList.add(lbs);
        //textView.setText( Lbs.getString(bdLocation));
    }

    @Override
    public void onReceivePoi(BDLocation poiLocation) {
        if (poiLocation == null) {
            return;
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
        CommonView.displayLong(this, sb.toString());
    }
    /**
     * 定位接口    end
     * ***/


    /**
     * 绘制折线，该折线状态随地图状态变化
     * @return 折线对象
     */
    public Graphic drawLine(List<Lbs> lbsList) {
        if(lbsList == null || lbsList.size() == 0) return null;
        GeoPoint[] geoPoints = new GeoPoint[lbsList.size()];
        int i=0;
        for (Lbs lbs : lbsList) {
            //构建线
            double mLat = lbs.getLatitude();
            double mLon = lbs.getLongitude();
            int lat = (int) (mLat*1E6);
            int lon = (int) (mLon*1E6);
            GeoPoint pt = new GeoPoint(lat, lon);
            geoPoints[i] = pt;
            i++;
        }
        return drawLine(geoPoints);
    }
    /**
     * 绘制折线，该折线状态随地图状态变化
     *
     * @return 折线对象
     */
    public Graphic drawLine(GeoPoint[] geoPoints) {
        //构建线
        Geometry lineGeometry = new Geometry();
        lineGeometry.setPolyLine(geoPoints);
        //设定样式
        Symbol lineSymbol = new Symbol();
        Symbol.Color lineColor = lineSymbol.new Color();
        lineColor.red = 255;
        lineColor.green = 0;
        lineColor.blue = 0;
        lineColor.alpha = 255;
        lineSymbol.setLineSymbol(lineColor, 10);
        //生成Graphic对象
        Graphic lineGraphic = new Graphic(lineGeometry, lineSymbol);
        return lineGraphic;
    }


    /**
     *地图接口 ，start
     * */
    @Override
    public void onMapMoveFinish() {
        /**
         * 在此处理地图移动完成回调
         * 缩放，平移等操作完成后，此回调被触发
         */
    }

    @Override
    public void onClickMapPoi(MapPoi mapPoiInfo) {
        /**
         * 在此处理底图poi点击事件
         * 显示底图poi名称并移动至该点
         * 设置过： mMapController.enableClick(true); 时，此回调才能被触发
         *
         */
        String title = "";
        if (mapPoiInfo != null){
            title = mapPoiInfo.strText;
            CommonView.displayShort(this, title);
            mMapController.animateTo(mapPoiInfo.geoPt);
        }
    }

    @Override
    public void onGetCurrentMap(Bitmap b) {
        /**
         *  当调用过 mMapView.getCurrentMap()后，此回调会被触发
         *  可在此保存截图至存储设备
         */
    }

    @Override
    public void onMapAnimationFinish() {
        /**
         *  地图完成带动画的操作（如: animationTo()）后，此回调被触发
         */
    }
    /**
     * 在此处理地图载完成事件
     */
    @Override
    public void onMapLoadFinish() {
        CommonView.displayShort(this, "地图加载完成");

    }
    /**
     *地图接口 ，end
     * */



    private void sendMyLocationInfo(LocationData locData) {

        Map<String, String> jsonMap = new HashMap<String, String>();

        Gson gson = new Gson();
        String localInfo = gson.toJson(locData);
        jsonMap.put("localInfo", localInfo);
        String msg = gson.toJson(jsonMap);

        //sendMsgToUser(android.content.Context context,
        //java.lang.String app_id,
        //java.lang.String user_id,
        //java.lang.String base64MsgKey,
        //java.lang.String base64Msg)
        String user_id = sendUserId;
        String base64MsgKey = SystemAdapter.currentUser.getApp_user_id();//最好设置成发送者id
        String base64Msg = msg;
        PushManager.sendMsgToUser(this, SystemConfig.BDAppID, user_id, base64MsgKey, base64Msg);
        CommonView.displayLong(this, "发送完成" + SystemAdapter.currentUser.getUsername());
    }

    private void showReceiveLocalInfo(String localInfo) {
        Gson gson = new Gson();
        LocationData ld = gson.fromJson(localInfo, LocationData.class);
        if(ld == null) {
            CommonView.displayLong(this, "转换失败");
        } else{
            CommonView.displayLong(this, "转换成功" );
            //记录点
            localdataList.add(ld);
            CommonView.displayLong(this, "密友发来了他的位置");
        }
    }

    private void findFriendLocationInfo() {

        Map<String, String> jsonMap = new HashMap<String, String>();
        Gson gson = new Gson();
        jsonMap.put("find", "find");
        String msg = gson.toJson(jsonMap);

        //sendMsgToUser(android.content.Context context,
        //java.lang.String app_id,
        //java.lang.String user_id,
        //java.lang.String base64MsgKey,
        //java.lang.String base64Msg)
        String user_id = sendUserId;
        String base64MsgKey = SystemAdapter.currentUser.getApp_user_id();//最好设置成发送者id
        String base64Msg = msg;
        PushManager.sendMsgToUser(this, SystemConfig.BDAppID, user_id, base64MsgKey, base64Msg);
    }


    private void sendMyLocationInfo() {
        //定位然后，发送我的位置
        mLocationClient.start();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!mLocationClient.isStarted()) {
                    mLocationClient.start();
                }
                if (mLocationClient != null && mLocationClient.isStarted()) {
                    mLocationClient.requestLocation();
                } else {
                    CommonView.displayLong(MapLocusActivity.this, "locClient is null or not started");
                }

                MyLocationOverlay myLocationOverlay = new MyLocationOverlay(mMapView);
                //手动将位置源置为天安门，在实际应用中，请使用百度定位SDK获取位置信息，要在SDK中显示一个位置，需要使用百度经纬度坐标（bd09ll）
                if(lbsList.size() == 0) return;
                LocationData locData = LbsConvert.Lbs2LocationData(lbsList.get(lbsList.size() - 1));
                myLocationOverlay.setData(locData);
                mMapView.getOverlays().add(myLocationOverlay);
                mMapView.refresh();
                mMapView.getController().animateTo(new GeoPoint((int) (locData.latitude * 1e6),
                        (int) (locData.longitude * 1e6)));
                mLocationClient.stop();
                if(lbsList.size() == 0) {
                    CommonView.displayShort(MapLocusActivity.this, "暂无历史记录,请先定位");
                } else {
                    sendMyLocationInfo(locData);
                }
            }
        },10000);
    }


    @Click(R.id.btn_right)
    void btn_right_onClick() {
        mLocationClient.start();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!mLocationClient.isStarted()) {
                    mLocationClient.start();
                }
                if (mLocationClient != null && mLocationClient.isStarted()) {
                    mLocationClient.requestLocation();
                } else {
                    CommonView.displayLong(MapLocusActivity.this, "locClient is null or not started");
                }

                MyLocationOverlay myLocationOverlay = new MyLocationOverlay(mMapView);
                //手动将位置源置为天安门，在实际应用中，请使用百度定位SDK获取位置信息，要在SDK中显示一个位置，需要使用百度经纬度坐标（bd09ll）
                if(lbsList.size() == 0) return;
                LocationData locData = LbsConvert.Lbs2LocationData(lbsList.get(lbsList.size() - 1));
                myLocationOverlay.setData(locData);
                mMapView.getOverlays().add(myLocationOverlay);
                mMapView.refresh();
                mMapView.getController().animateTo(new GeoPoint((int) (locData.latitude * 1e6),
                        (int) (locData.longitude * 1e6)));
                mLocationClient.stop();

            }
        },1000);
    }
}