package m.z.imagelocus.service;

import android.content.Context;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import m.z.imagelocus.config.SystemAdapter;
import m.z.imagelocus.entity.User;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Winnid
 * @Title:
 * @Description:
 * @date 13-9-11
 * @Version V1.0
 * Created by Winnid on 13-9-11.
 */
public class UserService {

    private Context context;
    private DbUtils db;

    public UserService(Context contextThis) {
        db = DbUtils.create(contextThis, "imagelocus");
        context = contextThis;
    }

    public Map<String, Object> login(User user) {
        //空检查
        if (user == null) {
            throw new NullPointerException();
        }
        //数据验证

        //检查数据库中是否存在此用户
        Map<String, Object> mapLoginInfo = new HashMap<String, Object>();
        try {
            User userInDB = db.findFirst(Selector.from(User.class)
                    .where(WhereBuilder.b("username", "=", user.getUsername())
                            .append("password", "=", user.getPassword())
                    )
            );
            //存在 -登录
            if (userInDB != null) {
                user = userInDB;
                mapLoginInfo.put("user", userInDB);
                mapLoginInfo.put("info", "存在");
            }
            //不存在 -保存
            else {
                db.saveOrUpdate(user);
                mapLoginInfo.put("user", user);
                mapLoginInfo.put("info", "不存在");
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
        SystemAdapter.currentUser = user;
        return mapLoginInfo;
    }

    public void commitScore(User user) {
        //空检查
        if (user == null) {
            throw new NullPointerException();
        }
        //数据验证

        //检查数据库中是否存在此用户
        try {
            User userInDB = db.findFirst(Selector.from(User.class)
                    .where(WhereBuilder.b("username", "=", user.getUsername())
                            .append("password", "=", user.getPassword())
                    )
            );
            //存在
            if (userInDB != null) {
                db.saveOrUpdate(user);
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
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
    }

}