package m.z.imagelocus.entity;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Finder;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

import java.util.Date;

/**
 * 用户详细信息
 *
 * @author Winnid
 */
@Table(name = "user_info")
public class UserInfo {
    @Id(column = "id")
    private Integer id;

    @Column(column = "user_id")
    private Integer user_id;

    @Column(column = "name")
    private String name;
    @Column(column = "email")
    private String email;
    @Column(column = "registerDate")
    private Date registerDate;

    @Column(column = "state")
    private int state = 0;

    @Column(column = "money")
    private Double money;
    @Column(column = "score")
    private Double score;


    /////////////getter and setter 不能省略哦///////////////
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
