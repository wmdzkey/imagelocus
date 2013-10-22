package m.z.imagelocus.activity.push.demo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.android.common.logging.Log;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.googlecode.androidannotations.annotations.*;
import m.z.common.CommonView;
import m.z.imagelocus.R;
import m.z.imagelocus.activity.push.tool.Utils;
import m.z.imagelocus.config.SystemConfig;
import org.json.JSONException;
import org.json.JSONObject;

@NoTitle
@EActivity(R.layout.activity_push_demo_main)
public class PushDemoActivity extends Activity {

    @ViewById(R.id.text)
	TextView infoText;
    @ViewById(R.id.btn_initAK)
	Button initWithApiKey;
    @ViewById(R.id.btn_rich)
	Button displayRichMedia;

    @AfterViews
    void init() {
        Resources resource = this.getResources();
        String pkgName = this.getPackageName();

        initWithApiKey.setOnClickListener(initApiKeyButtonListener());
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
        if (infoText != null) {
            infoText.setText(content);
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
	 * 
	 * @param intent
	 *            intent
	 */
	private void handleIntent(Intent intent) {
        CommonView.displayLong(this, "准备handleIntent");

		String action = intent.getAction();

		if (Utils.ACTION_RESPONSE.equals(action)) {

			String method = intent.getStringExtra(Utils.RESPONSE_METHOD);

			if (PushConstants.METHOD_BIND.equals(method)) {
				String toastStr = "";
				int errorCode = intent.getIntExtra(Utils.RESPONSE_ERRCODE, 0);
				if (errorCode == 0) {
					String content = intent
							.getStringExtra(Utils.RESPONSE_CONTENT);
					String appid = "";
					String channelid = "";
					String userid = "";

					try {
						JSONObject jsonContent = new JSONObject(content);
						JSONObject params = jsonContent
								.getJSONObject("response_params");
						appid = params.getString("appid");
						channelid = params.getString("channel_id");
						userid = params.getString("user_id");
					} catch (JSONException e) {
						Log.e(Utils.TAG, "Parse bind json infos error: " + e);
					}

					SharedPreferences sp = PreferenceManager
							.getDefaultSharedPreferences(this);
					Editor editor = sp.edit();
					editor.putString("appid", appid);
					editor.putString("channel_id", channelid);
					editor.putString("user_id", userid);
					editor.commit();

					showChannelIds();

					toastStr = "最后到这里：Bind Success";
				} else {
					toastStr = "Bind Fail, Error Code: " + errorCode;
					if (errorCode == 30607) {
						Log.d("Bind Fail", "update channel token-----!");
					}
				}

				Toast.makeText(this, toastStr, Toast.LENGTH_LONG).show();
			}
		} else if (Utils.ACTION_LOGIN.equals(action)) {
            CommonView.displayLong(this, "进什么Login啊");

            String accessToken = intent
					.getStringExtra(Utils.EXTRA_ACCESS_TOKEN);
			PushManager.startWork(getApplicationContext(),
					PushConstants.LOGIN_TYPE_ACCESS_TOKEN, accessToken);
		} else if (Utils.ACTION_MESSAGE.equals(action)) {
			String message = intent.getStringExtra(Utils.EXTRA_MESSAGE);
			String summary = "终于接收到消息了Receive message from server:\n\t";
			Log.e(Utils.TAG, summary + message);
			JSONObject contentJson = null;
			String contentStr = message;
			try {
				contentJson = new JSONObject(message);
				contentStr = contentJson.toString(4);//"【我被Json了】"
			} catch (JSONException e) {
				Log.d(Utils.TAG, "Parse message json exception.");
			}
			summary += contentStr;
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(summary);
			builder.setCancelable(true);
			Dialog dialog = builder.create();
			dialog.setCanceledOnTouchOutside(true);
			dialog.show();
		} else {
			Log.i(Utils.TAG, "Activity normally start!");
		}
	}


    private OnClickListener initApiKeyButtonListener() {
        return new OnClickListener() {

            public void onClick(View v) {
                // 以apikey的方式登录，一般放在主Activity的onCreate中
                PushManager.startWork(getApplicationContext(),
                        PushConstants.LOGIN_TYPE_API_KEY,
                        Utils.getMetaValue(PushDemoActivity.this, "api_key"));
            }
        };
    }


    @Click(R.id.btn_pushtest)
    void pushtestClicked(View view) {
        CommonView.displayLong(this, "准备开始发送数据");
        //sendMsgToUser(android.content.Context context,
        // java.lang.String app_id,
        // java.lang.String user_id,
        // java.lang.String base64MsgKey,
        // java.lang.String base64Msg)
        String user_id = "937186758361389669";
        //String base64MsgKey = "a4H3Hocn2AWnGnMgpqERqwbfzeGE6Iby";
        String base64MsgKey = "1";//最好设置成发送者id
        String base64Msg = "逃避，不一定躲得过，面对，不一定最难受；孤单，不一定不快乐；得到，不一定能长久；失去，不一定，不再有；转身，不一定最软弱；别急着说别无选择，别以为世上只有对与错，许多事情的答案都不是只有一个，所以我们，永远有路可以走！";
        PushManager.sendMsgToUser(this, SystemConfig.BDAppID, user_id, base64MsgKey, base64Msg);

    }
}
