package cn.sczhckj.order.data.bean;

/**
 * @describe: 购物车数据菜品
 * @author: Like on 2016/12/14.
 * @Email: 572919350@qq.com
 */

public class CartBean {
    private Integer id;
    private Integer cateId;
    private Integer number;
    private Double price;

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

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", cateId=" + cateId +
                ", number=" + number +
                ", price=" + price +
                '}';
    }
}
