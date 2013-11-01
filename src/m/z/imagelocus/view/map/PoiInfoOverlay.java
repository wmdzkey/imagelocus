
package m.z.imagelocus.view.map;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.PoiOverlay;
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
import java.util.Map;

/**
 * 继承PoiOverlay重写dispatchTap实现点击处理
 * Created by Winnid on 13-10-23.
 */
public class PoiInfoOverlay extends PoiOverlay {

    public PoiInfoOverlay(Activity activity, MapView mapView) {
        super(activity, mapView);
    }


}
