package m.z.imagelocus.entity;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

import java.util.Date;

/**
 * @author Winnid
 * @Title: lbs存储存于数据库
 * @Description:lbs存储存于数据库
 * @date 13-9-15
 * @Version V1.0
 * Created by Winnid on 13-9-15.
 */
@Table(name = "lbs")
public class Lbs {

    @Id(column = "id")
    private Integer id;
    @Column(column = "user_id")
    private Integer user_id;
    @Column(column = "app_user_id")
    private String app_user_id;

    //百度位置字段
    @Column(column = "latitude")
    private double latitude;//纬度
    @Column(column = "longitude")
    private double longitude;//经度
    @Column(column = "speed")
    private float speed;
    @Column(column = "direction")
    private float direction;
    @Column(column = "satellitesNum")
    private int satellitesNum;

    //位置通用字段
    @Column(column = "locType")
    private int locType;
    @Column(column = "radius")
    private float radius;//等同于accuracy
    @Column(column = "addrStr")
    private String addrStr;


    //LBS云字段
    @Column(column = "createTime")
    private Date createTime;
    @Column(column = "modifyTime")
    private Date modifyTime;

    @Column(column = "province")
    private String province;//省
    @Column(column = "city_id")
    private Integer city_id;//市id
    @Column(column = "city")
    private String city;//城市名
    @Column(column = "district")
    private String district;//区名
    @Column(column = "tags")
    private String tags;//	标签以空格分隔

    @Column(column = "geotable_id")
    private Integer geotable_id;//表主键,记录关联的geotable的标识

    @Column(column = "title")
    private String title;//poi名称
    @Column(column = "coord_type")
    private int coord_type;//用户上传的坐标的类型//1：未加密的GPS坐标2：国测局加密3：百度加密

    @Column(column = "distance")
    private Integer distance;//距离
    @Column(column = "weight")
    private Integer weight;//权重

    //自定义字段
    @Column(column = "state")
    private int state = 0;//状态


    public Lbs() {}

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

    public String getApp_user_id() {
        return app_user_id;
    }

    public void setApp_user_id(String app_user_id) {
        this.app_user_id = app_user_id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getDirection() {
        return direction;
    }

    public void setDirection(float direction) {
        this.direction = direction;
    }

    public int getSatellitesNum() {
        return satellitesNum;
    }

    public void setSatellitesNum(int satellitesNum) {
        this.satellitesNum = satellitesNum;
    }

    public int getLocType() {
        return locType;
    }

    public void setLocType(int locType) {
        this.locType = locType;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public String getAddrStr() {
        return addrStr;
    }

    public void setAddrStr(String addrStr) {
        this.addrStr = addrStr;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public Integer getCity_id() {
        return city_id;
    }

    public void setCity_id(Integer city_id) {
        this.city_id = city_id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Integer getGeotable_id() {
        return geotable_id;
    }

    public void setGeotable_id(Integer geotable_id) {
        this.geotable_id = geotable_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCoord_type() {
        return coord_type;
    }

    public void setCoord_type(int coord_type) {
        this.coord_type = coord_type;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
