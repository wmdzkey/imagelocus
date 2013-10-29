package m.z.imagelocus.service.http;

import android.content.Context;
import com.google.gson.Gson;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.RequestParams;
import m.z.common.X3HttpProgressBar;
import m.z.imagelocus.config.SystemConfig;
import m.z.imagelocus.entity.Lbs;
import m.z.imagelocus.entity.convert.LbsConvert;
import m.z.imagelocus.entity.yun.LbsListYun;
import m.z.imagelocus.entity.yun.LbsYun;
import m.z.imagelocus.entity.yun.UserLocListYun;
import m.z.imagelocus.entity.yun.UserLocYun;
import m.z.util.X3HttpUtil;

import java.util.*;

/**
 * Created by Winnid on 13-10-19.
 */
public abstract class SearchUserService {

    public Context mContext;

    public static enum FunctionName {
        sendUserLocServer, findUserLoc, findUserLocBySex;
    }

    public SearchUserService(Context context, FunctionName functionName, Object... objects) {
        this.mContext = context;
        switch (functionName) {
            case sendUserLocServer:
                sendUserLocServer((UserLocYun) objects[0]);
                break;
            case findUserLoc:
                findUserLoc();
                break;
            case findUserLocBySex:
                findUserLocBySex();
                break;
        }
    }
    public void sendUserLocServer(UserLocYun userLocYun) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("title",  userLocYun.getApp_user_id() + "在" + userLocYun.getAddress());
        params.addBodyParameter("geotable_id", SystemConfig.BDSearchUserTableId);
        params.addBodyParameter("latitude", userLocYun.getLatitude()+"");
        params.addBodyParameter("longitude", userLocYun.getLongitude()+"");
        params.addBodyParameter("coord_type", 1+"");
        params.addBodyParameter("ak", SystemConfig.BDLbsKey);
        params.addBodyParameter("address", userLocYun.getAddress());
        params.addBodyParameter("app_user_id", userLocYun.getApp_user_id());//这里暂时先使用app_user_id
        params.addBodyParameter("username", userLocYun.getUsername());
        params.addBodyParameter("sex", userLocYun.getSex() + "");
        params.addBodyParameter("userhead", userLocYun.getUserhead());
        params.addBodyParameter("phone", userLocYun.getPhone());

        new X3HttpUtil(mContext, SystemConfig.BDLbsUrl_ADD, HttpRequest.HttpMethod.POST, params) {

            @Override
            public void doSuccess(String result) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("msg","发送成功");
                doResult(map);
            }

        };
    }

    public void findUserLoc() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("ak",  SystemConfig.BDLbsKey);
        params.addQueryStringParameter("geotable_id", SystemConfig.BDSearchUserTableId);
        params.addQueryStringParameter("page_size", "200");
        params.addQueryStringParameter("calldatz", new Date().getTime()+"");


        new X3HttpUtil(mContext, SystemConfig.BDLbsUrl_FIND, HttpRequest.HttpMethod.GET, params) {

            @Override
            public void doSuccess(String result) {
                Map<String, Object> map = new HashMap<String, Object>();
                Gson gson = new Gson();
                UserLocListYun userLocListYun = gson.fromJson(result, UserLocListYun.class);
                if(userLocListYun != null && userLocListYun.getPois() != null && userLocListYun.getPois().length != 0) {
                    List<UserLocYun> userLocYunList = new ArrayList<UserLocYun>();
                    for(UserLocYun userLocYun : userLocListYun.getPois()) {
                        userLocYunList.add(userLocYun);
                    }
                    map.put("userLocYunList", userLocYunList);
                    map.put("msg","查询结果:" + userLocListYun.getPois().length);
                } else {
                    map.put("msg","查询结果:0");
                }
                doResult(map);
            }

        };
    }


    public void findUserLocBySex() {
    }

    abstract public void doResult(Map<String, Object> resultMap);
}