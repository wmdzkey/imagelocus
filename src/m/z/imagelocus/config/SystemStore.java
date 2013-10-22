package m.z.imagelocus.config;

import m.z.imagelocus.entity.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Winnid
 * @Title:
 * @Description:
 * @date 13-9-11
 * @Version V1.0
 * Created by Winnid on 13-9-11.
 */
public class SystemStore {
    //存放临时数据
    public static Map<String, Object> TempData = new HashMap<String, Object>();

    //存放用户数据(暂时)
    public static List<User> userData = new ArrayList<User>();

    static {
        User user1 = new User();
        user1.setApp_user_id("843804516070431639");
        user1.setUsername("王明东");
        userData.add(user1);
        User user2 = new User();
        user2.setApp_user_id("817844040394073629");
        user2.setUsername("火腿肠");
        userData.add(user2);
        User user3 = new User();
        user3.setApp_user_id("1083330851853254418");
        user3.setUsername("小米回忆");
        userData.add(user3);
        User user4 = new User();
        user4.setApp_user_id("1069138469804112849");
        user4.setUsername("李敏");
        userData.add(user4);
    }

}
