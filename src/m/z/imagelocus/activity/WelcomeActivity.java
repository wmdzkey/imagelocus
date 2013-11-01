package m.z.imagelocus.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.NoTitle;
import m.z.common.CommonView;
import m.z.imagelocus.R;
import m.z.imagelocus.activity.push.tool.PushUtils;
import m.z.imagelocus.config.SystemAdapter;
import m.z.imagelocus.config.SystemConfig;
import m.z.imagelocus.service.Service;


/**
 * @author Winnid
 * @version V1.2
 * @Title: 欢迎动画activity
 * @Package com.z.einstein.activity
 * @Description: 用户其启动界面时候的一个启动页面完成一些初始化工作
 * @date 2013-9-11
 */
@NoTitle
@EActivity(R.layout.activity_welcome)
public class WelcomeActivity extends Activity {

    Intent intent = null;

    @AfterViews
    void init() {
        initBaiduPush(this);
        //创建一个新的线程来显示欢迎动画，指定时间后结束，跳转至指定界面
        new Thread(new Runnable() {

            public void run() {
                try {
                    Thread.sleep(2000);
                    checkRegist();
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //获取应用的上下文，生命周期是整个应用，应用结束才会结束
                    getApplicationContext().startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();
        Service.init(this);
    }

    /**
     *初始化百度云推送
     * */
    private void initBaiduPush(Context context) {
        // 以apikey的方式登录，一般放在主Activity的onCreate中
        PushManager.startWork(context,
                PushConstants.LOGIN_TYPE_API_KEY,
                PushUtils.getMetaValue());

    }

    /**检测是否注册过*/
    private void checkRegist() {
        //获取到sharepreference 对象， 参数一为xml文件名，参数为文件的可操作模式
        SharedPreferences sp = this.getSharedPreferences(SystemConfig.SPName, MODE_APPEND);
        //读取
        String isremenber = null;
        isremenber = sp.getString("ISREMENBER", "none");

        if(isremenber.equals("YES")) {
            intent = new Intent(WelcomeActivity.this, LoginActivity_.class);
        } else {
            intent = new Intent(WelcomeActivity.this, RegistActivity_.class);
        }

        String app_user_id = null;
        app_user_id = sp.getString("user_id", "none");
        //CommonView.display(this, app_user_id);
        if(!app_user_id.equals("none")) {
            SystemAdapter.currentAppUserId = app_user_id;
        }
    }
}