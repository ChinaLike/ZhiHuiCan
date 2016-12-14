package cn.sczhckj.order.data.bean;

import java.util.List;

/**
 * @describe: 菜品属性
 * @author: Like on 2016/12/13.
 * @Email: 572919350@qq.com
 */

public class FoodBean {

    private Integer id;//菜品ID
    private Integer cateId;//分类ID
    private String name;//菜品名称
    private Integer permiss;//选择权限，0-不可选，1-可选
    private Integer maximum;//最大可选数量，0-不控制
    private Double price;//单价，基础价格
    private Integer imageId;//缩略图ID，Android端不用
    private String imageUrl;//缩略图URL
    private Long sales;//销量数
    private Long favors;//点赞数
    private Integer stockout;//是否缺货，暂时保留
    private Integer number;//默认数量
    private List<PriceBean> prices;//价格策略
    private boolean isFavor;//是否点赞
    private Integer finishFood;//完成菜品数量

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCateId() {
        return cateId;
    }

    public void setCateId(Integer cateId) {
        this.cateId = cateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPermiss() {
        return permiss;
    }

    public void setPermiss(Integer permiss) {
        this.permiss = permiss;
    }

    public Integer getMaximum() {
        return maximum;
    }

    public void setMaximum(Integer maximum) {
        this.maximum = maximum;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getSales() {
        return sales;
    }

    public void setSales(Long sales) {
        this.sales = sales;
    }

    public Long getFavors() {
        return favors;
    }

    public void setFavors(Long favors) {
        this.favors = favors;
    }

    public Integer getStockout() {
        return stockout;
    }

    public void setStockout(Integer stockout) {
        this.stockout = stockout;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public List<PriceBean> getPrices() {
        return prices;
    }

    public void setPrices(List<PriceBean> prices) {
        this.prices = prices;
    }

    public boolean isFavor() {
        return isFavor;
    }

    public void setFavor(boolean favor) {
        isFavor = favor;
    }

    public Integer getFinishFood() {
        return finishFood;
    }

    public void setFinishFood(Integer finishFood) {
        this.finishFood = finishFood;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", cateId=" + cateId +
                ", name='" + name + '\'' +
                ", permiss=" + permiss +
                ", maximum=" + maximum +
                ", price=" + price +
                ", imageId=" + imageId +
                ", imageUrl='" + imageUrl + '\'' +
                ", sales=" + sales +
                ", favors=" + favors +
                ", stockout=" + stockout +
                ", number=" + number +
                ", prices=" + prices +
                ", isFavor=" + isFavor +
                ", finishFood=" + finishFood +
                '}';
    }
}
