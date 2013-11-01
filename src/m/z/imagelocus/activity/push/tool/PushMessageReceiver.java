package m.z.imagelocus.activity.push.tool;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.lidroid.xutils.util.LogUtils;
import m.z.common.CommonView;
import m.z.common.X3Dialog;
import m.z.imagelocus.activity.friend.ChatActivity_;
import m.z.imagelocus.activity.map.MapLocusActivity_;
import m.z.imagelocus.config.SystemAdapter;
import m.z.imagelocus.config.SystemConfig;
import m.z.imagelocus.service.Service;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Push消息处理receiver
 */
public class PushMessageReceiver extends BroadcastReceiver {

    public Context mContext;
    public Intent mIntent;

    //使用次数
    public static int useNum = 0;
    public static boolean bindingState = false;

    /**
	 * @param context
	 *            Context
	 * @param intent
	 *            接收的intent
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
        LogUtils.d(">>> Receive intent: \r\n" + intent);
        mContext = context;
        mIntent = intent;
		if (intent.getAction().equals(PushConstants.ACTION_MESSAGE)) {
            onActionMessage(context, intent);
		} else if (intent.getAction().equals(PushConstants.ACTION_RECEIVE)) {
            onActionReceive(context, intent);
        //可选。通知用户点击事件处理
        } else if (intent.getAction().equals(PushConstants.ACTION_RECEIVER_NOTIFICATION_CLICK)) {
            onActionReceiveNotifivationClick(context, intent);
        }
    }


    private void onActionMessage(Context context, Intent intent) {
        //获取消息内容
        String message = intent.getExtras().getString(PushConstants.EXTRA_PUSH_MESSAGE_STRING);
        String EXTRA_EXTRA = intent.getStringExtra(PushConstants.EXTRA_EXTRA);
        //消息的用户自定义内容读取方式
        LogUtils.i( "onMessage: " + message);
        //自定义内容的json串
        LogUtils.i("EXTRA_EXTRA = " + EXTRA_EXTRA);


        //用户在此自定义处理消息
        String contentStr = message;
        //过滤传递后产生对出一对[]的问题
        if (message.startsWith("[") && message.endsWith("]")){
            contentStr = message.substring(2,message.length()-2);
        }
        CommonView.displayLong(context, contentStr);

        //处理消息Json串
        processCustomMessage(contentStr);
    }


    private void onActionReceive(Context context, Intent intent) {
        //处理绑定等方法的返回数据
        //PushManager.startWork()的返回值通过PushConstants.METHOD_BIND得到
        String method = intent.getStringExtra(PushConstants.EXTRA_METHOD);

        //方法返回错误码。若绑定返回错误（非0），则应用将不能正常接收消息。
        //绑定失败的原因有多种，如网络原因，或access token过期。
        //请不要在出错时进行简单的startWork调用，这有可能导致死循环。
        //可以通过限制重试次数，或者在其他时机重新调用来解决。
        int errorCode = intent.getIntExtra(PushConstants.EXTRA_ERROR_CODE, PushConstants.ERROR_SUCCESS);

        //获取内容json格式
        //{
        //    “request_id”:12394838223,
        //    “response_params”:
        //    {
        //       “appid”: 0696110321,
        //       “user_id”:sdfiowefjwo92f-23423dfafdssadf-asfdas,
        //       “channel_id”:4923859573096872165,
        //    }
        //}
        String content = "";
        if (intent.getByteArrayExtra(PushConstants.EXTRA_CONTENT) != null) {
            //返回内容
            content = new String(intent.getByteArrayExtra(PushConstants.EXTRA_CONTENT));
        }

        //进行绑定
        if(!bindingState) {
            //用户在此自定义处理消息,以下代码为demo界面展示用
            LogUtils.d( "onMessage: method : " + method);
            LogUtils.d( "onMessage: result : " + errorCode);
            LogUtils.d( "onMessage: content : " + content);

            pushBind(method, errorCode, content);
        }
        useNum ++;
    }

    /**
     * 进行绑定
     */
    private void pushBind(String method, int errorCode, String content) {
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
                LogUtils.e("Parse bind json infos error: " + e);
            }

            //保存到SP
            SharedPreferences sp = mContext.getSharedPreferences(SystemConfig.SPName, mContext.MODE_APPEND);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("appid", appid);
            editor.putString("channel_id", channelid);
            editor.putString("user_id", userid);
            editor.commit();

            content = "\t应用App ID: " + appid + "\n\t频道channel ID: " + channelid
                    + "\n\t用户User ID: " + userid + "\n\t";

            if(Service.userService != null && userid != null) {
                SystemAdapter.currentAppUserId = userid;
                Service.userService.savePushUserId(userid);
            }

            PushManager.bind(mContext, PushConstants.BIND_STATUS_ONLINE);
            PushMessageReceiver.bindingState = true;
            //CommonView.display(mContext, content);
            //CommonView.display(mContext, "绑定成功");
        } else {
            CommonView.displayLong(mContext, "绑定失败,错误代码: " + errorCode);
            //弹出对话框，提示重试
            //bindRetryDialog();
        }
    }

    private void bindRetryDialog() {
        new X3Dialog(mContext, "绑定失败要重试么？") {
            @Override
            public void doConfirm() {
                // 以apikey的方式登录，一般放在主Activity的onCreate中
                PushManager.startWork(mContext,
                        PushConstants.LOGIN_TYPE_API_KEY,
                        PushUtils.getMetaValue());
            }
            @Override
            public void doCancel() {

            }
        };
    }


    /**
     *通知用户点击事件处理
     * */
    private void onActionReceiveNotifivationClick(Context context, Intent intent) {
        CommonView.displayLong(context, "点击消息提醒后，需要我执行什么呢？");
        LogUtils.d( "intent=" + intent.toUri(0));
        //自定义内容的json串
        LogUtils.d( "EXTRA_EXTRA = " + intent.getStringExtra(PushConstants.EXTRA_EXTRA));

//			Intent aIntent = new Intent();
//			aIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			//设置自定义消息提醒显示
//			aIntent.setClass(context, CustomActivity.class);
//            // 通知标题
//            String title = intent.getStringExtra(PushConstants.EXTRA_NOTIFICATION_TITLE);
//            // 通知内容
//            String content = intent.getStringExtra(PushConstants.EXTRA_NOTIFICATION_CONTENT);
//			aIntent.putExtra(PushConstants.EXTRA_NOTIFICATION_TITLE, title);
//			aIntent.putExtra(PushConstants.EXTRA_NOTIFICATION_CONTENT, content);
//			context.startActivity(aIntent);
    }


    //send msg to MainActivity
    private void processCustomMessage(String contentStr) {
        //用户在此自定义处理消息
        Map<String, String> jsonMap = SystemAdapter.gson.fromJson(contentStr, Map.class);


        //发送位置信息
        if(jsonMap.containsKey("localInfo")) {
            Intent responseIntent = null;
            responseIntent = new Intent();
            responseIntent.putExtra("localInfo", jsonMap.get("localInfo"));
            responseIntent.setClass(mContext, MapLocusActivity_.class);
            responseIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(responseIntent);
        }
        //请求位置信息
        if(jsonMap.containsKey("find")) {
            Intent responseIntent = null;
            responseIntent = new Intent();
            responseIntent.putExtra("find", jsonMap.get("find"));
            responseIntent.setClass(mContext, MapLocusActivity_.class);
            responseIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(responseIntent);
        }
        //加入聊天列表
        if(jsonMap.containsKey("chat_msg")) {
            Intent responseIntent = null;
            responseIntent = new Intent(mContext, ChatActivity_.class);
            responseIntent.putExtra("chat_msg", jsonMap.get("chat_msg"));
            responseIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(responseIntent);
        }

    }
}