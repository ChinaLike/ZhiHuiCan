package cn.sczhckj.order.data.bean.food;

/**
 * @describe: 购物车菜品提交信息
 * @author: Like on 2016/12/14.
 * @Email: 572919350@qq.com
 */

public class CartBean {

    private Integer id;//分类
    private Integer cateId;//一级分类
    private Integer count;//数量
    private Integer type;//价格类型ID
    private Double price;//执行价格
    private Double originPrice;//原始价格

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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getOriginPrice() {
        return originPrice;
    }

    public void setOriginPrice(Double originPrice) {
        this.originPrice = originPrice;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", cateId=" + cateId +
                ", count=" + count +
                ", type=" + type +
                ", price=" + price +
                ", originPrice=" + originPrice +
                '}';
    }
}
