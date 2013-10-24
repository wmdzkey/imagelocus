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
public class UserLocConvert {

    public static UserLocYun BDLocation2UserLocYun(BDLocation bdLocation) {
        UserLocYun userLocYun = new UserLocYun();
        userLocYun.setLatitude(bdLocation.getLatitude());
        userLocYun.setLongitude(bdLocation.getLongitude());
        userLocYun.setAddress(bdLocation.getAddrStr());
        return userLocYun;
    }
}
