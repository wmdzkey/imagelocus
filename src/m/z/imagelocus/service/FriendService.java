package m.z.imagelocus.service;

import android.content.Context;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import m.z.imagelocus.config.SystemAdapter;
import m.z.imagelocus.config.SystemConfig;
import m.z.imagelocus.entity.Friend;
import m.z.imagelocus.entity.Lbs;
import m.z.imagelocus.entity.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Winnid
 * @Title:
 * @Description:
 * @date 13-9-11
 * @Version V1.0
 * Created by Winnid on 13-9-11.
 */
public class FriendService {

    private Context context;
    private DbUtils db;

    public FriendService(Context contextThis) {
        db = DbUtils.create(contextThis, SystemConfig.DBNameSQLite);
        context = contextThis;
    }

    /**
     *是否存在
     * */
    public Friend exist(User user) {
        //空检查
        if (user == null) {
            throw new NullPointerException();
        }
        Friend friendInDB = null;
        try {
            friendInDB = db.findFirst(Selector.from(Friend.class)
                    .where(WhereBuilder.b("app_user_id", "=", SystemAdapter.currentUser.getApp_user_id())
                            .append("app_friend_user_id", "=", user.getApp_user_id())
                    )
            );
        } catch (DbException e) {
            e.printStackTrace();
        }
        return friendInDB;
    }

    /**
     *增加
     * */
    public Map<String, Object> add(User user) {
        //空检查
        if (user == null) {
            throw new NullPointerException();
        }
        //数据验证

        //检查数据库中是否存在此记录
        Map<String, Object> mapInfo = new HashMap<String, Object>();
        try {
            //存在
            Friend friendInDB = exist(user);
            if (friendInDB != null) {
                mapInfo.put("info", "已经加过好友了");
            }
            //不存在 -保存
            else {
                Friend friend = new Friend();
                friend.setApp_user_id(SystemAdapter.currentUser.getApp_user_id());
                friend.setUser_id(SystemAdapter.currentUser.getId());
                friend.setApp_friend_user_id(user.getApp_user_id());
                friend.setFriend_user_id(user.getId());
                friend.setFriendname(user.getUsername());
                friend.setFriendhead(user.getUserhead());
                db.saveOrUpdate(friend);
                mapInfo.put("friend", friend);
                mapInfo.put("info", "添加成功");
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
        return mapInfo;
    }

    public List<Friend> find(String app_user_id) {
        List<Friend> friendList = new ArrayList<Friend>();
        //空检查
        if (app_user_id == null) {
            throw new NullPointerException();
        }
        //数据验证

        try {
            friendList = db.findAll(Selector.from(Friend.class).where(WhereBuilder.b("app_user_id", "=", app_user_id)));
        } catch (DbException e) {
            e.printStackTrace();
        }
        return friendList;
    }

    public User findUserByFriendAppUserId(String friend_app_user_id) {
        User user = new User();
        //空检查
        if (friend_app_user_id == null) {
            throw new NullPointerException();
        }
        //数据验证

        try {
            Friend friend = db.findFirst(Selector.from(Friend.class).where(WhereBuilder.b("app_friend_user_id", "=", friend_app_user_id)));
            if(friend != null) {
                user.setId(friend.getFriend_user_id());
                user.setApp_user_id(friend.getApp_friend_user_id());
                user.setUsername(friend.getFriendname());
                user.setUserhead(friend.getFriendhead());
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return user;
    }

    public Map<String, Object> delete(User user) {
        //空检查
        if (user == null) {
            throw new NullPointerException();
        }
        //数据验证

        //检查数据库中是否存在此记录
        //检查数据库中是否存在此记录
        Map<String, Object> mapInfo = new HashMap<String, Object>();
        try {
            //存在
            Friend friendInDB = exist(user);
            if (friendInDB != null) {
                mapInfo.put("info", "你没有加他/她为好友，无需删除，若不显示请退出后重新进入");
            }
            //不存在 -删除
            else {
                db.delete(friendInDB);
                mapInfo.put("info", "删除成功");
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return mapInfo;
    }

}

