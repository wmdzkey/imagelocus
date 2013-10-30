package m.z.imagelocus.entity;

import java.util.Date;

/**
 * Created by Winnid on 13-10-30.
 */
public class LbsDo {

    private Date time;
    private Lbs lbs;
    private String keyword;
    private String category;
    private Integer categoryDetail;
    private double distance;

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Lbs getLbs() {
        return lbs;
    }

    public void setLbs(Lbs lbs) {
        this.lbs = lbs;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getCategoryDetail() {
        return categoryDetail;
    }

    public void setCategoryDetail(Integer categoryDetail) {
        this.categoryDetail = categoryDetail;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
