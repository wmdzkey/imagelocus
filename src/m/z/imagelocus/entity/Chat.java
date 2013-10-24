package m.z.imagelocus.entity;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Finder;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

import java.util.Date;

/**
 * 用户聊天
 *
 * @author Winnid
 */
@Table(name = "chat")
public class Chat {
    @Id(column = "id")
    private Integer id;

    @Column(column = "user_id")
    private Integer user_id;

    @Column(column = "content")
    private String content;
    @Column(column = "isComeMsg")
    private boolean isComeMsg;
    @Column(column = "chatTime")
    private Date chatTime;

/////////////getter and setter 不能省略哦///////////////
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isComeMsg() {
        return isComeMsg;
    }

    public void setComeMsg(boolean isComeMsg) {
        this.isComeMsg = isComeMsg;
    }

    public Date getChatTime() {
        return chatTime;
    }

    public void setChatTime(Date chatTime) {
        this.chatTime = chatTime;
    }

}
