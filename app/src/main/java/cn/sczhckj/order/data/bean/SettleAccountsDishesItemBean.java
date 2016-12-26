package cn.sczhckj.order.data.bean;

import java.io.Serializable;

import cn.sczhckj.order.data.bean.food.PriceBean;

/**
 * @describe: 结账界面单品清单
 * @author: Like on 2016/11/13.
 * @Email: 572919350@qq.com
 */

public class SettleAccountsDishesItemBean implements Serializable {

    private String id;

    private String name;

    private int number;

    private int price;

    private PriceBean priceBean;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public PriceBean getPriceTypeBean() {
        return priceBean;
    }

    public void setPriceTypeBean(PriceBean priceTypeBean) {
        this.priceBean = priceTypeBean;
    }

    @Override
    public String toString() {
        return "SettleAccountsDishesItemBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", number=" + number +
                ", price=" + price +
                ", priceTypeBean=" + priceBean +
                '}';
    }
}
