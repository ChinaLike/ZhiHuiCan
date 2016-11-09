package cn.sczhckg.order.data.listener;

import cn.sczhckg.order.data.bean.DishesBean;

/**
 * @describe: 购物车数据监听
 * @author: Like on 2016/11/9.
 * @Email: 572919350@qq.com
 */

public interface OnShoppingCartListener {
    void shoppingCart(DishesBean bean);
}
