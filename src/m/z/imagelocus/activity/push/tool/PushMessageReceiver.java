package m.z.imagelocus.activity.push.tool;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.c;
import com.google.gson.Gson;
import m.z.common.CommonView;
import m.z.imagelocus.activity.friend.ChatActivity_;
import m.z.imagelocus.activity.map.MapLocusActivity_;
import m.z.imagelocus.activity.push.demo.CustomActivity;

import java.util.Map;

/**
 * Push消息处理receiver
 */
public class PushMessageReceiver extends BroadcastReceiver {
	/** TAG to Log */
	public static final String TAG = PushMessageReceiver.class.getSimpleName();
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
	public void onReceive(final Context context, Intent intent) {

		Log.d(TAG, ">>> Receive intent: \r\n" + intent);

		if (intent.getAction().equals(PushConstants.ACTION_MESSAGE)) {
            onReceiverMessage(context, intent);

		} else if (intent.getAction().equals(PushConstants.ACTION_RECEIVE)) {

            //处理绑定等方法的返回数据
			//PushManager.startWork()的返回值通过PushConstants.METHOD_BIND得到

			//获取方法
			final String method = intent.getStringExtra(PushConstants.EXTRA_METHOD);

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
                Log.d(TAG, "onMessage: method : " + method);
                Log.d(TAG, "onMessage: result : " + errorCode);
                Log.d(TAG, "onMessage: content : " + content);

                Intent responseIntent = null;
                responseIntent = new Intent(Utils.ACTION_RESPONSE);
                responseIntent.putExtra(Utils.RESPONSE_METHOD, method);
                responseIntent.putExtra(Utils.RESPONSE_ERRCODE, errorCode);
                responseIntent.putExtra(Utils.RESPONSE_CONTENT, content);
                responseIntent.setClass(context, PushInitActivity_.class);
                responseIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(responseIntent);
            }
            useNum++;
       	//可选。通知用户点击事件处理
		} else if (intent.getAction().equals(PushConstants.ACTION_RECEIVER_NOTIFICATION_CLICK)) {

            CommonView.displayLong(context, "点击消息提醒后，需要我执行什么呢？");
            Log.d(TAG, "intent=" + intent.toUri(0));
			//自定义内容的json串
        	Log.d(TAG, "EXTRA_EXTRA = " + intent.getStringExtra(PushConstants.EXTRA_EXTRA));

			Intent aIntent = new Intent();
			aIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			//设置自定义消息提醒显示
			aIntent.setClass(context, CustomActivity.class);
            // 通知标题
            String title = intent.getStringExtra(PushConstants.EXTRA_NOTIFICATION_TITLE);
            // 通知内容
            String content = intent.getStringExtra(PushConstants.EXTRA_NOTIFICATION_CONTENT);
			aIntent.putExtra(PushConstants.EXTRA_NOTIFICATION_TITLE, title);
			aIntent.putExtra(PushConstants.EXTRA_NOTIFICATION_CONTENT, content);
			context.startActivity(aIntent);
		}
	}

    /**
     *接收到消息处理
     * */
    private void onReceiverMessage(Context context, Intent intent) {

        //获取消息内容
        String message = intent.getExtras().getString(PushConstants.EXTRA_PUSH_MESSAGE_STRING);
        String EXTRA_EXTRA = intent.getStringExtra(PushConstants.EXTRA_EXTRA);
        //消息的用户自定义内容读取方式
        Log.i(TAG, "onMessage: " + message);
        //自定义内容的json串
        Log.d(TAG, "EXTRA_EXTRA = " + EXTRA_EXTRA);


        //用户在此自定义处理消息
        String contentStr = message;
        //过滤传递后产生对出一对[]的问题
        if (message.startsWith("[") && message.endsWith("]")){
            contentStr = message.substring(2,message.length()-2);
        }
        //CommonView.displayLong(context, contentStr);


        //转化json对象
        Gson gson = new Gson();
        Map<String, String> jsonMap = gson.fromJson(contentStr, Map.class);


        //发送位置信息
        if(jsonMap.containsKey("localInfo")) {
            Intent responseIntent = null;
            responseIntent = new Intent();
            responseIntent.putExtra("localInfo", jsonMap.get("localInfo"));
            responseIntent.setClass(context, MapLocusActivity_.class);
            responseIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(responseIntent);
        }
        //请求位置信息
        if(jsonMap.containsKey("find")) {
            Intent responseIntent = null;
            responseIntent = new Intent();
            responseIntent.putExtra("find", jsonMap.get("find"));
            responseIntent.setClass(context, MapLocusActivity_.class);
            responseIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(responseIntent);
        }
        //加入聊天列表
        if(jsonMap.containsKey("chat_msg")) {
            Intent responseIntent = null;
            responseIntent = new Intent(context, ChatActivity_.class);
            responseIntent.putExtra("chat_msg", jsonMap.get("chat_msg"));
            responseIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(responseIntent);
        }

    }

}
