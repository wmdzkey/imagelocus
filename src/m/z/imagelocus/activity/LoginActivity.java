package m.z.imagelocus.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.googlecode.androidannotations.annotations.*;
import m.z.common.CommonView;
import m.z.common.X3ProgressBar;
import m.z.common.X3SimpleProgressBar;
import m.z.imagelocus.R;
import m.z.imagelocus.activity.push.tool.JPushUtil;
import m.z.imagelocus.config.SystemAdapter;
import m.z.imagelocus.config.SystemConfig;
import m.z.imagelocus.entity.User;
import m.z.imagelocus.service.Service;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 登陆界面activity
 */
@NoTitle
@EActivity(R.layout.activity_login)
public class LoginActivity extends Activity {

    public static LoginActivity instance = null;

    @ViewById(R.id.iv_logo)
    ImageView iv_logo;

    @ViewById(R.id.et_username)
    EditText et_username;
    @ViewById(R.id.et_pwd)
    EditText et_pwd;

    @ViewById(R.id.btn_more_pop)
    Button btn_more_pop;//账户下拉按钮

    @ViewById(R.id.btn_login)
    Button btn_login;//登录按钮

    User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
    }

    @AfterViews
    void init() {
        Service.init(this);
        checkRegist();
    }

    @Click(R.id.btn_more_pop)
    void btn_more_pop_onClick() {
        CommonView.displayShort(instance, "尽请期待");
    }


    @Click(R.id.btn_login)
    public void btn_login_onClick() {
        if (!nullChecked()) return;
        user.setUsername(et_username.getText().toString());
        user.setPassword(et_pwd.getText().toString());

        login();
    }


    void login() {
        //检测绑定服务是否能够正常运行
        if(JPushUtil.isOpenNetwork(instance)) {
            if(SystemAdapter.currentAppUserId != null && !SystemAdapter.currentAppUserId.equals("")) {
                loginLocal();
            } else {
                //检测绑定服务是否成功获取app_user_id
                final X3SimpleProgressBar x3SimpleProgressBar = new X3SimpleProgressBar(instance);
                final Timer timer = new Timer(true);
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if(SystemAdapter.currentAppUserId != null || !SystemAdapter.currentAppUserId.equals("")) {
                            timer.cancel();
                            x3SimpleProgressBar.stop();
                            user.setApp_user_id(SystemAdapter.currentAppUserId);
                            loginLocal();
                        } else {
                            CommonView.display(instance, "请稍等正在绑定网络服务，稍后登陆会自完成");
                        }
                    }
                }, 0, 500);
            }
        } else {
            CommonView.display(instance, "无网络连接，请检查网络");
        }

    }

    @UiThread
    void loginLocal() {

        X3ProgressBar<Map<String, Object>> x3ProgressBar = new X3ProgressBar<Map<String, Object>>(instance, "正在登录...", false, null, false) {
            @Override
            public Map<String, Object> doWork() {
                return Service.userService.login(user);
            }

            @Override
            public void doResult(Map<String, Object> result) {
                if (result.get("info") != null && result.get("info").toString().equals("存在")) {
                    CommonView.displayShort(instance, "欢迎你回来," + SystemAdapter.currentUser.getUsername());
                    Intent intentToMain = new Intent(instance, MainActivity_.class);
                    intentToMain.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intentToMain);
                    finish();
                } else {
                    CommonView.displayShort(instance, "用户名或密码错误");
                    cancelRemenberUser();
                }
            }
        };
        x3ProgressBar.start();
    }


    /**检测是否注册过*/
    private void checkRegist() {
        //获取到sharepreference 对象， 参数一为xml文件名，参数为文件的可操作模式
        SharedPreferences sp = this.getSharedPreferences(SystemConfig.SPName, MODE_APPEND);
        //读取
        String username = null;
        String password = null;
        String isremenber = null;
        username = sp.getString("USERNAME", "none");
        password = sp.getString("PASSWORD", "none");
        isremenber = sp.getString("ISREMENBER", "none");

        if(isremenber.equals("YES")) {
            user.setUsername(username);
            user.setPassword(password);
            login();
        }
    }

    /**取消记住用户信息*/
    private void cancelRemenberUser() {
        //获取到sharepreference 对象， 参数一为xml文件名，参数为文件的可操作模式
        SharedPreferences sp = this.getSharedPreferences(SystemConfig.SPName, MODE_APPEND);
        //获取到编辑对象
        SharedPreferences.Editor edit = sp.edit();
        //添加新的值，可见是键值对的形式添加
        edit.putString("USERNAME", "none");
        edit.putString("PASSWORD", "none");
        edit.putString("ISREMENBER", "NO");
        //提交.
        edit.commit();
    }

    /**
     * 空检查
     */
    private boolean nullChecked() {
        if (et_username.getText() == null || et_username.getText().toString().trim().length() == 0) {
            CommonView.displayLong(instance, "用户名不能为空");
            return false;
        }

        if (et_pwd.getText() == null || et_pwd.getText().toString().trim().length() == 0) {
            CommonView.displayLong(instance, "密码不能为空");
            return false;
        }

        return true;
    }

    /**
     * 登录页-logo点击3下功能
     */
    int iv_logo_clickNum = 0;
    @Click(R.id.iv_logo)
    void iv_login_logo_onClick() {
        if (iv_logo_clickNum == 2) {
            iv_logo_clickNum = 0;
            CommonView.displayShort(instance, "JPushUtil.isOpenNetwork(instance)" + JPushUtil.isOpenNetwork(instance));
//            Intent intentToHide = new Intent(instance, MainActivity_.class);
//            startActivity(intentToHide);
        } else {
            CommonView.displayShort(instance, "这个版本没有隐藏功能");
        }
        iv_logo_clickNum++;
    }

    @Click(R.id.btn_regist)
    public void btn_regist_onClick() {
        cancelRemenberUser();
        Intent intent = new Intent(instance, RegistActivity_.class);
        startActivity(intent);
    }
}
