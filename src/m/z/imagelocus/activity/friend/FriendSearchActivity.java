package m.z.imagelocus.activity.friend;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.googlecode.androidannotations.annotations.*;
import m.z.common.CommonView;
import m.z.imagelocus.R;
import m.z.imagelocus.adapter.friend.X3FriendAdapter;
import m.z.imagelocus.config.SystemAdapter;
import m.z.imagelocus.config.SystemStore;
import m.z.imagelocus.entity.User;
import m.z.imagelocus.entity.convert.LbsConvert;
import m.z.imagelocus.entity.yun.UserLocYun;
import m.z.imagelocus.service.http.SearchUserService;

import java.util.*;

/**
 * @author Winnid
 * @Title:
 * @Description:
 * @date 13-10-11
 * @Version V1.0
 * Created by Winnid on 13-10-11.
 */
@NoTitle
@EActivity(R.layout.activity_friend_search)
public class FriendSearchActivity extends Activity implements AdapterView.OnItemClickListener {

    public static FriendSearchActivity instance = null;

    //左上角第一个按钮
    @ViewById(R.id.btn_left)
    Button btn_left;
    //中间标题
    @ViewById(R.id.tv_middle)
    TextView tv_middle;
    //右上角第一个按钮
    @ViewById(R.id.btn_right)
    Button btn_right;



    //数据适配器装载List
    @ViewById(R.id.lv_friend)
    ListView lv_friend;
    //数据
    List<User> list_friend;
    X3FriendAdapter x3ap_items_friend;

    Timer timer;
    // 定位相关
    LocationClient locClient;
    LocationListener locListener = new LocationListener();
    UserLocYun locDataNow = null;    //定位数据


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
    }

    @AfterViews
    void init() {
        tv_middle.setText("查找朋友");
        btn_right.setOnTouchListener(btn_rightTouchListener);

        list_friend = SystemStore.userData;
        x3ap_items_friend = new X3FriendAdapter(instance, list_friend);
        lv_friend.setAdapter(x3ap_items_friend);
        lv_friend.setOnItemClickListener(this);
    }

    void addItem(List<UserLocYun> userLocYunList) {

        if(userLocYunList != null && userLocYunList.size() != 0) {
            list_friend.clear();
            List<String> uniqueAppUserIdList = new ArrayList<String>();
            for(UserLocYun userLocYun : userLocYunList) {
                if(!userLocYun.getApp_user_id().equals(SystemAdapter.currentUser.getApp_user_id())) {
                    if(!uniqueAppUserIdList.contains(userLocYun.getApp_user_id())) {
                        User user = new User();
                        user.setApp_user_id(userLocYun.getApp_user_id());
                        user.setUsername(userLocYun.getUsername());
                        list_friend.add(user);
                        uniqueAppUserIdList.add(userLocYun.getApp_user_id());
                    }
                }
            }
            x3ap_items_friend = new X3FriendAdapter(instance, list_friend);
            x3ap_items_friend.notifyDataSetChanged();
            lv_friend.setAdapter(x3ap_items_friend);
        }
        CommonView.displayShort(instance, list_friend.size()+"");

    }


    @Click(R.id.btn_left)
    void btn_left_onClick() {
        CommonView.displayShort(this, "返回");
    }

    private OnTouchListener btn_rightTouchListener = new OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {

            if(event.getAction() == MotionEvent.ACTION_DOWN){
                //按住事件发生后执行代码的区域
                CommonView.displayShort(instance, "按住");
                //开始请求定位
                initLocation();

                timer = new Timer(true);
                //第一个参数是要操作的方法，第二个参数是要设定延迟的时间，第三个参数是周期的设定，每隔多长时间执行该操作.
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        //確定我的位置
                        if(locDataNow != null) {
                            doInUIThread();
                        }
                    }
                }, 0, 5*1000);
            }
            if(event.getAction() == MotionEvent.ACTION_UP){
                //松开事件发生后执行代码的区域
                CommonView.displayShort(instance, "鬆開");
                //停止發送
                timer.cancel();
            }
            return false;
        }
    };

    @UiThread
    void doInUIThread() {
        //CommonView.displayShort(instance, "可以执行");
        //必須要獲取的成功
        //發送我的數據
        new SearchUserService(instance, SearchUserService.FunctionName.sendUserLocServer, locDataNow) {
            @Override
            public void doResult(Map<String, Object> resultMap) {
                CommonView.displayShort(instance, (String) resultMap.get("msg"));
            }
        };

        //同時獲取我周圍的數據
        new SearchUserService(instance, SearchUserService.FunctionName.findUserLoc) {
            @Override
            public void doResult(Map<String, Object> resultMap) {
                CommonView.displayShort(instance, (String) resultMap.get("msg"));
                List<UserLocYun> userLocYunList = (List<UserLocYun>) resultMap.get("userLocYunList");
                addItem(userLocYunList);
            }
        };
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Map<String, Object> map_content = (Map<String, Object>) parent.getItemAtPosition(position);
        String app_user_id = map_content.get("app_user_id").toString();
        String username = map_content.get("name").toString();
//        Intent intentToChat = new Intent(instance, ChatActivity_.class);
//        intentToChat.putExtra("app_user_id", app_user_id);
//        intentToChat.putExtra("username", username);
//        intentToChat.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//        startActivity(intentToChat);
        CommonView.displayShort(instance, app_user_id + " , " + username);
    }

    /**
     *初始化位置
     */
    private void initLocation() {

        locClient = new LocationClient( this );
        locClient.registerLocationListener( locListener );
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);//打开gps
        option.setAddrType("all");//返回的定位结果包含地址信息
        option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(999);//设置发起定位请求的间隔时间为10000ms
        option.disableCache(false);//禁止启用缓存定位
        option.setPriority(LocationClientOption.GpsFirst);
        option.setPoiNumber(2);    //最多返回POI个数
        option.setPoiDistance(500); //poi查询距离
        option.setPoiExtraInfo(true); //是否需要POI的电话和地址等详细信息
        locClient.setLocOption(option);
        locClient.start();
    }

    /**
     * 手动触发一次定位请求
     */
    public void requestLocClick(){
        locClient.requestLocation();
        CommonView.displayLong(instance, "正在定位……");
    }

    /**
     * 定位SDK监听函数
     */
    public class LocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation == null)
                return ;
            //保存定位数据
            locDataNow = LbsConvert.BDLocation2UserLocYun(bdLocation);
//            locDataNow.setUsername("王明东");
//            locDataNow.setApp_user_id("843804516070431639");
            locDataNow.setUsername(SystemAdapter.currentUser.getUsername());
            locDataNow.setApp_user_id(SystemAdapter.currentUser.getApp_user_id());
            locDataNow.setSex(0);
        }

        @Override
        public void onReceivePoi(BDLocation bdLocation) {

        }
    }

}
