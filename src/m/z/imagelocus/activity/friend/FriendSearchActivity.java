package m.z.imagelocus.activity.friend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.googlecode.androidannotations.annotations.*;
import m.z.common.CommonView;
import m.z.imagelocus.R;
import m.z.imagelocus.adapter.friend.X3FriendAdapter;
import m.z.imagelocus.config.SystemStore;
import m.z.imagelocus.entity.User;

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
public class FriendSearchActivity extends Activity implements AdapterView.OnItemClickListener {

    public static FriendSearchActivity instance = null;

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

    @AfterViews
    void init() {
        tv_middle.setText("查找朋友");

        list_friend = SystemStore.userData;
        x3ap_items_friend = new X3FriendAdapter(instance, list_friend);
        lv_friend.setAdapter(x3ap_items_friend);
        lv_friend.setOnItemClickListener(this);

    }


    @Click(R.id.btn_left)
    void btn_left_onClick() {
        CommonView.displayShort(this, "返回");
    }

    @Click(R.id.btn_right)
    void btn_right_onClick() {
        CommonView.displayShort(this, "加密友");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Map<String, Object> map_content = (Map<String, Object>) parent.getItemAtPosition(position);
        String app_user_id = map_content.get("app_user_id").toString();
        String username = map_content.get("name").toString();
//        Intent intentToChat = new Intent(instance, ChatActivity_.class);
//        intentToChat.putExtra("app_user_id", app_user_id);
//        intentToChat.putExtra("username", username);
//        intentToChat.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//        startActivity(intentToChat);
        CommonView.displayShort(instance, app_user_id + " , " + username);
    }
}
