package m.z.imagelocus.config;

import com.google.gson.Gson;
import m.z.imagelocus.entity.Lbs;
import m.z.imagelocus.entity.User;

/**
 * @author Winnid
 * @Title:
 * @Description:
 * @date 13-9-11
 * @Version V1.0
 * Created by Winnid on 13-9-11.
 */
public class SystemAdapter {
    public static Gson gson = new Gson();

    public static User currentUser;
    public static Lbs currentUserLbs;
}