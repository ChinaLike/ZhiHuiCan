package cn.sczhckj.order.mode.impl;

import java.util.List;

import cn.sczhckj.order.data.bean.food.FoodBean;
import cn.sczhckj.order.until.show.L;

/**
 * @ describe: 刷新菜品
 * @ author: Like on 2016/12/20.
 * @ email: 572919350@qq.com
 */

public class RefreshFoodImpl {

    private static RefreshFoodImpl mRefreshFood;

    public static RefreshFoodImpl getInstance() {
        if (mRefreshFood == null) {
            return new RefreshFoodImpl();
        } else {
            return mRefreshFood;
        }
    }

    /**
     * 通过一个对象刷新,只作为菜品列表数据刷新
     *
     * @param bean     单个菜品对象
     * @param beanList 原始菜品集合
     */
    public List<FoodBean> refreshFood(FoodBean bean, List<FoodBean> beanList) {
        int id = bean.getId();//菜品ID
        int cateId = bean.getCateId();//菜品分类
        for (FoodBean item : beanList) {
            if (item.getId() == id && item.getCateId() == cateId) {
                item.setCount(bean.getCount());
            }
        }
        return beanList;
    }

    /**
     * 通过集合刷新，只作为菜品列表数据刷新
     *
     * @param bean     新菜品集合
     * @param beanList 原始菜品集合
     */
    public List<FoodBean> refreshFood(List<FoodBean> bean, List<FoodBean> beanList) {
        for (FoodBean item : bean) {
            int id = item.getId();
            int cateId = item.getCateId();
            for (FoodBean itemP : beanList) {
                if (itemP.getId() == id && itemP.getCateId() == cateId) {
                    itemP.setCount(item.getCount());
                }
            }
        }
        return beanList;
    }

    /**
     * 获取菜品列表后，把购物车数据数据与之比较，如果有数量，则把比较后的数量与之对应后
     * 在把数据填充到菜品列表中显示
     *
     * @param orderList    已下单列表
     * @param disOrderList 未下单列表
     * @param beanList     网络返回列表
     * @return
     */
    public List<FoodBean> compare(List<FoodBean> orderList, List<FoodBean> disOrderList, List<FoodBean> beanList) {
        for (FoodBean bean : beanList) {
            int id = bean.getId();
            int cateId = bean.getCateId();
            int count=0;
            for (FoodBean itemC : orderList) {
                if (itemC.getId() == id && itemC.getCateId() == cateId) {
                    count=count+itemC.getCount();
                    /**设置已下单数量*/
                    bean.setOrderCount(itemC.getCount());
                }
            }
            for (FoodBean itemC : disOrderList) {
                if (itemC.getId() == id && itemC.getCateId() == cateId) {
                    count=count+itemC.getCount();
                    /**设置未下单数量*/
                    bean.setDisOrderCount(itemC.getCount());
                }
            }
            bean.setCount(count);
        }

        return beanList;
    }

}
