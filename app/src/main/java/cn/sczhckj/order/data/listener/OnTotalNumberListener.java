package cn.sczhckj.order.data.listener;

/**
 * @describe: 总数量的监听
 * @author: Like on 2016/11/9.
 * @Email: 572919350@qq.com
 */

public interface OnTotalNumberListener {
    void totalNumber(int totalPrice, int potNumber, int dishesNumber);
}
