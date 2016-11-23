package cn.sczhckg.order.data.bean;

import java.io.Serializable;

/**
 * @describe: 会员优惠类型
 * @author: Like on 2016/11/21.
 * @Email: 572919350@qq.com
 */

public class VipFavorableBean implements Serializable{

    private int id;

    private boolean isSelect;

    private String name;

    private int price;

    private int favorablePrice;

    private int isThis;

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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getFavorablePrice() {
        return favorablePrice;
    }

    public void setFavorablePrice(int favorablePrice) {
        this.favorablePrice = favorablePrice;
    }

    public int getIsThis() {
        return isThis;
    }

    public void setIsThis(int isThis) {
        this.isThis = isThis;

    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    @Override
    public String toString() {
        return "{" +
                "isThis=" + isThis +
                ", favorablePrice=" + favorablePrice +
                ", price=" + price +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
