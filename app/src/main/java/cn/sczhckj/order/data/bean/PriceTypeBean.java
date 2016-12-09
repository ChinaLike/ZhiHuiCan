package cn.sczhckj.order.data.bean;

import java.io.Serializable;

/**
 * @describe: 价格优惠类型
 * @author: Like on 2016/11/8.
 * @Email: 572919350@qq.com
 */

public class PriceTypeBean implements Serializable {

    private int id;//优惠ID

    private int type;//优惠类型

    private int price;//优惠价格

    private String title;

    private String imageUrl;

    private String color;

    private Integer imageId;//图片ID


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", type=" + type +
                ", price=" + price +
                ", title='" + title + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", color='" + color + '\'' +
                ", imageId=" + imageId +
                '}';
    }
}
