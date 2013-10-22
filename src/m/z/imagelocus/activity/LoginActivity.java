package m.z.imagelocus.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.googlecode.androidannotations.annotations.*;
import m.z.common.CommonView;
import m.z.common.X3ProgressBar;
import m.z.imagelocus.R;
import m.z.imagelocus.activity.map.MapImpressActivity_;
import m.z.imagelocus.activity.push.tool.PushInitActivity_;
import m.z.imagelocus.config.SystemAdapter;
import m.z.imagelocus.entity.User;
import m.z.imagelocus.service.Service;

import java.util.Map;

/**
 * 登陆界面activity
 */
@NoTitle
@EActivity(R.layout.activity_login)
public class LoginActivity extends Activity {

    public static LoginActivity instance = null;

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
    }

    @Click(R.id.btn_login_more_pop)
    void btn_login_more_pop_onClick() {
        CommonView.displayShort(instance, "尽请期待");
    }


    @Click(R.id.btn_login)
    public void btn_login_onClick() {
        if (!nullChecked()) return;
        user.setUsername(et_login_username.getText().toString());
        user.setPassword(et_login_pwd.getText().toString());
        X3ProgressBar<Map<String, Object>> x3ProgressBar = new X3ProgressBar<Map<String, Object>>(instance, "正在登录...", false, null, false) {
            @Override
            public Map<String, Object> doWork() {
                return Service.userService.login(user);
            }

            @Override
            public void doResult(Map<String, Object> result) {
                if (result.get("info") != null && result.get("info").toString().equals("存在")) {
                    CommonView.displayShort(instance, "欢迎你回来," + SystemAdapter.currentUser.getUsername());
                } else {
                    CommonView.displayShort(instance, "欢迎你加入," + SystemAdapter.currentUser.getUsername());
                }
                Intent intentToMain = new Intent(instance, PushInitActivity_.class);
                intentToMain.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intentToMain);
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

        return true;
    }

    /**
     * 登录页-logo点击3下功能
     */
    int iv_login_logo_clickNum = 0;
    @Click(R.id.iv_login_logo)
    void iv_login_logo_onClick() {
        if (iv_login_logo_clickNum == 2) {
            iv_login_logo_clickNum = 0;
            CommonView.displayShort(instance, "有恒心，酒香不怕巷子深");
            Intent intentToHide = new Intent(instance, MapImpressActivity_.class);
            startActivity(intentToHide);
        } else {
            CommonView.displayShort(instance, "这个版本没有隐藏功能");
        }
        iv_login_logo_clickNum++;
    }
}
