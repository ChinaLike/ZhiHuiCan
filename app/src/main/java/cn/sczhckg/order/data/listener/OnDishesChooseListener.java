package cn.sczhckg.order.data.listener;

import java.util.List;

import cn.sczhckg.order.data.bean.DishesBean;

/**
 * @describe: 菜品选择监听
 * @author: Like on 2016/11/9.
 * @Email: 572919350@qq.com
 */

public interface OnDishesChooseListener {
    void dishesChoose(List<DishesBean> mList);
}
