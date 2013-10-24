package m.z.imagelocus.activity.push.client;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.*;
import com.baidu.android.pushservice.PushManager;
import com.googlecode.androidannotations.annotations.*;
import m.z.common.CommonView;
import m.z.imagelocus.R;
import m.z.imagelocus.adapter.push.PushChatAdapter;
import m.z.imagelocus.config.SystemConfig;
import m.z.imagelocus.config.SystemStore;
import m.z.imagelocus.entity.User;

import java.util.ArrayList;
import java.util.List;

@NoTitle
@EActivity(R.layout.activity_push_chat)
public class ChatActivity extends Activity {

    @ViewById(R.id.lv_chat_view)
    ListView lv_chat_view;
    @ViewById(R.id.et_chat_content)
    EditText et_chat_content;
    @ViewById(R.id.btn_chat_push)
	Button btn_chat_push;
    @ViewById(R.id.spn_push_userlist)
    Spinner spn_push_userlist;

    public static String sendUserId;

    public static PushChatAdapter pushChatAdapter;
    public static List<String> chatList = new ArrayList<String>();
    public static List<User> userList = new ArrayList<User>();
    public static ArrayAdapter<User> userAdapter;

    @AfterViews
    void init() {

        //初始化推送显示消息
        pushChatAdapter = new PushChatAdapter(this, chatList);
        lv_chat_view.setAdapter(pushChatAdapter);


        //初始化用户列表
        userList = SystemStore.userData;
        userAdapter = new ArrayAdapter<User>(this, android.R.layout.simple_spinner_item, userList);
        userAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_push_userlist.setAdapter(userAdapter);
        spn_push_userlist.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView arg0, View arg1, int arg2, long arg3) {
                sendUserId = userAdapter.getItem(arg2).getApp_user_id();
                //CommonView.displayLong(ChatActivity.this, "将要发送给" + userAdapter.getItem(arg2).getUsername());
            }
            public void onNothingSelected(AdapterView arg0) {
                arg0.setVisibility(View.VISIBLE);
            }
        });

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

	}

    @Click(R.id.btn_chat_push)
    void chatPushClicked(View view) {
        //sendMsgToUser(android.content.Context context,
        //java.lang.String app_id,
        //java.lang.String user_id,
        //java.lang.String base64MsgKey,
        //java.lang.String base64Msg)
        String user_id = sendUserId;
        String base64MsgKey = sendUserId;//最好设置成发送者id
        String base64Msg = et_chat_content.getText().toString();
        PushManager.sendMsgToUser(this, SystemConfig.BDAppID, user_id, base64MsgKey, base64Msg);
        et_chat_content.setText("");
    }

}
