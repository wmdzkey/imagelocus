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
import m.z.imagelocus.entity.User;
import m.z.imagelocus.service.Service;

/**
 * 登陆界面activity
 */
@NoTitle
@EActivity(R.layout.activity_regist)
public class RegistActivity extends Activity {

    public static RegistActivity instance = null;

    @ViewById(R.id.iv_logo)
    ImageView iv_logo;

    @ViewById(R.id.et_username)
    EditText et_username;
    @ViewById(R.id.et_pwd)
    EditText et_pwd;

    @ViewById(R.id.btn_more_pop)
    Button btn_more_pop;//账户下拉按钮

    @ViewById(R.id.btn_regist)
    Button btn_regist;//登录按钮

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


    @Click(R.id.btn_more_pop)
    void btn_login_more_pop_onClick() {
        CommonView.displayShort(instance, "尽请期待");
    }


    @Click(R.id.btn_regist)
    public void btn_regist_onClick() {
        if (!nullChecked()) return;
        user.setUsername(et_username.getText().toString());
        user.setPassword(et_pwd.getText().toString());
        regist();
    }

    void regist() {
        X3ProgressBar<User> x3ProgressBar = new X3ProgressBar<User>(instance, "正在检测...", false, null, false) {
            @Override
            public User doWork() {
                return Service.userService.exist(user);
            }

            @Override
            public void doResult(User result) {
                if (result != null) {
                    CommonView.displayShort(instance, "被被人用了，换个用户名吧");
                } else {
                    Intent _intent= new Intent(instance, RegistInfoActivity_.class);
                    _intent.putExtra("username", user.getUsername());
                    _intent.putExtra("password", user.getPassword());
                    _intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(_intent);
                }
            }
        };
        x3ProgressBar.start();
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
     * logo点击3下功能
     */
    int iv_logo_clickNum = 0;
    @Click(R.id.iv_logo)
    void iv_login_logo_onClick() {
        if (iv_logo_clickNum == 2) {
            iv_logo_clickNum = 0;
            CommonView.displayShort(instance, "这个版本没有隐藏功能");
//            Intent intentToHide = new Intent(instance, MainActivity_.class);
//            startActivity(intentToHide);
        }
        iv_logo_clickNum++;
    }

}
