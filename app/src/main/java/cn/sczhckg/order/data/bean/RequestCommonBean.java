package cn.sczhckg.order.data.bean;

import java.util.List;

/**
 * @describe: 数据请求通用的Bean
 * @author: Like on 2016/11/25.
 * @Email: 572919350@qq.com
 */

public class RequestCommonBean {

    /**用户登录*/
    private String password;


    /**
     * 开桌获取数据
     */
    private String id;//用于开桌用户ID

    private String deviceId;//用于开桌设备ID



    /**
     * 购物车数据验证请求
     */

    private Integer type;//开桌标识0-开桌
    private List<DishesBean> dishesList;//菜品数据
    private Integer person;//本桌已选人数



    /**
     * 点菜界面分类数据显示
     */
    private Integer orderType;//点菜类型

    /**点菜界面根据分类请求菜品的数据*/

    /**
     * 菜品详情
     */
    private String name;

    /**
     * 团购券验证
     */
    private String group;

    /**
     * 支付请求
     */
    private Integer favorableType;

    private String userId;

    private Integer giftMoney;


    /**
     * 评价数据上传
     */
    private Float ratingBar1;
    private Float ratingBar2;
    private Float ratingBar3;
    private Float ratingBar4;
    private Integer hotWordId;
    private String opinion;

    /**VIP申请*/
    private String userName;
    private String phone;


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public List<DishesBean> getDishesList() {
        return dishesList;
    }

    public void setDishesList(List<DishesBean> dishesList) {
        this.dishesList = dishesList;
    }

    public Integer getPerson() {
        return person;
    }

    public void setPerson(Integer person) {
        this.person = person;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Integer getFavorableType() {
        return favorableType;
    }

    public void setFavorableType(Integer favorableType) {
        this.favorableType = favorableType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getGiftMoney() {
        return giftMoney;
    }

    public void setGiftMoney(Integer giftMoney) {
        this.giftMoney = giftMoney;
    }

    public Float getRatingBar1() {
        return ratingBar1;
    }

    public void setRatingBar1(Float ratingBar1) {
        this.ratingBar1 = ratingBar1;
    }

    public Float getRatingBar2() {
        return ratingBar2;
    }

    public void setRatingBar2(Float ratingBar2) {
        this.ratingBar2 = ratingBar2;
    }

    public Float getRatingBar3() {
        return ratingBar3;
    }

    public void setRatingBar3(Float ratingBar3) {
        this.ratingBar3 = ratingBar3;
    }

    public Float getRatingBar4() {
        return ratingBar4;
    }

    public void setRatingBar4(Float ratingBar4) {
        this.ratingBar4 = ratingBar4;
    }

    public Integer getHotWordId() {
        return hotWordId;
    }

    public void setHotWordId(Integer hotWordId) {
        this.hotWordId = hotWordId;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "{" +
                "password='" + password + '\'' +
                ", id='" + id + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", type=" + type +
                ", dishesList=" + dishesList +
                ", person=" + person +
                ", orderType=" + orderType +
                ", name='" + name + '\'' +
                ", group='" + group + '\'' +
                ", favorableType=" + favorableType +
                ", userId='" + userId + '\'' +
                ", giftMoney=" + giftMoney +
                ", ratingBar1=" + ratingBar1 +
                ", ratingBar2=" + ratingBar2 +
                ", ratingBar3=" + ratingBar3 +
                ", ratingBar4=" + ratingBar4 +
                ", hotWordId=" + hotWordId +
                ", opinion='" + opinion + '\'' +
                ", userName='" + userName + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
