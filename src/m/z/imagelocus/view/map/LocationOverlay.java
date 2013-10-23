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
import m.z.imagelocus.entity.Lbs;
import m.z.imagelocus.entity.convert.LbsConvert;
import m.z.util.CalendarUtil;
import m.z.util.ImageUtil;

/**
 * 继承MyLocationOverlay重写dispatchTap实现点击处理
 * Created by Winnid on 13-10-23.
 */
public class LocationOverlay extends MyLocationOverlay {

    public Lbs lbs = null;
    private PopupOverlay pop  = null;     //弹出泡泡图层，浏览节点时使用
    private View popView = null;     //泡泡图层样式
    public MapView mapView;
    private LayoutInflater inflater;
    private boolean useNative;

    public LocationOverlay(MapView mapView, PopupOverlay pop, View popView) {
        super(mapView);
        this.mapView = mapView;
        this.pop = pop;
        this.popView = popView;
        this.inflater = LayoutInflater.from(mapView.getContext());
        this.useNative = true;
    }

    public LocationOverlay(MapView mapView, PopupOverlay pop, View popView, boolean useNative) {
        super(mapView);
        this.mapView = mapView;
        this.pop = pop;
        this.popView = popView;
        this.inflater = LayoutInflater.from(mapView.getContext());
        this.useNative = useNative;
    }

    @Override
    protected boolean dispatchTap() {

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

        if(!useNative) {
            //String app_user_id = SystemAdapter.currentUser.getApp_user_id();

            //构建用户头像缩小图片变成圆角
            Drawable drawable_head = mapView.getResources().getDrawable(R.drawable.default_avatar_winnid);
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
            this.setMarker(dw);
        }

    }
}
