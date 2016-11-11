package cn.sczhckg.order.data.bean;

/**
 * @describe: 请求字段常量
 * @author: Like on 2016/11/4.
 * @Email: 572919350@qq.com
 */

public class Constant {

    /**购物车数量加减时发布的事件标识*/
    public static final int CART_NUMBER_EVENT=0;

    /**开桌*/
    public final static int OPEN_TABLE=0;
    /**点菜*/
    public final static int ORDER=1;
    /**结账*/
    public final static int SETTLE_ACCOUNTS=2;

    /**VIP判断请求常量*/
    public final static String USERNAME="userName";
    public final static String PASSWORD="password";
    /**首页锅底，推荐菜品等*/
    public final static String ID="id";
    public final static String DEVICE_ID="deviceId";
    /**开桌数据请求*/
    public final static String TABLE="table";
    public final static String TYPE="type";
    public final static String PARAMS="params";
    public final static String PERSON="person";

}
