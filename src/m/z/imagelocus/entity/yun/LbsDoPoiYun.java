package m.z.imagelocus.entity.yun;

import m.z.imagelocus.entity.Lbs;

import java.util.Date;

/**
 * Created by Winnid on 13-10-30.
 */
public class LbsDoPoiYun {

    private Integer id;
    private double[] location = new double[2];
    //经度,纬度
    private double latitude;//纬度
    private double longitude;//经度

    private String province;//省
    private Integer city_id;//市id
    private String city;//城市名
    private String district;//区名
    private String address;//地址
    private String tags;//	标签以空格分隔

    private Integer geotable_id;//表主键,记录关联的geotable的标识

    private String title;//poi名称
    private int coord_type;//用户上传的坐标的类型//1：未加密的GPS坐标2：国测局加密3：百度加密


    private double createTime;
    private double modifyTime;

    //自定义字段
    private int state = 0;//状态
    private String app_user_id;
    private String keyword;

    private Integer distance;
    private Integer category;
    private Integer categoryDetail;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double[] getLocation() {
        return location;
    }

    public void setLocation(double[] location) {
        this.location = location;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public double getCreateTime() {
        return createTime;
    }

    public void setCreateTime(double createTime) {
        this.createTime = createTime;
    }

    public double getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(double modifyTime) {
        this.modifyTime = modifyTime;
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

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public Integer getCategoryDetail() {
        return categoryDetail;
    }

    public void setCategoryDetail(Integer categoryDetail) {
        this.categoryDetail = categoryDetail;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }
}
