package cn.sczhckg.order.data.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @describe: 菜品详情
 * @author: Like on 2016/11/16.
 * @Email: 572919350@qq.com
 */

public class DetailsBean implements Serializable{

    private int type;//菜品分类

    private String id;//菜品ID

    private String name;// 菜品名称

    private int permiss;//菜品可点属性，0-消费者可选择， 1-消费者不可选择

    private int price;//菜品价格

    private List<String> urls;//菜品图片链接地址

    private long sales;//销量

    private long collect;//收藏数

    private int stockout;//缺货标识

    private int number;//初始数量

    private List<PriceTypeBean> priceType;//优惠价格类型

    private int totalEvaluate;//总评价数

    private int goodEvaluate;//好评总数

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

    public int getPermiss() {
        return permiss;
    }

    public void setPermiss(int permiss) {
        this.permiss = permiss;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
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

    public int getTotalEvaluate() {
        return totalEvaluate;
    }

    public void setTotalEvaluate(int totalEvaluate) {
        this.totalEvaluate = totalEvaluate;
    }

    public int getGoodEvaluate() {
        return goodEvaluate;
    }

    public void setGoodEvaluate(int goodEvaluate) {
        this.goodEvaluate = goodEvaluate;
    }
}
