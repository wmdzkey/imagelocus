package m.z.imagelocus.service;

import android.content.Context;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.LocationData;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import m.z.imagelocus.config.SystemAdapter;
import m.z.imagelocus.config.SystemConfig;
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
        db = DbUtils.create(contextThis, SystemConfig.DBNameSQLite);
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


}