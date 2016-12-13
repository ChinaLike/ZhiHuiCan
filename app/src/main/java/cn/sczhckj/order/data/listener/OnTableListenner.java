package cn.sczhckj.order.data.listener;

/**
 * @describe: 桌面设置监听
 * @author: Like on 2016/11/22.
 * @Email: 572919350@qq.com
 */

public interface OnTableListenner {

    /**
     * 台桌名称
     *
     * @param tableName
     */
    void table(String tableName);

    /**
     * 服务员信息
     *
     * @param waiter
     */
    void waiter(String waiter);

    /**
     * 就餐人数
     *
     * @param number
     */
    void person(int number);

}
