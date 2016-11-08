package cn.sczhckg.order.data.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @describe: 菜品属性
 * @author: Like on 2016/11/8.
 * @Email: 572919350@qq.com
 */

public class DishesBean implements Serializable{

    private String id;//菜品ID

    private String name;// 菜品名称

    private String price;//菜品价格

    private String url;//菜品图片链接地址

    private long sales;//销量

    private long collect;//收藏数

    private int stockout;//缺货标识

    private int number;//初始数量

    private List<PriceTypeBean> priceType;//优惠价格类型

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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
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
}
