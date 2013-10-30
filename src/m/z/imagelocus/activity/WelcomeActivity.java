package m.z.imagelocus.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.NoTitle;
import m.z.imagelocus.R;
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
        //创建一个新的线程来显示欢迎动画，指定时间后结束，跳转至指定界面
        new Thread(new Runnable() {

            public void run() {
                try {
                    Thread.sleep(1500);
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
    }
}