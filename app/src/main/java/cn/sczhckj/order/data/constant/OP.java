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
     * 台桌状态切换
     */
    String TABLE_SWITCH_STATUS = "switchStatus";

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
     * 推送 结账完成
     */
    String PUSH_BILL_FINISH = "billFinish";

    /**
     * APP推送 心跳
     */
    String PUSH_HEART = "heart";

    /**
     * 通知刷新菜品数据
     */
    String PUSH_REFRESH_FOOD = "refreshFood";

    /**
     * 通知刷新用户数据
     */
    String PUSH_REFRESH_USER = "refreshUser";
    /**
     * 通知刷新点菜记录
     */
    String PUSH_REFRESH_RECORD = "refreshRecord";
    /**
     * 单桌点菜
     */
    String PUSH_ALONE_ORDER = "aloneOrder";
    /**
     * 并桌
     */
    String PUSH_MERGE_TABLE = "mergeTable";
    /**
     * 版本检查
     */
    String PUSH_CHECK_VERSION = "checkVersion";
    /**
     * 换桌
     */
    String PUSH_EXCHANGE_TABLE = "exchangeTable";

    /**
     * 获取结账清单
     */
    String BILL_BILL = "bill";

    /**
     * 打赏参数
     */
    String BILL_AWARDS = "awards";

    /**
     * 结账
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

    /**
     * 台桌初始化
     */
    String TABLE_INIT = "tableInit";

    /**
     * 异常收集
     */
    String EXCEPTION = "exception";

    /**
     * 服务员登录
     */
    String PRODUCE_WAITRESSLOGIN = "waitressLogin";

    /**
     * 获取所有台桌分类
     */
    String PRODUCE_TABLECATE = "tableCate";

    /**
     * 获取台桌属性
     */
    String PRODUCE_TABLEATTR = "tableAttr";

    /**
     * 获取所有台桌
     */
    String PRODUCE_ALL_TABLE = "allTable";


}
