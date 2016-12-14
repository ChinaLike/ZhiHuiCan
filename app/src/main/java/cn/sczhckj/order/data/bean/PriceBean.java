package cn.sczhckj.order.data.bean;

/**
 * @describe: 价格策略属性，即优惠类型等
 * @author: Like on 2016/12/13.
 * @Email: 572919350@qq.com
 */

public class PriceBean {

    private Integer id;//价格ID
    private Double price;//价格
    private Integer type;//价格类型
    private String title;//价格名称
    private String color;//颜色配置
    private Integer imageId;//图片ID，Android端不用
    private String imageUrl;//图片URL
    private Integer active;//是否使用该价格 0-不是 1-是

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", price=" + price +
                ", type=" + type +
                ", title='" + title + '\'' +
                ", color='" + color + '\'' +
                ", imageId=" + imageId +
                ", imageUrl='" + imageUrl + '\'' +
                ", active=" + active +
                '}';
    }
}