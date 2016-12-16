package cn.sczhckj.order.data.bean;

/**
 * @describe: 业务请求参数
 * @author: Like on 2016/11/18.
 * @Email: 572919350@qq.com
 */

public interface OP {

    /**
     * 卡号登录
     */
    String USER_LOGIN = "login";

    /**
     * 发送短信验证码
     */
    String USER_SMS = "sms";

    /**
     * 快捷登录
     */
    String USER_SMSLOGIN = "smsLogin";

    /**
     * 获取会员信息
     */
    String USER_INFO = "info";


    /**
     * 开桌数据信息
     */
    String TABLE_OPEN_INFO = "openInfo";

    /**
     * 台桌信息
     */
    String TABLE_INFO = "info";

    /**
     * 菜品分类
     */
    String FOOD_CATE = "cates";

    /**
     * 菜品列表
     */
    String FOOD_LIST = "foods";

    /**
     * 菜品信息
     */
    String FOOD_INFO = "info";

    /**
     * 菜品图片
     */
    String FOOD_IMAGE = "images";

    /**
     * 菜品价格
     */
    String FOOD_PRICE = "prices";

    /**
     * 下单
     */
    String ORDER_ORDER = "order";


    /**
     * 版本
     */
    String VERSION = "version";

    /**
     * 登录
     */
    String LOGIN = "login";

    /**
     * 获取短信验证码
     */
    String LOGIN_AUTH_CODE = "loginAuthCode";

    /**
     * 办理VIP
     */
    String APPLY_FOR_VIP = "applyForVip";


    /**
     * 开桌数据显示
     */
    String OPEN_TABLE_VERIFY = "openTable";

    /**
     * 点菜界面分类显示
     */
    String ORDER_CLASSIFY_SHOW = "orderClassifyShow";

    /**
     * 点菜界面菜品显示
     */
    String ORDER_DISHES_SHOW = "orderDishesShow";

    /**
     * 菜品详情显示
     */
    String DISHES_DETAILS = "dishesDetails";

    /**
     * 结账清单数据请求
     */
    String ACCOUNTS_LIST = "accountsList";

    /**
     * 结账时电子优惠券验证
     */
    String ACCOUNTS_GROUPON = "accountsGroupon";

    /**
     * 结账时所有信息验证完后，开始结账
     */
    String ACCOUNTS_PAY = "accountsPay";

    /**
     * 评价数据请求
     */
    String ACCOUNTS_EVALUATE_SHOW = "accountsEvaluateShow";

    /**
     * 评价数据提交
     */
    String ACCOUNTS_EVALUATE = "accountsEvaluate";

    /**
     * 呼叫服务
     */
    String SERVICE = "service";

    /**
     * 刷新购物车
     */
    String REFRESH_CART = "refreshCart";

    /**
     * 刷新二维码
     */
    String REFRESH_QRCODE = "refreshQRCode";

    /**
     * 有虎类型验证
     */
    String ACCOUNTS_FAVORABLE_TYPE = "accountsFavorableType";

}
