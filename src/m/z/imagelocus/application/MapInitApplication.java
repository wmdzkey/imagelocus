package m.z.imagelocus.application;

import android.app.Application;
import android.content.Context;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.*;
import m.z.common.CommonView;
import m.z.imagelocus.config.SystemConfig;

public class MapInitApplication extends Application {

    private static MapInitApplication mInstance = null;
    public boolean m_bKeyRight = true;
    public BMapManager mBMapManager = null;

    public static final String strKey = SystemConfig.BDMapKey;
    /*
    	注意：为了给用户提供更安全的服务，Android SDK自v2.1.3版本开始采用了全新的Key验证体系。
    	因此，当您选择使用v2.1.3及之后版本的SDK时，需要到新的Key申请页面进行全新Key的申请，
    	申请及配置流程请参考开发指南的对应章节
    */

    public static MapInitApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        initEngineManager(this);
    }

    //初始化地图模块
    public void initEngineManager(Context context) {
        if (mBMapManager == null) {
            mBMapManager = new BMapManager(context);
        }

        if (!mBMapManager.init(strKey, new BaiduMapGeneralListener())) {
             CommonView.displayLong(MapInitApplication.getInstance().getApplicationContext(), "地图模块  初始化错误!");
        }
    }

    // 常用事件监听，用来处理通常的网络错误，授权验证错误等
    public static class BaiduMapGeneralListener implements MKGeneralListener {

        @Override
        public void onGetNetworkState(int iError) {
            if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
                CommonView.displayLong(MapInitApplication.getInstance().getApplicationContext(), "您的网络出错啦！");
            }
            else if (iError == MKEvent.ERROR_NETWORK_DATA) {
                CommonView.displayLong(MapInitApplication.getInstance().getApplicationContext(), "输入正确的检索条件！");
            }
        }

        @Override
        public void onGetPermissionState(int iError) {
            if (iError ==  MKEvent.ERROR_PERMISSION_DENIED) {
                //授权Key错误：
                CommonView.displayLong(MapInitApplication.getInstance().getApplicationContext(), "授权Key失效！请下载最新版本");
                MapInitApplication.getInstance().m_bKeyRight = false;
            }
        }
    }

}