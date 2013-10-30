package m.z.imagelocus.config;

import m.z.imagelocus.entity.LbsDo;
import m.z.imagelocus.entity.User;
import m.z.util.CalendarUtil;

import java.util.*;

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

    //存放自动推送数据
    public static List<LbsDo> lbsDoData = new ArrayList<LbsDo>();

    static {
        LbsDo lbsDo = new LbsDo();
        lbsDo.setTime(CalendarUtil.createTime(9, 0, 0));
        lbsDo.setCategory(Code.Category.NAMES[Code.Category.Restaurant_1]);
        lbsDo.setCategoryDetail(Code.Category.Restaurant.All);
        lbsDo.setDistance(500);
        lbsDo.setKeyword("美食");
        lbsDoData.add(lbsDo);
        LbsDo lbsDo2 = new LbsDo();
        lbsDo2.setTime(CalendarUtil.createTime(13, 0, 0));
        lbsDo2.setCategory(Code.Category.NAMES[Code.Category.Restaurant_1]);
        lbsDo2.setCategoryDetail(Code.Category.Restaurant.All);
        lbsDo2.setDistance(500);
        lbsDo2.setKeyword("美食");
        lbsDoData.add(lbsDo2);
        LbsDo lbsDo3 = new LbsDo();
        lbsDo3.setTime(CalendarUtil.createTime(19, 25, 0));
        lbsDo3.setCategory(Code.Category.NAMES[Code.Category.Restaurant_1]);
        lbsDo3.setCategoryDetail(Code.Category.Restaurant.All);
        lbsDo3.setDistance(500);
        lbsDo3.setKeyword("美食");
        lbsDoData.add(lbsDo3);
    }

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
