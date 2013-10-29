package m.z.imagelocus.entity;
/**
 * 用户简易
 * @author Winnid
 */

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

@Table(name = "friend")
public class Friend {
    @Id(column = "id")
    private Integer id;
    @Column(column = "user_id")
    private Integer user_id;
    @Column(column = "friend_user_id")
    private Integer friend_user_id;
    @Column(column = "app_user_id")
    private String app_user_id;
    @Column(column = "app_friend_user_id")
    private String app_friend_user_id;
    @Column(column = "state")
    private int state = 0;//状态
    @Column(column = "friendname")
    private String friendname;
    @Column(column = "friendhead")
    private String friendhead;
    @Column(column = "friendphone")
    private String friendphone;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getFriend_user_id() {
        return friend_user_id;
    }

    public void setFriend_user_id(Integer friend_user_id) {
        this.friend_user_id = friend_user_id;
    }

    public String getApp_user_id() {
        return app_user_id;
    }

    public void setApp_user_id(String app_user_id) {
        this.app_user_id = app_user_id;
    }

    public String getApp_friend_user_id() {
        return app_friend_user_id;
    }

    public void setApp_friend_user_id(String app_friend_user_id) {
        this.app_friend_user_id = app_friend_user_id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getFriendname() {
        return friendname;
    }

    public void setFriendname(String friendname) {
        this.friendname = friendname;
    }

    public String getFriendhead() {
        return friendhead;
    }

    public void setFriendhead(String friendhead) {
        this.friendhead = friendhead;
    }

    public String getFriendphone() {
        return friendphone;
    }

    public void setFriendphone(String friendphone) {
        this.friendphone = friendphone;
    }

}
