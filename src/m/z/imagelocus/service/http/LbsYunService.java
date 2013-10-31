package m.z.imagelocus.service.http;

import android.content.Context;
import com.google.gson.Gson;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.RequestParams;
import m.z.common.X3HttpProgressBar;
import m.z.imagelocus.config.SystemConfig;
import m.z.imagelocus.entity.Friend;
import m.z.imagelocus.entity.Lbs;
import m.z.imagelocus.entity.convert.LbsYunConvert;
import m.z.imagelocus.entity.yun.LbsListYun;
import m.z.imagelocus.entity.yun.LbsYun;
import m.z.imagelocus.service.Service;

import java.util.*;

/**
 * Created by Winnid on 13-10-19.
 */
public abstract class LbsYunService {

    public Context mContext;

    public static enum FunctionName {
        sendBaiduServer, findAllLbs, findLbsByApp_User_id, findLbsMyFriendByApp_User_id;
    }

    public LbsYunService(Context context, FunctionName functionName, Object... objects) {
        this.mContext = context;
        switch (functionName) {
            case sendBaiduServer:
                sendBaiduServer((Lbs) objects[0]);
                break;
            case findAllLbs:
                findAllLbs();
                break;
            case findLbsByApp_User_id:
                findLbsByApp_User_id((String) objects[0]);
                break;
            case findLbsMyFriendByApp_User_id:
                findLbsMyFriendByApp_User_id((String) objects[0]);
                break;
        }
    }
    public void sendBaiduServer(Lbs lbs) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("title",  lbs.getApp_user_id() + "在" + lbs.getAddrStr());
        params.addBodyParameter("geotable_id", SystemConfig.BDLbsTableId);
        params.addBodyParameter("latitude", lbs.getLatitude()+"");
        params.addBodyParameter("longitude", lbs.getLongitude()+"");
        params.addBodyParameter("coord_type", 3+"");
        params.addBodyParameter("ak", SystemConfig.BDLbsKey);
        params.addBodyParameter("address", lbs.getAddrStr());
        params.addBodyParameter("app_user_id", lbs.getApp_user_id());//这里暂时先使用app_user_id
        params.addBodyParameter("radius", lbs.getRadius()+"");
        params.addBodyParameter("locType", lbs.getLocType()+"");
        params.addBodyParameter("satellitesNum", lbs.getSatellitesNum()+"");
        params.addBodyParameter("direction", lbs.getDirection()+"");
        params.addBodyParameter("speed", lbs.getSpeed()+"");
        params.addBodyParameter("createTime", lbs.getCreateTime().getTime()+"");

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
        params.addQueryStringParameter("geotable_id", SystemConfig.BDLbsTableId);
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


    public void findLbsByApp_User_id(String app_user_id) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("ak",  SystemConfig.BDLbsKey);
        params.addQueryStringParameter("geotable_id", SystemConfig.BDLbsTableId);
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

    public void findLbsByTime(Date startTime, Date endTime) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("ak",  SystemConfig.BDLbsKey);
        params.addQueryStringParameter("geotable_id", SystemConfig.BDLbsTableId);
        params.addQueryStringParameter("createTime", startTime.getTime() + "," + endTime.getTime());
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

    public void findLbsByApp_User_id_Time(String app_user_id, Date startTime, Date endTime) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("ak",  SystemConfig.BDLbsKey);
        params.addQueryStringParameter("geotable_id", SystemConfig.BDLbsTableId);
        params.addQueryStringParameter("app_user_id", app_user_id);
        params.addQueryStringParameter("createTime", startTime.getTime() + "," + endTime.getTime());
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

    public void findLbsMyFriendByApp_User_id(String app_user_id) {
        //读取FriendAppUserId
        List<Friend> friendList = Service.friendService.find(app_user_id);

        if(friendList != null && friendList.size() != 0) {
            for(Friend friend : friendList) {
                //循环检索
                String friend_app_user_id = friend.getApp_friend_user_id();
                RequestParams params = new RequestParams();
                params.addQueryStringParameter("ak",  SystemConfig.BDLbsKey);
                params.addQueryStringParameter("geotable_id", SystemConfig.BDLbsTableId);
                params.addQueryStringParameter("app_user_id", friend_app_user_id);
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
        }
    }

    abstract public void doResult(Map<String, Object> resultMap);
}