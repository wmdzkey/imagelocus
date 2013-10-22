package m.z.imagelocus.service;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import m.z.imagelocus.entity.yun.LbsYun;
import m.z.imagelocus.service.http.LbsYunService;

/**
 * @author Winnid
 * @Title:
 * @Description:
 * @date 13-9-25
 * @Version V1.0
 * Created by Winnid on 13-9-25.
 */
public class Service {

    public static UserService userService;
    public static UserInfoService userInfoService;
    public static LbsService lbsService;

    public static void init(Context mContext) {
        userService = new UserService(mContext);
        userInfoService = new UserInfoService(mContext);
        lbsService = new LbsService(mContext);
    }
}

//之前的版本
//public class Service {
//    public UserService userService;
//    public UserInfoService userInfoService;
//    public LbsService lbsService;
//
//    public Service(Context context) {
//        userService = new UserService(context);
//        userInfoService = new UserInfoService(context);
//        lbsService = new LbsService(context);
//    }
//    public static Service init(Context applicationContext) {
//        return  new Service(applicationContext);
//    }
//}
