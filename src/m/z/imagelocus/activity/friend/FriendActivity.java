package m.z.imagelocus.activity.friend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.googlecode.androidannotations.annotations.*;
import m.z.common.CommonView;
import m.z.common.X3ProgressBar;
import m.z.imagelocus.R;
import m.z.imagelocus.activity.push.tool.PushInitActivity_;
import m.z.imagelocus.adapter.friend.X3FriendAdapter;
import m.z.imagelocus.config.SystemAdapter;
import m.z.imagelocus.config.SystemStore;
import m.z.imagelocus.entity.Friend;
import m.z.imagelocus.entity.User;
import m.z.imagelocus.service.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Winnid
 * @Title:
 * @Description:
 * @date 13-10-11
 * @Version V1.0
 * Created by Winnid on 13-10-11.
 */
@NoTitle
@EActivity(R.layout.activity_friend)
public class FriendActivity extends Activity implements AdapterView.OnItemClickListener {

    public static FriendActivity instance = null;

    //左上角第一个按钮
    @ViewById(R.id.btn_left)
    Button btn_left;
    //中间标题
    @ViewById(R.id.tv_middle)
    TextView tv_middle;
    //右上角第一个按钮
    @ViewById(R.id.btn_right)
    Button btn_right;

    //数据适配器装载List
    @ViewById(R.id.lv_friend)
    ListView lv_friend;
    //数据
    List<User> list_friend;
    X3FriendAdapter x3ap_items_friend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
    }

    @Override
    protected void onStart() {
        super.onStart();
        //loadFriend();
    }

    @AfterViews
    void init() {
        tv_middle.setText("我的朋友");
        loadFriend();
    }

    private void loadFriend() {
        X3ProgressBar<List<User>> x3ProgressBar = new X3ProgressBar<List<User>>(instance, "获取好友列表...", false, null, false) {
            @Override
            public List<User> doWork() {
                List<Friend> friendList = Service.friendService.find(SystemAdapter.currentUser.getApp_user_id());
                List<User> userList = new ArrayList<User>();
                if(friendList != null && friendList.size() != 0) {
                    for (Friend friend : friendList) {
                        User user = new User();
                        user.setApp_user_id(friend.getApp_friend_user_id());
                        user.setUsername(friend.getFriendname());
                        user.setUserhead(friend.getFriendhead());
                        userList.add(user);
                    }
                }
                return userList;
            }

            @Override
            public void doResult(List<User> result) {
                if (result != null && result.size() != 0) {
                    list_friend = result;
                    x3ap_items_friend = new X3FriendAdapter(instance, list_friend);
                    x3ap_items_friend.notifyDataSetChanged();
                    lv_friend.setAdapter(x3ap_items_friend);
                    lv_friend.setOnItemClickListener(instance);
                } else {
                    CommonView.displayShort(instance, "你还没有好友快去添加吧");
                }
            }
        };
    }


    @Click(R.id.btn_left)
    void btn_left_onClick() {
        loadFriend();
        CommonView.displayShort(this, "已刷新");
    }

    @Click(R.id.btn_right)
    void btn_right_onClick() {
        Intent _intent = new Intent(instance, FriendSearchActivity_.class);
        startActivity(_intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Map<String, Object> map_content = (Map<String, Object>) parent.getItemAtPosition(position);
        String app_user_id = map_content.get("app_user_id").toString();
        String username = map_content.get("name").toString();
        String userhead = map_content.get("img").toString();
        Intent intentToChat = new Intent(instance, ChatActivity_.class);
        intentToChat.putExtra("app_user_id", app_user_id);
        intentToChat.putExtra("username", username);
        intentToChat.putExtra("userhead", userhead);
        intentToChat.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intentToChat);
    }
}
