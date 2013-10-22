package m.z.imagelocus.service;

import android.content.Context;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.LocationData;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import m.z.imagelocus.entity.Lbs;
import m.z.imagelocus.entity.convert.LbsConvert;

import java.util.*;

/**
 * @author Winnid
 * @Title:
 * @Description:
 * @date 13-9-17
 * @Version V1.0
 * Created by Winnid on 13-9-17.
 */
public class LbsService {

    private Context context;
    private DbUtils db;

    public LbsService(Context contextThis) {
        db = DbUtils.create(contextThis, "imagelocus");
        context = contextThis;
    }

    public void save(Lbs lbs) {
        //空检查
        if (lbs == null) {
            throw new NullPointerException();
        }
        //数据验证

        //检查数据库中是否存在此用户
        try {
            db.saveOrUpdate(lbs);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    public List<Lbs> findByUser_id(Integer user_id) {
        List<Lbs> lbsList = new ArrayList<Lbs>();
        List<Object> objList = new ArrayList<Object>();
        //空检查
        if (user_id == null) {
            throw new NullPointerException();
        }
        //数据验证

        //保存或更新
        try {
            //objList = db.findAll(Selector.from(Lbs.class));
            //Lbs lbs = db.findFirst(Selector.from(Lbs.class).where(WhereBuilder.b("user_id","=",user_id)));
            lbsList = db.findAll(Selector.from(Lbs.class).where(WhereBuilder.b("user_id","=",user_id)));
        } catch (DbException e) {
            e.printStackTrace();
        }
        return lbsList;
    }

    public Lbs createLbs(Integer user_id, Object obj) {
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

    public Lbs createLbs(String app_user_id, Object obj) {
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