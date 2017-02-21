package cn.sczhckj.order.data.bean.food;

import java.util.List;

import cn.sczhckj.order.data.constant.Constant;
import cn.sczhckj.order.until.TextUntils;

/**
 * @describe: 菜品属性
 * @author: Like on 2016/12/13.
 * @Email: 572919350@qq.com
 */

public class FoodBean {

    private Integer id;//菜品ID
    private Integer cateId;//分类ID
    private Integer cateAlias;//分类别名，用于必选界面
    private String name;//菜品名称
    private Integer permiss;//选择权限，0-不可选，1-可选
    private Integer maximum;//最大可选数量，0-不控制
    private Double price;//当前价格，无优惠价格时，等于 originPrice
    private Integer type;//价格类型ID
    private String priceImageUrl;//价格图标
    private Double originPrice;//原始价格
    private Integer imageId;//缩略图ID，Android端不用
    private String imageUrl;//缩略图URL
    private Long sales;//销量数
    private Long favors;//点赞数
    private Integer stockout;//是否缺货，暂时保留
    private Integer count;//下单数量
    private Integer finishCount;//已上数量
    private List<PriceBean> prices;//价格策略
    private boolean isFavor;//是否点赞
    private List<ImageBean> images;//图片轮播


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
        return TextUntils.empty(permiss, Constant.PERMISS_AGREE);
    }

    public void setPermiss(Integer permiss) {
        this.permiss = permiss;
    }

    public Integer getMaximum() {
        return TextUntils.empty(maximum);
    }

    public void setMaximum(Integer maximum) {
        this.maximum = maximum;
    }

    public Double getPrice() {
        return TextUntils.empty(price);
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
        return TextUntils.empty(sales);
    }

    public void setSales(Long sales) {
        this.sales = sales;
    }

    public Long getFavors() {
        return TextUntils.empty(favors);
    }

    public void setFavors(Long favors) {
        this.favors = favors;
    }

    public Integer getStockout() {
        return TextUntils.empty(stockout);
    }

    public void setStockout(Integer stockout) {
        this.stockout = stockout;
    }

    public Integer getCount() {
        return TextUntils.empty(count);
    }

    public void setCount(Integer count) {
        this.count = count;
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

    public Integer getFinishCount() {
        return TextUntils.empty(finishCount);
    }

    public void setFinishCount(Integer finishCount) {
        this.finishCount = finishCount;
    }

    public String getPriceImageUrl() {
        return priceImageUrl;
    }

    public void setPriceImageUrl(String priceImageUrl) {
        this.priceImageUrl = priceImageUrl;
    }

    public Double getOriginPrice() {
        return TextUntils.empty(originPrice);
    }

    public void setOriginPrice(Double originPrice) {
        this.originPrice = originPrice;
    }

    public List<ImageBean> getImages() {
        return images;
    }

    public void setImages(List<ImageBean> images) {
        this.images = images;
    }

    public Integer getType() {
        return TextUntils.empty(type);
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getCateAlias() {
        return cateAlias;
    }

    public void setCateAlias(Integer cateAlias) {
        this.cateAlias = cateAlias;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", cateId=" + cateId +
                ", cateAlias=" + cateAlias +
                ", name='" + name + '\'' +
                ", permiss=" + permiss +
                ", maximum=" + maximum +
                ", price=" + price +
                ", type=" + type +
                ", priceImageUrl='" + priceImageUrl + '\'' +
                ", originPrice=" + originPrice +
                ", imageId=" + imageId +
                ", imageUrl='" + imageUrl + '\'' +
                ", sales=" + sales +
                ", favors=" + favors +
                ", stockout=" + stockout +
                ", count=" + count +
                ", finishCount=" + finishCount +
                ", prices=" + prices +
                ", isFavor=" + isFavor +
                ", images=" + images +
                '}';
    }
}
