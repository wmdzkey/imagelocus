package m.z.imagelocus.view.map;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import m.z.common.CommonView;
import m.z.imagelocus.R;
import m.z.imagelocus.config.SystemAdapter;
import m.z.imagelocus.entity.Lbs;
import m.z.imagelocus.entity.User;
import m.z.imagelocus.entity.convert.LbsConvert;
import m.z.imagelocus.entity.convert.LbsYunConvert;
import m.z.imagelocus.entity.convert.UserConvert;
import m.z.imagelocus.entity.yun.LbsDoPoiYun;
import m.z.imagelocus.service.Service;
import m.z.util.CalendarUtil;
import m.z.util.ImageUtil;

import java.util.Date;

/**
 * 继承MyLocationOverlay重写dispatchTap实现点击处理
 * Created by Winnid on 13-10-23.
 */
public class LocationOverlay extends MyLocationOverlay {

    public Lbs lbs = null;
    public LbsDoPoiYun lbsDoPoiYun = null;

    private PopupOverlay pop  = null;     //弹出泡泡图层，浏览节点时使用
    private View popView = null;     //泡泡图层样式

    public MapView mapView;
    private LayoutInflater inflater;
    private boolean useNative;
    private int popViewType = 0;
    public LocationOverlay(MapView mapView, PopupOverlay pop) {
        super(mapView);
        this.inflater = LayoutInflater.from(mapView.getContext());
        this.mapView = mapView;
        this.pop = pop;
        this.popView = createPopView();
        this.useNative = true;
    }

    public LocationOverlay(MapView mapView, PopupOverlay pop, boolean useNative) {
        super(mapView);
        this.inflater = LayoutInflater.from(mapView.getContext());
        this.mapView = mapView;
        this.pop = pop;
        this.popView = createPopView();
        this.useNative = useNative;
    }

    public LocationOverlay(MapView mapView, PopupOverlay pop, boolean useNative, int popViewType) {
        super(mapView);
        this.inflater = LayoutInflater.from(mapView.getContext());
        this.mapView = mapView;
        this.pop = pop;
        this.popView = createPopView();
        this.useNative = true;
        this.popViewType = popViewType;
    }

    private View createPopView() {
        if(popViewType == 0) {
            return inflater.inflate(R.layout.view_paopao_map_user, null);
        } else if(popViewType == 1) {
            return inflater.inflate(R.layout.view_paopao_map_keyword, null);
        } else {
            return inflater.inflate(R.layout.view_paopao_map_user, null);
        }
    }

    /**
     * 点击后弹出显示
     * */
    @Override
    protected boolean dispatchTap() {
        if(popViewType == 0) {
            showPopView0();
        } else if(popViewType == 1) {
            showPopView1();
        } else {
            showPopView0();
        }
        return true;
    }

    /**
     *显示popview
     * */
    private void showPopView0() {
        TextView tv_pop_locinfo = (TextView) popView.findViewById(R.id.tv_location_info);
        TextView tv_pop_loctime = (TextView) popView.findViewById(R.id.tv_location_time);

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
        CommonView.displayLong(mapView.getContext(), (int) (lbs.getLatitude() * 1e6) + " , " + (int) (lbs.getLongitude() * 1e6));
    }
    /**
     *显示popview
     * */
    private void showPopView1() {
        TextView tv_pop_locinfo = (TextView) popView.findViewById(R.id.tv_location_info);
        TextView tv_pop_loctime = (TextView) popView.findViewById(R.id.tv_location_time);

        if(lbs != null) {
            tv_pop_locinfo.setText("搜 \"" + lbsDoPoiYun.getKeyword() + "\"");
            tv_pop_loctime.setText(CalendarUtil.showSimpleTime(new Date(((long)(lbsDoPoiYun.getCreateTime())))));
        } else {
            tv_pop_locinfo.setText("我的位置");
            tv_pop_loctime.setText("暂无记录");

        }
        //处理点击事件,弹出泡泡
        pop.showPopup(ImageUtil.getBitmapFromView(popView),
                new GeoPoint((int)(lbs.getLatitude()*1e6), (int)(lbs.getLongitude()*1e6)),
                58);
        CommonView.displayLong(mapView.getContext(), (int) (lbs.getLatitude() * 1e6) + " , " + (int) (lbs.getLongitude() * 1e6));
    }


    public void setData(Lbs lbs) {
        this.lbs = lbs;
        super.setData(LbsConvert.Lbs2LocationData(lbs));
        drawOverlayPoint();
    }

    public void setData(LbsDoPoiYun lbsDoPoiYun) {
        this.lbsDoPoiYun = lbsDoPoiYun;
        setData(LbsYunConvert.LbsDoPoiYun2Lbs(lbsDoPoiYun));
    }

    /**
     *构建位置图层定位点图标
     * */
    public void drawOverlayPoint() {

        if(!useNative) {
            Drawable drawable_head = null;
            String app_user_id = lbs.getApp_user_id();
            User user;
            if(app_user_id.equals(SystemAdapter.currentUser.getApp_user_id())) {
                //构建用户头像缩小图片变成圆角
                user = SystemAdapter.currentUser;
            } else {
                user = Service.friendService.findUserByFriendAppUserId(app_user_id);
            }

            drawable_head = UserConvert.getUserHead(mapView.getContext(), user.getUserhead());
            Bitmap bitmap_head = ImageUtil.drawableToBitmap(drawable_head);
            bitmap_head = ImageUtil.getRoundedCornerBitmap(bitmap_head, 90);
            drawable_head = ImageUtil.bitmapToDrawable(bitmap_head);

            //创建标记点View
            View convertView = inflater.inflate(R.layout.view_paopao_map_point, null);
            TextView tv_name = (TextView)convertView.findViewById(R.id.tv_name);
            ImageView iv_img = (ImageView)convertView.findViewById(R.id.iv_img);
            tv_name.setText(user.getUsername());
            iv_img.setImageDrawable(drawable_head);
            Bitmap bmp = ImageUtil.getBitmapFromView(convertView);
            Drawable dw = ImageUtil.bitmapToDrawable(bmp);

            //当传入marker为null时，使用默认图标绘制
            this.setMarker(dw);
        }

    }

}
