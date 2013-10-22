package m.z.imagelocus.entity.convert;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.LocationData;
import m.z.imagelocus.entity.Lbs;
import m.z.imagelocus.entity.yun.LbsYun;

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

    public static LbsYun Lbs2LbsYun(Lbs lbs) {
        LbsYun _lbsYun= new LbsYun();
        _lbsYun.setAddress(lbs.getAddrStr());
        _lbsYun.setCity(lbs.getCity());
        _lbsYun.setCity_id(lbs.getCity_id());
        _lbsYun.setCoord_type(lbs.getCoord_type());
        _lbsYun.setDistance(lbs.getDistance());
        _lbsYun.setDistrict(lbs.getDistrict());
        _lbsYun.setGeotable_id(lbs.getGeotable_id());
        _lbsYun.setId(lbs.getId());
        _lbsYun.setLatitude(lbs.getLatitude());
        _lbsYun.setLocation(new double[]{lbs.getLongitude(), lbs.getLatitude()});
        _lbsYun.setLongitude(lbs.getLongitude());
        _lbsYun.setProvince(lbs.getProvince());
        _lbsYun.setState(lbs.getState());
        _lbsYun.setTags(lbs.getTags());
        _lbsYun.setTitle(lbs.getTitle());
        _lbsYun.setWeight(lbs.getWeight());
        _lbsYun.setUser_id(lbs.getUser_id());
        _lbsYun.setApp_user_id(lbs.getApp_user_id());

        _lbsYun.setCreateTime(lbs.getCreateTime().getTime());
        _lbsYun.setModifyTime(lbs.getModifyTime().getTime());

        return _lbsYun;
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

    public static Lbs LbsYun2Lbs(LbsYun lbsYun) {
        Lbs lbs = new Lbs();
        lbs.setAddrStr(lbsYun.getAddress());
        lbs.setCity(lbsYun.getCity());
        lbs.setCity_id(lbsYun.getCity_id());
        lbs.setCoord_type(lbsYun.getCoord_type());
        lbs.setDistance(lbsYun.getDistance());
        lbs.setDistrict(lbsYun.getDistrict());
        lbs.setGeotable_id(lbsYun.getGeotable_id());
        lbs.setId(lbsYun.getId());
        lbs.setLatitude(lbsYun.getLatitude());
        lbs.setLongitude(lbsYun.getLongitude());
        lbs.setProvince(lbsYun.getProvince());
        lbs.setState(lbsYun.getState());
        lbs.setTags(lbsYun.getTags());
        lbs.setTitle(lbsYun.getTitle());
        lbs.setWeight(lbsYun.getWeight());
        lbs.setApp_user_id(lbsYun.getApp_user_id());
        lbs.setUser_id(lbsYun.getUser_id());

        lbs.setCreateTime(new Date(Math.round(lbsYun.getCreateTime())));
        lbs.setModifyTime(new Date(Math.round(lbsYun.getModifyTime())));
        return lbs;
    }
}
