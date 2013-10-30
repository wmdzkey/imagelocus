package m.z.imagelocus.entity.yun;

/**
 * Created by Winnid on 13-10-30.
 */
public class Poi {
    //addr":"xx路xx号"，"y":"40.2234","dis":"324.3","x":"116.3424","name":"xx酒店","tel":""
    public String addr;
    public double y;
    public double x;
    public double dis;
    public String name;
    public String tel;

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getDis() {
        return dis;
    }

    public void setDis(double dis) {
        this.dis = dis;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
