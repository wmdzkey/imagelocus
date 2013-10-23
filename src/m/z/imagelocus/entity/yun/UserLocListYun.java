package m.z.imagelocus.entity.yun;

/**
 * @author Winnid
 * @Title:百度LBS云存储实体类
 * @Description:百度LBS云存储实体类
 * @date 13-9-15
 * @Version V1.0
 * Created by Winnid on 13-9-15.
 */
public class UserLocListYun {

    private int status;
    private String message;
    private int size;
    private int total;
    private UserLocYun[] pois;

    public UserLocYun[] getPois() {
        return pois;
    }

    public void setPois(UserLocYun[] pois) {
        this.pois = pois;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
