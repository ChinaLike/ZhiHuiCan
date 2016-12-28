package cn.sczhckj.order.data.constant;

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
     * 开桌下单
     */
    String TABLE_OPEN = "open";

    /**
     * 台桌信息
     */
    String TABLE_INFO = "info";

    /**
     * 设置台桌人数
     */
    String TABLE_SET_PERSON_NUM = "setPersonNum";

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
     * 退菜
     */
    String FOOD_REFUND = "refund";

    /**
     * 点赞
     */
    String FOOD_FAVOR = "favor";

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
     * 刷新菜品
     */
    String ORDER_REFRESH = "refresh";

    /**
     * 获取服务类型
     */
    String SERVICE_SERVICES = "services";

    /**
     * 呼叫服务
     */
    String SERVICE_CALL = "call";

    /**
     * 终止服务
     */
    String SERVICE_ABORT = "abort";

    /**
     * 推送 上菜信息
     */
    String PUSH_ARRIVE = "arrive";

    /**
     * 推送 服务完成
     */
    String PUSH_COMPLETE = "complete";

    /**
     * 推送 上锁
     */
    String PUSH_LOCK = "lock";

    /**
     * 推送 解锁
     */
    String PUSH_UNLOCK = "unLock";

    /**
     * 获取结账清单
     */
    String BILL_BILL = "bill";

    /**
     * 打赏参数
     */
    String BILL_AWARDS = "awards";

    /**
     * 打赏参数
     */
    String BILL_COMMIT = "commit";

    /**
     * 优惠信息列表
     */
    String CARD_INFO = "info";
    /**
     * 用户办卡申请
     */
    String CARD_APPLY = "apply";

    /**
     * 优惠信息列表
     */
    String EVAL_INFO = "items";
    /**
     * 提交评价
     */
    String EVAL_COMMIT = "commit";

    /**
     * Apk版本更新
     */
    String DEVICE_UPDATE = "update";


}
