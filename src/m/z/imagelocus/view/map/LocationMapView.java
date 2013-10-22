package m.z.imagelocus.view.map;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.PopupOverlay;

/**
 * @author Winnid
 * @Title: 继承MapView重写onTouchEvent实现泡泡处理操作
 * @Description: 继承MapView重写onTouchEvent实现泡泡处理操作
 * @date 13-10-14
 * @Version V1.0
 * Created by Winnid on 13-10-14.
 */
public class LocationMapView extends MapView {

    public static PopupOverlay pop  = null;//弹出泡泡图层，点击图标使用

    public LocationMapView(Context context) {
        super(context);
    }
    public LocationMapView(Context context, AttributeSet attrs){
        super(context,attrs);
    }
    public LocationMapView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
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
