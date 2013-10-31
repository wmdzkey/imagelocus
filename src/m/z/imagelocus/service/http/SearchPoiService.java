package m.z.imagelocus.service.http;

import android.content.Context;
import com.google.gson.Gson;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.RequestParams;
import m.z.common.X3HttpProgressBar;
import m.z.imagelocus.config.SystemAdapter;
import m.z.imagelocus.config.SystemConfig;
import m.z.imagelocus.entity.Friend;
import m.z.imagelocus.entity.Lbs;
import m.z.imagelocus.entity.convert.LbsConvert;
import m.z.imagelocus.entity.convert.LbsYunConvert;
import m.z.imagelocus.entity.yun.LbsDoPoiListYun;
import m.z.imagelocus.entity.yun.LbsDoPoiYun;
import m.z.imagelocus.entity.yun.LbsListYun;
import m.z.imagelocus.entity.yun.LbsYun;
import m.z.imagelocus.service.Service;

import java.util.*;

/**
 * Created by Winnid on 13-10-19.
 */
public abstract class SearchPoiService {

    public Context mContext;

    public static enum FunctionName {
        sendBaiduServer, findAllLbs, findLbsByApp_User_id, findByTimeBucket;
    }

    public SearchPoiService(Context context, FunctionName functionName, Object... objects) {
        this.mContext = context;
        switch (functionName) {
            case sendBaiduServer:
                sendBaiduServer((LbsDoPoiYun) objects[0]);
                break;
            case findAllLbs:
                findAllLbs();
                break;
            case findLbsByApp_User_id:
                findLbsByApp_User_id((String) objects[0]);
                break;
            case findByTimeBucket:
                findByTimeBucket((Date) objects[0], (Date) objects[1]);
                break;
        }
    }
    public void sendBaiduServer(LbsDoPoiYun lbsDoPoiYun) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("title",  lbsDoPoiYun.getKeyword());
        params.addBodyParameter("geotable_id", SystemConfig.BDSearchPoiTableId);
        params.addBodyParameter("latitude", lbsDoPoiYun.getLatitude()+"");
        params.addBodyParameter("longitude", lbsDoPoiYun.getLongitude()+"");
        params.addBodyParameter("coord_type", 3+"");
        params.addBodyParameter("ak", SystemConfig.BDLbsKey);
        params.addBodyParameter("app_user_id", lbsDoPoiYun.getApp_user_id());//这里暂时先使用app_user_id
        params.addBodyParameter("keyword", lbsDoPoiYun.getKeyword());
        params.addBodyParameter("createTime", Math.round(lbsDoPoiYun.getCreateTime())+"");

        if(lbsDoPoiYun.getCategory() != null) {
            params.addBodyParameter("category", lbsDoPoiYun.getCategory().toString());
        }
        if(lbsDoPoiYun.getCategory() != null) {
            params.addBodyParameter("categoryDetail", lbsDoPoiYun.getCategoryDetail().toString());
        }

        new X3HttpProgressBar(mContext, SystemConfig.BDLbsUrl_ADD, HttpRequest.HttpMethod.POST, params) {

            @Override
            public void doSuccess(String result) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("msg","发送成功");
                doResult(map);
            }

        };
    }

    public void findAllLbs() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("ak",  SystemConfig.BDLbsKey);
        params.addQueryStringParameter("geotable_id", SystemConfig.BDSearchPoiTableId);
        params.addQueryStringParameter("page_size", "200");
        params.addQueryStringParameter("calldatz", new Date().getTime()+"");


        new X3HttpProgressBar(mContext, SystemConfig.BDLbsUrl_FIND, HttpRequest.HttpMethod.GET, params) {

            @Override
            public void doSuccess(String result) {
                Map<String, Object> map = new HashMap<String, Object>();
                LbsDoPoiListYun lbsDoPoiListYun = SystemAdapter.gson.fromJson(result, LbsDoPoiListYun.class);
                if(lbsDoPoiListYun != null && lbsDoPoiListYun.getPois() != null && lbsDoPoiListYun.getPois().length != 0) {
                    List<LbsDoPoiYun> lbsDoPoiYunList = new ArrayList<LbsDoPoiYun>();
                    for(LbsDoPoiYun lbsYun : lbsDoPoiListYun.getPois()) {
                        lbsDoPoiYunList.add(lbsYun);
                    }
                    map.put("lbsDoPoiYunList", lbsDoPoiYunList);
                    map.put("msg","查询结果:" + lbsDoPoiListYun.getPois().length);
                } else {
                    map.put("msg","查询结果: 0");
                }
                doResult(map);
            }

        };
    }


    public void findLbsByApp_User_id(String app_user_id) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("ak",  SystemConfig.BDLbsKey);
        params.addQueryStringParameter("geotable_id", SystemConfig.BDSearchPoiTableId);
        params.addQueryStringParameter("app_user_id", app_user_id);
        params.addQueryStringParameter("page_size", "200");
        params.addQueryStringParameter("calldatz", new Date().getTime()+"");

        new X3HttpProgressBar(mContext, SystemConfig.BDLbsUrl_FIND, HttpRequest.HttpMethod.GET, params) {

            @Override
            public void doSuccess(String result) {
                Map<String, Object> map = new HashMap<String, Object>();
                Gson gson = new Gson();
                LbsListYun lbsListYun = gson.fromJson(result, LbsListYun.class);
                if(lbsListYun != null && lbsListYun.getPois() != null && lbsListYun.getPois().length != 0) {
                    List<Lbs> lbsList = new ArrayList<Lbs>();
                    for(LbsYun lbsYun : lbsListYun.getPois()) {
                        lbsList.add(LbsYunConvert.LbsYun2Lbs(lbsYun));
                    }
                    map.put("lbsList", lbsList);
                    map.put("msg","查询结果:" + lbsListYun.getPois().length);
                } else {
                    map.put("msg","查询结果: 0");
                }
                doResult(map);
            }

        };
    }

    public void findByTimeBucket(Date startTime, Date endTime) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("ak",  SystemConfig.BDLbsKey);
        params.addQueryStringParameter("geotable_id", SystemConfig.BDSearchPoiTableId);
        params.addQueryStringParameter("create_time", startTime.getTime() + "," + endTime.getTime());
        params.addQueryStringParameter("page_size", "200");
        params.addQueryStringParameter("calldatz", new Date().getTime()+"");

        new X3HttpProgressBar(mContext, SystemConfig.BDLbsUrl_FIND, HttpRequest.HttpMethod.GET, params) {

            @Override
            public void doSuccess(String result) {
                Map<String, Object> map = new HashMap<String, Object>();
                Gson gson = new Gson();
                LbsListYun lbsListYun = gson.fromJson(result, LbsListYun.class);
                if(lbsListYun != null && lbsListYun.getPois() != null && lbsListYun.getPois().length != 0) {
                    List<Lbs> lbsList = new ArrayList<Lbs>();
                    for(LbsYun lbsYun : lbsListYun.getPois()) {
                        lbsList.add(LbsYunConvert.LbsYun2Lbs(lbsYun));
                    }
                    map.put("lbsList", lbsList);
                    map.put("msg","查询结果:" + lbsListYun.getPois().length);
                } else {
                    map.put("msg","查询结果: 0");
                }
                doResult(map);
            }

        };
    }


    abstract public void doResult(Map<String, Object> resultMap);
}