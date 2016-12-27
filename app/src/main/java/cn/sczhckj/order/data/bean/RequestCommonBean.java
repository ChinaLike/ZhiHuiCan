package cn.sczhckj.order.data.bean;

import java.util.List;

import cn.sczhckj.order.data.bean.eval.EvalItemBean;
import cn.sczhckj.order.data.bean.food.CartBean;
import cn.sczhckj.order.data.bean.food.FoodBean;

/**
 * @describe: 数据请求通用的Bean
 * @author: Like on 2016/11/25.
 * @Email: 572919350@qq.com
 */

public class RequestCommonBean {

    private String memberCode;//卡号
    private String password;//密码
    private String phone;//用户手机号
    private String identity;//短信验证码
    private String userId;//用户ID
    private String deviceId;//用于开桌设备ID
    private Integer personCount;//就餐人数
    private List<CartBean> foods;//购物车数据
    private Integer orderType;//点菜类型
    private Integer number;//人数
    private Integer cateId;//菜品分类ID
    private Integer foodId;//菜品ID
    private Integer serviceId;//服务ID
    private Integer recordId;//消费记录ID
    private Integer count;//退菜数量
    private Integer cardId;//申请卡片类型
    private String name;//用户姓名
    private List<Integer> words;//热词
    private List<EvalItemBean> items;//评价星星

    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getPersonCount() {
        return personCount;
    }

    public void setPersonCount(Integer personCount) {
        this.personCount = personCount;
    }

    public List<CartBean> getFoods() {
        return foods;
    }

    public void setFoods(List<CartBean> foods) {
        this.foods = foods;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getCateId() {
        return cateId;
    }

    public void setCateId(Integer cateId) {
        this.cateId = cateId;
    }

    public Integer getFoodId() {
        return foodId;
    }

    public void setFoodId(Integer foodId) {
        this.foodId = foodId;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getCardId() {
        return cardId;
    }

    public void setCardId(Integer cardId) {
        this.cardId = cardId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<EvalItemBean> getItems() {
        return items;
    }

    public void setItems(List<EvalItemBean> items) {
        this.items = items;
    }

    public List<Integer> getWords() {
        return words;
    }

    public void setWords(List<Integer> words) {
        this.words = words;
    }

    @Override
    public String toString() {
        return "{" +
                "memberCode='" + memberCode + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", identity='" + identity + '\'' +
                ", userId='" + userId + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", personCount=" + personCount +
                ", foods=" + foods +
                ", orderType=" + orderType +
                ", number=" + number +
                ", cateId=" + cateId +
                ", foodId=" + foodId +
                ", serviceId=" + serviceId +
                ", recordId=" + recordId +
                ", count=" + count +
                ", cardId=" + cardId +
                ", name='" + name + '\'' +
                ", words=" + words +
                ", items=" + items +
                '}';
    }
}
