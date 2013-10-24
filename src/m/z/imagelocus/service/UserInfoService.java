package m.z.imagelocus.service;

import android.content.Context;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import m.z.imagelocus.config.SystemConfig;
import m.z.imagelocus.entity.UserInfo;

/**
 * @author Winnid
 * @Title:
 * @Description:
 * @date 13-9-25
 * @Version V1.0
 * Created by Winnid on 13-9-25.
 */
public class UserInfoService {

    private Context context;
    private DbUtils db;

    public UserInfoService(Context contextThis) {
        db = DbUtils.create(contextThis, SystemConfig.DBNameSQLite);
        context = contextThis;
    }


    public UserInfo findByUser(Integer userId) {
        UserInfo userInfo = null;
        //空检查
        if (userId == null) {
            throw new NullPointerException();
        }
        //数据验证

        //保存或更新
        try {
            userInfo = db.findFirst(Selector.from(UserInfo.class).where(WhereBuilder.b("user_id","=",userId)));
        } catch (DbException e) {
            e.printStackTrace();
        }
        return userInfo;
    }

}