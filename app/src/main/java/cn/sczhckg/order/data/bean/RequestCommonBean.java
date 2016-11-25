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

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 开桌获取数据
     */
    private String id;//用于开桌用户ID

    private String deviceId;//用于开桌设备ID


    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * 购物车数据验证请求
     */

    private int type;//开桌标识0-开桌
    private List<DishesBean> dishesList;//菜品数据
    private int person;//本桌已选人数


    public void setType(int type) {
        this.type = type;
    }

    public void setDishesList(List<DishesBean> dishesList) {
        this.dishesList = dishesList;
    }

    public void setPerson(int person) {
        this.person = person;
    }

    /**
     * 点菜界面分类数据显示
     */
    private int orderType;//点菜类型

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    /**点菜界面根据分类请求菜品的数据*/

    /**
     * 菜品详情
     */
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 团购券验证
     */
    private String group;

    public void setGroup(String group) {
        this.group = group;
    }

    /**
     * 支付请求
     */
    private int favorableType;

    private String userId;

    private int giftMoney;

    public void setFavorableType(int favorableType) {
        this.favorableType = favorableType;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setGiftMoney(int giftMoney) {
        this.giftMoney = giftMoney;
    }


    /**
     * 评价数据上传
     */
    private float ratingBar1;
    private float ratingBar2;
    private float ratingBar3;
    private float ratingBar4;
    private int hotWordId;
    private String opinion;

    public void setRatingBar1(float ratingBar1) {
        this.ratingBar1 = ratingBar1;
    }

    public void setRatingBar2(float ratingBar2) {
        this.ratingBar2 = ratingBar2;
    }

    public void setRatingBar3(float ratingBar3) {
        this.ratingBar3 = ratingBar3;
    }

    public void setRatingBar4(float ratingBar4) {
        this.ratingBar4 = ratingBar4;
    }

    public void setHotWordId(int hotWordId) {
        this.hotWordId = hotWordId;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    /**VIP申请*/
    private String userName;
    private String phone;

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
