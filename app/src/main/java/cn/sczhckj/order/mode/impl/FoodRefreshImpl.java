package cn.sczhckj.order.mode.impl;

import android.support.v7.widget.RecyclerView;

import java.util.List;

import cn.sczhckj.order.data.bean.food.FoodBean;

/**
 * @ describe:  菜品刷新实现
 * @ author: Like on 2016/12/22.
 * @ email: 572919350@qq.com
 */

public class FoodRefreshImpl {

    private static FoodRefreshImpl mFoodRefresh;

    private FoodRefreshImpl() {

    }

    public static FoodRefreshImpl getInstance() {
        if (mFoodRefresh == null) {
            synchronized (FoodRefreshImpl.class) {
                if (mFoodRefresh == null) {
                    mFoodRefresh = new FoodRefreshImpl();
                }
            }
        }
        return mFoodRefresh;
    }

    /**
     * 根据一个菜品属性刷新整个列表
     *
     * @param bean 本地需要添加
     * @param list 最新数据
     * @return
     */
    public List<FoodBean> refreshFood(FoodBean bean, List<FoodBean> list) {
        List<FoodBean> currList = list;
        int id = bean.getId();
        int cateId = bean.getCateId();
        for (FoodBean item : currList) {
            if (item.getId() == id && item.getCateId() == cateId) {
                item.setCount(bean.getCount());
            }
        }
        return currList;
    }

    /**
     * 把一个集合数据添加到另外一个集合中
     *
     * @param mList
     * @param list
     * @return
     */
    public List<FoodBean> refreshFood(List<FoodBean> mList, List<FoodBean> list) {
        List<FoodBean> currList = list;
        List<FoodBean> myList = mList;
        for (FoodBean itemP : myList) {
            int id = itemP.getId();
            int cateId = itemP.getCateId();
            for (FoodBean itemC : currList) {
                if (itemC.getId() == id && itemC.getCateId() == cateId) {
                    itemC.setCount(itemP.getCount());
                }
            }
        }
        return currList;
    }

    /**
     * 退菜数据处理
     *
     * @param bean
     * @param list
     * @return
     */
    public List<FoodBean> refund(FoodBean bean, List<FoodBean> list) {
        List<FoodBean> currList = list;
        int id = bean.getId();
        int cateId = bean.getCateId();
        FoodBean removeBean = null;
        for (FoodBean item : currList) {
            if (item.getId() == id && item.getCateId() == cateId) {
                int count = item.getCount() - 1;
                if (count > 0) {
                    item.setCount(count);
                } else {
                    removeBean = item;
                }
            }
        }
        if (removeBean != null) {
            currList.remove(removeBean);
        }

        return currList;
    }


}
