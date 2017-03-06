package cn.sczhckj.order.mode.impl;

import java.util.ArrayList;
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
     * 比较原有集合中数据，如果有则添加数量或减少数量，如果没有则新加一条记录
     *
     * @param bean
     * @param mList
     */
    public void compare(FoodBean bean, List<FoodBean> mList) {
        int id = bean.getId();
        int priceTypeId = bean.getType();
        int cateId = bean.getCateId();
        boolean isAdd = false;
        for (FoodBean item : mList) {
            if (item.getId() == id && item.getType() == priceTypeId && item.getCateId() == cateId) {
                /**查出有该条记录*/
                isAdd = true;
                item.setCount(bean.getCount());
            }
        }
        if (!isAdd) {
            mList.add(bean);
        }
        checkData(mList);
    }

    /**
     * 检测集合中是否有数量为0的数据
     *
     * @param mList
     */
    private void checkData(List<FoodBean> mList) {
        List<FoodBean> removeBean = new ArrayList<>();
        for (FoodBean bean : mList) {
            if (bean.getCount() == 0) {
                removeBean.add(bean);
            }
        }
        for (FoodBean bean : removeBean) {
            mList.remove(bean);
        }
    }

    /**
     * 刷新未下单数据
     *
     * @param currentList 更新的数据
     * @param list        原来的数据
     */
    public static void refreshDisOrderFood(List<FoodBean> currentList, List<FoodBean> list) {
        if (currentList == null || currentList.size() == 0) {
            return;
        }
        /**遍历原来的数据，逐条对应刷新*/
        for (FoodBean bean : list) {
            int cateId = bean.getCateId();
            int foodId = bean.getId();
            for (FoodBean item : currentList) {
                if (item.getCateId() == cateId && item.getId() == foodId) {
                    bean.setOriginPrice(item.getOriginPrice());//替换原始价格
                    bean.setPrice(item.getPrice());//替换执行价格
                    bean.setType(item.getType());//替换价格类型
                    bean.setPriceImageUrl(item.getPriceImageUrl());//替换价格图标
                    break;
                }
            }
        }
    }
}
