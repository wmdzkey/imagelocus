package m.z.imagelocus.entity.convert;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.LocationData;
import m.z.imagelocus.entity.Lbs;
import m.z.imagelocus.entity.yun.LbsDoPoiYun;
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
public class LbsYunConvert {

    public static UserLocYun BDLocation2UserLocYun(BDLocation bdLocation) {
        UserLocYun userLocYun = new UserLocYun();
        userLocYun.setLatitude(bdLocation.getLatitude());
        userLocYun.setLongitude(bdLocation.getLongitude());
        userLocYun.setAddress(bdLocation.getAddrStr());
        return userLocYun;
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
        _lbsYun.setLocation(new double[]{lbs.getLongitude(), lbs.getLatitude()});
        _lbsYun.setLatitude(lbs.getLatitude());
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
        lbs.setLatitude(lbsYun.getLocation()[1]);
        lbs.setLongitude(lbsYun.getLocation()[0]);
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

    public static LbsDoPoiYun Lbs2LbsDoPoiYun(Lbs lbs) {
        LbsDoPoiYun _lbsYun= new LbsDoPoiYun();
        _lbsYun.setAddress(lbs.getAddrStr());
        _lbsYun.setCity(lbs.getCity());
        _lbsYun.setCity_id(lbs.getCity_id());
        _lbsYun.setCoord_type(lbs.getCoord_type());
        _lbsYun.setDistrict(lbs.getDistrict());
        _lbsYun.setGeotable_id(lbs.getGeotable_id());
        _lbsYun.setId(lbs.getId());
        _lbsYun.setLocation(new double[]{lbs.getLongitude(), lbs.getLatitude()});
        _lbsYun.setLatitude(lbs.getLatitude());
        _lbsYun.setLongitude(lbs.getLongitude());
        _lbsYun.setProvince(lbs.getProvince());
        _lbsYun.setState(lbs.getState());
        _lbsYun.setTags(lbs.getTags());
        _lbsYun.setTitle(lbs.getTitle());
        _lbsYun.setApp_user_id(lbs.getApp_user_id());
        _lbsYun.setCreateTime(lbs.getCreateTime().getTime());
        return _lbsYun;
    }

    public static Lbs LbsDoPoiYun2Lbs(LbsDoPoiYun lbsDoPoiYun) {
        Lbs lbs = new Lbs();
        lbs.setAddrStr(lbsDoPoiYun.getAddress());
        lbs.setCity(lbsDoPoiYun.getCity());
        lbs.setCity_id(lbsDoPoiYun.getCity_id());
        lbs.setCoord_type(lbsDoPoiYun.getCoord_type());
        lbs.setDistance(lbsDoPoiYun.getDistance());
        lbs.setDistrict(lbsDoPoiYun.getDistrict());
        lbs.setGeotable_id(lbsDoPoiYun.getGeotable_id());
        lbs.setId(lbsDoPoiYun.getId());
        lbs.setLatitude(lbsDoPoiYun.getLocation()[1]);
        lbs.setLongitude(lbsDoPoiYun.getLocation()[0]);
        lbs.setProvince(lbsDoPoiYun.getProvince());
        lbs.setState(lbsDoPoiYun.getState());
        lbs.setTags(lbsDoPoiYun.getTags());
        lbs.setTitle(lbsDoPoiYun.getTitle());
        lbs.setApp_user_id(lbsDoPoiYun.getApp_user_id());

        lbs.setCreateTime(new Date(Math.round(lbsDoPoiYun.getCreateTime())));
        lbs.setModifyTime(new Date(Math.round(lbsDoPoiYun.getModifyTime())));
        return lbs;
    }
}
