package cn.sczhckj.order.data.bean.food;

/**
 * @describe: 购物车菜品提交信息
 * @author: Like on 2016/12/14.
 * @Email: 572919350@qq.com
 */

public class CartBean {

    private Integer id;//分类
    private Integer cateId;//一级分类
    private Integer number;//数量
    private Integer type;//价格类型ID
    private Double price;//价格

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

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", cateId=" + cateId +
                ", number=" + number +
                ", type=" + type +
                ", price=" + price +
                '}';
    }
}
