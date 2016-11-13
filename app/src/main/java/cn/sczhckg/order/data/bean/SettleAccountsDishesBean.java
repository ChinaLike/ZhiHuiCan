package cn.sczhckg.order.data.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @describe: 结账界面--菜品属性
 * @author: Like on 2016/11/13.
 * @Email: 572919350@qq.com
 */

public class SettleAccountsDishesBean implements Serializable{

    private int id;

    private String name;

    private int totalNumber;

    private int totalPrice;

    private List<SettleAccountsDishesItemBean> itemDishes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(int totalNumber) {
        this.totalNumber = totalNumber;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<SettleAccountsDishesItemBean> getItemDishes() {
        return itemDishes;
    }

    public void setItemDishes(List<SettleAccountsDishesItemBean> itemDishes) {
        this.itemDishes = itemDishes;
    }

    @Override
    public String toString() {
        return "SettleAccountsDishesBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", totalNumber=" + totalNumber +
                ", totalPrice=" + totalPrice +
                ", itemDishes=" + itemDishes +
                '}';
    }
}
