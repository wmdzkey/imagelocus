package m.z.imagelocus.activity.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.NoTitle;
import m.z.imagelocus.R;
import m.z.imagelocus.service.Service;


/**
 * @author Winnid
 * @version V1.2
 * @Title: 设置activity
 * @Package m.z.imagelocus.activity
 * @Description: 用户其启动界面时候的一个启动页面完成一些初始化工作
 * @date 2013-9-11
 */
@NoTitle
@EActivity(R.layout.activity_setting)
public class SettingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}