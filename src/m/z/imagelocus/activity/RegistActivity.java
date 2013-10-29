package m.z.imagelocus.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.googlecode.androidannotations.annotations.*;
import m.z.common.CommonView;
import m.z.common.X3ProgressBar;
import m.z.imagelocus.R;
import m.z.imagelocus.activity.push.tool.PushInitActivity_;
import m.z.imagelocus.config.SystemAdapter;
import m.z.imagelocus.config.SystemConfig;
import m.z.imagelocus.entity.User;
import m.z.imagelocus.service.Service;
import m.z.util.SIMCardUtil;

import java.util.Map;

/**
 * 登陆界面activity
 */
@NoTitle
@EActivity(R.layout.activity_regist)
public class RegistActivity extends Activity {

    public static RegistActivity instance = null;

    @ViewById(R.id.iv_login_logo)
    ImageView iv_login_logo;

    @ViewById(R.id.et_login_username)
    EditText et_login_username;
    @ViewById(R.id.et_login_pwd)
    EditText et_login_pwd;

    @ViewById(R.id.btn_login_more_pop)
    Button btn_login_more_pop;//账户下拉按钮

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

        //检测是否注册过
        checkRegist();
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

    /**记住用户信息*/
    private void remenberUser() {
        //获取到sharepreference 对象， 参数一为xml文件名，参数为文件的可操作模式
        SharedPreferences sp = this.getSharedPreferences(SystemConfig.SPName, MODE_APPEND);
        //获取到编辑对象
        SharedPreferences.Editor edit = sp.edit();
        //添加新的值，可见是键值对的形式添加
        edit.putString("USERNAME", user.getUsername());
        edit.putString("PASSWORD", user.getPassword());
        edit.putString("ISREMENBER", "YES");
        //提交.
        edit.commit();
    }


    @Click(R.id.btn_login_more_pop)
    void btn_login_more_pop_onClick() {
        CommonView.displayShort(instance, "尽请期待");
    }


    @Click(R.id.btn_regist)
    public void btn_regist_onClick() {
        if (!nullChecked()) return;
        user.setUsername(et_login_username.getText().toString());
        user.setPassword(et_login_pwd.getText().toString());
        regist();
    }

    void regist() {
        X3ProgressBar<Map<String, Object>> x3ProgressBar = new X3ProgressBar<Map<String, Object>>(instance, "正在登录...", false, null, false) {
            @Override
            public Map<String, Object> doWork() {
                return Service.userService.regist(user);
            }

            @Override
            public void doResult(Map<String, Object> result) {
                if (result.get("info") != null && result.get("info").toString().equals("存在")) {
                    CommonView.displayShort(instance, "被被人用了，换个用户名吧");
                } else {
                    CommonView.displayShort(instance, "欢迎你加入," + SystemAdapter.currentUser.getUsername());
                    Intent intentToMain = new Intent(instance, PushInitActivity_.class);
                    intentToMain.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intentToMain);
                    remenberUser();
                    finish();
                }
            }
        };
    }

    void login() {
        X3ProgressBar<Map<String, Object>> x3ProgressBar = new X3ProgressBar<Map<String, Object>>(instance, "正在登录...", false, null, false) {
            @Override
            public Map<String, Object> doWork() {
                return Service.userService.login(user);
            }

            @Override
            public void doResult(Map<String, Object> result) {
                if (result.get("info") != null && result.get("info").toString().equals("存在")) {
                    CommonView.displayShort(instance, "欢迎你回来," + SystemAdapter.currentUser.getUsername());
                    Intent intentToMain = new Intent(instance, PushInitActivity_.class);
                    intentToMain.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intentToMain);
                    finish();
                } else {
                    CommonView.displayShort(instance, "用户名或密码错误");
                    Intent intent = new Intent(instance, LoginActivity_.class);
                    startActivity(intent);
                }
            }
        };
    }

    /**
     * 空检查
     */
    private boolean nullChecked() {
        if (et_login_username.getText() == null || et_login_username.getText().toString().trim().length() == 0) {
            CommonView.displayLong(instance, "用户名不能为空");
            return false;
        }

        if (et_login_pwd.getText() == null || et_login_pwd.getText().toString().trim().length() == 0) {
            CommonView.displayLong(instance, "密码不能为空");
            return false;
        }

        if (user.getUserhead() == null || user.getUserhead().equals("")) {
            CommonView.displayLong(instance, "请选择一个头像");
            return false;
        }

        return true;
    }

    /**
     * logo点击3下功能
     */
    int iv_login_logo_clickNum = 0;
    @Click(R.id.iv_login_logo)
    void iv_login_logo_onClick() {
        if (iv_login_logo_clickNum == 2) {
            iv_login_logo_clickNum = 0;
            CommonView.displayShort(instance, "这个版本没有隐藏功能");
//            CommonView.displayShort(instance, "有恒心，酒香不怕巷子深");
//            Intent intentToHide = new Intent(instance, MainActivity_.class);
//            startActivity(intentToHide);
        }
        iv_login_logo_clickNum++;
    }

    @ViewById(R.id.iv_user_head_1)
    ImageView iv_user_head_1;
    @ViewById(R.id.iv_user_head_2)
    ImageView iv_user_head_2;
    @ViewById(R.id.iv_user_head_3)
    ImageView iv_user_head_3;
    @ViewById(R.id.iv_user_head_4)
    ImageView iv_user_head_4;
    @ViewById(R.id.iv_user_head_5)
    ImageView iv_user_head_5;
    @ViewById(R.id.iv_user_head_6)
    ImageView iv_user_head_6;
    @ViewById(R.id.iv_user_head_7)
    ImageView iv_user_head_7;
    @ViewById(R.id.iv_user_head_8)
    ImageView iv_user_head_8;
    @ViewById(R.id.iv_user_head_9)
    ImageView iv_user_head_9;
    @ViewById(R.id.iv_user_head_10)
    ImageView iv_user_head_10;


    @Click({R.id.iv_user_head_1,R.id.iv_user_head_2,R.id.iv_user_head_3,R.id.iv_user_head_4,R.id.iv_user_head_5,
            R.id.iv_user_head_6,R.id.iv_user_head_7,R.id.iv_user_head_8,R.id.iv_user_head_9,R.id.iv_user_head_10})
    void iv_head_onClick(View view) {
        iv_user_head_1.setImageDrawable(null);
        iv_user_head_2.setImageDrawable(null);
        iv_user_head_3.setImageDrawable(null);
        iv_user_head_4.setImageDrawable(null);
        iv_user_head_5.setImageDrawable(null);
        iv_user_head_6.setImageDrawable(null);
        iv_user_head_7.setImageDrawable(null);
        iv_user_head_8.setImageDrawable(null);
        iv_user_head_9.setImageDrawable(null);
        iv_user_head_10.setImageDrawable(null);
        switch (view.getId()) {
            case R.id.iv_user_head_1:
                iv_user_head_1.setImageResource(R.drawable.select);
                user.setUserhead("1");
                break;
            case R.id.iv_user_head_2:
                iv_user_head_2.setImageResource(R.drawable.select);
                user.setUserhead("2");
                break;
            case R.id.iv_user_head_3:
                iv_user_head_3.setImageResource(R.drawable.select);
                user.setUserhead("3");
                break;
            case R.id.iv_user_head_4:
                iv_user_head_4.setImageResource(R.drawable.select);
                user.setUserhead("4");
                break;
            case R.id.iv_user_head_5:
                iv_user_head_5.setImageResource(R.drawable.select);
                user.setUserhead("5");
                break;
            case R.id.iv_user_head_6:
                iv_user_head_6.setImageResource(R.drawable.select);
                user.setUserhead("6");
                break;
            case R.id.iv_user_head_7:
                iv_user_head_7.setImageResource(R.drawable.select);
                user.setUserhead("7");
                break;
            case R.id.iv_user_head_8:
                iv_user_head_8.setImageResource(R.drawable.select);
                user.setUserhead("8");
                break;
            case R.id.iv_user_head_9:
                iv_user_head_9.setImageResource(R.drawable.select);
                user.setUserhead("9");
                break;
            case R.id.iv_user_head_10:
                iv_user_head_10.setImageResource(R.drawable.select);
                user.setUserhead("10");
                break;
        }
    }
}
