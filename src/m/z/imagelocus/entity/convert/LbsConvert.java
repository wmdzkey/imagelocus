package m.z.imagelocus.entity.convert;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.LocationData;
import m.z.imagelocus.entity.Lbs;
import m.z.imagelocus.entity.yun.LbsYun;
import m.z.imagelocus.entity.yun.UserLocYun;

import java.util.Date;

/**
 * @author Winnid
 * @Title:
 * @Description:
 * @date 13-9-21
 * @Version V1.0
 * Created by Winnid on 13-9-21.
 */
public class LbsConvert {

    public static BDLocation Lbs2BdLocation(Lbs lbs) {
        BDLocation _bdLocation = new BDLocation();
        _bdLocation.setLatitude(lbs.getLatitude());
        _bdLocation.setLongitude(lbs.getLongitude());
        _bdLocation.setSpeed(lbs.getSpeed());
        _bdLocation.setDerect(lbs.getDirection());
        _bdLocation.setSatelliteNumber(lbs.getSatellitesNum());
        _bdLocation.setLocType(lbs.getLocType());
        _bdLocation.setRadius(lbs.getRadius());
        _bdLocation.setAddrStr(lbs.getAddrStr());
        return _bdLocation;
    }

    public static LocationData Lbs2LocationData(Lbs lbs) {
        LocationData _locationData = new LocationData();
        _locationData.latitude = lbs.getLatitude();
        _locationData.longitude = lbs.getLongitude();
        _locationData.speed = lbs.getSpeed();
        _locationData.direction = lbs.getDirection();
        _locationData.satellitesNum = lbs.getSatellitesNum();
        //如果不显示定位精度圈，将accuracy赋值为0即可
        _locationData.accuracy = lbs.getRadius();
        return _locationData;
    }

    public static Lbs BDLocation2Lbs(BDLocation bdLocation) {
        Lbs lbs = new Lbs();
        lbs.setLatitude(bdLocation.getLatitude());
        lbs.setLongitude(bdLocation.getLongitude());
        lbs.setSpeed(bdLocation.getSpeed());
        lbs.setDirection(bdLocation.getDerect());
        lbs.setSatellitesNum(bdLocation.getSatelliteNumber());
        lbs.setLocType(bdLocation.getLocType());
        lbs.setRadius(bdLocation.getRadius());
        lbs.setAddrStr(bdLocation.getAddrStr());
        return lbs;
    }

    public static Lbs LocationData2Lbs(LocationData locationData) {
        Lbs lbs = new Lbs();
        lbs.setLatitude(locationData.latitude);
        lbs.setLongitude(locationData.longitude);
        lbs.setSpeed(locationData.speed);
        lbs.setDirection(locationData.direction);
        lbs.setSatellitesNum(locationData.satellitesNum);
        //如果不显示定位精度圈，将accuracy赋值为0即可
        lbs.setRadius(locationData.accuracy);
        return lbs;
    }

    public static Lbs createLbs(Integer user_id, Object obj) {
        Lbs lbs = null;
        if(user_id == null || obj == null) {
            return null;
        }
        if(obj instanceof BDLocation) {
            lbs = LbsConvert.BDLocation2Lbs((BDLocation) obj);
        } else if(obj instanceof LocationData) {
            lbs =LbsConvert.LocationData2Lbs((LocationData) obj);
        }
        lbs.setUser_id(user_id);
        lbs.setCreateTime(new Date());
        return lbs;
    }

    public static Lbs createLbs(String app_user_id, Object obj) {
        Lbs lbs = null;
        if(app_user_id == null || obj == null) {
            return null;
        }
        if(obj instanceof BDLocation) {
            lbs = LbsConvert.BDLocation2Lbs((BDLocation) obj);
        } else if(obj instanceof LocationData) {
            lbs =LbsConvert.LocationData2Lbs((LocationData) obj);
        }
        lbs.setApp_user_id(app_user_id);
        lbs.setCreateTime(new Date());
        return lbs;
    }
}
