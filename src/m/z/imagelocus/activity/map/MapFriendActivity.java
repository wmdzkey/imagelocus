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
import m.z.imagelocus.config.SystemAdapter;
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
@EActivity(R.layout.activity_map_friend)
public class MapFriendActivity extends Activity{

    public static MapFriendActivity instance = null;
    private LayoutInflater inflater;

    @ViewById(R.id.tv_middle)
    TextView tv_middle;
    //右上角第一个按钮
    @ViewById(R.id.btn_right)
    Button btn_right;

    List<Lbs> lbsList = null;    //位置数据
    List<LocationOverlay> locationOverlayList = null;   //位置图层

    //弹出泡泡图层
    private PopupOverlay pop  = null;     //弹出泡泡图层，浏览节点时使用
    private View popView = null;    //泡泡view

    //地图相关，使用继承MapView的LocationMapView目的是重写touch事件实现泡泡处理
    //如果不处理touch事件，则无需继承，直接使用MapView即可
    @ViewById(R.id.bmap_view)
    LocationMapView mMapView;	    // 地图View
    MapController mMapController = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        inflater = LayoutInflater.from(this);
    }

    @AfterViews
    void init() {

        tv_middle.setText("朋友们的位置");

        //地图初始化
        mMapController = mMapView.getController();
        mMapView.getController().setZoom(14);
        mMapView.getController().enableClick(true);
        mMapView.setBuiltInZoomControls(true);

        //创建弹出泡泡图层
        initPaopao();
        //创建位置数据
        readLbsData();
    }

    /**
     * 创建弹出泡泡图层
     */
    public void initPaopao(){
        popView = getLayoutInflater().inflate(R.layout.impress_paopao_view, null);
        //泡泡点击响应回调
        PopupClickListener popListener = new PopupClickListener(){
            @Override
            public void onClickedPopup(int index) {
                Log.v("click", "clickpaopao");
            }
        };
        pop = new PopupOverlay(mMapView, popListener);
        mMapView.pop = pop;
    }


    /**
     * 创建位置数据
     * */
    private void readLbsData() {

        //lbsDataHistory = Service.lbsService.findByUser_id(user_id);

        //String app_user_id = SystemAdapter.currentUser.getApp_user_id();
        //new LbsYunService(instance, LbsYunService.FunctionName.findLbsByApp_User_id, app_user_id) {
        new LbsYunService(instance, LbsYunService.FunctionName.findAllLbs) {
            @Override
            public void doResult(Map<String, Object> resultMap) {
                CommonView.displayShort(instance, (String) resultMap.get("msg"));
                lbsList = (List<Lbs>) resultMap.get("lbsList");
                setLocationOverlay();
            }
        };
    }

    /**
     * 创建位置图层
     */
    private void setLocationOverlay() {
        if(lbsList != null && lbsList.size() != 0) {
            //清除之前的位置数据
            mMapView.getOverlays().clear();
            locationOverlayList = new ArrayList<LocationOverlay>();

            for(Lbs lbs : lbsList) {
                LocationOverlay locOverlay = new LocationOverlay(mMapView);
                //设置为0则不显示精度圈
                lbs.setRadius(0);
                locOverlay.setData(lbs);
                locationOverlayList.add(locOverlay);
            }

            mMapView.getOverlays().addAll(locationOverlayList);
        }
        mMapView.refresh();

    }

    //继承MyLocationOverlay重写dispatchTap实现点击处理
    public class LocationOverlay extends MyLocationOverlay{

        public Lbs lbs = null;

        public LocationOverlay(MapView mapView) {
            super(mapView);
        }
        @Override
        protected boolean dispatchTap() {

            TextView tv_pop_locinfo = (TextView) popView.findViewById(R.id.tv_location_info);
            TextView tv_pop_loctime = (TextView) popView.findViewById(R.id.tv_location_time);

            if(lbs != null) {
                tv_pop_locinfo.setText(lbs.getAddrStr());
                tv_pop_loctime.setText(CalendarUtil.showSimpleTime(lbs.getCreateTime(), "MM月dd日 hh:mm"));
            } else {
                tv_pop_locinfo.setText("我的位置");
                tv_pop_loctime.setText("暂无记录");

            }
            //处理点击事件,弹出泡泡
            pop.showPopup(ImageUtil.getBitmapFromView(popView),
                    new GeoPoint((int)(lbs.getLatitude()*1e6), (int)(lbs.getLongitude()*1e6)),
                    58);
            CommonView.displayLong(instance,  (int)(lbs.getLatitude()*1e6) + " , " +  (int)(lbs.getLongitude()*1e6));
            return true;
        }

        public void setData(Lbs lbs) {
            this.lbs = lbs;
            super.setData(LbsConvert.Lbs2LocationData(lbs));
            drawOverlayPoint();
        }

        /**
         *构建位置图层定位点图标
         * */
        public void drawOverlayPoint() {

            //String app_user_id = SystemAdapter.currentUser.getApp_user_id();

            //构建用户头像缩小图片变成圆角
            Drawable drawable_head = getResources().getDrawable(R.drawable.default_avatar_winnid);
            Bitmap bitmap_head = ImageUtil.drawableToBitmap(drawable_head);
            bitmap_head = ImageUtil.getRoundedCornerBitmap(bitmap_head, 90);
            drawable_head = ImageUtil.bitmapToDrawable(bitmap_head);

            //创建标记点View
            View convertView = inflater.inflate(R.layout.impress_paopao_point, null);
            TextView tv_name = (TextView)convertView.findViewById(R.id.tv_name);
            ImageView iv_img = (ImageView)convertView.findViewById(R.id.iv_img);
            tv_name.setText("Winnid");
            iv_img.setImageDrawable(drawable_head);
            Bitmap bmp = ImageUtil.getBitmapFromView(convertView);
            Drawable dw = ImageUtil.bitmapToDrawable(bmp);

            //当传入marker为null时，使用默认图标绘制
            //this.setMarker(dw);
            this.setMarker(null);
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




