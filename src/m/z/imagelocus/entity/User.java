package m.z.imagelocus.entity;
/**
 * 用户简易
 * @author Winnid
 */

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

@Table(name = "user")
public class User {
    @Id(column = "id")
    private Integer id;
    @Column(column = "username")
    private String username;
    @Column(column = "userhead")
    private String userhead;
    @Column(column = "password")
    private String password;
    @Column(column = "state")
    private int state = 0;
    @Column(column = "app_user_id")
    private String app_user_id;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getApp_user_id() {
        return app_user_id;
    }

    public void setApp_user_id(String app_user_id) {
        this.app_user_id = app_user_id;
    }

    public String getUserhead() {
        return userhead;
    }

    public void setUserhead(String userhead) {
        this.userhead = userhead;
    }

    @Override
    public String toString() {
        //return ToStringBuilder.reflectionToString(this);
        return "[ " + username + "; 百度云推id: " + app_user_id + "]";
    }
}
