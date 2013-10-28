package m.z.imagelocus.activity.push.tool;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.baidu.android.common.logging.Log;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.googlecode.androidannotations.annotations.*;
import com.lidroid.xutils.util.LogUtils;
import m.z.common.CommonView;
import m.z.common.X3Dialog;
import m.z.common.X3ProgressBar;
import m.z.common.X3SimpleProgressBar;
import m.z.imagelocus.R;
import m.z.imagelocus.activity.MainActivity_;
import m.z.imagelocus.activity.push.client.ChatActivity_;
import m.z.imagelocus.config.SystemAdapter;
import m.z.imagelocus.entity.User;
import m.z.imagelocus.service.Service;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

@NoTitle
@EActivity(R.layout.activity_push_init)
public class PushInitActivity extends Activity {

    public static PushInitActivity instance = null;

    @ViewById(R.id.text_init_message)
	TextView text_init_message;

    @ViewById(R.id.btn_init_go)
    Button btn_init_go;
    @ViewById(R.id.btn_init_getid)
    Button btn_init_getid;

    Timer timer;
    X3SimpleProgressBar x3SimpleProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
    }

    @AfterViews
    void init() {
        // 以apikey的方式登录，一般放在主Activity的onCreate中
        PushManager.startWork(getApplicationContext(),
                PushConstants.LOGIN_TYPE_API_KEY,
                Utils.getMetaValue());
        btn_init_go.setClickable(false);

        x3SimpleProgressBar = new X3SimpleProgressBar(instance);

        timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(PushManager.isConnected(instance)) {
                    timer.cancel();
                    x3SimpleProgressBar.stop();
                }
            }
        }, 0, 100);
    }

	@Override
	public void onStart() {
		super.onStart();
		PushManager.activityStarted(this);
	}

    @Override
    public void onStop() {
        super.onStop();
        PushManager.activityStoped(this);
    }

	@Override
	public void onResume() {
		super.onResume();
		showChannelIds();
	}

    private void showChannelIds() {
        String appId = null;
        String channelId = null;
        String clientId = null;

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        appId = sp.getString("appid", "");
        channelId = sp.getString("channel_id", "");
        clientId = sp.getString("user_id", "");

        String content = "\t应用id App ID: " + appId + "\n\t频道id channel ID: " + channelId
                + "\n\t用户id User ID: " + clientId + "\n\t";
        if (text_init_message != null) {
            text_init_message.setText(content);
        }
    }

	@Override
	protected void onNewIntent(Intent intent) {
		// 如果要统计Push引起的用户使用应用情况，请实现本方法，且加上这一个语句
		setIntent(intent);
		handleIntent(intent);
	}


	/**
	 * 处理Intent
	 * @param intent
	 */
	private void handleIntent(Intent intent) {

		String action = intent.getAction();

		if (Utils.ACTION_RESPONSE.equals(action)) {

			String method = intent.getStringExtra(Utils.RESPONSE_METHOD);
            int errorCode = intent.getIntExtra(Utils.RESPONSE_ERRCODE, 0);
            String content = intent.getStringExtra(Utils.RESPONSE_CONTENT);

            //对应是startWork的返回
			if (PushConstants.METHOD_BIND.equals(method)) {
                String toastStr = "";
				if (errorCode == 0) {
					String appid = "";
					String channelid = "";
					String userid = "";
					try {
						JSONObject jsonContent = new JSONObject(content);
						JSONObject params = jsonContent.getJSONObject("response_params");
						appid = params.getString("appid");
						channelid = params.getString("channel_id");
						userid = params.getString("user_id");
					} catch (JSONException e) {
						Log.e(Utils.TAG, "Parse bind json infos error: " + e);
					}

					SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
					Editor editor = sp.edit();
					editor.putString("appid", appid);
					editor.putString("channel_id", channelid);
					editor.putString("user_id", userid);
					editor.commit();

					showChannelIds();

                    if(SystemAdapter.currentUser == null) {
                        return;
                    }

                    //保存app_user_id
                    savePushUserId(userid);

                    PushManager.bind(this, PushConstants.BIND_STATUS_ONLINE);
                    PushMessageReceiver.bindingState = true;
					toastStr = "初始化完成，绑定成功";
                    btn_init_go.setClickable(true);
                    goActivity();
				} else {
					toastStr = "绑定失败,错误代码: " + errorCode;
                    //弹出对话框，提示重试
                    bindRetryDialog();
                    if (errorCode == 30607) {
						Log.d("Bind Fail", "update channel token-----!");
					}
				}

				CommonView.displayShort(this, toastStr);
			}
		} else {
			LogUtils.i("Push Init Activity 正常 启动!");
		}
	}

    @Click(R.id.btn_init_go)
    void initGoClicked(View view) {
        CommonView.displayShort(this, "进入聊天界面");
        Intent intentToChat = new Intent(PushInitActivity.this, ChatActivity_.class);
        startActivity(intentToChat);
    }

    @Click(R.id.btn_init_getid)
    void initGetIdClicked(View view) {
        CommonView.displayShort(this, "我的云推id :" + SystemAdapter.currentUser.getApp_user_id());
    }

    protected void bindRetryDialog() {
        new X3Dialog(instance, "绑定失败要重试么？") {
            @Override
            public void doConfirm() {
                // 以apikey的方式登录，一般放在主Activity的onCreate中
                PushManager.startWork(getApplicationContext(),
                        PushConstants.LOGIN_TYPE_API_KEY,
                        Utils.getMetaValue());
            }
            @Override
            public void doCancel() {

            }
        };
    }


    private void goActivity() {
        Intent intentToMain = new Intent(PushInitActivity.this, MainActivity_.class);
        intentToMain.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivityForResult(intentToMain, 0);
        finish();
    }


    //保存登录用户云推id
    private void savePushUserId(String clientId) {
        User user = SystemAdapter.currentUser;
        user.setApp_user_id(clientId);
        Service.userService.saveOrUpdate(user);
    }

    @Override
    public void onBackPressed() {
        CommonView.displayShort(instance, "稍等片刻...");
    }
}
