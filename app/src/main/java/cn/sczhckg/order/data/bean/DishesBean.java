package cn.sczhckg.order.data.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @describe: 菜品属性
 * @author: Like on 2016/11/8.
 * @Email: 572919350@qq.com
 */

public class DishesBean implements Serializable {

    private int type;//菜品分类

    private String id;//菜品ID

    private String name;// 菜品名称

    private int price;//菜品价格

    private String url;//菜品图片链接地址

    private long sales;//销量

    private long collect;//收藏数

    private int stockout;//缺货标识

    private int number;//初始数量

    private int totalPrice;//总价

    private int permiss;//菜品可点属性，0-消费者可选择， 1-消费者不可选择

    private List<PriceTypeBean> priceType;//优惠价格类型

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getSales() {
        return sales;
    }

    public void setSales(long sales) {
        this.sales = sales;
    }

    public long getCollect() {
        return collect;
    }

    public void setCollect(long collect) {
        this.collect = collect;
    }

    public int getStockout() {
        return stockout;
    }

    public void setStockout(int stockout) {
        this.stockout = stockout;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public List<PriceTypeBean> getPriceType() {
        return priceType;
    }

    public void setPriceType(List<PriceTypeBean> priceType) {
        this.priceType = priceType;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getPermiss() {
        return permiss;
    }

    public void setPermiss(int permiss) {
        this.permiss = permiss;
    }

    @Override
    public String toString() {
        return "{" +
                "type=" + type +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", url='" + url + '\'' +
                ", sales=" + sales +
                ", collect=" + collect +
                ", stockout=" + stockout +
                ", number=" + number +
                ", totalPrice=" + totalPrice +
                ", permiss=" + permiss +
                ", priceType=" + priceType +
                '}';
    }
}
