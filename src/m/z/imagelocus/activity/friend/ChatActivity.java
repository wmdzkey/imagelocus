package m.z.imagelocus.activity.friend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import com.baidu.android.pushservice.PushManager;
import com.google.gson.Gson;
import com.googlecode.androidannotations.annotations.*;
import m.z.common.CommonView;
import m.z.imagelocus.R;
import m.z.imagelocus.config.SystemAdapter;
import m.z.imagelocus.adapter.friend.ChatAdapter;
import m.z.imagelocus.config.SystemConfig;
import m.z.imagelocus.entity.Chat;

import java.util.*;

@NoTitle
@EActivity(R.layout.activity_friend_chat)
public class ChatActivity extends Activity {

    public static ChatActivity instance = null;
    public Gson gson = new Gson();

    @ViewById(R.id.tv_middle)
    TextView tv_middle;
    @ViewById(R.id.btn_send)
    Button btn_send;
    @ViewById(R.id.et_content)
    EditText et_content;

    @ViewById(R.id.lv_chat)
    ListView lv_chat;

    List<Chat> chatList = null;
    ChatAdapter chatAdapter = null;

    public static String sendUserId;
    public static String sendUsername;
    public static String sendUserhead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
    }

    @AfterViews
    void init() {
        sendUserId = getIntent().getStringExtra("app_user_id");
        sendUsername = getIntent().getStringExtra("username");
        sendUserhead = getIntent().getStringExtra("userhead");
        CommonView.displayLong(instance, "将要发送给" + sendUsername);

        tv_middle.setText(sendUsername);
        btn_send.setText("发送");

        chatList = new ArrayList<Chat>();
        chatAdapter = new ChatAdapter(this,chatList);
        lv_chat.setAdapter(chatAdapter);
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
        String message = intent.getStringExtra("chat_msg");
        Chat chat = gson.fromJson(message, Chat.class);
        chat.setComeMsg(true);
        chatList.add(chat);
        chatAdapter.notifyDataSetChanged();
        lv_chat.setSelection(chatList.size() - 1);
	}

    @Click(R.id.btn_send)
    void btn_send_onClick() {

        if (!et_content.getText().toString().equals("")) {
            //发送消息
            send(et_content.getText().toString());
        }else {
            CommonView.displayShort(instance, "写点什么吧~");
        }
    }

    private void send(String chat_msg) {
        Map<String, String> jsonMap = new HashMap<String, String>();

        Chat chat = new Chat();
        chat.setChatTime(new Date());
        chat.setContent(chat_msg);
        chat.setComeMsg(false);
        chat.setUsername(SystemAdapter.currentUser.getUsername());
        chat.setUserhead(SystemAdapter.currentUser.getUserhead());
        chat.setUser_id(SystemAdapter.currentUser.getId());

        jsonMap.put("chat_msg", gson.toJson(chat));
        String msg = gson.toJson(jsonMap);

        //sendMsgToUser(android.content.Context context,
        //java.lang.String app_id,
        //java.lang.String user_id,
        //java.lang.String base64MsgKey,
        //java.lang.String base64Msg)
        String user_id = sendUserId;
        String base64MsgKey = SystemAdapter.currentUser.getApp_user_id();//最好设置成发送者id
        String base64Msg = msg;
        PushManager.sendMsgToUser(this, SystemConfig.BDAppID, user_id, base64MsgKey, base64Msg);

        chatList.add(chat);
        chatAdapter.notifyDataSetChanged();
        lv_chat.setSelection(chatList.size() - 1);
        et_content.setText("");
    }


}
