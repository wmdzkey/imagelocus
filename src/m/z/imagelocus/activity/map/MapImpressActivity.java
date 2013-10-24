package m.z.imagelocus.activity.map;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.map.MyLocationOverlay.LocationMode;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.googlecode.androidannotations.annotations.*;
import m.z.common.CommonView;
import m.z.imagelocus.R;
import m.z.imagelocus.entity.Lbs;
import m.z.imagelocus.entity.convert.LbsConvert;
import m.z.imagelocus.service.Service;
import m.z.imagelocus.service.http.LbsYunService;
import m.z.imagelocus.view.map.LocationMapView;
import m.z.util.CalendarUtil;
import m.z.util.ImageUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@NoTitle
@EActivity(R.layout.activity_map_impress)
public class MapImpressActivity extends Activity{


    private enum E_BUTTON_TYPE {
        LOC,
        COMPASS,
        FOLLOW
    }

    private E_BUTTON_TYPE mCurBtnType;


    public static MapImpressActivity instance = null;
    private LayoutInflater inflater;

    @ViewById(R.id.tv_middle)
    TextView tv_middle;
    //右上角第一个按钮
    @ViewById(R.id.btn_right)
    Button btn_right;
    //历史路径上一步下一步
    @ViewById(R.id.btn_history_before)
    Button btn_history_before;
    @ViewById(R.id.btn_history_next)
    Button btn_history_next;

    // 定位相关
    LocationClient locClient;
    LocationListener locListener = new LocationListener();

    LocationData locDataNow = null;    //定位数据
    Lbs lbsDataNow = null;    //定位数据
    List<Lbs> lbsDataHistory = null;    //历史定位数据
    int historyPoint = 0;    //历史定位数据

    LocationOverlay locOverlay = null;      // 定位图层

    //弹出泡泡图层
    private PopupOverlay pop  = null;     //弹出泡泡图层，浏览节点时使用
    private View popView = null;    //泡泡view

    //地图相关，使用继承MapView的LocationMapView目的是重写touch事件实现泡泡处理
    //如果不处理touch事件，则无需继承，直接使用MapView即可
    @ViewById(R.id.bmap_view)
    LocationMapView mMapView;	    // 地图View
    MapController mMapController = null;

    //UI相关
    @ViewById(R.id.btn_locate)
    Button btn_locate;
    boolean isRequest = false;//是否手动触发请求定位
    boolean isFirstLoc = true;//是否首次定位

    //测试新功能;
    @ViewById(R.id.btn_test)
    Button btn_test;
    @Click(R.id.btn_test)
    void btn_test_onClick() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        inflater = LayoutInflater.from(this);
    }

    @AfterViews
    void init() {
        mCurBtnType = E_BUTTON_TYPE.LOC;

        tv_middle.setText("我的印迹");
        btn_history_before.setVisibility(View.GONE);
        btn_history_next.setVisibility(View.GONE);

        //地图初始化
        mMapController = mMapView.getController();
        mMapView.getController().setZoom(14);
        mMapView.getController().enableClick(true);
        mMapView.setBuiltInZoomControls(true);

        //创建 弹出泡泡图层
        createPaopao();

        //定位初始化
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

        locDataNow = new LocationData();
        lbsDataHistory = new ArrayList<Lbs>();

        //定位图层初始化
        locOverlay = new LocationOverlay(mMapView);
        //设置定位数据
        locOverlay.setData(locDataNow);
        //添加定位图层
        mMapView.getOverlays().add(locOverlay);
        locOverlay.enableCompass();
        //修改定位数据后刷新图层生效
        mMapView.refresh();

        Drawable drawable = getResources().getDrawable(R.drawable.default_avatar_winnid);
        Bitmap bitmap = ImageUtil.drawableToBitmap(drawable);
        bitmap = ImageUtil.getRoundedCornerBitmap(bitmap, 90);
        drawable = ImageUtil.bitmapToDrawable(bitmap);

        View convertView = inflater.inflate(R.layout.impress_paopao_point, null);
        TextView tv_name = (TextView)convertView.findViewById(R.id.tv_name);
        ImageView iv_img = (ImageView)convertView.findViewById(R.id.iv_img);
        tv_name.setText("Winnid");
        iv_img.setImageDrawable(drawable);
        modifyLocationOverlayIcon(ImageUtil.getBitmapFromView(convertView));
    }

    @Click(R.id.btn_locate)
    void btn_locate_onClick() {
        switch (mCurBtnType) {
            case LOC:
                //手动定位请求
                requestLocClick();
                break;
            case COMPASS:
                locOverlay.setLocationMode(LocationMode.NORMAL);
                btn_locate.setText("定位");
                mCurBtnType = E_BUTTON_TYPE.LOC;
                break;
            case FOLLOW:
                locOverlay.setLocationMode(LocationMode.COMPASS);
                btn_locate.setText("罗盘");
                mCurBtnType = E_BUTTON_TYPE.COMPASS;
                break;
        }
    }

    /**
     * 手动触发一次定位请求
     */
    public void requestLocClick(){
        isRequest = true;
        locClient.requestLocation();
        CommonView.displayLong(instance, "正在定位……");
    }
    /**
     * 修改位置图标
     * @param bmp
     */
    public void modifyLocationOverlayIcon(Bitmap bmp){
        //当传入marker为null时，使用默认图标绘制
        locOverlay.setMarker(ImageUtil.bitmapToDrawable(bmp));
        //修改图层，需要刷新MapView生效
        mMapView.refresh();
    }
    /**
     * 创建弹出泡泡图层
     */
    public void createPaopao(){
        popView = getLayoutInflater().inflate(R.layout.impress_paopao_view, null);
        //泡泡点击响应回调
        PopupClickListener popListener = new PopupClickListener(){
            @Override
            public void onClickedPopup(int index) {
                Log.v("click", "clickpaopao");
            }
        };
        pop = new PopupOverlay(mMapView,popListener);
        mMapView.pop = pop;
    }


    /*****************************************************************************/
    /********************************Listener*************************************/
    /*****************************************************************************/

    /**
     * 定位SDK监听函数
     */
    public class LocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation == null)
                return ;
            //保存定位数据
            saveLocData(bdLocation);

            //更新定位数据
            locOverlay.setData(lbsDataNow);
            //更新图层数据执行刷新后生效
            mMapView.refresh();
            //是手动触发请求或首次定位时，移动到定位点
            if (isRequest || isFirstLoc){
                //移动地图到定位点
                Log.d("LocationOverlay", "receive location, animate to it");
                mMapController.animateTo(new GeoPoint((int)(lbsDataNow.getLatitude()* 1e6), (int)(lbsDataNow.getLongitude() *  1e6)));
                isRequest = false;

                locOverlay.setLocationMode(LocationMode.FOLLOWING);
                btn_locate.setText("跟随");
                mCurBtnType = E_BUTTON_TYPE.FOLLOW;
            }
            //首次定位完成
            isFirstLoc = false;
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
    }

    @Click(R.id.btn_right)
    void btn_right_onClick() {
        //读取历史记录
        readLocData("843804516070431639");
        //绘制历史信息
        if(lbsDataHistory != null && lbsDataHistory.size() != 0) {

            //标记点
            for (Lbs lbs : lbsDataHistory) {
                LocationOverlay locOverlay = new LocationOverlay(mMapView);
                //设置为0则不显示精度圈
                lbs.setRadius(0);
                locOverlay.setData(lbs);
                mMapView.getOverlays().add(locOverlay);
            }
            mMapView.refresh();

            btn_history_before.setVisibility(View.VISIBLE);
            btn_history_next.setVisibility(View.VISIBLE);
            historyPoint = lbsDataHistory.size()-1;
        }
    }

    @Click(R.id.btn_history_before)
    void btn_history_before_onClick() {
        //读取上一个坐标点
        //移动
        //pop信息
        if(historyPoint != -1) {
            moveAndPopupHistory(1);
        }

    }

    @Click(R.id.btn_history_next)
    void btn_history_next_onClick() {
        //读取下一个坐标点
        //移动
        //pop信息
        if(historyPoint != -1) {
            moveAndPopupHistory(-1);
        }
    }

    /**
     *移动,pop信息
     * */
    private void moveAndPopupHistory(int type) {
        if(type == 1 && historyPoint > 0) {
            Lbs lbs = lbsDataHistory.get(--historyPoint);
            mMapController.animateTo(new GeoPoint((int)(lbs.getLatitude()* 1e6), (int)(lbs.getLongitude() *  1e6)));
            popLbsInfo(lbs);
        } else if(type == -1 && historyPoint < lbsDataHistory.size()-1) {
            Lbs lbs = lbsDataHistory.get(++historyPoint);
            mMapController.animateTo(new GeoPoint((int)(lbs.getLatitude()* 1e6), (int)(lbs.getLongitude() *  1e6)));
            popLbsInfo(lbs);
        } else {
            CommonView.displayShort(instance, "没有记录了");
        }

    }

    /**
     *弹出pop信息
     * */
    void popLbsInfo(Lbs lbs) {
        TextView tv_pop_locinfo =(TextView) popView.findViewById(R.id.tv_location_info);
        TextView tv_pop_loctime =(TextView) popView.findViewById(R.id.tv_location_time);

        if(lbs != null) {
            tv_pop_locinfo.setText(lbs.getAddrStr());
            tv_pop_loctime.setText(CalendarUtil.showSimpleTime(lbs.getCreateTime()));
        } else {
            tv_pop_locinfo.setText("我的位置");
            tv_pop_loctime.setText("暂无记录");

        }
        //处理点击事件,弹出泡泡
        pop.showPopup(ImageUtil.getBitmapFromView(popView),
                new GeoPoint((int)(lbs.getLatitude()*1e6), (int)(lbs.getLongitude()*1e6)),
                58);
    }

    /**
     * 读取定位数据
     * */
    private void readLocData(String app_user_id) {
        //lbsDataHistory = Service.lbsService.findByUser_id(user_id);
        new LbsYunService(instance, LbsYunService.FunctionName.findLbsByApp_User_id, app_user_id) {
            @Override
            public void doResult(Map<String, Object> resultMap) {
                CommonView.displayShort(instance, (String) resultMap.get("msg"));
                lbsDataHistory = (List<Lbs>) resultMap.get("lbsList");
            }
        };
    }

    /**
     * 保存定位数据
     * */
    private void saveLocData(BDLocation bdLocation) {

        Lbs lbs =  LbsConvert.createLbs("843804516070431639", bdLocation);
        //Lbs lbs =  Service.lbsService.createLbs(SystemAdapter.currentUser.getApp_user_id(), bdLocation);
        lbsDataNow = lbs;
        lbsDataHistory.add(lbs);

        //本地保存
        // Service.lbsService.save(lbs);
        //保存到云端
        new LbsYunService(instance, LbsYunService.FunctionName.sendBaiduServer, lbs) {
            @Override
            public void doResult(Map<String, Object> resultMap) {
                CommonView.displayShort(instance, (String) resultMap.get("msg"));
            }
        };
    }


    //继承MyLocationOverlay重写dispatchTap实现点击处理
    public class LocationOverlay extends MyLocationOverlay{

        public Lbs lbs = null;

        public LocationOverlay(MapView mapView) {
            super(mapView);
        }
        @Override
        protected boolean dispatchTap() {

            TextView tv_pop_locinfo =(TextView) popView.findViewById(R.id.tv_location_info);
            TextView tv_pop_loctime =(TextView) popView.findViewById(R.id.tv_location_time);

            if(lbs != null) {
                tv_pop_locinfo.setText(lbs.getAddrStr());
                tv_pop_loctime.setText(CalendarUtil.showSimpleTime(lbs.getCreateTime()));
            } else {
                tv_pop_locinfo.setText("我的位置");
                tv_pop_loctime.setText("暂无记录");

            }
            //处理点击事件,弹出泡泡
            pop.showPopup(ImageUtil.getBitmapFromView(popView),
                    new GeoPoint((int)(lbs.getLatitude()*1e6), (int)(lbs.getLongitude()*1e6)),
                    58);
            return true;
        }

        public void setData(Lbs lbs) {
            this.lbs = lbs;
            super.setData(LbsConvert.Lbs2LocationData(lbs));
        }

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
        //退出时销毁定位
        if (locClient != null)
            locClient.stop();
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

}




