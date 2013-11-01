package m.z.imagelocus.service;

import android.content.Context;
import android.content.SharedPreferences;
import com.baidu.android.pushservice.PushManager;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import m.z.common.CommonView;
import m.z.imagelocus.config.SystemAdapter;
import m.z.imagelocus.config.SystemConfig;
import m.z.imagelocus.entity.Friend;
import m.z.imagelocus.entity.User;
import m.z.util.SIMCardUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Winnid
 * @Title:
 * @Description:
 * @date 13-9-11
 * @Version V1.0
 * Created by Winnid on 13-9-11.
 */
public class UserService {

    private Context mContext;
    private DbUtils db;

    public UserService(Context contextThis) {
        db = DbUtils.create(contextThis, SystemConfig.DBNameSQLite);
        mContext = contextThis;
    }

    /**
     *是否存在
     * */
    public User exist(User user) {
        //空检查
        if (user == null) {
            throw new NullPointerException();
        }
        User userInDB = null;
        try {
            userInDB = db.findFirst(Selector.from(User.class)
                    .where(WhereBuilder.b("username", "=", user.getUsername())
                            .append("password", "=", user.getPassword())
                    )
            );
        } catch (DbException e) {
            e.printStackTrace();
        }
        //存在
        if (userInDB != null) {
            return userInDB;
        } else {
            return null;
        }
    }

    public Map<String, Object> regist(User user) {
        //空检查
        if (user == null) {
            throw new NullPointerException();
        }
        //数据验证

        //检查数据库中是否存在此用户
        Map<String, Object> mapInfo = new HashMap<String, Object>();
        try {
            //存在
            User userInDB = exist(user);
            if (userInDB != null) {
                mapInfo.put("user", userInDB);
                mapInfo.put("info", "存在");
            }
            //不存在 -保存
            else {
                refreshUserInfo(user);

                db.saveOrUpdate(user);
                mapInfo.put("user", user);
                mapInfo.put("info", "不存在");
                SystemAdapter.currentUser = user;
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return mapInfo;
    }

    public Map<String, Object> login(User user) {
        //空检查
        if (user == null) {
            throw new NullPointerException();
        }
        //数据验证

        //检查数据库中是否存在此用户
        Map<String, Object> mapInfo = new HashMap<String, Object>();
        //存在 -登录
        User userInDB = exist(user);
        if (userInDB != null) {
            mapInfo.put("user", userInDB);
            mapInfo.put("info", "存在");
            SystemAdapter.currentUser = userInDB;
        }
        //不存在 -保存
        else {
            refreshUserInfo(user);
            mapInfo.put("user", user);
            mapInfo.put("info", "不存在");
        }
        return mapInfo;
    }


    public User find(Integer id) {
        User user = null;
        //空检查
        if (id == null) {
            throw new NullPointerException();
        }
        //数据验证

        //保存或更新
        try {
            user = db.findById(User.class, id);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return user;
    }

    public void saveOrUpdate(User user) {
        //空检查
        if (user == null) {
            throw new NullPointerException();
        }
        //数据验证

        //检查数据库中是否存在此用户
        try {
            db.saveOrUpdate(user);
        } catch (DbException e) {
            e.printStackTrace();
        }
        SystemAdapter.currentUser = user;
    }

    public void refreshUserInfo(User user) {
        SIMCardUtil siminfo = new SIMCardUtil(mContext);
        String phone = siminfo.getNativePhoneNumber();
        user.setPhone(phone);
        try {
            db.saveOrUpdate(user);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    //保存登录用户云推id
    public void savePushUserId(final String app_userid) {
        final Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(SystemAdapter.currentUser != null) {
                    SystemAdapter.currentUser.setApp_user_id(app_userid);
                    Service.userService.saveOrUpdate(SystemAdapter.currentUser);
                    CommonView.display(mContext, "绑定更新完成");
                    timer.cancel();
                } else{
                    CommonView.display(mContext, "暂无用户登陆信息, 等待绑定");
                }
            }
        }, 0, 5000);
    }

}