package m.z.imagelocus.activity.impress;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.baidu.mapapi.map.MapController;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.googlecode.androidannotations.annotations.*;
import m.z.common.CommonView;
import m.z.imagelocus.R;
import m.z.imagelocus.entity.Lbs;
import m.z.imagelocus.service.http.LbsYunService;
import m.z.imagelocus.view.map.LocationMapView;
import m.z.imagelocus.view.map.LocationOverlay;
import m.z.util.CalendarUtil;
import m.z.util.ImageUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@NoTitle
@EActivity(R.layout.activity_impress_history)
public class ImpressHistoryActivity extends Activity{

    public static ImpressHistoryActivity instance = null;
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

    List<Lbs> lbsDataHistory = null;    //历史定位数据
    List<LocationOverlay> locationOverlayList = null;   //位置图层
    int historyPoint = -1;

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
        tv_middle.setText("我的印象");

        //地图初始化
        mMapController = mMapView.getController();
        mMapView.getController().setZoom(14);
        mMapView.getController().enableClick(true);
        mMapView.setBuiltInZoomControls(true);

        //创建位置数据
        readLbsData();

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
                lbsDataHistory = (List<Lbs>) resultMap.get("lbsList");
                setLocationOverlay();
            }
        };
    }

    /**
     * 创建位置图层
     */
    private void setLocationOverlay() {
        if(lbsDataHistory != null && lbsDataHistory.size() != 0) {
            //清除之前的位置数据
            mMapView.getOverlays().clear();
            locationOverlayList = new ArrayList<LocationOverlay>();

            historyPoint = lbsDataHistory.size()-1;

            for(Lbs lbs : lbsDataHistory) {
                LocationOverlay locOverlay = new LocationOverlay(mMapView, mMapView.pop);
                //设置为0则不显示精度圈
                lbs.setRadius(0);
                locOverlay.setData(lbs);
                locationOverlayList.add(locOverlay);
            }

            mMapView.getOverlays().addAll(locationOverlayList);
        }
        mMapView.refresh();

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
        View popView = inflater.inflate(R.layout.view_paopao_map_user, null);
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
        mMapView.pop.showPopup(ImageUtil.getBitmapFromView(popView),
                new GeoPoint((int)(lbs.getLatitude()*1e6), (int)(lbs.getLongitude()*1e6)),
                58);
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




