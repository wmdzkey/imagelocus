package m.z.imagelocus.entity.yun;

/**
 * Created by Winnid on 13-10-30.
 */
public class LbsDoPoiListYun {

    private int status;
    private String message;
    private int size;
    private int total;
    private LbsDoPoiYun[] pois;


    public LbsDoPoiYun[] getPois() {
        return pois;
    }

    public void setPois(LbsDoPoiYun[] pois) {
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
