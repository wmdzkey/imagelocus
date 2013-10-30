package m.z.imagelocus.view.map;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import m.z.common.CommonView;
import m.z.imagelocus.R;
import m.z.imagelocus.entity.Lbs;
import m.z.util.CalendarUtil;
import m.z.util.ImageUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Winnid
 * @Title: 继承MapView重写onTouchEvent实现泡泡处理操作
 * @Description: 继承MapView重写onTouchEvent实现泡泡处理操作
 * @date 13-10-14
 * @Version V1.0
 * Created by Winnid on 13-10-14.
 */
public class LocationMapView extends MapView {

    public Context mContext;

    public LocationMapView(Context context) {
        super(context);
        mContext = context;
    }
    public LocationMapView(Context context, AttributeSet attrs){
        super(context,attrs);
        mContext = context;
    }
    public LocationMapView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        mContext = context;
    }


    /**
     * 移动到指定坐标
     */
    public void movePoint(MapController controller, double latitude, double longitude) {
        controller.animateTo(new GeoPoint((int)(latitude* 1e6), (int)(longitude *  1e6)));
    }


    /**
     * 添加定位标记
     */
    public void addLocationMarker(Lbs lbs) {
        LocationOverlay locOverlay = new LocationOverlay(this, this.pop);
        locOverlay.setData(lbs);
        this.getOverlays().add(locOverlay);
        this.refresh();
    }

    List<LocationOverlay> locationOverlayList = new ArrayList<LocationOverlay>();
    /**
     * 添加所有标记
     */
    public void addAllMarker(List<Lbs> lbsList) {

        if(lbsList != null && lbsList.size() != 0) {
            locationOverlayList = new ArrayList<LocationOverlay>();
            for(Lbs lbs : lbsList) {
                LocationOverlay locOverlay = new LocationOverlay(this, this.pop);
                //设置为0则不显示精度圈
                lbs.setRadius(0);
                locOverlay.setData(lbs);
                locationOverlayList.add(locOverlay);
            }
            this.getOverlays().addAll(locationOverlayList);
        }
        this.refresh();
    }


    /**
     * 删除所有标记
     */
    public void removeAllMarker() {
        this.getOverlays().clear();
        this.refresh();
    }

    public PopupOverlay pop = createPop();
    private PopupOverlay createPop() {
        //泡泡点击响应回调
        PopupClickListener popListener = new PopupClickListener(){
            @Override
            public void onClickedPopup(int index) {
            }
        };
        return new PopupOverlay(this, popListener);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        if (!super.onTouchEvent(event)){
            //消隐泡泡
            if (pop != null && event.getAction() == MotionEvent.ACTION_UP)
                pop.hidePop();
        }
        return true;
    }

}
